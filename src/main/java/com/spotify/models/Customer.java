package com.spotify.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer implements Serializable {
    private UUID userIdentifier;
    private String username;
    private String password;
    private String clientName;
    private String clientLastname;
    private int clientAge;
    private List<UUID> followedArtist=new ArrayList<>();
    private List<PlayList> clientPlayLists=new ArrayList<>();

    //Constructors
    public Customer() {
    }
    public Customer(String user, String password, String clientName, String clientLastname, int clientAge) {
        this.userIdentifier = UUID.randomUUID();
        this.username = user;
        this.password = password;
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.clientAge = clientAge;
    }

    public Customer(UUID userIdentifier, String user, String password, String clientName, String clientLastname, int clientAge) {
        this.userIdentifier = userIdentifier;
        this.username = user;
        this.password = password;
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.clientAge = clientAge;
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

    public UUID getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(UUID userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public List<UUID> getFollowedArtist() {
        return followedArtist;
    }

    public void setFollowedArtist(List<UUID> followedArtist) {
        this.followedArtist = followedArtist;
    }

    public void setClientPlayLists(List<PlayList> clientPlayLists) {
        this.clientPlayLists = clientPlayLists;
    }

    public List<PlayList> getClientPlayLists() {
        return clientPlayLists;
    }

    public void setClientPlayLists() {
        this.clientPlayLists = clientPlayLists;
    }
    public void addPlayList (String playListName){
        PlayList playList= new PlayList(playListName);
        getClientPlayLists().add(playList);
    }
    //TOSTRING
    @Override
    public String toString() {
        return "Client{" +
                "userIdentifier=" + userIdentifier +
                ", user='" + username + '\'' +
                ", password='" + password + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientLastname='" + clientLastname + '\'' +
                ", clientAge=" + clientAge +
                '}';
    }

}
