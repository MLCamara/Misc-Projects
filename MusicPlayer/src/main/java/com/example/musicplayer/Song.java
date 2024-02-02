package com.example.musicplayer;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import java.io.File;
import java.io.IOException;

public class Song {
    private String title;
    private String artist;
    private File file;
    public static final String NA = "N/A";
    public static final String UA = "Unknown Artist";
    public Song(File f){
        if(f != null){
            file = f;
            try {
                MP3File mp3File = new MP3File(f);
                Tag tag = mp3File.getTag();
                if(tag != null) {
                    artist = tag.getFirst(FieldKey.ARTIST);
                    title = tag.getFirst(FieldKey.TITLE);
                }
            } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public String getTitle(){
        if(title == null || title.isEmpty() && file != null){
            return file.getName();
        }
        return title;
    }
    public String getArtist(){
        if(artist == null || artist.isEmpty() && file != null){
            return UA;
        }
        return artist;
    }
    public File getFile(){
        return file;
    }

    public String toURItoString(){
        return file.toURI().toString();
    }

    public boolean isNull(){
        if(file == null){
            return true;
        }
        return false;
    }

    public String getTitleAndName(){
        if (this.isNull()) {
            return NA;
        }
        return this.getTitle() + " - " + this.getArtist();
    }
}
