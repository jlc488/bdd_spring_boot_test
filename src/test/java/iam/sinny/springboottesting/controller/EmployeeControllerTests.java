package iam.sinny.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iam.sinny.springboottesting.model.Employee;
import iam.sinny.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @Autowired
    ObjectMapper objectMapper;

    //JUnit Test for create employee
    @Test
    @DisplayName("Create Employee")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //give - precondition or setup
        Employee employee = Employee.builder().firstName("sin").lastName("kang").email("emp@gmail.com").build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    //JUnit Test for get all employees REST API
    @Test
    @DisplayName("Get All Employee ")
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        //give - precondition or setup
        List<Employee> list = new ArrayList<>();
        list.add(Employee.builder().firstName("sin1").lastName("kang1").email("s1@gmail.com").build());
        list.add(Employee.builder().firstName("sin2").lastName("kang2").email("s2@gmail.com").build());

        given(employeeService.getAllEmployees()).willReturn(list);

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    //JUnit Test for get employee by id REST API
    @Test
    @DisplayName("Get Employee by Id")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //give - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder().firstName("sin").lastName("kang").email("emp@gmail.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    //JUnit Test for get employee by id REST API
    @Test
    @DisplayName("Get Employee by Id - negative")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //give - precondition or setup
        long employeeId = 1L;

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //JUnit Test for Update employee
    @Test
    @DisplayName("Update Employee")
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //give - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder().firstName("sin1").lastName("kang2").email("emp1@gmail.com").build();
        Employee updatedEmployee = Employee.builder().firstName("sin2").lastName("kang2").email("emp2@gmail.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

    //JUnit Test for Update employee
    @Test
    @DisplayName("Update Employee - negative")
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnEmptyObject() throws Exception {
        //give - precondition or setup
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder().firstName("sin2").lastName("kang2").email("emp2@gmail.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    //JUnit Test for delete employee
    @Test
    @DisplayName("Delete Employee")
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //give - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}