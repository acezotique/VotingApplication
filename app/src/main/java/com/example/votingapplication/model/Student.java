package com.example.votingapplication.model;

import java.io.Serializable;

public class Student implements Serializable {

    private String matricNo, password, name, ic, faculty, year, semester, email;

    public Student(){
    }

    public Student(String matricNo, String password, String name, String ic, String faculty, String year, String semester, String email) {
        this.matricNo = matricNo;
        this.password = password;
        this.name = name;
        this.ic = ic;
        this.faculty = faculty;
        this.year = year;
        this.semester = semester;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMatricNo() {
        return matricNo;
    }

    public String getPassword() {
        return password;
    }

    public String getIc() {
        return ic;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getYear() {
        return year;
    }

    public String getSemester() {
        return semester;
    }

    public String getEmail() {
        return email;
    }
}
