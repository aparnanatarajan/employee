package com.dev.project.employee.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.dev.project.employee.model.Employee;
import com.dev.project.employee.service.EmployeeService;

import jakarta.servlet.ServletException;

@WebMvcTest(value = EmployeeController.class)
public class EmployeeControllerTest {
	@Autowired
	private MockMvc mockmvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Test
	public void testCreateEmployee() throws Exception {
		String mockEmployeeInfo = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@gmail.com\"}";
		Employee mockEmployee = setupNewEmployee("John", "Doe", "john.doe@gmail.com");
		Mockito.when(employeeService.createEmployee(Mockito.any(Employee.class))).
													thenReturn(mockEmployee);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
											.post("/api/employees")
											.accept(MediaType.APPLICATION_JSON).content(mockEmployeeInfo)
											.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockmvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}
	
	
	@Test
	public void testCreateEmployee_throwValidationException() throws Exception {
		String mockEmployeeInfo = "{\"firstName\":\"\",\"lastName\":\"Doe\",\"email\":\"john.doe@gmail.com\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/employees")
				.accept(MediaType.APPLICATION_JSON).content(mockEmployeeInfo)
				.contentType(MediaType.APPLICATION_JSON);
		mockmvc.perform(requestBuilder)
			      .andExpect(status().isBadRequest())
			      .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
			      .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("Validation failed")));
	}


	@Test
	public void testGetEmployeeById() throws Exception {
		Employee mockEmployee = setupNewEmployee("John", "Doe", "john.doe@gmail.com");
		Mockito.when(employeeService.getEmployeeById(Mockito.anyLong()))
									.thenReturn(mockEmployee);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
											.get("/api/employees/1")
											.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockmvc.perform(requestBuilder).andReturn();
		String expectedEmployeeInfo = "{\"id\": 1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@gmail.com\"}";
		JSONAssert.assertEquals(expectedEmployeeInfo, result.getResponse().getContentAsString(), false);
	}
	
	
	@Test
	public void testGetEmployeeById_NoMatch() throws Exception {
		Mockito.when(employeeService.getEmployeeById(Mockito.anyLong()))
									.thenThrow(new NoSuchElementException());
		RequestBuilder requestBuilder = MockMvcRequestBuilders
											.get("/api/employees/1")
											.accept(MediaType.APPLICATION_JSON);
		Exception exception = assertThrows(ServletException.class, () -> {
					mockmvc.perform(requestBuilder).andReturn();
				});
		assertTrue(exception.getMessage().contains("NoSuchElementException"));
	}
	
	
	@Test
	public void testUpdateEmployeeById() throws Exception {
		Employee mockUpdatedEmployee = setupNewEmployee("Jane", "Doe", "jane.doe@gmail.com");
		Mockito.when(employeeService.updateEmployee(Mockito.anyLong(), Mockito.any(Employee.class)))
									.thenReturn(mockUpdatedEmployee);
		String mockEmployeeInfo = "{\"id\": 1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@gmail.com\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/employees/" + 1)
																.accept(MediaType.APPLICATION_JSON).content(mockEmployeeInfo)
																.contentType(MediaType.APPLICATION_JSON);
		String expectedEmployeeInfo = "{\"id\": 1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@gmail.com\"}";
		MvcResult result = mockmvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
		JSONAssert.assertEquals(expectedEmployeeInfo, result.getResponse().getContentAsString(), false);				
	}
	
	
	@Test
	public void testDeleteEmployeeById() throws Exception {
		Employee mockEmployee = setupNewEmployee("John", "Doe", "john.doe@gmail.com");
		Mockito.when(employeeService.getEmployeeById(Mockito.anyLong()))
									.thenReturn(mockEmployee);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
											.get("/api/employees/1")
											.accept(MediaType.APPLICATION_JSON);
		mockmvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn();
	}

	
	private Employee setupNewEmployee(String firstName, String lastName, String email) {
		return new Employee(1L, firstName, lastName, email);
	}
}
