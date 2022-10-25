package com.workmotion.service;


import com.workmotion.entity.Employee;
import com.workmotion.state_enum.EmployeeEvents;

public interface EmployeeService {
	
	public Employee save(Employee employee) ;
	public Employee getById(Long employeeId);
	
	public Employee changeState(Long employeeId,EmployeeEvents event);
	
}
