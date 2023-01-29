package com.example.votingapplication.model;

public class Vote {

    private String id, name, club, image, faculty;
    private boolean isChecked = false;
    private  boolean isGeneral;
    private int count;

    public Vote(){}

    public Vote(String id, String name, String club, boolean isGeneral, int count, String image, String faculty) {
        this.id = id;
        this.name = name;
        this.club = club;
        this.isGeneral = isGeneral;
        this.count = count;
        this.image = image;
        this.faculty = faculty;
    }

    public Vote(String name){
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isGeneral() {
        return isGeneral;
    }

    public void setGeneral(boolean checked) {
        isGeneral = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setCount(int count){
        this.count = count;
    }

    public int getCount(){
        return count;
    }

    public String getImage() {
        return image;
    }

    public String getFaculty() {
        return faculty;
    }
}
