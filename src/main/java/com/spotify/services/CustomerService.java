package com.spotify.services;

import com.spotify.exceptions.NotFoundException;
import com.spotify.exceptions.UserNameAlreadyTakenException;
import com.spotify.models.Artist;
import com.spotify.models.Customer;
import com.spotify.models.PlayList;
import com.spotify.models.Song;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class CustomerService implements Serializable {

    private final String userPattern = "^[a-zA-Z0-9-_-]{8,30}$";
    private final String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*._-]).{8,}$";

    Map<UUID, Customer> customerByID;
    Map<String, Customer> customerByUsername;
    ArtistService artistServiceCall =new ArtistService();

    public CustomerService() {
        this.customerByID = new HashMap<>();
        this.customerByUsername = new HashMap<>();
    }


    public boolean addCustomerToDatabase(String username,
                                         String userPassword,
                                         String clientName,
                                         String clientLastname,
                                         int clientAge) throws UserNameAlreadyTakenException, IllegalArgumentException{
        if (usernameIsTaken(username)) {
            throw new UserNameAlreadyTakenException(String.format("Username %s is already taken", username));
        }
        if (!ageOver18(clientAge)){
            throw new IllegalArgumentException(String.format("Client is not an adult"));
        }
        Customer customer=new Customer(username,userPassword,clientName,clientLastname,clientAge);
        return addCustomerToDatabase(customer);
    }


    private boolean addCustomerToDatabase (Customer customer) throws IllegalArgumentException{
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (!validUsername(customer.getUsername())){
            throw new IllegalArgumentException("Customer username must contain 8 to 30 characters and cannot use special characters despite of _");
        }
        if (!validPassword(customer.getPassword())){
            throw new IllegalArgumentException("Customer password must contain 8 characters, one upper case letter, one number and one special character.");
        }
        if (customer.getClientName() == null || customer.getClientName().isEmpty()){
            throw new IllegalArgumentException("Customer name can't be null");
        }
        if (customer.getClientLastname()==null || customer.getClientLastname().isEmpty()){
            throw new IllegalArgumentException("Customer lastname can't be null");
        }
        return customerByID.put(customer.getUserIdentifier(),customer)== null && customerByUsername.put(customer.getUsername(), customer)==null;
    }


    private boolean usernameIsTaken(String username) {
        return customerByID.values().stream()
                .anyMatch(customer -> customer.getUsername().equals(username));
    }


    //delete functions
    public boolean deleteCustomerByID(UUID customerID) throws NullPointerException, NotFoundException {

        if (!customerByID.containsKey(customerID)) {
            throw new NotFoundException(String.format("Customer with id %s not found", customerID));
        }

        Customer customer = customerByID.get(customerID);
        return customerByID.remove(customerID) != null && customerByUsername.remove(customer.getUsername()) != null;
    }


    public boolean deleteCustomerByUsername(String username) throws NullPointerException, NotFoundException {

        if (!customerByUsername.containsKey(username)) {
            throw new NotFoundException(String.format("Customer with given username not found", username));
        }

        Customer customer = customerByUsername.get(username);
        return customerByUsername.remove(username) != null && customerByID.remove(customer.getUserIdentifier()) != null;
    }


    private boolean validUsername (String userName){
        if (!userName.matches(userPattern)){
            return false;
        }
        return true;
    }
    private boolean validPassword (String password){
        return password.matches(passwordPattern);
    }
    private boolean ageOver18 (int customerAge){
        int minimalAge = 18;
        return customerAge >= minimalAge;
    }

    protected void clearDatabase() {
        customerByID.clear();
        customerByUsername.clear();
    }
    private boolean addCustomerToDatabase(List<Customer> customers) {

        return customers.stream().allMatch(
                customer-> addCustomerToDatabase(customer)
        );
    }
    public boolean loadCustomersFromCSVFile(String path,
                                         String delimiter,
                                         FileService fileService)
            throws IOException, NotFoundException {

        List<Customer> customers = fileService.loadCustomersFromCSVFile(path, delimiter);

        return addCustomerToDatabase(customers);
    }



    public void loadCustomersFromBinaryFileUsingTheEntireList(String filePath,
                                                              FileService fileService) throws IOException, ClassNotFoundException{
        List<Customer> customers =fileService.loadCustomersFromBinFile(filePath);
        clearDatabase();

        addCustomerToDatabase(customers);
    }
    public void saveCustomersToBinaryFileUsingTheEntireList(String filePath,
                                                         FileService fileService) throws IOException {

        List<Customer> customers = new ArrayList<>(customerByID.values());
        fileService.saveCustomersIntoBinFile(filePath, customers);

    }

    public void printCustomerMap (){
        for (Map.Entry customers: customerByID.entrySet()){
            System.out.println(customers.getValue().toString());
        }
    }


    public void createNewPlayList(String customerUsername, String playlistName) {
        for (Customer customerToSearch : customerByID.values()){
            if (customerToSearch.getUsername().equals(customerUsername)){
                customerToSearch.addPlaylist(playlistName);
            }
        }
    }
    public void printUserPlayLists (String customerUsername){
        for (Customer customerToSearch : customerByID.values()){
            if (customerToSearch.getUsername().equals(customerUsername)){
                System.out.println(customerToSearch.getClientPlayListsbyID());
            }
        }
    }
    public void addSongsToCustomerPlayList(String costumerUsername, UUID playListID, UUID songID) {

    }
    public UUID searchUUIDBasedOnSongName (String songName){
        return null;
    }
    public List<Customer> ArtisttoList(String customerUsername, UUID artistID ) {
        return customerByID.values().stream()
                .toList();
    }
    public void addFollowedArtistToCustomer (String customerUsername, UUID artistID){
        boolean artistExists=artistServiceCall.verifyIfArtistExists(artistID);
        if (artistExists){
            for (Customer customer: customerByID.values()){
                customer.addFollowedartist(artistID);
            }
        }
        else{
            System.out.println ("Artist does not exist");
        }
    }
    public void artistExists (UUID artistID){
        SongService songService= new SongService();
        List<String> artistList=songService.listAllArtists();
    }
}
