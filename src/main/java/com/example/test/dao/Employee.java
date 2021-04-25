package com.example.test.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

import javax.persistence.Entity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Employee {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Calender calender;

    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.calender = new Calender();
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return firstName+" "+lastName;
    }

    public String getEmail(){
        return email;
    }

    @JsonIgnore
    public Calender getCalender() {
        return calender;
    }

    public Calender.Days getMeeting(String date) throws ParseException {
        return calender.getMeeting(dateFormat.parse(date));
    }

    public String addMeeting(String date, String start, String end) throws ParseException {
        String [] startTimeString = start.split(":");
        String [] endTimeString = end.split(":");
        int startHour = Integer.parseInt(startTimeString[0]);
        int startMin = Integer.parseInt(startTimeString[1]);
        int endHour = Integer.parseInt(endTimeString[0]);
        int endMin = Integer.parseInt(endTimeString[1]);
        Date todayDate = dateFormat.parse(date);

        if(start.equals(end)){
            return "meeting duration cannot be 0 min";
        }

        if(startHour>endHour || (startHour==endHour && startMin>endMin)){
            return "start time cannot be greater than end time";
        }

        if(calender.conflict(todayDate, startHour, startMin, endHour, endMin)){
            return "Meeting conflict";
        }

        return calender.addMeeting(todayDate,startHour, startMin, endHour, endMin).toString();
    }
}
