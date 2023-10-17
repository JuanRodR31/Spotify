package com.spotify.models;
import java.io.Serializable;
import java.util.UUID;
public class Artist implements Serializable {
    private UUID artistID;
    private String artistName;

    public Artist() {
    }

    public Artist(String artistName) {
        this.artistID = UUID.randomUUID();
        this.artistName = artistName;
    }

    public Artist(UUID artistID, String artistName) {
        this.artistID = artistID;
        this.artistName = artistName;
    }

    public UUID getArtistID() {
        return artistID;
    }

    public void setArtistID(UUID artistID) {
        this.artistID = artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistID=" + artistID +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
