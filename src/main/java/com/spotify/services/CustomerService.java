package com.spotify.services;

import com.spotify.exceptions.NotFoundException;
import com.spotify.exceptions.UserNameAlreadyTakenException;
import com.spotify.models.Customer;
import com.spotify.models.PlayList;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


public class CustomerService implements Serializable {

    private List<Customer> customerList=new ArrayList<>();
    private String userPattern = "^[a-zA-Z0-9-_-]{8,30}$";
    Map<UUID, Customer> customerByID;
    Map<String, Customer> customerByUsername;

    public CustomerService() {
        this.customerByID = new HashMap<>();
        this.customerByUsername = new HashMap<>();
    }

    public List<Customer> getCustomerList() {
        return new ArrayList<>(customerList);
    }
    public boolean addCustomerToDatabase(String username,
                                         String userPassword,
                                         String clientName,
                                         String clientLastname,
                                         int clientAge) throws UserNameAlreadyTakenException, IllegalArgumentException{
        if (usernameIsTaken(username)) {
            throw new UserNameAlreadyTakenException(String.format("Username %s is already taken", username));
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
        return customerByID.put(customer.getUserIdentifier(),customer)== null && customerByUsername.put(customer.getUsername(), customer)==null;
    }
    /*public void createUser (String userName,
                            String userPassword,
                            String clientName,
                            String clientLastname,
                            int clientAge) throws UserNameAlreadyTakenException, IllegalArgumentException{
        boolean validateUser=validUsername(userName);
        if (!validateUser){
            System.out.println("invalid username");
        }

        else {
            Customer customer= new Customer(userName,userPassword,clientName,clientLastname,clientAge);
            customerList.add(customer);
            System.out.println("customer added successfully");
        }

    }*/
    private boolean usernameIsTaken(String username) {
        return customerByID.values().stream()
                .noneMatch(customer -> customer.getUsername().equals(username));
    }
    //delete functions
    public boolean deleteCustomerByID(UUID ownerId) throws NullPointerException, NotFoundException {

        if (!customerByID.containsKey(ownerId)) {
            throw new NotFoundException(String.format("Owner with id %s not found", ownerId));
        }

        Customer owner = customerByID.get(ownerId);
        return customerByID.remove(ownerId) != null && customerByUsername.remove(owner.getUsername()) != null;
    }
    public boolean deleteCustomerByUsername(String username) throws NullPointerException, NotFoundException {

        if (!customerByUsername.containsKey(username)) {
            throw new NotFoundException(String.format("Owner with given username not found", username));
        }

        Customer owner = customerByUsername.get(username);
        return customerByUsername.remove(username) != null && customerByID.remove(owner.getUserIdentifier()) != null;
    }

    /*public void deleteUserUsingUsername (String username){
        boolean checkUserDelete=false;
        for (Customer customer: customerList){
            if (customer.getUsername().equals(username)){
                customerList.remove(customer);
                checkUserDelete=true;
            }
        }
        if (checkUserDelete){
            System.out.println("user deleted successfully");
        }
        else{
            System.out.println("user not found");
        }
    }*/
    private boolean validUsername (String userName){
        if (!userName.matches(userPattern)){
            return false;
        }
        for (Customer customer: customerList){
            if (userName.equals(customer.getUsername())){
                    return false;
            }
        }
        return true;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
    protected void clearDatabase() {
        customerByID.clear();
        customerByUsername.clear();
    }
    private boolean addCustomerToDatabase(List<Customer> customers) {

        return customers.stream().allMatch(
                owner -> addCustomerToDatabase(owner)
        );
    }
    public boolean loadCustomersFromCSVFile(String path,
                                         String delimiter,
                                         FileService fileService)
            throws IOException, NotFoundException {

        List<Customer> owners = fileService.loadCustomersFromCSVFile(path, delimiter);

        return addCustomerToDatabase(owners);
    }

    /*public void saveCustomersToCSVFile(String filePath, FileService fileService) throws IOException {
        List<String> ownersListToCSV = this.customerByID.values().stream()
                .map(customer -> customer.toCSV(";"))
                .toList();

        fileService.writeTextFile(filePath, ownersListToCSV);

    }*/

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

    public void printCustomerList() {
        for (Customer customer: customerList){
            System.out.println(customer);
        }
    }
    public void printCustomerMap (){
        for (Map.Entry customers: customerByID.entrySet()){
            System.out.println(customers.getValue());
        }
    }
    public void createNewPlayList(String costumerUsername, String playlistName) {
        for (Customer customer: customerList) {
            if (customer.getUsername().equals(costumerUsername)){
                customer.addPlayList(playlistName);
            }
        }
    }

    public void addSongsToCustomerPlayList(String costumerUsername, UUID playListID, UUID songID) {
        for (Customer customer: customerList) {
            if (customer.getUsername().equals(costumerUsername)){
                for (PlayList playList: customer.getClientPlayLists()){
                    if (playList.getPlaylistID().equals(playListID)){
                        playList.getSongIDs().add(songID);
                    }
                }
            }
        }
    }
    public UUID searchUUIDBasedOnSongName (String songName){
        return null;
    }
    public void followArtist(String customerUsername,UUID artistID ) {
        for (Customer customer: customerList){
            if (customer.getUsername().equals(customerUsername)){
                customer.getFollowedArtist().add(artistID);
            }
        }
    }
}
