package com.mbi.study.repository;

import com.mbi.study.repository.entity.Loan;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> getLoansByUserId(@NotNull Long userId);
}
