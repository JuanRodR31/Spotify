package com.spotify.services;

import com.spotify.models.Customer;
import com.spotify.models.Song;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class SongService implements Serializable {
    private  List<Song> songList=new ArrayList<>();
    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void createSong (String songName, String artistName, String genre, int songLength, String songAlbum){
        //crear las excepciones aqui
        Song song =new Song(songName,artistName,genre,songLength,songAlbum);
        songList.add(song);
    }
    public void listSongsThatContainKW (String keyWord, String parameter){
        switch (parameter){
            case "1":{
                for  (Song song:songList) {
                    if (song.getSongName().contains(keyWord)){
                        System.out.println(song);
                    }
                }
                break;
            }
            case "2":{
                for  (Song song:songList) {
                    if (song.getArtistName().contains(keyWord)){
                        System.out.println(song);
                    }
                }
                break;
            }
            case "3":{
                for  (Song song:songList) {
                    if (song.getGenre().contains(keyWord)){
                        System.out.println(song);
                    }
                }
                break;
            }
            case "4":{
                for  (Song song:songList) {
                    if (song.getAlbum().contains(keyWord)){
                        System.out.println(song);
                    }
                }
                break;
            }
        }

    }
    public void deleteSongUsingID (UUID IDToDelete){
        boolean checkDelete=false;
        for  (Song song:songList){
            if (IDToDelete==song.getSongIdentifier()){
                songList.remove(song);
                checkDelete=true;
            }
        }
        if (checkDelete){
            System.out.println("Song deleted successfully");
        }
        else{
            System.out.println("Song not found");
        }
    }
    public void listArtisteByMusicGenre (String genreToSearch){
        for (Song song: songList){
            if (song.getGenre().equals(genreToSearch)){
                System.out.println(song.getArtistName());
            }
        }
    }

    public void printSongList() {
        for (Song song: songList){
            System.out.println(song);
        }
    }
}
