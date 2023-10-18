package com.spotify.services.enums;

public enum CustomerAttributesEnum {
    CUSTOMERID(0, "userID"),
    USERNAME (1, "userName"),
    PASSWORD (2, "customerPassword"),
    CUSTOMERNAME (3, "customerName"),
    CUSTOMERLASTNAME (4, "customerLastName"),
    CUSTOMERAGE(5, "customerAge"),
    FOLLOWEDARTISTS(6, "followedArtists");
    private int index;
    private String headerName;

    CustomerAttributesEnum(int index, String headerName) {
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
