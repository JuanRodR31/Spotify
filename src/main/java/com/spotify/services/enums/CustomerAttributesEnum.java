package com.spotify.services.enums;

public enum CustomerAttributesEnum {
    CUSTOMERTYPE (0,"customerType"),
    CUSTOMERID(1, "userID"),
    USERNAME (2, "userName"),
    PASSWORD (3, "customerPassword"),
    CUSTOMERNAME (4, "customerName"),
    CUSTOMERLASTNAME (5, "customerLastName"),
    CUSTOMERAGE(6, "customerAge"),
    FOLLOWEDARTISTS(7, "followedArtists");
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
