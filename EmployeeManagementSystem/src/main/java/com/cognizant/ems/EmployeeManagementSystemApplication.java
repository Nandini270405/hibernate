package com.cognizant.ems;

import com.cognizant.ems.model.Department;
import com.cognizant.ems.model.Employee;
import com.cognizant.ems.projection.EmployeeProjection;
import com.cognizant.ems.service.DepartmentService;
import com.cognizant.ems.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class EmployeeManagementSystemApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeManagementSystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner runDemo(EmployeeService employeeService, DepartmentService departmentService) {
        return args -> {
            LOGGER.info("Inside EMS Application Runner...");

            // 1. Create Departments (Exercise 2 & 4 CRUD)
            Department hr = new Department();
            hr.setName("Human Resources");
            departmentService.save(hr);

            Department rd = new Department();
            rd.setName("Research & Development");
            departmentService.save(rd);

            LOGGER.info("Saved departments: R&D ID={}, HR ID={}", rd.getId(), hr.getId());

            // 2. Create Employees (Exercise 2 & 4 CRUD)
            Employee emp1 = new Employee();
            emp1.setName("Alice Miller");
            emp1.setEmail("alice@company.com");
            emp1.setDepartment(rd);
            employeeService.save(emp1);

            Employee emp2 = new Employee();
            emp2.setName("Bob Davis");
            emp2.setEmail("bob@company.com");
            emp2.setDepartment(rd);
            employeeService.save(emp2);

            Employee emp3 = new Employee();
            emp3.setName("Charlie Smith");
            emp3.setEmail("charlie@company.com");
            emp3.setDepartment(hr);
            employeeService.save(emp3);

            LOGGER.info("Saved employees Alice, Bob, Charlie.");

            // 3. Retrieve and display Auditing details (Exercise 7)
            Employee fetchedEmp1 = employeeService.getById(emp1.getId()).orElse(null);
            if (fetchedEmp1 != null) {
                LOGGER.info("--- Auditing Fields (Exercise 7) ---");
                LOGGER.info("Employee: {}", fetchedEmp1.getName());
                LOGGER.info("Created By: {}", fetchedEmp1.getCreatedBy());
                LOGGER.info("Created Date: {}", fetchedEmp1.getCreatedDate());
                LOGGER.info("Last Modified By: {}", fetchedEmp1.getLastModifiedBy());
                LOGGER.info("Last Modified Date: {}", fetchedEmp1.getLastModifiedDate());
            }

            // 4. Custom Queries and Named Queries (Exercise 5)
            LOGGER.info("--- Named Query Results (Exercise 5) ---");
            List<Employee> byEmailNamed = employeeService.findByEmailNamed("bob@company.com");
            byEmailNamed.forEach(e -> LOGGER.info("Named Query match: {}, Email: {}", e.getName(), e.getEmail()));

            LOGGER.info("--- Custom JPQL Query Results (Exercise 5) ---");
            List<Employee> byDept = employeeService.findByDepartmentName("Research & Development");
            byDept.forEach(e -> LOGGER.info("JPQL Query match (R&D): {}", e.getName()));

            // 5. Pagination and Sorting (Exercise 6)
            LOGGER.info("--- Pagination and Sorting Results (Exercise 6) ---");
            // Find employees with name containing 'i', page 0, size 2, sorted by name descending
            Page<Employee> page = employeeService.searchByName("i", PageRequest.of(0, 2, Sort.by("name").descending()));
            LOGGER.info("Total Elements found: {}", page.getTotalElements());
            LOGGER.info("Total Pages: {}", page.getTotalPages());
            page.getContent().forEach(e -> LOGGER.info("Page Content: {} - {}", e.getName(), e.getEmail()));

            // 6. Projections (Exercise 8)
            LOGGER.info("--- Projections Results (Exercise 8) ---");
            List<EmployeeProjection> projections = employeeService.getProjectedEmployees();
            projections.forEach(p -> LOGGER.info("Projection match: ID={}, Name={}, Email={}", p.getId(), p.getName(), p.getEmail()));

            // 7. Hibernate Batch Processing (Exercise 10)
            LOGGER.info("--- Batch Processing (Exercise 10) ---");
            List<Employee> batchEmployees = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                Employee temp = new Employee();
                temp.setName("BatchEmp " + i);
                temp.setEmail("batch_" + i + "@company.com");
                temp.setDepartment(hr);
                batchEmployees.add(temp);
            }
            long startTime = System.currentTimeMillis();
            employeeService.saveEmployeesInBatch(batchEmployees);
            long endTime = System.currentTimeMillis();
            LOGGER.info("Saved 50 employees in batch mode. Execution took {} ms.", (endTime - startTime));
            
            LOGGER.info("EMS Application Demo execution finished successfully!");
        };
    }
}
