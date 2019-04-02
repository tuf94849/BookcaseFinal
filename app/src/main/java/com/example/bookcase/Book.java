package com.example.bookcase;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Book implements Parcelable {

    public int id;
    public String title;
    public String author;
    public int published;
    public String coverURL;

    public Book(JSONObject jsonBook) throws JSONException {
        this.title = jsonBook.getString("title"); this.author = jsonBook.getString("author");
        this.coverURL = jsonBook.getString("cover_url");
        this.id = jsonBook.getInt("book_id"); this.published = jsonBook.getInt("published");
    }

    protected Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        coverURL = in.readString();
        published = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setPublished(int published) {
        this.published = published;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    //getters
    public int getId() {
        return id;
    }
    public int getPublished() {
        return published;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getCoverURL() {
        return coverURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(coverURL);
        dest.writeInt(published);

    }
}
