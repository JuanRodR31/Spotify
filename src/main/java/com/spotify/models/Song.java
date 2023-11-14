
package com.spotify.models;

import com.spotify.Playable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class Song implements Serializable {
    private UUID songIdentifier;
    private String songName;
    private String artistName;
    private String genre;
    private int songLength;
    private String Album;

    //Constructors

    public Song() {
    }

    public Song( String songName, String artistName, String genre, int songLength, String album) {
        this.songIdentifier = UUID.randomUUID();
        this.songName = songName;
        this.artistName = artistName;
        this.genre = genre;
        this.songLength = songLength;
        Album = album;
    }

    public Song(UUID songIdentifier, String songName, String artistName, String genre, int songLength, String album) {
        this.songIdentifier = songIdentifier;
        this.songName = songName;
        this.artistName = artistName;
        this.genre = genre;
        this.songLength = songLength;
        Album = album;
    }
    //Getters and Setters

    public UUID getSongIdentifier() {
        return songIdentifier;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getSongLength() {
        return songLength;
    }

    public void setSongLength(int songLength) {
        this.songLength = songLength;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songIdentifier=" + songIdentifier +
                ", songName='" + songName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", genre='" + genre + '\'' +
                ", songLength=" + songLength +
                ", Album='" + Album + '\'' +
                '}';
    }

}
