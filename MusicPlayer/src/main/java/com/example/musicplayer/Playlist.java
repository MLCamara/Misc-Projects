package com.example.musicplayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Playlist<String,Song> {
    private HashMap<String,Song> palylist;


    public Playlist(String n, Song s){
        palylist = new HashMap<>();
        palylist.put(n,s);
    }
    public Playlist(){
        palylist = new HashMap<>();
    }

    public void add(String n, Song s){
        palylist.put(n,s);
    }

    public void remove(String n){
        palylist.remove(n);
    }
    public Collection<Song> getSongs(){
        return palylist.values();
    }
    public Set<String> getNames(){
        return palylist.keySet();
    }

    public boolean isEmpty(){
        return palylist.isEmpty();
    }
}
