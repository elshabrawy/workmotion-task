# Workmotion Employee

Building a platform including an employee management system.

The employees on this system are assigned to different states.

The states (State machine) for A given Employee are:

    ADDED
    IN-CHECK
    APPROVED
    ACTIVE
Initially when an employee is added it will be assigned "ADDED" state automatically.
The allowed state transitions are:
	
	ADDED -> IN-CHECK *-> APPROVED -> ACTIVE

Furthermore, IN-CHECK state is special and has the following orthogonal child substates:

    SECURITY_CHECK_STARTED
    SECURITY_CHECK_FINISHED
    WORK_PERMIT_CHECK_STARTED
    WORK_PERMIT_CHECK_FINISHED


# Technologies
Java 11, Spring-boot and Spring State Machine

# How to run it
- Clone the main project by this command:
 `git clone https://github.com/w-salloum/workmotion`
- From the terminal go to ./workmotion and use maven to build the project `mvn clean package`

- Run the application by `java -jar workmotion/target/workmotion-employee.jar` , it will run it on the port 8082 ( you can change the port from application.properties)

- Or you can use docker, first run  `docker build -t workmotion-employee.jar . ` , then run the docker by `docker run -p 8082:8082 workmotion-employee.jar` 

- Go to http://localhost:8082/api-docs.html to check APIs documentation (build using OpenAPI Swagger ) 
- To create an employee, use POST API http://localhost:8085/api/employee/add-employee
- To check employee info, use GET API http://localhost:8085/api/employee/employee-details?employeeId=EmployeeID
- To change state for an employee, use POST API http://localhost:8085/api/employee/change-state

# How it works
- When we create an employee it will be in ADDED state automatically.
- If you want to change the employee state, you need to call `api/employee/change-state` API as a post request with two parameters 
1- `employeeId` : employee ID 2- `action` which could be one of the following 
`[IN_CHECK, WORK_PERMIT_FINISH ,SECURITY_FINISH, FINAL_APPROVE,WORK_PERMIT_VERIFY]` which are used to move between the states `[ADDED ,INCHECK , APPROVED , ACTIVE ,	SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED,SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_FINISHED,	REJECTED]`
- Employee object has a state field witch shows the current state 
- Employee object has a history field witch shows the EVENT and the STATE in order, in case you use the wrong EVENT with the employee state, it will not update the state, and it will stay in the current state, and the history will show that , in the following example , I created an employee and used the action APPROVE_ADDED, then APPROVE_WORK_PERMIT
```JSON
{
 "employeeId": 1,
 "firstName": "Employee First Name",
 "lastName": "Employee Last Name",
 "mail": "a.b@gmail.com",
 "Age": "45",
  "state": [
    "INCHECK",
    "SECURITY_CHECK_STARTED",
    "WORK_PERMIT_CHECK_FINISHED"
  ],
  "history": [
    "Initial State:[ADDED]",
    "Event:APPROVE_ADDED State:[INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED]",
    "Event:APPROVE_WORK_PERMIT State:[INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED]"
  ]
}
```
now let us use FINAL_APPROVE ( which is supposed to move the state from APPROVED to ACTIVE), we will see the state does not change and history will show that action
```JSON
{
  "employeeId": 1,
  "firstName": "Employee First Name",
  "lastName": "Employee Last Name",
  "mail": "a.b@gmail.com",
  "Age": "45",
  "state": [
    "INCHECK",
    "SECURITY_CHECK_STARTED",
    "WORK_PERMIT_CHECK_FINISHED"
  ],
  "history": [
    "Initial State:[ADDED]",
    "Event:APPROVE_ADDED State:[INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED]",
    "Event:APPROVE_WORK_PERMIT State:[INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED]",
    "Event:FINAL_APPROVE State:[INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED]"
  ]
}
```
# Testing

- There is a unit test to test the StateMachine configuration for the 3 given happy Scenarios and the two Scenarios
- There is another test to test calling API and the expected states after that.
