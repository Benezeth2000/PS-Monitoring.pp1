package com.visthome.psmonitoringapp.entity;

public class AllUsers {
    String email;
    String password;
    String uniqueID;
    String role;

    public AllUsers() {
    }


    public AllUsers(String email, String password, String uniqueID, String role) {
        this.email = email;
        this.password = password;
        this.uniqueID = uniqueID;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
