package com.cognizant.ems.service;

import com.cognizant.ems.model.Employee;
import com.cognizant.ems.projection.EmployeeProjection;
import com.cognizant.ems.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Employee> getById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Employee> searchByName(String name, Pageable pageable) {
        return employeeRepository.findByNameContaining(name, pageable);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByEmailNamed(String email) {
        return employeeRepository.findByEmailNamed(email);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByDepartmentName(String deptName) {
        return employeeRepository.findByDepartmentName(deptName);
    }

    @Transactional(readOnly = true)
    public List<EmployeeProjection> getProjectedEmployees() {
        return employeeRepository.findProjectedBy();
    }

    // Hibernate Batch Processing (Exercise 10)
    @Transactional
    public void saveEmployeesInBatch(List<Employee> employees) {
        int batchSize = 20;
        for (int i = 0; i < employees.size(); i++) {
            employeeRepository.save(employees.get(i));
            if (i > 0 && i % batchSize == 0) {
                // Flush a batch of inserts and release memory
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
