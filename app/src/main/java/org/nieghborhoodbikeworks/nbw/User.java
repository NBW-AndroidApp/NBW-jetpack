package org.nieghborhoodbikeworks.nbw;

public class User {
    private String name;
    private String email;
    private boolean signedWaiver;
    private String uid;
    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public User(String name, String email) {
        this.setName(name);
        this.setEmail(email);
        this.setSignedWaiver(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSignedWaiver() {
        return signedWaiver;
    }

    public void setSignedWaiver(boolean signedWaiver) {
        this.signedWaiver = signedWaiver;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
