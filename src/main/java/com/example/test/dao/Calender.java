package com.example.test.dao;

import java.util.*;

public class Calender {

    static public class Pair{
        private int startHour;
        private int startMin;
        private int endHour;
        private int endMin;
        public Pair(int startHour, int startMin, int endHour, int endMin){
            this.startHour = startHour;
            this.startMin = startMin;
            this.endHour = endHour;
            this.endMin = endMin;
        }
        public int getStartHour(){
            return startHour;
        }
        public int getStartMin(){
            return startMin;
        }
        public int getEndHour(){
            return endHour;
        }
        public int getEndMin(){
            return endMin;
        }

        @Override
        public String toString() {
            return "Time {" +
                    "start=" + ((startHour<10)?"0"+startHour:startHour) +
                    ":" + ((startMin<10)?"0"+startMin:startMin) +
                    ", end=" + ((endHour<10)?"0"+endHour:endHour) +
                    ":" + ((endMin<10)?"0"+endMin:endMin) +
                    '}';
        }
    }

    public class Days{

        private Date date; //yyyy-MM-dd
        private TreeSet<Pair> slots;

        public Days(Date date){
            slots = new TreeSet<>((o1, o2) -> ((o1.startHour>o2.startHour) || (o1.startHour==o2.startHour && o1.startMin>o2.startMin))?1:-1);
            this.date = date;
        }

        public Days(Date date, int startHour, int startMin, int endHour, int endMin){
            this(date);
            addSlot(startHour, startMin, endHour, endMin);
        }

        public TreeSet<Pair> getSlots() {
            return slots;
        }

        public void addSlot(int startHour, int startMin, int endHour, int endMin){
            slots.add(new Pair(startHour, startMin, endHour, endMin));
        }

        @Override
        public String toString() {
            return "Days {" +
                    "date=" + date +
                    ", slots=" + slots.toString() +
                    '}';
        }

        public boolean conflict(int startHour, int startMin, int endHour, int endMin) {
            for(Pair p: slots){
                if(!(p.endHour<startHour || (p.endHour==startHour && p.endMin<=startMin) ||
                        endHour<p.startHour || (endHour==p.startHour && endMin<=p.startMin))){
                    return true;
                }
            }
            return false;
        }
    }

    HashMap<Date,Days> days;

    public Calender(){
        days = new HashMap<>();
    }

    public Days addMeeting(Date date,int startHour, int startMin, int endHour, int endMin){
        if(days.containsKey(date)){
            days.get(date).addSlot(startHour, startMin, endHour, endMin);
        }else{
            days.put(date, new Days(date, startHour, startMin, endHour, endMin));
        }
        return days.get(date);
    }

    public Days getMeeting(Date date){
        if(!days.containsKey(date))
            return null;
        return days.get(date);
    }

    public boolean conflict(Date date, int startHour, int startMin, int endHour, int endMin){
        if(!days.containsKey(date))
            return false;
        return days.get(date).conflict(startHour, startMin, endHour, endMin);
    }
}
