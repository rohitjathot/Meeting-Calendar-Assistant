# Meeting-Calendar-Assistant

Add Employee:
POST: http://localhost:8080/employees/add
Body:{"id":"0","firstName":"rohit", "lastName":"jathot","email":"rohit.jathot@gmail.com"}

Get all employees:
GET: http://localhost:8080/employees/all

Get meeting slots for the employee:
GET: http://localhost:8080/employees/getMeeting/{empId}/{date}
example: http://localhost:8080/employees/getMeeting/1/2021-04-26

Book Meeting:
POST: http://localhost:8080/employees/bookMeeting/{empId}/{date}/{startTime}/{endTime}
Example: http://localhost:8080/employees/bookMeeting/4/2021-04-25/4:00/6:00

Get free slots between 2 employees
GET: http://localhost:8080/employees/getFreeSlot/{firstEmployeeID}/{SecondEmployeeID}/{date}/{durationInMin}
Example: http://localhost:8080/employees/getFreeSlot/0/1/2021-04-25/30

Check if there is a conflict between many employees
GET: http://localhost:8080/employees/checkAllEmployeeConflict
Body: {"start": "5:00", "end": "10:00", "empId": [0,1,2,4], "date": "2021-04-25"}
