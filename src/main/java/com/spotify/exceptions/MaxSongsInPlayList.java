package com.spotify.exceptions;

public class MaxSongsInPlayList extends Exception {
    public MaxSongsInPlayList(String message) {
        super(message);
    }
}
