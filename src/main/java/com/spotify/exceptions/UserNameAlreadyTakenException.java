package com.spotify.exceptions;

public class UserNameAlreadyTakenException extends Exception{
    public UserNameAlreadyTakenException(String message){
        super (message);
    }
}
