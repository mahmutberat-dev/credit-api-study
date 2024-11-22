package com.mbi.study.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private Long name;
    @Column(name = "surname")
    private Long surname;
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Column(name = "used_credit_limit")
    private BigDecimal usedCreditLimit;

    public boolean hasEnoughLimit(BigDecimal newCreditAmount) {
        return newCreditAmount.compareTo(creditLimit) <= 0;
    }
}
