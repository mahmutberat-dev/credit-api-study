package com.mbi.study.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Column(name = "used_credit_limit")
    private BigDecimal usedCreditLimit;

    public boolean hasEnoughLimit(BigDecimal newCreditAmount) {
        return newCreditAmount.compareTo(creditLimit) <= 0;
    }
}
