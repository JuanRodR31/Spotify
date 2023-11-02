package com.spotify.models;

import com.spotify.exceptions.NotFoundException;

import java.util.*;

public class PremiumCustomer extends Customer{
    private List<PlayList> playlists;

    public PremiumCustomer(String customerType,String user, String password, String clientName, String clientLastname, int clientAge) {
        super(customerType,user, password, clientName, clientLastname, clientAge);
        this.playlists = new ArrayList<>();
    }

    public PremiumCustomer(String customerType,UUID customerIdentifier, String user, String password, String clientName, String clientLastname, int clientAge) {
        super(customerType,customerIdentifier, user, password, clientName, clientLastname, clientAge);
        this.playlists = new ArrayList<>();
    }

    public PremiumCustomer(String customerType,UUID customerIdentifier, String username, String password, String clientName, String clientLastname, int clientAge, Set<UUID> followedArtist) {
        super(customerType,customerIdentifier, username, password, clientName, clientLastname, clientAge, followedArtist);
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
    public void removeSongFromPlaylist(UUID playlistId, UUID songId) throws NotFoundException{
        boolean playlistExists =false;
        boolean songExists =false;
        for (PlayList playlistToSearch: playlists){
            if (playlistToSearch.getPlaylistID().equals(playlistId)){
                playlistExists=true;
                for (UUID songIdToSearch : playlistToSearch.getSongIDs()){
                    if  (songIdToSearch.equals(songId)){
                        playlistToSearch.getSongIDs().remove(songId);
                        songExists=true;
                    }
                }
                if (songExists==false){
                    throw new NotFoundException("song not found");
                }
            }
        }
        if (playlistExists=false) {
            throw new NotFoundException("playlist not found");
        }
    }

    @Override
    public List<UUID> getSongsFromPlaylist(UUID playlistId) throws NotFoundException{
        List <UUID> songsList=new ArrayList<>();
        boolean playListExists= false;
        for (PlayList playList: playlists){
            if (playList.getPlaylistID().equals(playlistId)){
                playListExists=true;
                songsList.addAll(playList.getSongIDs());
            }
        }
        if (!playListExists){
            throw new NotFoundException("Playlist not found");
        }
        return songsList;
    }
}
