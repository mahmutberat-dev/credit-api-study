package com.mbi.study.repository.entity;

import com.mbi.study.common.UserRoleEnum;
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
public class Customer extends BaseEntity implements LoanAppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerIdGenerator")
    @SequenceGenerator(name = "customerIdGenerator", sequenceName = "customer_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "password")
    private String password;
    @Column(name = "username")
    private String username;
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Column(name = "used_credit_limit")
    private BigDecimal usedCreditLimit;

    @OneToMany(mappedBy = "customer")
    private List<Loan> loans;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum roleName;

    public boolean hasEnoughLimit(BigDecimal newCreditAmount) {
        return newCreditAmount.add(usedCreditLimit).compareTo(creditLimit) <= 0;
    }

    public void addUserCreditLimit(BigDecimal totalLoanAmount) {
        BigDecimal newUsedLimit = getUsedCreditLimit().add(totalLoanAmount);
        setUsedCreditLimit(newUsedLimit);
    }

    @Override
    public long getUserId() {
        return id;
    }

    @Override
    public String getUserName() {
        return "%s_%s".formatted(name, surname);
    }
}
