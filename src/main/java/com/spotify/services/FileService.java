package com.spotify.services;

import com.spotify.models.Artist;
import com.spotify.models.Customer;
import com.spotify.models.PlayList;
import com.spotify.models.Song;
import com.spotify.services.enums.CustomerAttributesEnum;
import com.spotify.services.enums.PlaylistAttributesEnum;
import com.spotify.services.enums.SongAttributesEnum;
import com.spotify.services.enums.ArtistAttributesEnum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileService implements Serializable{
    public static final String COMMA_DELIMITER = ",";
    public static final String OPEN_CURLY_BRACE = "{";
    public static final String CLOSE_CURLY_BRACE = "}";
    public static final String EMPTY_STRING = "";
    public List<Customer> readCustomersFromCSV(String path, String delimiter) throws IOException {

        File file = new File(path);

        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        List<Customer> customers = new ArrayList<>();

        for (String line : lines) {

            String[] values = line.split(delimiter);

            // Extract song data from the CSV line.
            Customer artist = getCustomerFromCSVLine(values);

            customers.add(artist);


        }
        return customers;
    }

    public List<Customer> readCustomersFromCSVUsingStreams(String path, String delimiter)
            throws IOException {
        return Files.lines(new File(path).toPath())
                .map(line -> line.split(delimiter))
                .map(this::getCustomerFromCSVLine)
                .collect(Collectors.toList());
    }

    private Customer getCustomerFromCSVLine(String[] values) {

        String id = values[CustomerAttributesEnum.CUSTOMERID.getIndex()];
        String username = values[CustomerAttributesEnum.USERNAME.getIndex()];
        String password = values[CustomerAttributesEnum.PASSWORD.getIndex()];
        String name = values[CustomerAttributesEnum.CUSTOMERNAME.getIndex()];
        String lastName = values[CustomerAttributesEnum.CUSTOMERLASTNAME.getIndex()];
        int age = Integer.parseInt(values[CustomerAttributesEnum.CUSTOMERAGE.getIndex()]);
        String artistFollowedIds = values[CustomerAttributesEnum.FOLLOWEDARTISTS.getIndex()];

        Customer customer = new Customer(UUID.fromString(id), username, password, name, lastName, age);

        String artistIdsFollowed =
                extractElementsBetweenCurlyBraces(artistFollowedIds);

        String[] artistIdsFollowedArray = splitAndDeleteSpaces(artistIdsFollowed, COMMA_DELIMITER);

        for(String artistIds : artistIdsFollowedArray){
            customer.addFollowedArtist(UUID.fromString(artistIds));
        }
        Set<UUID> artistIdsFollowedSet = Stream.of(artistIdsFollowedArray)
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        Customer alternativeCustomer = new Customer(UUID.fromString(id), username, password, name, lastName, age, artistIdsFollowedSet);
        return customer;
    }

    public List<Song> loadSongFromCSVFile(String path, String delimiter) throws IOException {
        File file = new File(path);

        List<String> lines =
                Files.readAllLines(file.toPath(),
                        StandardCharsets.UTF_8);

        List<Song> songList = new ArrayList<>();

        for (String line : lines) {
            String[] values = line.split(delimiter);
            UUID songID =
                    UUID.fromString(values[SongAttributesEnum.SONGID.getIndex()]);
            String songName =
                    values[SongAttributesEnum.SONGNAME.getIndex()];
            String artist=
                    values[SongAttributesEnum.ARTIST.getIndex()];
            String genre =
                    values[SongAttributesEnum.GENRE.getIndex()];
            int songLength =
                    Integer.valueOf(values[SongAttributesEnum.SONGLENGTH.getIndex()]);
            String album =
                    values[SongAttributesEnum.ALBUM.getIndex()];


            Song song = new Song(songID,songName,artist,genre,songLength,album);
            songList.add(song);
        }
        return songList;
    }

    public void saveCustomersIntoBinFile(String filePath, List<Customer> customers)
    throws IOException {
            File file = new File(filePath);
            FileService fileService = new FileService();
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)){
                oos.writeObject(customers);
                fos.close();
            }catch (NotSerializableException exception) {
                // Output expected NotSerializeableExceptions.
                System.out.println(exception);
            }
    }

    public ArrayList<Customer> loadCustomersFromBinFile(String filePath)throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ArrayList<Customer>) ois.readObject();

        }
    }

    public void saveSongsIntoBinFile(String filePath, List <Song> songs) throws IOException{
        File file = new File(filePath);

        //try with resources will close the file automatically
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            // Write the list of animals to the file.
            oos.writeObject(songs);
        }

    }


    public ArrayList<Song> loadSongFromBinFile(String filePath) throws IOException, ClassNotFoundException {

        File file = new File(filePath);
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ArrayList<Song>) ois.readObject();

        }

    }

    public List<Artist> readArtistsFromCSV(String path, String delimiter) throws IOException {
        File file = new File(path);

        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        List<Artist> artists = new ArrayList<>();

        for (String line : lines) {
            String[] values = line.split(delimiter);
            Artist artist = getArtistFromCSVLine(values);
            artists.add(artist);
        }
        return artists;
    }

    public List<Artist> readArtistsFromCSVUsingStreams(String path, String delimiter)
            throws IOException {
        return Files.lines(new File(path).toPath())
                .map(line -> line.split(delimiter))
                .map(this::getArtistFromCSVLine)
                .collect(Collectors.toList());
    }
    private Artist getArtistFromCSVLine(String[] values) {

        String id = values[ArtistAttributesEnum.ARTISTID.getIndex()];
        String name = values[ArtistAttributesEnum.ARTISTNAME.getIndex()];

        Artist artist = new Artist(UUID.fromString(id), name);

        return artist;


    }



    private String extractElementsBetweenCurlyBraces(String setString){

        return setString
                // Remove the curly braces from the string
                .replace(OPEN_CURLY_BRACE, EMPTY_STRING)
                .replace(CLOSE_CURLY_BRACE, EMPTY_STRING);
    }
    private String[] splitAndDeleteSpaces(String stringToSplit, String delimiter){

        return  Stream.of(stringToSplit.split(delimiter))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public Map<UUID, List<PlayList>> readPlayListFromCSV(String path, String delimiter) throws IOException {


        File file = new File(path);
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        Map<UUID,List<PlayList>> playListByCustomer = new HashMap<>();


        for(String line : lines) {
            String[] values = line.split(delimiter);

            Map.Entry<UUID,PlayList> playlistEntry = getPlayListFromCSVLine(values);

            if(!playListByCustomer.containsKey(playlistEntry.getKey())) {
                playListByCustomer.put(playlistEntry.getKey(), new ArrayList<>());

            }
            List<PlayList> customerPlayList = playListByCustomer.get(playlistEntry.getKey());

            customerPlayList.add(playlistEntry.getValue());

            playListByCustomer.put(playlistEntry.getKey(),customerPlayList);
        }

        return playListByCustomer;


    }

    public Map<UUID, List<PlayList>> readPlayListFromCSVUsingStreams(String path, String delimiter)
            throws IOException {

        return Files.lines(new File(path).toPath())
                .map(line -> line.split(delimiter))
                .map(this::getPlayListFromCSVLine)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    private Map.Entry<UUID,PlayList> getPlayListFromCSVLine(String[] values) {

        String id = values[PlaylistAttributesEnum.PLAYLISTID.getIndex()];
        String name = values[PlaylistAttributesEnum.PLAYLISTNAME.getIndex()];
        String customerId = values[PlaylistAttributesEnum.CUSTOMERID.getIndex()];
        String songIds = values[PlaylistAttributesEnum.PLAYLISTSONGS.getIndex()];

        String[] songIdsArray = getArrayFromStringSet(songIds);

        List<UUID> songIdsList = Stream.of(songIdsArray)
                .map(UUID::fromString)
                .collect(Collectors.toList());

        PlayList playlist = new PlayList(UUID.fromString(id),name, songIdsList);

        return Map.entry(UUID.fromString(customerId),playlist);

    }

    private String[] getArrayFromStringSet(String valueFromCSV) {

        String valueWithoutCurlyBraces = extractElementsBetweenCurlyBraces(valueFromCSV);
        return splitAndDeleteSpaces(valueWithoutCurlyBraces, COMMA_DELIMITER);

    }
}
