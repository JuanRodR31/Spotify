package com.spotify.services;

import com.spotify.exceptions.NotFoundException;
import com.spotify.models.Song;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class SongService implements Serializable {
    private  List<Song> songList=new ArrayList<>();
    Map<UUID,Song >songByID;
    public SongService (){
        this.songByID=new HashMap<>();
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public boolean addSongToDatabase(
            String songName,
            String artistName,
            String genre,
            int songLength,
            String album
    ) throws IllegalArgumentException {
        if (!validSongLength(songLength)) {
            throw new IllegalArgumentException("Song length can't be 0" );
        }
        if (songName==null|| songName.isEmpty()){
            throw new IllegalArgumentException("Song can't be null");
        }
        if (artistName==null|| artistName.isEmpty()){
            throw new IllegalArgumentException("artist can't be null");
        }
        if (genre == null || genre.isEmpty()){
            throw new IllegalArgumentException("genre can't be null");
        }
        if (album==null || album.isEmpty()){
            throw new IllegalArgumentException("album can't be null");
        }
        Song song = new Song (songName,artistName,genre,songLength,album);
        //put method returns null if the key is not present in the map
        return addSongToDatabase(song);

    }

    private boolean addSongToDatabase(Song song) throws IllegalArgumentException {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }
        return songByID.put(song.getSongIdentifier(), song) == null;
    }
    private boolean addSongToDatabase(List<Song> songs) {

        return songs.stream().allMatch(
                song -> addSongToDatabase(song)
        );
    }
    private boolean validSongLength (int songLength){
        return songLength>=1;
    }


    public List<String> getSongsFilteredBy(Set<Integer> searchCriterias, String searchedValue) {

        Predicate<Song> songFilterBySearchCriteria = getSongFilter(searchCriterias, searchedValue);

        return songByID.values().stream()
                .filter(songFilterBySearchCriteria)
                .map(Song::toString)
                .toList();

    }
    private Predicate<Song> getSongFilter(Set<Integer> searchCriterias, String searchedValue) {

        return song -> searchCriterias.stream()
                .anyMatch(searchCriteria -> getSearchValue(song, searchCriteria).equalsIgnoreCase(searchedValue));
    }
    private String getSearchValue(Song song, Integer searchCriteria) {
        return switch (searchCriteria) {
            case 1 -> song.getSongName();
            case 2 -> song.getArtistName();
            case 3 -> song.getGenre();
            case 4 -> song.getAlbum();
            default -> throw new IllegalArgumentException("Invalid search criteria");
        };
    }
    public void deleteSongByID(UUID songId) throws NullPointerException, NotFoundException {

        if (!songByID.containsKey(songId)) {
            throw new NotFoundException(String.format("Song with id %s not found", songId));
        }

        Song song = songByID.get(songId);
        songByID.remove(songId);
    }

    public List<String> listArtistByMusicGenre (String genreToSearch){
        return songByID.values().stream()
                .filter(song -> song.getGenre().equalsIgnoreCase(genreToSearch))
                .map(Song::getArtistName)
                .distinct()
                .toList();

    }
    public List<String> listAllArtists (){
        return songByID.values().stream()
                .map(Song::getArtistName)
                .distinct()
                .toList();
    }

    public void printSongList() {
        for (Song song: songList){
            System.out.println(song);
        }
    }


    protected void clearDatabase() {
        songByID.clear();
    }
    public boolean loadSongsFromCSVFile(String path,
                                            String delimiter,
                                            FileService fileService)
            throws IOException{

        List<Song> songs = fileService.loadSongFromCSVFile(path, delimiter);

        return addSongToDatabase(songs);
    }

    public void loadSongsFromBinaryFileUsingTheEntireList(String filePath,
                                                           FileService fileService) throws IOException, ClassNotFoundException {

        List<Song> songs =
                fileService.loadSongFromBinFile(filePath);
        clearDatabase();

        addSongToDatabase(songs);

    }
    public void saveSongsToBinaryFileUsingTheEntireList(String filePath,
                                                            FileService fileService) throws IOException {

        List<Song> songs = new ArrayList<>(songByID.values());
        fileService.saveSongsIntoBinFile(filePath, songs);

    }
    public void printSongMap (){
        for (Map.Entry songs: songByID.entrySet()){
            System.out.println(songs.getValue());
        }
    }

    public boolean verifyIfSongExists(UUID songID) {
        for (Song song : songByID.values()){
            if (song.getSongIdentifier().equals(songID)){
                return true;
            }
        }
        return false;
    }

    public List<UUID> getALlIDs() {
        List<UUID> allIDs = new ArrayList<>();
        for (Song song : songByID.values()){
            allIDs.add(song.getSongIdentifier());
        }
        return allIDs;
    }

}
