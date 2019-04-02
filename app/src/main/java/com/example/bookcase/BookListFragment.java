package com.example.bookcase;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {


    public BookListFragment() {
        // Required empty public constructor
    }

    ListView listView;
    Context c;
    public BookInterface listener;

    ArrayList<String> bookList;
    Book books;
    JSONArray bookArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_book_list, container, false);
        listView = v.findViewById(R.id.BookListView);

        bookList = new ArrayList<>();
        downloadBook();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookInterface) {
            listener = (BookInterface) context;
        } else {
            throw new RuntimeException(context.toString());
        }
        this.c = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface BookInterface {
        void bookPicked(Book book);
    }

    public void downloadBook() {
        new Thread() {
            public void run() {
                String urlString = "https://kamorris.com/lab/audlib/booksearch.php";
                try {
                    URL url = new URL(urlString);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String tmpString;
                    while ((tmpString = reader.readLine()) != null) {
                        builder.append(tmpString);
                    }
                    Message msg = Message.obtain();
                    msg.obj = builder.toString();
                    urlHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler urlHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                bookArray = new JSONArray((String) msg.obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < bookArray.length(); i++){
                try {
                    JSONObject jsonData = bookArray.getJSONObject(i);
                    String title = jsonData.getString("title");
                    bookList.add(title);
                    Log.d("Book ", bookArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, bookList);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        books = new Book(bookArray.getJSONObject(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //books = (Book) parent.getItemAtPosition(position);
                    ((BookInterface) c).bookPicked(books);
                }
            });
            return false;
        }
    });

}
