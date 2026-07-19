package com.cognizant.ems.controller;

import com.cognizant.ems.model.Employee;
import com.cognizant.ems.projection.EmployeeProjection;
import com.cognizant.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return employeeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        return employeeService.getById(id)
                .map(existing -> {
                    existing.setName(employeeDetails.getName());
                    existing.setEmail(employeeDetails.getEmail());
                    existing.setDepartment(employeeDetails.getDepartment());
                    Employee updated = employeeService.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (employeeService.getById(id).isPresent()) {
            employeeService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Pagination and Sorting Search endpoint (Exercise 6)
    @GetMapping("/search")
    public Page<Employee> search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeService.searchByName(name, pageable);
    }

    // Named Query endpoint (Exercise 5)
    @GetMapping("/by-email")
    public List<Employee> getByEmailNamed(@RequestParam String email) {
        return employeeService.findByEmailNamed(email);
    }

    // Custom JPQL Query endpoint (Exercise 5)
    @GetMapping("/by-department")
    public List<Employee> getByDepartmentName(@RequestParam String deptName) {
        return employeeService.findByDepartmentName(deptName);
    }

    // Projections endpoint (Exercise 8)
    @GetMapping("/projected")
    public List<EmployeeProjection> getProjected() {
        return employeeService.getProjectedEmployees();
    }
}
