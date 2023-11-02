package com.spotify.services;

import com.spotify.exceptions.NotFoundException;
import com.spotify.exceptions.UserNameAlreadyTakenException;
import com.spotify.models.Customer;
import com.spotify.models.PlayList;
import com.spotify.models.PremiumCustomer;
import com.spotify.models.RegularCustomer;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class CustomerService implements Serializable {

    private final String userPattern = "^[a-zA-Z0-9-_-]{8,30}$";
    private final String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*._-]).{8,}$";
    Map<UUID, Customer> customerByID;
    Map<String, Customer> customerByUsername;
    ArtistService artistServiceCall =new ArtistService();
    private static List<Customer> customers = new ArrayList<>();
    public CustomerService() {
        this.customerByID = new HashMap<>();
        this.customerByUsername = new HashMap<>();
    }


    public boolean addCustomerToDatabase(String customerType, String username,
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
        if (customerType.equalsIgnoreCase("premium")){
            Customer customer=new PremiumCustomer(customerType,username,userPassword,clientName,clientLastname,clientAge);
            customers.add(customer);
            return addCustomerToDatabase(customer);
        }
        else if (customerType.equalsIgnoreCase("regular")) {
            Customer customer=new RegularCustomer(customerType,username,userPassword,clientName,clientLastname,clientAge);
            customers.add(customer);
            return addCustomerToDatabase(customer);
        }
        else{
            return false;
        }
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
        customers.add(customer);
        return customerByID.put(customer.getCustomerIdentifier(),customer)== null && customerByUsername.put(customer.getUsername(), customer)==null;
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
        return customerByUsername.remove(username) != null && customerByID.remove(customer.getCustomerIdentifier()) != null;
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
            throws IOException {

        List<Customer> customers = fileService.readCustomersFromCSV(path, delimiter);

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



    /*public void printUserPlayLists (String customerUsername){
        for (Customer customerToSearch : customerByID.values()){
            if (customerToSearch.getUsername().equals(customerUsername)){
                System.out.println(customerToSearch.getClientPlayListsbyID());
            }
        }
    }*/

    public void addSongsToCustomerPlayList(String costumerUsername, UUID playListID, UUID songID) {
        for (Customer customer: customerByID.values()){
            if (customer.getUsername().equals(costumerUsername)){
                for (PlayList playList : customer.getPlaylists()){
                    if (playList.getPlaylistID().equals(playListID)){
                        playList.addSong(songID);
                    }
                }
            }
        }
    }
    public UUID searchUUIDBasedOnSongName (String songName){
        return null;
    }
    public List<Customer> ArtisttoList(String customerUsername, UUID artistID ) {
        return customerByID.values().stream()
                .toList();
    }
    public void addFollowedArtistToCustomer (String customerUsername, UUID artistID, ArtistService artistServiceCall){
        boolean artistExists= artistServiceCall.verifyIfArtistExists(artistID);
        if (artistExists){
            for (Customer customer: customerByID.values()){
                if (customer.getUsername().equalsIgnoreCase(customerUsername)){
                    customer.addFollowedArtist(artistID);
                }
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
    public void readCustomersWithPlayListsFromCSV(String customersPath,
                                                  String delimiter,
                                                  String playlistsPath,
                                                  FileService fileService) throws IOException {
        List<Customer> customers =
                fileService.readCustomersFromCSV(customersPath, delimiter);

        Map<UUID, List<PlayList>> playlistsByCustomerId =
                fileService.readPlayListFromCSV(playlistsPath, delimiter);

        addPlaylistsToCustomers(customers, playlistsByCustomerId);
        addCustomerToDatabase(customers);
    }
    private void addPlaylistsToCustomers(List<Customer> customersWithoutPlayLists, Map<UUID, List<PlayList>> playlistsByCustomerId) {
        for(Customer customer : customersWithoutPlayLists){
            List<PlayList> playlists = playlistsByCustomerId.get(customer.getCustomerIdentifier());
            if(playlists != null){
                customer.addPlaylists(playlists);
            }
        }

    }


    public void createNewPlayList(String customerUserName, String playListName) {
        for (Customer customer: customerByID.values()){
            if (customer.getUsername().equals(customerUserName)){
                PlayList playList=new PlayList(playListName);
                customer.addPlaylist(String.valueOf(playList));
            }
        }
    }
    public List<UUID> obtainIDsOfAllFollowedArtists() {

        return customers.stream()
                .flatMap(cliente -> cliente.getFollowedArtist().stream())
                .collect(Collectors.toList());

    }

}
