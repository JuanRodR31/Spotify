package com.spotify.services;

import com.spotify.models.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistService {
    private List<Artist> artistList=new ArrayList<>();
    public void createArtist(String artistName){
        Artist artist=new Artist(artistName);
        artistList.add(artist);
    }
    public void deleteArtistFromName (String artistName){
        for (Artist artist: artistList ){
            if (artist.getArtistName().equals(artist)){
                artistList.remove(artist);
            }
        }
    }
    public void modifyArtist (String artistNameToModify, String newArtist){
        for (Artist artist: artistList ){
            if (artist.getArtistName().equals(artist)){
                artist.setArtistName(newArtist);
            }
        }
    }
}
