package com.example.test.dao;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class EmployeeInputConflict {
    private String start;
    private String end;
    private ArrayList<Long> empId;
    private String date;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public ArrayList<Long> getEmpId() {
        return empId;
    }

    public String getDate() {
        return date;
    }
}
