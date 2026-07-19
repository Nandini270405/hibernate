package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Table(name = "attempt_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aq_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aq_at_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Attempt attempt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aq_qn_id", nullable = false)
    private Question question;

    @OneToMany(mappedBy = "attemptQuestion", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AttemptOption> attemptOptions;
}
