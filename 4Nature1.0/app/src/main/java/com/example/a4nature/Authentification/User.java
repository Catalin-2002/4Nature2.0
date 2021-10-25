package com.example.a4nature.Authentification;

public class User {
    public String fullName, age, email;
    public int score;

    public User(){

    }

    public User(String fullName, String age, String email, int score) {
        this.fullName= fullName;
        this.age= age;
        this.email=email;
        this.score=score;


    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
