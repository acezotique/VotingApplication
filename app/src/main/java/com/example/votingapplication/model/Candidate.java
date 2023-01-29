package com.example.votingapplication.model;

public class Candidate {

    private String id, image, name, club, faculty;
    private boolean isGeneral;
    private boolean isChecked = false;

    public Candidate() {
    }

    public Candidate(String image, String name, String club) {
        this.image = image;
        this.name = name;
        this.club = club;
    }

    public Candidate(String id, String image, String name, String club) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.club = club;
    }

    public Candidate(String id, String image, String name, String club, boolean isGeneral, String faculty) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.club = club;
        this.isGeneral = isGeneral;
        this.faculty = faculty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public boolean isGeneral() {
        return isGeneral;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFaculty() {
        return faculty;
    }
}
