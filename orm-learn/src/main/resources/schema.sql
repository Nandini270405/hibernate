DROP TABLE IF EXISTS attempt_option;
DROP TABLE IF EXISTS attempt_question;
DROP TABLE IF EXISTS options;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS attempt;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS employee_skill;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS country;

CREATE TABLE country (
    co_code VARCHAR(2) PRIMARY KEY,
    co_name VARCHAR(100) NOT NULL
);

CREATE TABLE stock (
    st_id INT AUTO_INCREMENT PRIMARY KEY,
    st_code VARCHAR(10) NOT NULL,
    st_date DATE NOT NULL,
    st_open DECIMAL(10,2) NOT NULL,
    st_close DECIMAL(10,2) NOT NULL,
    st_volume BIGINT NOT NULL
);

CREATE TABLE department (
    dp_id INT AUTO_INCREMENT PRIMARY KEY,
    dp_name VARCHAR(100) NOT NULL
);

CREATE TABLE employee (
    em_id INT AUTO_INCREMENT PRIMARY KEY,
    em_name VARCHAR(100) NOT NULL,
    em_salary DECIMAL(10,2) NOT NULL,
    em_permanent BOOLEAN NOT NULL,
    em_date_of_birth DATE NOT NULL,
    em_dp_id INT,
    FOREIGN KEY (em_dp_id) REFERENCES department(dp_id)
);

CREATE TABLE skill (
    sk_id INT AUTO_INCREMENT PRIMARY KEY,
    sk_name VARCHAR(100) NOT NULL
);

CREATE TABLE employee_skill (
    es_em_id INT NOT NULL,
    es_sk_id INT NOT NULL,
    PRIMARY KEY (es_em_id, es_sk_id),
    FOREIGN KEY (es_em_id) REFERENCES employee(em_id) ON DELETE CASCADE,
    FOREIGN KEY (es_sk_id) REFERENCES skill(sk_id) ON DELETE CASCADE
);

CREATE TABLE users (
    us_id INT AUTO_INCREMENT PRIMARY KEY,
    us_name VARCHAR(100) NOT NULL
);

CREATE TABLE attempt (
    at_id INT AUTO_INCREMENT PRIMARY KEY,
    at_us_id INT NOT NULL,
    at_date DATE NOT NULL,
    FOREIGN KEY (at_us_id) REFERENCES users(us_id)
);

CREATE TABLE question (
    qn_id INT AUTO_INCREMENT PRIMARY KEY,
    qn_text VARCHAR(255) NOT NULL
);

CREATE TABLE options (
    op_id INT AUTO_INCREMENT PRIMARY KEY,
    op_qn_id INT NOT NULL,
    op_text VARCHAR(100) NOT NULL,
    op_score DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (op_qn_id) REFERENCES question(qn_id)
);

CREATE TABLE attempt_question (
    aq_id INT AUTO_INCREMENT PRIMARY KEY,
    aq_at_id INT NOT NULL,
    aq_qn_id INT NOT NULL,
    FOREIGN KEY (aq_at_id) REFERENCES attempt(at_id),
    FOREIGN KEY (aq_qn_id) REFERENCES question(qn_id)
);

CREATE TABLE attempt_option (
    ao_id INT AUTO_INCREMENT PRIMARY KEY,
    ao_aq_id INT NOT NULL,
    ao_op_id INT NOT NULL,
    ao_selected BOOLEAN NOT NULL,
    FOREIGN KEY (ao_aq_id) REFERENCES attempt_question(aq_id),
    FOREIGN KEY (ao_op_id) REFERENCES options(op_id)
);
