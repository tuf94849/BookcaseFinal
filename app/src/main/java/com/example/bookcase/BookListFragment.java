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


    private BookInterface mListener;

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance(String param1, String param2) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ListView listView;
    Context c;
    ArrayList<String> bookList;
    Book books;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);

        listView = v.findViewById(R.id.bookList);
        bookList = new ArrayList<>();

        return v;
    }

    public void getBooks(final JSONArray bookArray){
        for(int i = 0; i < bookArray.length(); i++){
            try {
                JSONObject jsonData = bookArray.getJSONObject(i);
                String title = jsonData.getString("title");
                bookList.add(title);
                Log.d("Book", bookArray.get(i).toString());
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
                ((BookInterface) c).bookSelected(books);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookInterface) {
            mListener = (BookInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.c = context;
    }

    public interface BookInterface {
        void bookSelected(Book bookObj);
    }

}
