package com.workmotion.controller;

import com.workmotion.entity.Employee;
import com.workmotion.service.EmployeeService;
import com.workmotion.state_enum.EmployeeEvents;
import com.workmotion.state_enum.EmployeeStates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.containsString;


@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Test
    @Transactional
    void testValidState() throws Exception{

        Employee employee = Employee.builder().firstName("mohamed").lastName("Elshabrawy").mail("m.s.elshabrawy@gmail.com").age(45).build();
        employee = employeeService.save(employee);

        mockMvc.perform(get("/api/employee/employee-details?id="+employee.getEmployeeId())).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("id", employee.getEmployeeId().toString());
        params.add("action", EmployeeEvents.IN_CHECK.toString());
        mockMvc.perform(post("/api/employee/change-state").params(params)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        params = new LinkedMultiValueMap<>();
        params.add("id", employee.getEmployeeId().toString());
        params.add("action",EmployeeEvents.WORK_PERMIT_FINISH.toString());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/employee/change-state").params(params)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Employee emp2 = this.employeeService.getById(employee.getEmployeeId());

        assertEquals(emp2.getState().get(0), EmployeeStates.IN_CHECK);
        assert(emp2.getState().contains(EmployeeStates.SECURITY_CHECK_STARTED));
        assert(emp2.getState().contains(EmployeeStates.WORK_PERMIT_CHECK_FINISHED));


    }
    @Test
    @Transactional
    void testInvalidState() throws Exception{

        Employee employee = Employee.builder().firstName("mohamed").lastName("Elshabrawy").mail("m.s.elshabrawy@gmail.com").age(45).build();
        employee = employeeService.save(employee);

        mockMvc.perform(get("/api/employee/employee-details?id="+employee.getEmployeeId())).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("id", employee.getEmployeeId().toString());
        params.add("action", EmployeeEvents.IN_CHECK.toString());
        mockMvc.perform(post("/api/employee/change-state").params(params)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        params = new LinkedMultiValueMap<>();
        params.add("id", employee.getEmployeeId().toString());
        params.add("action",EmployeeEvents.FINAL_APPROVE.toString());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/employee/change-state").params(params)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andExpect(MockMvcResultMatchers.status().reason(containsString("event not accepted: ")));


    }

}