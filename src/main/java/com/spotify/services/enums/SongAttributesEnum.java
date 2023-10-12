package com.spotify.services.enums;

public enum SongAttributesEnum {
    SONGID(0, "songID"),
    SONGNAME(1, "songName"),
    ARTIST(2, "artistName"),
    GENRE (3,"genre"),
    SONGLENGTH(4, "songLength"),
    ALBUM (5, "album");
    private int index;
    private String headerName;

    SongAttributesEnum(int index, String headerName) {
        this.index = index;
        this.headerName = headerName;
    }
    public int getIndex() {
        return index;
    }
    public String getHeaderName() {
        return headerName;
    }
}

