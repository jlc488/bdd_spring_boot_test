package iam.sinny.springboottesting.repository;

import iam.sinny.springboottesting.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    //JUnit test for save employee
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        Employee employee = Employee.builder().firstName("sin").lastName("kang").email("jlc488@gmail.com").build();

        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //JUnit Test for
    @Test
    @DisplayName("Test for get all employee operation")
    public void givenEmployeelist_whenFindAll_thenEmployeeList() {
        //give - precondition or setup
        Employee employee1 = Employee.builder().firstName("sin1").lastName("kang1").email("jlc1@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("sin2").lastName("kang2").email("jlc2@gmail.com").build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or the behaviour that we are test
        List<Employee> list = employeeRepository.findAll();

        //then - verify the output
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }

    //JUnit Test for get emplooyee by id operation
    @Test
    @DisplayName("Test for get employee by id")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //give - precondition or setup
        Employee employee1 = Employee.builder().firstName("sin1").lastName("kang1").email("jlc1@gmail.com").build();

        employeeRepository.save(employee1);

        //when - action or the behaviour that we are test
        Employee employeeDB = employeeRepository.findById(employee1.getId()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //JUnit Test for
    @Test
    @DisplayName("Test for get employee by email")
    public void givenEmployeeEmail_whenFindByEmail_thenReturningEmployeeObject() {
        //give - precondition or setup
        Employee employee1 = Employee.builder().firstName("sin1").lastName("kang1").email("jlc1@gmail.com").build();
        employeeRepository.save(employee1);

        //when - action or the behaviour that we are test
        Employee employeeDB = employeeRepository.findEmployeeByEmail(employee1.getEmail()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();
    }
}