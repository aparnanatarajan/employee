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

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	@Autowired
    EmployeeService employeeService;

	@PostMapping
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
	    Employee newEmployee = employeeService.createEmployee(employee);
	    return new ResponseEntity<Employee>(newEmployee, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<Employee>> listAllEmployees() {
	    return new ResponseEntity<List<Employee>>(employeeService.listAllEmployees(), HttpStatus.OK);
	}
	
	@PutMapping("{employeeId}")
	public Employee readEmployees(@PathVariable(value = "employeeId") Long id, @RequestBody Employee empDetails) {
	    return employeeService.updateEmployee(id, empDetails);
	}
	
	@DeleteMapping("{employeeId}")
	public ResponseEntity<String> deleteEmployee(@PathVariable(value = "employeeId") Long id) {
		employeeService.deleteEmployee(id);
		return new ResponseEntity<String>("Employee has been deleted successfully.", HttpStatus.OK);
	}
}
