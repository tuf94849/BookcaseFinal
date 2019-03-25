package com.example.bookcase;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ViewPager viewPager = findViewById(R.id.BookViewPager);

    }
}
