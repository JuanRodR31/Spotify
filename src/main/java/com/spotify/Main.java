package com.spotify;

import com.spotify.exceptions.NotFoundException;
import com.spotify.exceptions.UserNameAlreadyTakenException;
import com.spotify.services.ArtistService;
import com.spotify.services.CustomerService;
import com.spotify.services.FileService;
import com.spotify.services.SongService;

import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;
public class Main {
    private static Scanner input=new Scanner(System.in);
    private static CustomerService customerServiceCall= new CustomerService();
    private static SongService songServiceCall = new SongService();
    private static ArtistService artistServiceCall= new ArtistService();
    private static final String userPattern = "^[a-zA-Z0-9-_-]{8,30}$";
    private static final String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*._-]).{8,}$";

    public static void main(String[] args) throws IOException, ClassNotFoundException, UserNameAlreadyTakenException {
        String option;
        final String customerCSVFilePath ="src/main/resources/customers.csv";
        final String songCSVFilePath = "src/main/resources/songs.csv";
        final String customerBinFileName= "src/main/resources/customers.bin";
        final String songBinFileName= "src/main/resources/songs.bin";
        final String artistCSVFilePath="src/main/resources/artists.csv";
        final String playlistCSVFilePah = "src/main/resources/playLists.csv";
        final String delimiter=";";
        FileService fileService = new FileService();

        do {

            System.out.println("Menu\n");
            System.out.println("Option 1: add new user to user list");
            System.out.println("Option 2: add new song to song list");
            System.out.println("Option 3: list songs that contain keyword");
            System.out.println("Option 4: List artist by genre");
            System.out.println("Option 5: Delete existing song using id");
            System.out.println("Option 6: Delete existing user using username");
            System.out.println("Option 7: Load user data from csv file");
            System.out.println("Option 8: Load song data from csv file");
            System.out.println("Option 9: Print customer list");
            System.out.println("Option 10: Print song list");
            System.out.println("Option 11: Create new playlist");
            System.out.println("Option 12: Add songs to existing playlist");
            System.out.println("Option 13: Follow an artist using user");
            System.out.println("Option 14: Save customer data in bin file");
            System.out.println("Option 15: Load customer data from bin file");
            System.out.println("Option 16: Save song data in bin file");
            System.out.println("Option 17: Load song data from bin file");
            System.out.println("Option 18: Create, Delete or modify artist");
            System.out.println("Option 19: Show Report");
            System.out.println("Option 20: import artists from csv file");
            System.out.println("Option 21: Print artist List");
            System.out.println("Option 0: Exit");
            System.out.println("Select your option: ");
            option = input.nextLine();
                switch (option) {

                    case "1": {
                        takeUserData();
                        break;
                    }
                    case "2": {
                        takeSongData();
                        break;
                    }
                    //3: list songs that contain keyword
                    case "3": {
                        listSongsThatContainKeyword();
                        break;
                    }
                    case "4": {
                        String genre = takeGenre();
                        List<String> artistsByGenre = songServiceCall.listArtistByMusicGenre(genre);
                        for (String artist : artistsByGenre) {
                            System.out.println(artist);
                        }
                        break;
                    }
                    //5: Delete existing song using id"
                    case "5": {
                        System.out.println("Enter song ID");
                        UUID deleteId = UUID.fromString(input.nextLine());
                        try {
                            songServiceCall.deleteSongByID(deleteId);
                            System.out.println("Song deleted successfully");
                        } catch (NotFoundException e) {
                            System.out.println("Song not found");
                        }
                        break;
                    }
                    case "6": {
                        System.out.println("Enter username");
                        String usernameToDelete = input.nextLine();
                        try {
                            customerServiceCall.deleteCustomerByUsername(usernameToDelete);
                            System.out.println("user deleted successfully");
                        } catch (NotFoundException e) {
                            System.out.println("Username not found");
                        }
                        break;
                    }
                    case "7": {
                        try {
                            customerServiceCall.readCustomersWithPlayListsFromCSV(customerCSVFilePath,delimiter,playlistCSVFilePah,fileService);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case "8": {
                        try {
                            songServiceCall.loadSongsFromCSVFile(songCSVFilePath, delimiter, fileService);
                        }catch (IOException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case "9": {
                        customerServiceCall.printCustomerMap();
                        break;
                    }
                    case "10": {
                        songServiceCall.printSongMap();
                        break;
                    }
                    //11. Create new playlist
                    case "11": {
                        System.out.println("Enter user: ");
                        String customerUserName = input.nextLine();
                        System.out.println("Enter playlist name: ");
                        String playListName = input.nextLine();
                        customerServiceCall.createNewPlayList(customerUserName, playListName);
                        break;
                    }
                    //12: Add songs to existing playlist
                    case "12": {
                        System.out.println("Enter user: ");
                        String customerUserName = input.nextLine();
                        System.out.println("Enter playlist ID: ");
                        UUID playlistID = UUID.fromString(input.nextLine());
                        System.out.println("Enter song ID: ");
                        UUID songID = UUID.fromString(input.nextLine());
                        boolean songExists = songServiceCall.verifyIfSongExists(songID);
                        if (songExists){
                            customerServiceCall.addSongsToCustomerPlayList(customerUserName,playlistID, songID);
                        }
                        else {
                            System.out.println("Song does not exist");
                        }

                        break;
                    }
                    //13: Follow an artist using user
                    case "13": {
                        System.out.println("Enter user: ");
                        String customerUserName = input.nextLine();
                        System.out.println("Enter artist ID: ");
                        UUID artistID = UUID.fromString(input.nextLine());
                        customerServiceCall.addFollowedArtistToCustomer(customerUserName, artistID, artistServiceCall);
                        break;
                    }
                    //14: Save customer data in bin file
                    case "14": {
                        customerServiceCall.saveCustomersToBinaryFileUsingTheEntireList(customerBinFileName, fileService);
                        break;
                    }
                    //15: Load customer data from bin file
                    case "15": {
                        customerServiceCall.loadCustomersFromBinaryFileUsingTheEntireList(customerBinFileName, fileService);
                        break;
                    }
                    //16: Save song data in bin file
                    case "16": {
                        songServiceCall.saveSongsToBinaryFileUsingTheEntireList(songBinFileName, fileService);
                        break;
                    }
                    //17: Load song data from bin file
                    case "17": {
                        songServiceCall.loadSongsFromBinaryFileUsingTheEntireList(songBinFileName, fileService);
                        break;
                    }
                    //18: Create, Delete or modify artist
                    case "18": {
                        System.out.println("1.Create artist\n2.Delete artist\n3. Modify artist");
                        String artistOption=input.nextLine();
                        switch (artistOption){
                            case "1":{
                                System.out.println("Enter artist name");
                                String artistName=input.nextLine();
                                artistServiceCall.createArtist(artistName);
                                break;
                            }
                            case "2":{
                                System.out.println("Enter artist name");
                                String artistName=input.nextLine();
                                artistServiceCall.deleteArtistFromName(artistName);
                                break;
                            }
                            case  "3":{
                                System.out.println("Enter artist name");
                                String artistName=input.nextLine();
                                System.out.println("Enter new artist name");
                                String newArtistName =input.nextLine();
                                artistServiceCall.modifyArtist(artistName,newArtistName);
                                break;
                            }
                        }
                        break;
                    }
                    //19: Show Reports
                    case "19":{
                        System.out.println("1. Show artist Followers\n2. Show artist popularity");
                        String reportOption=input.nextLine();
                        switch (reportOption){
                            case "1":{
                                printArtistFollowersReport(artistServiceCall,customerServiceCall,songServiceCall);
                                break;
                            }
                            case "2": {
                                System.out.println("Profe, le eché muchas ganas pero no pude, valore el esfuerzo :c");
                                break;
                            }
                        }

                        break;
                    }
                    //20: import artists from csv file
                    case "20":{
                        try {
                            artistServiceCall.loadArtistsFromCSVFile (artistCSVFilePath,delimiter,fileService);
                        } catch (NotFoundException e) {
                            System.out.println("File not found");
                        }
                        break;
                    }
                    //21: Print artist List
                    case "21": {
                        artistServiceCall.printArtistList();
                        break;
                    }
                    case "0": {
                        System.out.println("Bye!");
                        break;
                    }

                    default:
                        System.out.println("Unexpected value: " + option);
                }

            }
            while (!option.equalsIgnoreCase("0")) ;

    }
    private static void takeUserData (){
        System.out.println("Enter user: ");
        String userName=input.nextLine();
        System.out.println("Enter password: ");
        String userPassword=input.nextLine();
        System.out.println("Enter client name: ");
        String clientName=input.nextLine();
        System.out.println("Enter client lastname: ");
        String clientLastname=input.nextLine();
        System.out.println("Enter client age: ");
        int clientAge=parseInt(input.nextLine());
        try {
            customerServiceCall.addCustomerToDatabase(userName,userPassword,clientName,clientLastname,clientAge);
            System.out.println("Customer created successfully");
        }catch (UserNameAlreadyTakenException | IllegalArgumentException e){
        System.out.println(e.getMessage());
        }

    }
    private static void takeSongData (){
        System.out.println("Enter song name: ");
        String songName=input.nextLine();
        System.out.println("Enter artist name: ");
        String artistName=input.nextLine();
        System.out.println("Enter genre: ");
        String genre=input.nextLine();
        System.out.println("Enter song length: ");
        int songLength=parseInt(input.nextLine());
        System.out.println("Enter Album: ");
        String songAlbum=input.nextLine();
        try {
            songServiceCall.addSongToDatabase(songName, artistName, genre, songLength, songAlbum);
            System.out.println("Song created successfully");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }


    }
    private static void listSongsThatContainKeyword(){
        System.out.println("Enter search parameter: ");
        System.out.println("Option 1: Song name");
        System.out.println("Option 2: Artist name");
        System.out.println("Option 3: Genre");
        System.out.println("Option 4: Album");
        Set<Integer> parameter = Collections.singleton(Integer.valueOf(input.nextLine()));
        System.out.println("Enter search value: ");
        String keyWord = input.nextLine();
        List<String> filterResult = songServiceCall.getSongsFilteredBy(parameter, keyWord);
        for (String result : filterResult) {
            System.out.println(result);
        }
    }
    private static String takeGenre () {
        System.out.println("Enter genre: ");
        return input.nextLine();
    }
    public static void printArtistFollowersReport(ArtistService artistService,
                                                  CustomerService customerService,
                                                  SongService songService) {
        Map<String, Long> reporte = artistFollowersReport(artistService, customerService, songService);
        System.out.println("Artist    // Followers");
        for (Map.Entry<String, Long> entry : reporte.entrySet()) {
            String artist = entry.getKey();
            Long Followers = entry.getValue();
            System.out.println(artist +"  "+ Followers);
        }
    }
    public static Map<String, Long> artistFollowersReport(ArtistService artistService,
                                                          CustomerService customerService,
                                                          SongService songService) {
        List<UUID> idsSeguidores = customerService.obtainIDsOfAllFollowedArtists();
        Map<String, Long> cantidadDeSeguidoresParaCadaArtista = new HashMap<>();

        for (UUID idSeguidor : idsSeguidores) {
            String nombreArtista = getArtistByID(idSeguidor, artistService);
            cantidadDeSeguidoresParaCadaArtista.put(nombreArtista, cantidadDeSeguidoresParaCadaArtista.getOrDefault(nombreArtista, 0L) + 1);
        }

        return cantidadDeSeguidoresParaCadaArtista;
    }
    public static String getArtistByID(UUID followerID, ArtistService artistService) {
            return artistService.getArtistNameUsingID(followerID);
    }

}