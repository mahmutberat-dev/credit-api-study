package com.mbi.study.repository;

import com.mbi.study.repository.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findAllById(Long id);

}
