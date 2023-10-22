package com.example.androidapp;

import android.content.res.Resources;

public class Track {
    public String name  = "No name";
    public String author = "No author";
    public boolean isPlaying = false;
    //public String author = "Unknown";
    int resId;
    public Track(int r, String name, String author){ setName(name); setRes(r); setAuthor(author);}
    public void setName(String name) { this.name = name; }
    public void setRes(int res) { this.resId = res; }
    public void setAuthor(String author) { this.author = author; }
}
