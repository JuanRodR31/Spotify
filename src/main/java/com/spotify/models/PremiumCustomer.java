package com.spotify.models;

import com.spotify.exceptions.MaxSongsInPlayList;
import com.spotify.exceptions.NotFoundException;

import java.util.*;

public class PremiumCustomer extends Customer{
    private List<PlayList> playlists;

    public PremiumCustomer(String user, String password, String clientName, String clientLastname, int clientAge, List<PlayList> playlists) {
        super(user, password, clientName, clientLastname, clientAge);
        this.playlists = new ArrayList<>();
    }

    @Override
    public void addPlaylist(String name) {
        PlayList playlist1 = new PlayList(name);
        playlists.add(playlist1);
    }

    @Override
    public List<PlayList> getPlaylists() {
        return playlists;
    }

    @Override
    public void addPlaylists(List<PlayList> playlists1) {
        playlists.addAll(playlists1);
    }

    @Override
    public void removePlaylist(UUID playlistId) throws NotFoundException {
        boolean exists =false;
        for (PlayList playList: playlists){
            if (playList.getPlaylistID().equals(playlistId)){
                playlists.remove(playList);
                exists=true;
            }
        }
        if (!exists){
            throw new NotFoundException("playlist does not exist");
        }
        else {
            System.out.println("playlist deleted successfully");
        }
    }

    @Override
    public void addSongToPlaylist(UUID playlistId, UUID songId) throws NotFoundException {
        boolean exists=false;
        for (PlayList playList: playlists){
            if (playList.getPlaylistID().equals(playlistId)){
                playList.addSong(songId);
                exists=true;
            }
        }
        if (!exists){
            throw new NotFoundException("playlist not found, song can't be added.");
        }
    }

    @Override
    public void removeSongFromPlaylist(UUID playlistId, UUID songId) {
        for (PlayList playList: playlists){
            if (playList.getPlaylistID().equals(playlistId)){
                for (UUID songIDs: playList.getSongIDs()){
                    playList.getSongIDs().remove(songId);
                }
            }

        }
    }

    @Override
    public List<UUID> getSongsFromPlaylist(UUID playlistId) {
        List <UUID> songsList=new ArrayList<>();
        for (PlayList playList: playlists){
            if (playList.getPlaylistID().equals(playlistId)){
                songsList.addAll(playList.getSongIDs());
            }
        }
        return songsList;
    }
}
