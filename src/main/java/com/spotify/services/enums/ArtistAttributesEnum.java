package com.spotify.services.enums;

public enum ArtistAttributesEnum {
    ARTISTID(0, "userID"),
    ARTISTNAME(1,"artistName");
    private int index;
    private String headerName;
    ArtistAttributesEnum(int index, String headerName) {
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
