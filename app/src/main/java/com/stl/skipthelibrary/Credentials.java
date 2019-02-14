package com.stl.skipthelibrary;

import java.util.UUID;

public class Credentials {
    private String userName;
    private UUID secretID;
    private String hashedPassword;

    public Credentials(String userName, UUID secretID, String hashedPassword) {
        this.userName = userName;
        this.secretID = secretID;
        this.hashedPassword = hashedPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getSecretID() {
        return secretID;
    }

    public void setSecretID(UUID secretID) {
        this.secretID = secretID;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
