package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "attempt_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ao_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ao_aq_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AttemptQuestion attemptQuestion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ao_op_id", nullable = false)
    private Option option;

    @Column(name = "ao_selected", nullable = false)
    private boolean selected;
}
