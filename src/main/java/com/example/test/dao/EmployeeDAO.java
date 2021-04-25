package com.example.test.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeDAO {

    private static EmployeeDAO employeeDAO;
    public static EmployeeDAO getInstance(){
        if(employeeDAO==null)
            employeeDAO = new EmployeeDAO();
        return employeeDAO;
    }

    private HashMap<Long,Employee> allEmployees;

    private EmployeeDAO(){
        allEmployees = new HashMap<>();
    }

    public HashMap<Long, Employee> getAllEmployees()
    {
        return allEmployees;
    }

    public Long addEmployee(Employee employee) {
        allEmployees.put(employee.getId(), employee);
        return employee.getId();
    }

    public Employee getEmployee(Long id){
        if(allEmployees.containsKey(id)){
            return allEmployees.get(id);
        }
        return null;
    }

    public ArrayList<String> getFreeSlot(long firstEmp, long secondEmp, String date, int timeInMin) throws ParseException {
        if(!allEmployees.containsKey(firstEmp) && !allEmployees.containsKey(secondEmp)){
            return new ArrayList<String>(Collections.singletonList("Both Employee does not exist"));
        }
        if(!allEmployees.containsKey(firstEmp)){
            return new ArrayList<String>(Collections.singletonList("Employee with ID "+firstEmp+" does not exist"));
        }
        if(!allEmployees.containsKey(secondEmp)){
            return new ArrayList<String>(Collections.singletonList("Employee with ID "+secondEmp+" does not exist"));
        }

        Calender.Days firstEmpSlot = getEmployee(firstEmp).getMeeting(date);
        Calender.Days secondEmpSlot = getEmployee(secondEmp).getMeeting(date);

        TreeSet<Calender.Pair> allSlotsFirstEmp = firstEmpSlot.getSlots();
        TreeSet<Calender.Pair> allSlotsSecondEmp = secondEmpSlot.getSlots();
        ArrayList<String> allAvailableSlots = new ArrayList<>();

        Iterator<Calender.Pair> itr1 = allSlotsFirstEmp.iterator();
        Iterator<Calender.Pair> itr2 = allSlotsSecondEmp.iterator();

        int startTime1 = 0, startTime2 = 0, endTime1 = 0, endTime2 = 0;
        int preStartTime1 = 0, preStartTime2 = 0, preEndTime1 = 0, preEndTime2 = 0;

        //initialization
        if(itr1.hasNext()){
            Calender.Pair p1 = itr1.next();
            startTime1 = p1.getStartHour()*60+p1.getStartMin();
            endTime1 = p1.getEndHour()*60+p1.getEndMin();
        }
        if(itr2.hasNext()){
            Calender.Pair p2 = itr2.next();
            startTime2 = p2.getStartHour()*60+p2.getStartMin();
            endTime2 = p2.getEndHour()*60+p2.getEndMin();
        }

        while(true){
            int tempMinStart = Math.max(preEndTime1,preEndTime2);
            int tempMaxEnd = Math.min(startTime1,startTime2);

            if(tempMaxEnd-tempMinStart>=timeInMin){
                allAvailableSlots.add(new Calender.Pair(tempMinStart/60,tempMinStart%60,tempMaxEnd/60,tempMaxEnd%60).toString());
            }
            if(!itr1.hasNext() || !itr2.hasNext())
                break;
            if(endTime1<endTime2){
                preEndTime1 = endTime1;
                Calender.Pair p1 = itr1.next();
                startTime1 = p1.getStartHour()*60+p1.getStartMin();
                endTime1 = p1.getEndHour()*60+p1.getEndMin();
            }else{
                preEndTime2 = endTime2;
                Calender.Pair p2 = itr2.next();
                startTime2 = p2.getStartHour()*60+p2.getStartMin();
                endTime2 = p2.getEndHour()*60+p2.getEndMin();
            }
        }

        while (true){
            int tempMinStart = Math.max(preEndTime1,preEndTime2);
            int tempMaxEnd = Math.min(startTime1,startTime2);

            if(tempMaxEnd-tempMinStart>=timeInMin){
                allAvailableSlots.add(new Calender.Pair(tempMinStart/60,tempMinStart%60,tempMaxEnd/60,tempMaxEnd%60).toString());
            }
            if(!itr1.hasNext())
                break;
            preEndTime1 = endTime1;
            Calender.Pair p1 = itr1.next();
            startTime1 = p1.getStartHour()*60+p1.getStartMin();
            endTime1 = p1.getEndHour()*60+p1.getEndMin();
        }


        while (true){
            int tempMinStart = Math.max(preEndTime1,preEndTime2);
            int tempMaxEnd = Math.min(startTime1,startTime2);

            if(tempMaxEnd-tempMinStart>=timeInMin){
                allAvailableSlots.add(new Calender.Pair(tempMinStart/60,tempMinStart%60,tempMaxEnd/60,tempMaxEnd%60).toString());
            }
            if(!itr2.hasNext())
                break;
            preEndTime2 = endTime2;
            Calender.Pair p2 = itr2.next();
            startTime2 = p2.getStartHour()*60+p2.getStartMin();
            endTime2 = p2.getEndHour()*60+p2.getEndMin();
        }
        allAvailableSlots.add(new Calender.Pair(Math.max(endTime1,endTime2)/60,Math.max(endTime1,endTime2)%60,11,59).toString());
        return allAvailableSlots;
    }

    public String checkAllEmployeeConflict(EmployeeInputConflict object) throws ParseException {
        ArrayList<Long> empId = object.getEmpId();
        for(long id:empId){
            if(!allEmployees.containsKey(id)){
                return "Some employees does not exist in the data base";
            }
        }

        String start = object.getStart();
        String end = object.getEnd();
        String date = object.getDate();

        String [] startTimeString = start.split(":");
        String [] endTimeString = end.split(":");
        int startHour = Integer.parseInt(startTimeString[0]);
        int startMin = Integer.parseInt(startTimeString[1]);
        int endHour = Integer.parseInt(endTimeString[0]);
        int endMin = Integer.parseInt(endTimeString[1]);
        Date todayDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);

        for(long id:empId){
            if(allEmployees.get(id).getCalender().conflict(todayDate, startHour, startMin, endHour, endMin)){
                return "There is a conflict";
            }
        }

        return "There are no conflicts !";
    }
}
