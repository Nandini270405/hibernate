package com.cognizant.ems.service;

import com.cognizant.ems.model.Department;
import com.cognizant.ems.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Department> getById(Long id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
