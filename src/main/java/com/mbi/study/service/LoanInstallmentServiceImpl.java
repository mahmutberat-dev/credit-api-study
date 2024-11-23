package com.mbi.study.service;

import com.mbi.study.common.mapper.LoanInstallmentMapper;
import com.mbi.study.controller.dto.LoanInstallmentResponse;
import com.mbi.study.repository.LoanInstallmentRepository;
import com.mbi.study.repository.entity.LoanInstallment;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanInstallmentServiceImpl implements LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanInstallmentMapper loanInstallmentMapper;

    @Override
    public List<LoanInstallmentResponse> getByLoanId(Long loanId) {
        List<LoanInstallment> installments = loanInstallmentRepository.findAllById(loanId);
//        if (installments.isEmpty()) {
//            throw new EntityNotFoundException(String.format("There is no any installments found for loanId: %s", loanId));
//        }
        return installments.stream()
                .map(loanInstallmentMapper::fromLoanInstallment)
                .toList();
    }
}
