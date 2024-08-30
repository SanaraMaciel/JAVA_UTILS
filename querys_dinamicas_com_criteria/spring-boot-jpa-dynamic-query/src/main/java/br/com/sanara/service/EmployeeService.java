package br.com.sanara.service;

import org.springframework.data.domain.Pageable;

import br.com.sanara.model.Employee;

import java.util.List;


public interface EmployeeService {
    List<Employee> findEmployeeByEmployeeNameStartingWith(String name); // fetch list of Employee which start with
    List<Employee> findEmployeeByEmployeeRole(String role);         // fetch Employee by role
    List<Employee> findAll();
    Employee save(Employee employee);
    void delete(long employeeId);
    List<Employee> findByCriteria(String employeeName);
    List<Employee> findByCriteria(String employeeName,String employeeRole);
    List<Employee> findByLikeCriteria(String employeeName);
    List<Employee> findByLikeAndBetweenCriteria(String employeeName, int employeeIdStart, int employeeIdEnd);
    List<Employee> findByPagingCriteria(String employeeName,Pageable pageable);

}
