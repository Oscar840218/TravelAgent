package com.example.oscar.travelagent2;

/**
 * Created by Oscar on 2016/3/3.
 */
public class Location {
     String place;
     String weather;
     String maxtemp;
     String mintemp;

    public Location(String place,String weather,String maxtemp,String mintemp){
        this.place = place;
        this.weather = weather;
        this.maxtemp = maxtemp;
        this.mintemp = mintemp;
    }
    public String GetPlace(){
        return place;
    }
    public String GetWeather(){
        return weather;
    }
    public String GetMaxtemp(){
        return maxtemp;
    }
    public String GetMintemp(){
        return mintemp;
    }
}
