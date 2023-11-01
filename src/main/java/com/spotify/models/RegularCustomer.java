package com.spotify.models;

import com.spotify.exceptions.MaxSongsInPlayList;

import java.util.List;
import java.util.UUID;

public class RegularCustomer extends Customer {
    PlayList playList;
    public RegularCustomer() {
    }

    @Override
    public void addPlaylist(String name) {

    }

    @Override
    public List<PlayList> getPlaylists() {
        return (List<PlayList>) playList;
    }

    @Override
    public void addPlaylists(List<PlayList> playlists) {

    }

    @Override
    public void removePlaylist(UUID playlistId) {

    }

    @Override
    public void addSongToPlaylist(UUID playlistId, UUID songId) throws MaxSongsInPlayList {

    }

    @Override
    public void removeSongFromPlaylist(UUID playlistId, UUID songId) {

    }

    @Override
    public List<UUID> getSongsFromPlaylist(UUID playlistId) {
        return null;
    }
}
