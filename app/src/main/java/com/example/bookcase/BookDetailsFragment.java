package com.example.bookcase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsFragment extends Fragment {


    public BookDetailsFragment() {
        // Required empty public constructor
    }

    TextView tv;
    String bookPicked;
    Button btn;
    ImageView imageView;
    EditText editText;
    String title;
    String author;
    int publishYear;
    public static final String BOOK_KEY = "bookTitle";

    public static BookDetailsFragment newInstance(String book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BOOK_KEY, book);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            bookPicked = getArguments().getString(BOOK_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);
        tv = v.findViewById(R.id.bookTitle);

        imageView = v.findViewById(R.id.bookImage);
        btn = v.findViewById(R.id.btnSearch);
        editText = v.findViewById(R.id.searchBar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBook = editText.getText().toString();

            }
        });
        //tv.setText(bookPicked);
        //bookPicked(bookPicked);

        return v;
    }


    public void bookPicked (Book book){
        author = book.getAuthor();
        title = book.getTitle(); publishYear = book.getPublished();
        tv.setText(" \"" + title + "\" ");
        tv.append(", " + author);
        tv.append(", " + publishYear);
        String imageURL = book.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);
    }




}
