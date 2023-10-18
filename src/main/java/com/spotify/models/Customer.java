package com.spotify.models;

import com.spotify.Main;
import com.spotify.services.FileService;

import java.io.Serializable;
import java.util.*;

public class Customer implements Serializable {
    private UUID userIdentifier;
    private String username;
    private String password;
    private String clientName;
    private String clientLastname;
    private int clientAge;
    Set<UUID> followedArtist = new HashSet<>();
    private Map <UUID, PlayList> clientPlayListsbyID;
    //Constructors
    public Customer(){
        this.clientPlayListsbyID= new HashMap<>();
    }

    public Set<UUID> getFollowedArtist() {
        return followedArtist;
    }

    public void setFollowedArtist(Set<UUID> followedArtist) {
        this.followedArtist = followedArtist;
    }

    public Customer(String user, String password, String clientName, String clientLastname, int clientAge) {
        this.userIdentifier = UUID.randomUUID();
        this.username = user;
        this.password = password;
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.clientAge = clientAge;
        this.clientPlayListsbyID=new HashMap<>();

    }

    public Customer(UUID userIdentifier, String user, String password, String clientName, String clientLastname, int clientAge) {
        this.userIdentifier = userIdentifier;
        this.username = user;
        this.password = password;
        this.clientName = clientName;
        this.clientLastname = clientLastname;
        this.clientAge = clientAge;
        this.clientPlayListsbyID=new HashMap<>();
    }

    public Customer(UUID userIdentifier, String username, String password, String clientName, String clientLastname, int clientAge, Set<UUID>followedArtist) {
        this.userIdentifier = userIdentifier;
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

    public UUID getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(UUID userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void addPlaylist (PlayList playlist){
        clientPlayListsbyID.put(playlist.getPlaylistID(),playlist);
    }

    public Map<UUID, PlayList> getClientPlayListsbyID() {
        return clientPlayListsbyID;
    }

    public void setClientPlayListsbyID(Map<UUID, PlayList> clientPlayListsbyID) {
        this.clientPlayListsbyID = clientPlayListsbyID;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userIdentifier=" + userIdentifier +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientLastname='" + clientLastname + '\'' +
                ", clientAge=" + clientAge +
                ", followedArtist=" + followedArtist +
                ", clientPlayListsbyID=" + clientPlayListsbyID.values().toString()+
                '}';
    }

    public void addFollowedArtist(UUID artistID) {
        followedArtist.add(artistID);
    }

    public void addPlayLists(List<PlayList> playlists) {
        for (PlayList playlist : playlists) {
            UUID playlistID = playlist.getPlaylistID();
            clientPlayListsbyID.put(playlistID, playlist);
        }
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
}
