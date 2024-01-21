package iam.sinny.springboottesting.service;

import iam.sinny.springboottesting.exception.ResourceNotFoundException;
import iam.sinny.springboottesting.model.Employee;
import iam.sinny.springboottesting.repository.EmployeeRepository;
import iam.sinny.springboottesting.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeServiceImpl employeeService;

    Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder().id(1L).firstName("sin").lastName("kang").email("jlc488@gmail.com").build();
    }


    //JUnit Test for saveEmployee method
    @Test
    @DisplayName("Save Employee Test")
    public void giveEmployeeObject_whenSaveEmployee_thenReturnSavedEmployeeObject() {
        //give - precondition or setup
        given(employeeRepository.findEmployeeByEmail(employee.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour that we are test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit Test for saveEmployee method returns Exception
    @Test
    @DisplayName("Save Employee Exception Test")
    public void giveExistingEmail_whenSaveEmployee_thenThrowsException() {
        //give - precondition or setup
        given(employeeRepository.findEmployeeByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour that we are test
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //JUnit Test for Find all Employees
    @Test
    @DisplayName("Get all employees")
    public void giveEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {

        Employee employee2 = Employee.builder().id(2L).firstName("sin2").lastName("kang2").email("jlc2@gmail.com").build();
        //give - precondition or setup
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        //when - action or the behaviour that we are test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Get all employees (negative)")
    public void giveEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {

        //give - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behaviour that we are test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //JUnit Test for Get Employee by Id
    @Test
    @DisplayName("Get Employee by Id")
    public void giveEmployeeId_whenGetEmployeeId_thenReturnEmployeeObject() {
        //give - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();


        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit Test for Update Employee
    @Test
    @DisplayName("Update Employee")
    public void giveEmployeeObject_whenUpdateemployee_thenReturnEmployeeObject() {
        //give - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("test@gmail.com");
        employee.setFirstName("sinny");

        //when - action or the behaviour that we are test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("test@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("sinny");
    }

    //JUnit Test for Delete Employee by Id
    @Test
    @DisplayName("Delete employee by id")
    public void giveEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //give - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when - action or the behaviour that we are test
        employeeService.deleteEmployee(employeeId);

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}