package iam.sinny.springboottesting.service
        ;

import iam.sinny.springboottesting.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();
}