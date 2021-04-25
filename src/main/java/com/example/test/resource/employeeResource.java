package com.example.test.resource;

import com.example.test.dao.Calender;
import com.example.test.dao.Employee;
import com.example.test.dao.EmployeeDAO;
import com.example.test.dao.EmployeeInputConflict;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/employees")
public class employeeResource {

    @GetMapping("/all")
    public HashMap<Long, Employee> getEmployees() {
        return EmployeeDAO.getInstance().getAllEmployees();
    }

    @GetMapping("/getMeeting/{id}/{date}")
    public String getMeeting(@PathVariable long id, @PathVariable String date) {
        try{
            return EmployeeDAO.getInstance().getEmployee(id).getMeeting(date).toString();
        }catch (Exception e){
            e.printStackTrace();
            return "No meeting available for this Employee or Employee does not exist";
        }
    }

    @GetMapping("/getFreeSlot/{firstEmp}/{secondEmp}/{date}/{timeInMin}")
    public ArrayList<String> getFreeSlot(@PathVariable long firstEmp, @PathVariable long secondEmp, @PathVariable String date, @PathVariable int timeInMin) throws ParseException {
        return EmployeeDAO.getInstance().getFreeSlot(firstEmp, secondEmp, date, timeInMin);
    }

    @GetMapping(value="/checkAllEmployeeConflict",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public String checkAllEmployeeConflict(@RequestBody EmployeeInputConflict object) throws ParseException {
        return EmployeeDAO.getInstance().checkAllEmployeeConflict(object);
    }

    @PostMapping(value="/add",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public Employee addEmployees(@RequestBody final Employee employee) {
        return EmployeeDAO.getInstance().getEmployee(EmployeeDAO.getInstance().addEmployee(employee));
    }

    @PostMapping(value="/bookMeeting/{id}/{date}/{start}/{end}")
    public String bookMeeting(@PathVariable long id, @PathVariable String date,@PathVariable String start, @PathVariable String end) {
        try{
            return EmployeeDAO.getInstance().getEmployee(id).addMeeting(date, start, end);
        }catch (Exception e){
            e.printStackTrace();
            return "No Employee present in the database";
        }
    }
}
