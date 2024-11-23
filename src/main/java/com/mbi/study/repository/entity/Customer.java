package com.mbi.study.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Column(name = "used_credit_limit")
    private BigDecimal usedCreditLimit;

    @OneToMany(mappedBy = "customer")
    private List<Loan> loans;

    public boolean hasEnoughLimit(BigDecimal newCreditAmount) {
        return newCreditAmount.compareTo(creditLimit) <= 0;
    }
}
