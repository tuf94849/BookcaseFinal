package com.example.bookcase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {


    public BookListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> Books = new ArrayList<>();
        Books.add("Ender's Game");
        Books.add("My Brother Sam is Dead");
        Books.add("The Martian");
        Books.add("Among The Hidden");
        Books.add("Ready Player One");
        Books.add("One Flew Over The Cuckoo's Nest");
        Books.add("The Book of Basketball");
        Books.add("Fever 1793");
        Books.add("The Road");
        Books.add("Night");
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView BookListView = v.findViewById(R.id.BookListView);

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Books);

        BookListView.setAdapter(stringArrayAdapter);




        return v;
    }

}
