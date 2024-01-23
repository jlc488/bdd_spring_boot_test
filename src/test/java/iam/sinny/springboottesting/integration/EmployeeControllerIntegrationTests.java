package iam.sinny.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import iam.sinny.springboottesting.model.Employee;
import iam.sinny.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test for Employee Controller using test containers
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests extends AbstractionBaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();

    }

    @Test
    @DisplayName("Create Employee Integration Test")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //give - precondition or setup
        Employee employee = Employee.builder().firstName("sin").lastName("kang").email("emp@gmail.com").build();

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
    @DisplayName("Get All Employee integration test")
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        //give - precondition or setup
        List<Employee> list = new ArrayList<>();
        list.add(Employee.builder().firstName("sin1").lastName("kang1").email("s1@gmail.com").build());
        list.add(Employee.builder().firstName("sin2").lastName("kang2").email("s2@gmail.com").build());

        employeeRepository.saveAll(list);

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    //JUnit Test for get employee by id REST API
    @Test
    @DisplayName("Get Employee by Id integration test")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //give - precondition or setup
        Employee employee = Employee.builder().firstName("sin").lastName("kang").email("emp@gmail.com").build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    //JUnit Test for get employee by id REST API
    @Test
    @DisplayName("Get Employee by Id - negative integration test")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //give - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder().firstName("sin").lastName("kang").email("emp@gmail.com").build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //JUnit Test for Update employee
    @Test
    @DisplayName("Update Employee - integration test")
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //give - precondition or setup
        Employee savedEmployee = Employee.builder().firstName("sin1").lastName("kang2").email("emp1@gmail.com").build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder().firstName("sin2").lastName("kang2").email("emp2@gmail.com").build();


        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
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
    @DisplayName("Update Employee - negative integration test")
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnEmptyObject() throws Exception {
        //give - precondition or setup
        long employeeId = 11L;
        Employee savedEmployee = Employee.builder().firstName("sin1").lastName("kang2").email("emp1@gmail.com").build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder().firstName("sin2").lastName("kang2").email("emp2@gmail.com").build();

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
        Employee employee = Employee.builder().firstName("sin1").lastName("kang2").email("emp1@gmail.com").build();
        employeeRepository.save(employee);

        //when - action or the behaviour that we are test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}