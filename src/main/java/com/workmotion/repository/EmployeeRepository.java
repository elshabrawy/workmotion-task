package com.workmotion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workmotion.entity.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
