package com.spotify.services.enums;

public enum PlaylistAttributesEnum {
    PLAYLISTID (0, "Playlist ID"),
    PLAYLISTNAME(1, "Playlist name"),
    CUSTOMERID (2, "customer ID"),
    PLAYLISTSONGS (3, "Playlist songs");
    private int index;
    private String headerName;

    PlaylistAttributesEnum(int index, String headerName) {
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
