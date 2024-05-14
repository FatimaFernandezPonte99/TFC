package com.example.bookbuddy;

public class LibroData {
    private String title;
    private String author;
    private int num_pages;

    public LibroData(String title, String author, int num_pages) {
        this.title=title;
        this.author=author;
        this.num_pages=num_pages;
    }

    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public int getNum_pages() {return num_pages;}
}
