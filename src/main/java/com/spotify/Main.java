package com.spotify;

import com.spotify.exceptions.IllegalArgumentException;
import com.spotify.exceptions.NotFoundException;
import com.spotify.exceptions.UserNameAlreadyTakenException;
import com.spotify.models.Customer;
import com.spotify.models.Song;
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
    private static FileService fileServiceCall =new FileService();
    public static void main(String[] args) throws IOException, ClassNotFoundException, UserNameAlreadyTakenException {
        String option;
        final String customerCSVFilePath ="src/main/resources/customers.csv";
        final String songCSVFilePath = "src/main/resources/songs.csv";
        final String customerBinFileName= "src/main/resources/customers.bin";
        final String songBinFileName= "src/main/resources/songs.bin";
        final String delimiter=";";
        FileService fileService = new FileService();

        do{
            System.out.println("Menu\n");
            System.out.println("Option 1: add new user to user list");
            System.out.println("Option 2: add new song to song list");
            System.out.println("Option 3: list songs that contain keyword");
            System.out.println("Option 4: List artist by genre");
            System.out.println("Option 5: Delete existing song using id");
            System.out.println("Option 6: Delete existing user using username");
            System.out.println("Option 7: Load user data from csv file");
            System.out.println("Option 8: Load song data from file");
            System.out.println("Option 9: Print customer list");
            System.out.println("Option 10: Print song list");
            System.out.println("Option 11: Create new playlist");
            System.out.println("Option 12: Add songs to existing playlist");
            System.out.println("Option 13: Follow an artist using user");
            System.out.println("Option 14: Save customer data in bin file");
            System.out.println("Option 15: Load customer data from bin file");
            System.out.println("Option 16: Save song data in bin file");
            System.out.println("Option 17: Load song data from bin file");
            System.out.println("Option 18: Show Report");
            System.out.println("Option 0: Exit");
            System.out.println("Select your option: ");
            option=input.nextLine();
            switch (option){
                case "1": {
                    System.out.println("Option 0: Exit");
                    System.out.println("Select your option: ");
                    takeUserData();
                    break;
                }
                case "2": {
                    takeSongData();
                    break;
                }
                //3: list songs that contain keyword
                case "3": {
                    System.out.println("Enter search parameter: ");
                    System.out.println("Option 1: Song name");
                    System.out.println("Option 2: Artist name");
                    System.out.println("Option 3: Genre");
                    System.out.println("Option 4: Album");
                    Set<Integer> parameter= Collections.singleton(Integer.valueOf(input.nextLine()));
                    System.out.println("Enter search value: ");
                    String keyWord=input.nextLine();
                    List<String> filterResult=songServiceCall.getSongsFilteredBy(parameter,keyWord);
                    for (String result: filterResult){
                        System.out.println(result);
                    }
                    break;
                }
                case "4": {
                    String genre= takeGenre();
                    List<String> artistsByGenre= songServiceCall.listArtistByMusicGenre(genre);
                    for (String artist: artistsByGenre){
                        System.out.println(artist);
                    }
                    break;
                }
                case "5": {
                    System.out.println("Enter song ID");
                    UUID deleteId= UUID.fromString(input.nextLine());
                    songServiceCall.deleteSongUsingID(deleteId);
                    break;
                }
                case "6": {
                    System.out.println("Enter username");
                    String usernameToDelete=input.nextLine();
                    try {
                        customerServiceCall.deleteCustomerByUsername(usernameToDelete);
                    } catch (NotFoundException e) {
                        System.out.println("Username not found");
                    }
                    break;
                }
                case "7":{
                    try {
                        customerServiceCall.loadCustomersFromCSVFile(customerCSVFilePath,delimiter,fileService);
                    } catch (NotFoundException e) {
                        System.out.println("file not found");
                    }
                }
                case "8":{
                        songServiceCall.loadSongsFromCSVFile(songCSVFilePath,delimiter,fileService);
                    break;
                }
                case "9":{
                    customerServiceCall.printCustomerMap();
                    break;
                }
                case "10":{
                    songServiceCall.printSongMap();
                    break;
                }
                //11. Create new playlist
                case "11":{
                    System.out.println("Enter user: ");
                    String customerUserName=input.nextLine();
                    System.out.println("Enter playlist name: ");
                    String playListName=input.nextLine();
                    //customerServiceCall.createNewPlayList(customerUserName, playListName);
                    break;
                }
                //12: Add songs to existing playlist
                case "12":{
                    System.out.println("Enter user: ");
                    String customerUserName=input.nextLine();
                    System.out.println("Enter playlist ID: ");
                    UUID playlistID= UUID.fromString(input.nextLine());
                    System.out.println("Enter song ID: ");
                    UUID songID= UUID.fromString(input.nextLine());
                    /*System.out.println("Enter song name to search: ");
                    String songNameToSearch= input.nextLine();*/
                    //customerServiceCall.addSongsToCustomerPlayList(customerUserName,playlistID, songID);
                    break;
                }
                //13: Follow an artist using user
                case "13":{
                    System.out.println("Enter user: ");
                    String customerUserName=input.nextLine();
                    System.out.println("Enter playlist ID: ");
                    UUID artistID= UUID.fromString(input.nextLine());
                    customerServiceCall.followArtist(customerUserName,artistID);
                    break;
                }
                //14: Save customer data in bin file
                case "14":{
                    customerServiceCall.saveCustomersToBinaryFileUsingTheEntireList(customerBinFileName, fileService);
                    break;
                }
                //15: Load customer data from bin file
                case "15":{
                    customerServiceCall.loadCustomersFromBinaryFileUsingTheEntireList(customerBinFileName,fileService);
                    break;
                }
                //16: Save song data in bin file
                case "16":{
                    songServiceCall.saveSongsToBinaryFileUsingTheEntireList(songBinFileName,fileService);
                    break;
                }
                //17: Load song data from bin file
                case "17":{
                    songServiceCall.loadSongsFromBinaryFileUsingTheEntireList(songBinFileName,fileService);
                    break;
                }
                //18: Show Report
                case "18":{

                    break;
                }
                case "0": {
                    System.out.println("Bye!");
                    break;
                }

                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        }
        while (!option.equalsIgnoreCase("0"));
    }
    private static void takeUserData () throws UserNameAlreadyTakenException {
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
        }catch (UserNameAlreadyTakenException e){
        System.out.println("User Already taken");
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
        songServiceCall.addSongToDatabase(songName,artistName,genre,songLength,songAlbum);


    }
    private static String takeGenre () {
        System.out.println("Enter genre: ");
        String genre=input.nextLine();
        return genre;
    }
}

