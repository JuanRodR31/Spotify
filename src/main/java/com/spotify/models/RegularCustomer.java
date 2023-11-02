package com.spotify.models;

import com.spotify.exceptions.MaxSongsInPlayList;
import com.spotify.exceptions.NotFoundException;

import java.util.*;

public class RegularCustomer extends Customer {
    private PlayList uniquePlaylist;
    private static final String DEFAULT_PLAYLIST_NAME = "Playlist";

    public RegularCustomer(String customerType,String user, String password, String clientName, String clientLastname, int clientAge) {
        super(customerType,user, password, clientName, clientLastname, clientAge);
        int random = new Random().nextInt(10);
        this.uniquePlaylist = new PlayList(DEFAULT_PLAYLIST_NAME + random);
    }


    public RegularCustomer(String customerType,UUID customerIdentifier, String user, String password, String clientName, String clientLastname, int clientAge) {
        super(customerType, customerIdentifier, user, password, clientName, clientLastname, clientAge);
        int random = new Random().nextInt(10);
        this.uniquePlaylist = new PlayList(DEFAULT_PLAYLIST_NAME + random);
    }

    public RegularCustomer(String customerType,UUID customerIdentifier, String username, String password, String clientName, String clientLastname, int clientAge, Set<UUID> followedArtist) {
        super(customerType,customerIdentifier, username, password, clientName, clientLastname, clientAge, followedArtist);
        int random = new Random().nextInt(10);
        this.uniquePlaylist = new PlayList(DEFAULT_PLAYLIST_NAME + random);
    }


    @Override
    public void addPlaylist(String name) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Playlist"+ name + " cannot be added because you are a regular user");
    }

    @Override
    public List<PlayList> getPlaylists() {
        return List.of(uniquePlaylist) ;
    }

    @Override
    public void addPlaylists(List<PlayList> playlists) throws UnsupportedOperationException{
        if (playlists.size()==1){
            uniquePlaylist=playlists.get(0);
        }
        else {
            throw new UnsupportedOperationException("the list has more than one element or is empty");
        }
    }

    @Override
    public void removePlaylist(UUID playlistId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("you cant delete a playlist because you are a regular user");
    }

    @Override
    public void addSongToPlaylist(UUID playlistId, UUID songId) throws MaxSongsInPlayList {
        if (uniquePlaylist.getSongIDs().size()<=10){
            uniquePlaylist.addSong(songId);
        }
        else {
            throw new MaxSongsInPlayList("maximum song quantity reached (10)");
        }
    }

    @Override
    public void removeSongFromPlaylist(UUID playlistId, UUID songId) throws NotFoundException{
        boolean exists =false;
        if (uniquePlaylist.getPlaylistID().equals(playlistId)){
            for (UUID songIdToSearch : uniquePlaylist.getSongIDs()){
                if  (songIdToSearch.equals(songId)){
                    uniquePlaylist.getSongIDs().remove(songId);
                    exists=true;
                }
            }
            if (!exists){
                throw new NotFoundException("song not found");
            }
        }
        else {
            throw new NotFoundException("playlist not found");
        }
    }

    @Override
    public List<UUID> getSongsFromPlaylist(UUID playlistId) throws NotFoundException {
        List<UUID> songsList=new ArrayList<>();
        if(uniquePlaylist.getPlaylistID().equals(playlistId)){
            songsList.addAll(uniquePlaylist.getSongIDs());
        }
        else{
            throw new NotFoundException("Playlist not found");
        }
        return songsList;
    }
}
