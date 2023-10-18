package com.spotify.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayList implements Serializable {
    private UUID playlistID;
    private String playlistName;
    private List<UUID> SongIDs =new ArrayList<>();

    public PlayList() {
    }

    public PlayList(String playlistName) {
        this.playlistID = UUID.randomUUID();
        this.playlistName = playlistName;
    }

    public PlayList(UUID playlistID, String playlistName, List<UUID> songIDs) {
        this.playlistID = playlistID;
        this.playlistName = playlistName;
        SongIDs = songIDs;
    }

    public UUID getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(UUID playlistID) {
        this.playlistID = playlistID;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<UUID> getSongIDs() {
        return SongIDs;
    }

    public void setSongIDs(List<UUID> songIDs) {
        SongIDs = songIDs;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "playlistID=" + playlistID +
                ", playlistName='" + playlistName + '\'' +
                ", SongIDs=" + SongIDs +
                '}';
    }

    public void addSong(UUID songID) {
        SongIDs.add(songID);
    }
}
