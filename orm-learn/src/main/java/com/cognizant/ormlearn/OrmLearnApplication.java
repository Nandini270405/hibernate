package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.*;
import com.cognizant.ormlearn.service.*;
import com.cognizant.ormlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    private static CountryService countryService;
    private static StockService stockService;
    private static EmployeeService employeeService;
    private static DepartmentService departmentService;
    private static SkillService skillService;
    private static AttemptService attemptService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        countryService = context.getBean(CountryService.class);
        stockService = context.getBean(StockService.class);
        employeeService = context.getBean(EmployeeService.class);
        departmentService = context.getBean(DepartmentService.class);
        skillService = context.getBean(SkillService.class);
        attemptService = context.getBean(AttemptService.class);

        // --- Execute Handson Tests ---
        
        // 1. Country CRUD Operations
        LOGGER.info("---------- Country Handson 1 & 6 & 7 & 8 & 9 ----------");
        testGetAllCountries();
        testFindCountryByCode();
        testAddCountry();
        testUpdateCountry();
        testDeleteCountry();

        // 2. Query Methods on Country
        LOGGER.info("---------- Country Query Methods Handson 1 ----------");
        testFindByNameContaining();
        testFindByNameContainingOrderByNameAsc();
        testFindByNameStartingWith();

        // 3. Stock Queries
        LOGGER.info("---------- Stock Queries Handson 2 ----------");
        testStockQueries();

        // 4. Payroll Mappings (Many-to-One, One-to-Many, Many-to-Many)
        LOGGER.info("---------- Payroll Mappings Handson 4 & 5 & 6 ----------");
        testGetEmployee();
        testAddEmployee();
        testUpdateEmployee();
        testGetDepartment();
        testAddSkillToEmployee();

        // 5. HQL, JPQL & Native Queries
        LOGGER.info("---------- HQL and JPQL and Native Queries Handson 2 & 4 & 5 ----------");
        testGetAllPermanentEmployees();
        testAverageSalary();
        testGetAllEmployeesNative();

        // 6. Quiz attempt details HQL fetch
        LOGGER.info("---------- Quiz Attempt Fetch Handson 3 ----------");
        testGetAttempt();
        
        LOGGER.info("All Exercises executed successfully!");
    }

    // --- Country Test Methods ---
    private static void testGetAllCountries() {
        LOGGER.info("Start testGetAllCountries");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("Total countries={}", countries.size());
        LOGGER.info("End testGetAllCountries");
    }

    private static void testFindCountryByCode() {
        LOGGER.info("Start testFindCountryByCode");
        try {
            Country country = countryService.findCountryByCode("IN");
            LOGGER.debug("Found Country: {}", country);
        } catch (CountryNotFoundException e) {
            LOGGER.error("Country not found: {}", e.getMessage());
        }
        LOGGER.info("End testFindCountryByCode");
    }

    private static void testAddCountry() {
        LOGGER.info("Start testAddCountry");
        Country newCountry = new Country("ZZ", "Test Country");
        countryService.addCountry(newCountry);
        try {
            Country fetched = countryService.findCountryByCode("ZZ");
            LOGGER.debug("Added Country: {}", fetched);
        } catch (CountryNotFoundException e) {
            LOGGER.error("Error adding: {}", e.getMessage());
        }
        LOGGER.info("End testAddCountry");
    }

    private static void testUpdateCountry() {
        LOGGER.info("Start testUpdateCountry");
        try {
            countryService.updateCountry("ZZ", "Updated Test Country");
            Country fetched = countryService.findCountryByCode("ZZ");
            LOGGER.debug("Updated Country: {}", fetched);
        } catch (CountryNotFoundException e) {
            LOGGER.error("Error updating: {}", e.getMessage());
        }
        LOGGER.info("End testUpdateCountry");
    }

    private static void testDeleteCountry() {
        LOGGER.info("Start testDeleteCountry");
        countryService.deleteCountry("ZZ");
        try {
            countryService.findCountryByCode("ZZ");
            LOGGER.error("Country ZZ was not deleted!");
        } catch (CountryNotFoundException e) {
            LOGGER.debug("Deleted Country ZZ successfully: {}", e.getMessage());
        }
        LOGGER.info("End testDeleteCountry");
    }

    private static void testFindByNameContaining() {
        LOGGER.info("Start testFindByNameContaining");
        List<Country> list = countryService.findByNameContaining("ou");
        for (Country c : list) {
            LOGGER.debug("{}      {}", c.getCode(), c.getName());
        }
        LOGGER.info("End testFindByNameContaining");
    }

    private static void testFindByNameContainingOrderByNameAsc() {
        LOGGER.info("Start testFindByNameContainingOrderByNameAsc");
        List<Country> list = countryService.findByNameContainingOrderByNameAsc("ou");
        for (Country c : list) {
            LOGGER.debug("{}      {}", c.getCode(), c.getName());
        }
        LOGGER.info("End testFindByNameContainingOrderByNameAsc");
    }

    private static void testFindByNameStartingWith() {
        LOGGER.info("Start testFindByNameStartingWith");
        List<Country> list = countryService.findByNameStartingWith("Z");
        for (Country c : list) {
            LOGGER.debug("{}      {}", c.getCode(), c.getName());
        }
        LOGGER.info("End testFindByNameStartingWith");
    }

    // --- Stock Test Methods ---
    private static void testStockQueries() {
        LOGGER.info("Start testStockQueries");
        LOGGER.debug("--- Facebook Stocks Sep 2019 ---");
        List<Stock> fbSep = stockService.getFacebookStocksSeptember2019();
        for (Stock s : fbSep) {
            LOGGER.debug("{} | {} | {} | {} | {}", s.getCode(), s.getDate(), s.getOpen(), s.getClose(), s.getVolume());
        }

        LOGGER.debug("--- Google Stocks > 1250 ---");
        List<Stock> goog = stockService.getGoogleStocksAbovePrice(BigDecimal.valueOf(1250));
        for (Stock s : goog) {
            LOGGER.debug("{} | {} | {} | {} | {}", s.getCode(), s.getDate(), s.getOpen(), s.getClose(), s.getVolume());
        }

        LOGGER.debug("--- Top 3 Highest Volume Stocks ---");
        List<Stock> topVol = stockService.getTop3HighestVolumeStocks();
        for (Stock s : topVol) {
            LOGGER.debug("{} | {} | {} | {} | {}", s.getCode(), s.getDate(), s.getOpen(), s.getClose(), s.getVolume());
        }

        LOGGER.debug("--- Top 3 Lowest Netflix Stocks ---");
        List<Stock> lowestNflx = stockService.getTop3LowestStocks("NFLX");
        for (Stock s : lowestNflx) {
            LOGGER.debug("{} | {} | {} | {} | {}", s.getCode(), s.getDate(), s.getOpen(), s.getClose(), s.getVolume());
        }
        LOGGER.info("End testStockQueries");
    }

    // --- Payroll Test Methods ---
    private static void testGetEmployee() {
        LOGGER.info("Start testGetEmployee");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee: {}", employee);
        if (employee != null) {
            LOGGER.debug("Department: {}", employee.getDepartment().getName());
            LOGGER.debug("Skills: {}", employee.getSkillList());
        }
        LOGGER.info("End testGetEmployee");
    }

    private static void testAddEmployee() {
        LOGGER.info("Start testAddEmployee");
        Employee emp = new Employee();
        emp.setName("New Hired");
        emp.setSalary(60000.00);
        emp.setPermanent(true);
        emp.setDateOfBirth(Date.valueOf("1996-04-12"));
        
        Department dept = departmentService.get(1);
        emp.setDepartment(dept);
        
        employeeService.save(emp);
        LOGGER.debug("Added Employee ID: {}", emp.getId());
        LOGGER.info("End testAddEmployee");
    }

    private static void testUpdateEmployee() {
        LOGGER.info("Start testUpdateEmployee");
        Employee employee = employeeService.get(1);
        if (employee != null) {
            Department newDept = departmentService.get(2);
            employee.setDepartment(newDept);
            employeeService.save(employee);
            LOGGER.debug("Updated employee 1 department to: {}", employee.getDepartment().getName());
        }
        LOGGER.info("End testUpdateEmployee");
    }

    private static void testGetDepartment() {
        LOGGER.info("Start testGetDepartment");
        Department dept = departmentService.get(1);
        if (dept != null) {
            LOGGER.debug("Department Name: {}", dept.getName());
            LOGGER.debug("Employees under Department: {}", dept.getEmployeeList());
        }
        LOGGER.info("End testGetDepartment");
    }

    private static void testAddSkillToEmployee() {
        LOGGER.info("Start testAddSkillToEmployee");
        Employee employee = employeeService.get(4); // Alice Brown has no skills by default
        Skill skill = skillService.get(1); // Java skill
        if (employee != null && skill != null) {
            Set<Skill> skills = employee.getSkillList();
            skills.add(skill);
            employeeService.save(employee);
            LOGGER.debug("Added skill Java to Alice. Current Skills: {}", employee.getSkillList());
        }
        LOGGER.info("End testAddSkillToEmployee");
    }

    // --- HQL Test Methods ---
    private static void testGetAllPermanentEmployees() {
        LOGGER.info("Start testGetAllPermanentEmployees");
        List<Employee> permanent = employeeService.getAllPermanentEmployees();
        LOGGER.debug("Permanent Employees (HQL): {}", permanent);
        for (Employee e : permanent) {
            LOGGER.debug("Skills: {}", e.getSkillList());
        }
        LOGGER.info("End testGetAllPermanentEmployees");
    }

    private static void testAverageSalary() {
        LOGGER.info("Start testAverageSalary");
        double avg = employeeService.getAverageSalary();
        LOGGER.debug("Average salary overall: {}", avg);

        double avgDept1 = employeeService.getAverageSalary(1);
        LOGGER.debug("Average salary Department 1: {}", avgDept1);
        LOGGER.info("End testAverageSalary");
    }

    private static void testGetAllEmployeesNative() {
        LOGGER.info("Start testGetAllEmployeesNative");
        List<Employee> list = employeeService.getAllEmployeesNative();
        LOGGER.debug("All employees (Native Query): {}", list);
        LOGGER.info("End testGetAllEmployeesNative");
    }

    // --- Quiz attempt details ---
    private static void testGetAttempt() {
        LOGGER.info("Start testGetAttempt");
        Attempt attempt = attemptService.getAttempt(1, 1);
        if (attempt != null) {
            LOGGER.debug("User: {}, Attempt Date: {}", attempt.getUser().getName(), attempt.getDate());
            for (AttemptQuestion aq : attempt.getAttemptQuestions()) {
                Question q = aq.getQuestion();
                LOGGER.debug("{}", q.getText());
                int optionIdx = 1;
                for (Option o : q.getOptions()) {
                    boolean selected = false;
                    for (AttemptOption ao : aq.getAttemptOptions()) {
                        if (ao.getOption().getId() == o.getId()) {
                            selected = ao.isSelected();
                            break;
                        }
                    }
                    LOGGER.debug(" {}) {}\t{}\t{}", optionIdx++, o.getText(), o.getScore(), selected);
                }
                LOGGER.debug("");
            }
        }
        LOGGER.info("End testGetAttempt");
    }
}
