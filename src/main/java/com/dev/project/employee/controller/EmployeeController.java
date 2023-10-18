package com.dev.project.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.employee.model.Employee;
import com.dev.project.employee.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/employees")
@Log4j2
public class EmployeeController {
	
	@Autowired
    EmployeeService employeeService;

	@PostMapping
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
		log.info("Post received - createEmployee " + employee.getFirstName() + ", " 
													+ employee.getLastName() + ", " 
													+ employee.getEmail());
	    Employee newEmployee = employeeService.createEmployee(employee);
	    return new ResponseEntity<Employee>(newEmployee, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<Employee>> listAllEmployees() {
		log.info("Get request received - listAllEmployees");
	    return new ResponseEntity<List<Employee>>(employeeService.listAllEmployees(), HttpStatus.OK);
	}
	
	@GetMapping("{employeeId}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "employeeId") Long id) {
		log.info("Get request received - getEmployeeById - " + id);
	    return new ResponseEntity<Employee>(employeeService.getEmployeeById(id), HttpStatus.OK);
	}

	@PutMapping("{employeeId}")
	public Employee updateEmployee(@PathVariable(value = "employeeId") Long id, @Valid @RequestBody Employee employee) {
		log.info("Put request received - updateEmployee " + id + ", " 
															+ employee.getFirstName() + ", " 
															+ employee.getLastName() + ", " 
															+ employee.getEmail());
	    return employeeService.updateEmployee(id, employee);
	}
	
	@DeleteMapping("{employeeId}")
	public ResponseEntity<String> deleteEmployee(@PathVariable(value = "employeeId") Long id) {
		log.info("Delete request received - deleteEmployee - " + id);
		employeeService.deleteEmployee(id);
		return new ResponseEntity<String>("Employee has been deleted successfully.", HttpStatus.OK);
	}
}
