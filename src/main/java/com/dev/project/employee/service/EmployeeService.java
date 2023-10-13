package com.dev.project.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.project.employee.model.Employee;
import com.dev.project.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	EmployeeRepository employeeRepo;
	
	public Employee createEmployee(Employee employee) {
	    return employeeRepo.save(employee);
	}

	public List<Employee> listAllEmployees() {
	    return employeeRepo.findAll();
	}

	public Employee getEmployeeById(Long employeeId) {
		return employeeRepo.findById(employeeId).get();                                
	}
	
	public Employee updateEmployee(Long employeeId, Employee employee) {
		Employee currEmployee = employeeRepo.findById(employeeId).get();
		currEmployee.setFirstName(employee.getFirstName());
		currEmployee.setLastName(employee.getLastName());
		currEmployee.setEmail(employee.getEmail());
		
		return employeeRepo.save(currEmployee);                                
	}
	
	public void deleteEmployee(Long employeeId) {
		employeeRepo.deleteById(employeeId);
	}
}
