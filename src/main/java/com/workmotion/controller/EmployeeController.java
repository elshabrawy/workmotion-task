package com.workmotion.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.workmotion.entity.Employee;
import com.workmotion.state_enum.EmployeeEvents;
import com.workmotion.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Operation(summary = "Add Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee has been created, and the API return the employee info as JSON", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "4XX", description = "Bad Request- Missing data or date format exception", content = @Content)})
    @PostMapping("/add-employee")
    public ResponseEntity<Object> add(@Parameter(description = "Employee Json file contains First Name,Last Name,Email Address,Employee Age")
                                         @RequestBody() Employee employee ) {

        return new ResponseEntity<>(employeeService.save(employee), HttpStatus.CREATED);
    }

    @Operation(summary = "Get employee details by Employee ID")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the employee details as JSON, the history field will show you all the actions you did with the response states for that actions , the state field  will have the current state", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)})

    @GetMapping("/employee-details")
    public ResponseEntity<Object> getDetails(@Parameter(description = "Employee ID")
                                       @RequestParam(name = "employeeId") Long employeeId) {
         return new ResponseEntity(employeeService.getById(employeeId), HttpStatus.OK);
    }

    @Operation(summary = "Change the employee state")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the employee info as JSON, the history field will show you all the actions you did with the response states for that actions , the state field  will have the current state", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "4XX", description = "Bad request, action not defined", content = @Content)})

    @PostMapping("/change-state")
    public ResponseEntity<Object> change(@Parameter(description = "Employee ID")
                                         @RequestParam(name = "id") Long employeeId,
                                         @Parameter(description = "Could be one of these actions [IN_CHECK, WORK_PERMIT_FINISH ,SECURITY_FINISH, FINAL_APPROVE,WORK_PERMIT_VERIFY]")
                                         @RequestParam(name = "action") EmployeeEvents action) {

        return new ResponseEntity<>(employeeService.changeState(employeeId,action), HttpStatus.OK);

    }

}
