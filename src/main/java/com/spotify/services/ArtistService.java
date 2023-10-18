package com.spotify.services;

import com.spotify.exceptions.NotFoundException;
import com.spotify.models.Artist;
import com.spotify.models.Customer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtistService implements Serializable {
    private List<Artist> artistList=new ArrayList<>();

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public void createArtist(String artistName){
        Artist artist=new Artist(artistName);
        artistList.add(artist);
    }
    public void deleteArtistFromName (String artistName){
        for (Artist artist: artistList ){
            if (artist.getArtistName().equals(artistName)){
                artistList.remove(artist);
            }
        }
    }
    public void modifyArtist (String artistNameToModify, String newArtistData){
        for (Artist artist: artistList ){
            if (artist.getArtistName().equals(artistNameToModify)){
                artist.setArtistName(newArtistData);
            }
        }
    }

    public boolean verifyIfArtistExists (UUID artistToSearch){
        for (Artist artist : artistList ) {
            if (artist.getArtistID().equals(artistToSearch)) {
                return true;
            }
        }
        return false;
    }
    public void loadArtistsFromCSVFile(String path,
                                            String delimiter,
                                            FileService fileService)
            throws IOException, NotFoundException {

        List<Artist> artists = fileService.readArtistsFromCSVUsingStreams(path,delimiter);
        artistList=artists;
    }

    public void printArtistList() {
        for (Artist artist: artistList){
            System.out.println(artist.toString());
        }
    }

    public String getArtistNameUsingID(UUID idArtista) {
        for (Artist artist : artistList) {
            if (artist.getArtistID().equals(idArtista)) {
                return artist.getArtistName();
            }
        }
        return null;
    }
}
