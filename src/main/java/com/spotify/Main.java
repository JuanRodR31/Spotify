package com.spotify;

import com.spotify.exceptions.MaxSongsInPlayList;
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
    private static final String customerCSVFilePath ="src/main/resources/customers.csv";
    private static final String songCSVFilePath = "src/main/resources/songs.csv";
    private static final String customerBinFileName= "src/main/resources/customers.bin";
    private static final String songBinFileName= "src/main/resources/songs.bin";
    private static final String artistCSVFilePath="src/main/resources/artists.csv";
    private static final String playlistCSVFilePah = "src/main/resources/playLists.csv";
    private static final String delimiter=";";
    private static FileService fileService = new FileService();

    public static void main(String[] args) throws IOException, ClassNotFoundException, UserNameAlreadyTakenException {
        String option;
        do {
            printMenu();
            option = input.nextLine();
            switch (option) {
                case "1" -> createNewUser();
                case "2" -> createNewSong();
                case "3" -> listSongsThatContainKeyword();
                case "4" -> printArtistFromAGenre();
                case "5" -> deleteSongUsingID();
                case "6" -> deleteUserUsingUsername();
                case "7" -> loadCustomerFromCSV();
                case "8" -> loadSongsFromCSVFile();
                case "9" -> customerServiceCall.printCustomerMap();
                case "10" -> songServiceCall.printSongMap();
                case "11" -> createNewPlayList();
                case "12" -> addSongsToPlayList();
                case "13" -> followAnArtistUsingID();
                //14: Save customer data in bin file
                case "14" -> customerServiceCall.saveCustomersToBinaryFileUsingTheEntireList(customerBinFileName, fileService);
                //15: Load customer data from bin file
                case "15" -> customerServiceCall.loadCustomersFromBinaryFileUsingTheEntireList(customerBinFileName, fileService);
                //16: Save song data in bin file
                case "16" -> songServiceCall.saveSongsToBinaryFileUsingTheEntireList(songBinFileName, fileService);
                //17: Load song data from bin file
                case "17" -> songServiceCall.loadSongsFromBinaryFileUsingTheEntireList(songBinFileName, fileService);
                case "18" -> createDeleteOrModifyArtist();
                case "19" -> showReports();
                case "20" ->loadArtistFromCSVFile();
                case "21" -> artistServiceCall.printArtistList();
                case "22" -> removeSongFromPlayList();
                case "23" -> showSongsOfAPlaylist();
                case "24" -> playSongs();
                case "0" -> System.out.println("Bye!");
                default -> System.out.println("Unexpected value: " + option);
            }
        } while (!option.equals("0")) ;
    }

        private static void createNewUser() {
            System.out.println("Enter customer type: ");
            String customerType = input.nextLine();
            System.out.println("Enter user: ");
            String userName = input.nextLine();
            System.out.println("Enter password: ");
            String userPassword = input.nextLine();
            System.out.println("Enter client name: ");
            String clientName = input.nextLine();
            System.out.println("Enter client lastname: ");
            String clientLastname = input.nextLine();
            System.out.println("Enter client age: ");
            int clientAge = parseInt(input.nextLine());
            try {
                customerServiceCall.addCustomerToDatabase(customerType,userName, userPassword, clientName, clientLastname, clientAge);
                System.out.println("Customer created successfully");
            } catch (UserNameAlreadyTakenException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
        private static void createNewSong() {
            System.out.println("Enter song name: ");
            String songName = input.nextLine();
            System.out.println("Enter artist name: ");
            String artistName = input.nextLine();
            System.out.println("Enter genre: ");
            String genre = input.nextLine();
            System.out.println("Enter song length: ");
            int songLength = parseInt(input.nextLine());
            System.out.println("Enter Album: ");
            String songAlbum = input.nextLine();
            try {
                songServiceCall.addSongToDatabase(songName, artistName, genre, songLength, songAlbum);
                System.out.println("Song created successfully");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }


        }
        private static void listSongsThatContainKeyword () {
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
        public static void printArtistFollowersReport (ArtistService artistService,
                CustomerService customerService){
            Map<String, Long> report = artistFollowersReport(artistService, customerService);
            System.out.println("Artist  // Followers");
            for (Map.Entry<String, Long> entry : report.entrySet()) {
                String artist = entry.getKey();
                Long Followers = entry.getValue();
                System.out.println(artist + "\t" + Followers);
            }
        }
        public static Map<String, Long> artistFollowersReport (ArtistService artistService,
                CustomerService customerService){
            List<UUID> followersIds = customerService.obtainIDsOfAllFollowedArtists();
            Map<String, Long> followerQuantityPerArtist = new HashMap<>();

            for (UUID followerID : followersIds) {
                String artistName = getArtistByID(followerID, artistService);
                followerQuantityPerArtist.put(artistName, followerQuantityPerArtist.getOrDefault(artistName, 0L) + 1);
            }

            return followerQuantityPerArtist;
        }
        public static String getArtistByID (UUID followerID, ArtistService artistService){
            return artistService.getArtistNameUsingID(followerID);
        }
        private static void printArtistFromAGenre () {
            String genre = takeGenre();
            List<String> artistsByGenre = songServiceCall.listArtistByMusicGenre(genre);
            for (String artist : artistsByGenre) {
                System.out.println(artist);
            }
        }
        private static void deleteSongUsingID () {
            System.out.println("Enter song ID");
            UUID deleteId = UUID.fromString(input.nextLine());
            try {
                songServiceCall.deleteSongByID(deleteId);
                System.out.println("Song deleted successfully");
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        private static void loadSongsFromCSVFile () {
            try {
                songServiceCall.loadSongsFromCSVFile(songCSVFilePath, delimiter, fileService);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        private static void deleteUserUsingUsername () {
            System.out.println("Enter username");
            String usernameToDelete = input.nextLine();
            try {
                customerServiceCall.deleteCustomerByUsername(usernameToDelete);
                System.out.println("user deleted successfully");
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        private static void loadArtistFromCSVFile () {
            try {
                artistServiceCall.loadArtistsFromCSVFile(artistCSVFilePath, delimiter, fileService);
            } catch (NotFoundException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        private static void loadCustomerFromCSV (){
            try {
                customerServiceCall.readCustomersWithPlayListsFromCSV(customerCSVFilePath,delimiter,playlistCSVFilePah,fileService);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        private static void followAnArtistUsingID(){
            System.out.println("Enter user: ");
            String customerUserName = input.nextLine();
            System.out.println("Enter artist ID: ");
            UUID artistID = UUID.fromString(input.nextLine());
            customerServiceCall.addFollowedArtistToCustomer(customerUserName, artistID, artistServiceCall);
        }
        private static void createNewPlayList (){
            System.out.println("Enter user: ");
            String playlistName="null";
            String customerUserName = input.nextLine();
            if (customerServiceCall.customerIsPremium(customerUserName)){
                System.out.println("Enter playlist name: ");
                playlistName = input.nextLine();
            }
            try {
                customerServiceCall.createNewPlayList(customerUserName,playlistName);
            }catch (UnsupportedOperationException e){
                System.out.println(e.getMessage());
            }
        }
        private static void addSongsToPlayList (){
            System.out.println("Enter user: ");
            String customerUserName = input.nextLine();
            System.out.println("Enter playlist ID: ");
            UUID playlistID = UUID.fromString(input.nextLine());
            System.out.println("Enter song ID: ");
            UUID songID = UUID.fromString(input.nextLine());
            boolean songExists = songServiceCall.verifyIfSongExists(songID);
            if (songExists) {

                try {
                    customerServiceCall.addSongsToCustomerPlayList(customerUserName, playlistID, songID);
                } catch (MaxSongsInPlayList | NotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Song does not exist");
            }
        }
        private static void showReports (){
            System.out.println("1. Show artist Followers\n" +
                    "2. Show artist popularity\n" +
                    "3. Show unique Songs");
            String reportOption = input.nextLine();
            switch (reportOption) {
                case "1"-> printArtistFollowersReport(artistServiceCall, customerServiceCall);
                case "2"-> printArtistPopularityReport ();
                case "3"-> printUniqueSongs ();
            }
        }

    private static void printUniqueSongs() {
        List <UUID> allSongs=customerServiceCall.getAllSongsUUIDsFromPlayList();
        List <UUID> uniqueSongs= customerServiceCall.uniqueUUIDs(allSongs);
        List <String> uniqueSongsByName =songServiceCall.songIdsToSongNames(uniqueSongs);
        uniqueSongsByName.forEach(System.out::println);
    }

    private static void printArtistPopularityReport() {
        List <UUID> allSongs= customerServiceCall.getAllSongsUUIDsFromPlayList();
        List <String> songsToArtist= songServiceCall.transformSongIDsToArtistList(allSongs);
        List <String> uniqueArtists = songServiceCall.listUniqueArtists(songsToArtist);
        List <Integer> artistPopularityInUnits= songServiceCall.artistPopularityInUnits(uniqueArtists,songsToArtist);
        Integer maxPopularity= songServiceCall.getMaxPopularity(artistPopularityInUnits);
        List <Double> percentageList = songServiceCall.getpercentageList(artistPopularityInUnits,maxPopularity);
        for (int i = 0; i < uniqueArtists.size(); i++) {
            System.out.println(uniqueArtists.get(i) + " - " + artistPopularityInUnits.get(i)+" - "+ percentageList.get(i)+"%");
        }
    }

    private static void createDeleteOrModifyArtist () {
            System.out.println("1.Create artist\n2.Delete artist\n3. Modify artist");
            String artistOption = input.nextLine();
            switch (artistOption) {
                case "1"-> createArtist();
                case "2"-> deleteArtist();
                case "3" -> modifyArtist();
            }
        }
        private static void createArtist(){
            System.out.println("Enter artist name");
            String artistName = input.nextLine();
            artistServiceCall.createArtist(artistName);

        }
        private static void deleteArtist (){
            System.out.println("Enter artist name");
            String artistName = input.nextLine();
            artistServiceCall.deleteArtistFromName(artistName);
        }
        private static void modifyArtist (){
            System.out.println("Enter artist name");
            String artistName = input.nextLine();
            System.out.println("Enter new artist name");
            String newArtistName = input.nextLine();
            artistServiceCall.modifyArtist(artistName, newArtistName);
        }
        private static void removeSongFromPlayList(){
            System.out.println("Enter user: ");
            String customerUserName = input.nextLine();
            System.out.println("Enter playlist ID: ");
            UUID playlistID = UUID.fromString(input.nextLine());
            System.out.println("Enter song ID: ");
            UUID songID = UUID.fromString(input.nextLine());
            try {
                customerServiceCall.deleteSongFromAPlayList(customerUserName,playlistID,songID);
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        private static void showSongsOfAPlaylist (){
            System.out.println("Enter user: ");
            String customerUserName = input.nextLine();
            System.out.println("Enter playlist ID: ");
            UUID playlistID = UUID.fromString(input.nextLine());
            try {
                List<UUID>finalList=customerServiceCall.getSongsFromAnUserPlaylist(customerUserName,playlistID);
                List <String> finalListWithNames =songServiceCall.songIdsToSongNames(finalList);
                finalListWithNames.forEach(System.out::println);
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        private static void playSongs (){
            System.out.println("Enter user: ");
            String customerUserName = input.nextLine();
            System.out.println("Enter playlist ID: ");
            UUID playlistID = UUID.fromString(input.nextLine());
            List<UUID>finalList= null;
            try {
                finalList = customerServiceCall.getSongsFromAnUserPlaylist(customerUserName,playlistID);
                List <String> finalListWithNames =songServiceCall.songIdsToSongNames(finalList);
                List <String> playlistToPlay = songServiceCall.play (finalListWithNames);
                playlistToPlay.forEach(System.out::println);
            } catch (NotFoundException e) {
                System.out.println(e.getMessage());
            }


        }
        private static void printMenu(){
            System.out.println("Menu\n" +
                    "Option 1: add new user to user list\n" +
                    "Option 2: add new song to song list\n" +
                    "Option 3: list songs that contain keyword\n" +
                    "Option 4: List artist by genre\n" +
                    "Option 5: Delete existing song using id\n" +
                    "Option 6: Delete existing user using username\n" +
                    "Option 7: Load user data from csv file\n" +
                    "Option 8: Load song data from csv file\n" +
                    "Option 9: Print customer list\n" +
                    "Option 10: Print song list\n" +
                    "Option 11: Create new playlist\n" +
                    "Option 12: Add songs to existing playlist\n" +
                    "Option 13: Follow an artist using user\n" +
                    "Option 14: Save customer data in bin file\n" +
                    "Option 15: Load customer data from bin file\n" +
                    "Option 16: Save song data in bin file\n" +
                    "Option 17: Load song data from bin file\n" +
                    "Option 18: Create, Delete or modify artist\n" +
                    "Option 19: Show Report\n" +
                    "Option 20: import artists from csv file\n" +
                    "Option 21: Print artist List\n" +
                    "Option 22: Remove song from a playlist\n" +
                    "Option 23: Print songs from a playlist\n" +
                    "Option 24: Play songs from a playlist\n" +
                    "Option 0: Exit\n" +
                    "Select your option: ");
        }
}

