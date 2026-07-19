package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "attempt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "at_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "at_us_id", nullable = false)
    private User user;

    @Column(name = "at_date", nullable = false)
    private Date date;

    @OneToMany(mappedBy = "attempt", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AttemptQuestion> attemptQuestions;
}
