package com.cognizant.ems.repository;

import com.cognizant.ems.model.Employee;
import com.cognizant.ems.projection.EmployeeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Derived Query Method with pagination and sorting (Exercise 5 & 6)
    Page<Employee> findByNameContaining(String name, Pageable pageable);

    // Named Query Execution (Exercise 5)
    List<Employee> findByEmailNamed(@Param("email") String email);

    // Custom query using @Query annotation (Exercise 5)
    @Query("SELECT e FROM Employee e WHERE e.department.name = :deptName")
    List<Employee> findByDepartmentName(@Param("deptName") String deptName);

    // Projection method (Exercise 8)
    List<EmployeeProjection> findProjectedBy();

    List<Employee> findByDepartmentId(Long departmentId);
}
