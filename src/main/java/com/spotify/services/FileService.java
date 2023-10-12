package com.spotify.services;

import com.spotify.models.Customer;
import com.spotify.models.Song;
import com.spotify.services.enums.CustomerAttributesEnum;
import com.spotify.services.enums.SongAttributesEnum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileService {
    public List<Customer> loadCustomersFromCSVFile(String path, String delimiter) throws IOException {
        File file = new File(path);

        List<String> lines =
                Files.readAllLines(file.toPath(),
                        StandardCharsets.UTF_8);

        List<Customer> customerList = new ArrayList<>();

        for (String line : lines) {
            String[] values = line.split(delimiter);
            UUID userID =
                    UUID.fromString(values[CustomerAttributesEnum.CUSTOMERID.getIndex()]);
            String userName =
                    values[CustomerAttributesEnum.USERNAME.getIndex()];
            String customerPassword =
                    values[CustomerAttributesEnum.PASSWORD.getIndex()];
            String customerName=
                    values[CustomerAttributesEnum.CUSTOMERNAME.getIndex()];
            String customerLastName =
                    values[CustomerAttributesEnum.CUSTOMERLASTNAME.getIndex()];
            int customerAge =
                    Integer.valueOf(values[CustomerAttributesEnum.CUSTOMERAGE.getIndex()]);

            Customer customer = new Customer(userID,userName,customerPassword,customerName,customerLastName,customerAge);
            customerList.add(customer);
        }
        return customerList;
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

    public void writeTextFile(String path,
                              List<String> linesToWrite)
            throws IOException {
        File file = new File(path);

        Files.write(file.toPath(), linesToWrite, StandardCharsets.UTF_8);
    }
}
