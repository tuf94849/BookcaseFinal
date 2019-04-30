package com.example.bookcase;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import edu.temple.audiobookplayer.AudiobookService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    Context c;

    TextView tv;
    ImageView imageView;
    String bookSelected;
    String title;
    String author;
    String publish;
    public static final String BOOK_KEY = "myBook";
    Book pagerBooks;
    ImageButton playBTN;
    ImageButton pauseBTN;
    ImageButton stopBTN;
    SeekBar seekBar;
    ProgressBar progressBar;
    TextView progressText;
    Button dl_btn;
    SeekBar dl_progress;
    private BookDetailsInterface mListener;

    public static BookDetailsFragment newInstance(Book bookList) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BOOK_KEY, bookList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            pagerBooks = getArguments().getParcelable(BOOK_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        tv = view.findViewById(R.id.bookTitle);
        imageView = view.findViewById(R.id.bookImage);

        playBTN = view.findViewById(R.id.playButton);
        pauseBTN = view.findViewById(R.id.pauseButton);
        stopBTN = view.findViewById(R.id.stopButton);
        seekBar = view.findViewById(R.id.seekBar);
        progressBar = view.findViewById(R.id.progressBar);
        progressText = view.findViewById(R.id.progressText);
        dl_btn = view.findViewById(R.id.download_btn);
        dl_progress = view.findViewById(R.id.dl_progress);


        if(getArguments() != null) {
            displayBook(pagerBooks);
        }



        dl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                // check the button value
                if(dl_btn.getText().toString().equals("Download")) {
                    String query = "https://kamorris.com/lab/audlib/download.php?id=" + Integer.toString(pagerBooks.getId());
                    new downloadBook(dl_progress, dl_btn, c).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
                    //update button text to communicate to the user that the audiobook has downloaded
                    dl_btn.setText(R.string.delete_txt);

                    //otherwise it's a delete button
                } else {

                    //update button text to communicate to the user that the audiobook has been deleted
                    dl_btn.setText(R.string.download_txt);
                }
            }
        });

        return view;
    }

    public void displayBook(final Book bookObj){
        author = bookObj.getAuthor();
        title = bookObj.getTitle(); publish = bookObj.getPublished();
        tv.setText(" \"" + title + "\" "); tv.append(" " + author); tv.append(" " + publish);
        tv.setTextSize(30);
        tv.setTextColor(Color.BLACK);
        String imageURL = bookObj.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);

        playBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BookDetailsInterface)c).playBook(bookObj.getId());
            }
        });

        pauseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BookDetailsInterface)c).pauseBook();
            }
        });

        stopBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BookDetailsInterface)c).stopBook();
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setProgress(progress);
                ((BookDetailsInterface)c).seekBook(progress);
                progressText.setText(" " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //not needed
            }
        });
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListFragment.BookInterface) {
            mListener = (BookDetailsInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.c = context;
    }

    public interface BookDetailsInterface{
        void playBook(int id);
        void pauseBook();
        void stopBook();
        void seekBook(int position);
    }

    private static class downloadBook extends AsyncTask<String, Integer, String> {
        SeekBar dl_progress;
        Button dl_btn;
        Context ctx;
        char id;

        public downloadBook(SeekBar dl_progress, Button dl_btn, Context ctx) {
            this.dl_progress = dl_progress;
            this.dl_btn = dl_btn;
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //make download progress bar visible
            dl_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... sUrl) {
            id = sUrl[0].charAt(sUrl[0].length() - 1);
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(ctx.getFilesDir().getPath() + "/" + id + ".mp3");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if(isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if(fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch(Exception e) {
                return e.toString();
            } finally {
                try {
                    if(output != null)
                        output.close();
                    if(input != null)
                        input.close();
                } catch(IOException ignored) {
                }

                if(connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            dl_progress.setIndeterminate(false);
            dl_progress.setMax(100);
            dl_progress.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //hide the download bar since download is finished
            dl_progress.setVisibility(View.INVISIBLE);

            //check if file downloaded
            if(result != null) {
                Toast.makeText(ctx, "Download error: " + result, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(ctx, "File downloaded", Toast.LENGTH_SHORT).show();
                //update button text to communicate to the user that the audiobook has downloaded
                dl_btn.setText(R.string.delete_txt);
            }
        }
    }






}
