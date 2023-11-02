package com.spotify.models;

import com.spotify.exceptions.MaxSongsInPlayList;
import com.spotify.exceptions.NotFoundException;

import java.io.Serializable;
import java.util.*;

public abstract class Customer implements Serializable {
    protected String customerType;
    protected UUID customerIdentifier;
    protected String username;
    protected String password;
    protected String clientName;
    protected String clientLastname;
    protected int clientAge;
    Set<UUID> followedArtist = new HashSet<>();

    //Constructors

    public Set<UUID> getFollowedArtist() {
        return followedArtist;
    }

    public void setFollowedArtist(Set<UUID> followedArtist) {
        this.followedArtist = followedArtist;
    }

    public Customer(String customerType,String user, String password, String clientName, String clientLastname, int clientAge) {
        this.customerType =customerType;
        this.customerIdentifier = UUID.randomUUID();
        this.username = user;
        this.password = password;
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.clientAge = clientAge;

    }

    public Customer(String customerType,UUID userUUID,String user, String password, String clientName, String clientLastname, int clientAge) {
        this.customerType =customerType;
        this.customerIdentifier = userUUID;
        this.username = user;
        this.password = password;
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.clientAge = clientAge;

    }

    public Customer(String customerType, UUID customerIdentifier, String username, String password, String clientName, String clientLastname, int clientAge, Set<UUID>followedArtist) {
        this.customerType =customerType;
        this.customerIdentifier = customerIdentifier;
        this.username = username;
        this.password = password;
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.clientAge = clientAge;
        this.followedArtist = followedArtist;
    }


    //Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUser(String user) {
        this.username = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLastname() {
        return clientLastname;
    }

    public void setClientLastname(String clientLastname) {
        this.clientLastname = clientLastname;
    }

    public int getClientAge() {
        return clientAge;
    }

    public void setClientAge(int clientAge) {
        this.clientAge = clientAge;
    }

    public UUID getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(UUID customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerType='" + customerType + '\'' +
                ", customerIdentifier=" + customerIdentifier +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientLastname='" + clientLastname + '\'' +
                ", clientAge=" + clientAge +
                ", followedArtist=" + followedArtist +
                '}';
    }

    public void addFollowedArtist(UUID artistID) {
        followedArtist.add(artistID);
    }


    public Integer countFollowedArtist (UUID artistID){
        Integer counter=0;
        for (UUID artist : followedArtist) {
            if (followedArtist.equals(artistID)){
                counter++;
            }
        }
        return counter;
    }
    public abstract void addPlaylist(String name);
    public abstract List<PlayList> getPlaylists();
    public abstract void addPlaylists(List<PlayList> playlists);
    public abstract void removePlaylist(UUID playlistId) throws NotFoundException;
    public abstract void addSongToPlaylist(UUID playlistId, UUID songId) throws MaxSongsInPlayList, NotFoundException;
    public abstract void removeSongFromPlaylist(UUID playlistId, UUID songId) throws NotFoundException;
    public abstract List<UUID> getSongsFromPlaylist(UUID playlistId) throws NotFoundException;
}
