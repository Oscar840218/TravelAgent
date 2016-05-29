package com.example.oscar.travelagent2;


import java.io.Serializable;

public class Member implements Serializable {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String country;
    private String favorite_place;

    public Member(String name){
        this.name = name;
    }
    public void set_email(String email){
        this.email = email;
    }
    public void set_password(String password){
        this.password = password;
    }
    public void set_gender(String gender){
        this.gender = gender;
    }
    public void set_country(String country){
        this.country = country;
    }
    public void set_place(String favorite_place){
        this.favorite_place = favorite_place;
    }
    public String get_name(){
        return name;
    }
    public String get_email(){
        return email;
    }
    public String get_password(){
        return password;
    }
    public String get_gender(){
        return gender;
    }
    public String get_country(){
        return country;
    }
    public String get_place(){
        return favorite_place;
    }
}
