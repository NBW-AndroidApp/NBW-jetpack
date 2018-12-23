package org.nieghborhoodbikeworks.nbw;

/**
 * User class for storing fetched user data from Firebase database.
 */
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

    /**
     * This is used by Firebase when fetching user data upon signIn in {@link SharedViewModel}
     */
    public User() {}

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

    // IMPORTANT: PLEASE DON'T CHANGE THIS TO hasSignedWaiver SIMPLY BECAUSE IT'S MORE INTUITIVE.
    // FIREBASE WILL NOT WRITE THE RIGHT DATA IF YOU DO, AND I DON'T KNOW WHY.
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
