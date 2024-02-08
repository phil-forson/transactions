package com.project.transactions.service;

import com.project.transactions.exception.InvalidTransactionException;
import com.project.transactions.exception.TransactionNotFoundException;
import com.project.transactions.model.Transaction;
import com.project.transactions.repository.TransactionRepository;
import com.project.transactions.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction("1", "sender", "receiver", new BigDecimal("100.00"), new Date());
    }

    @Test
    void whenGetAllTransactionsWithPagination_thenSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Transaction> transactions = Collections.singletonList(transaction);
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, transactions.size());

        when(transactionRepository.findAll(pageable)).thenReturn(transactionPage);

        Page<Transaction> result = transactionService.getAllTransactions(pageable);

        assertAll("Ensure correct page information",
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(transaction.getId(), result.getContent().get(0).getId()));
    }

    @Test
    void whenGetTransactionById_thenSuccess() {
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        Transaction found = transactionService.getTransactionById(transaction.getId());
        assertNotNull(found);
        assertEquals(transaction.getId(), found.getId());
    }

    @Test
    void whenGetTransactionById_thenNotFound() {
        when(transactionRepository.findById("nonexistent")).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById("nonexistent"));
    }

    @Test
    void whenSaveTransactionWithValidData_thenSuccess() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction saved = transactionService.saveTransaction(transaction);
        assertNotNull(saved);
        assertEquals(transaction.getId(), saved.getId());
    }

    @Test
    void whenSaveTransactionWithInvalidData_thenThrowException() {
        Transaction invalidTransaction = new Transaction("", "", "", BigDecimal.ZERO, new Date());
        assertThrows(InvalidTransactionException.class, () -> transactionService.saveTransaction(invalidTransaction));
    }

    @Test
    void whenUpdateTransactionWithExistingId_thenSuccess() {
        Transaction updatedTransaction = new Transaction(transaction.getId(), "updatedSender", "updatedReceiver", new BigDecimal("200.00"), new Date());
        when(transactionRepository.existsById(transaction.getId())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        Transaction result = transactionService.updateTransaction(transaction.getId(), updatedTransaction);

        assertNotNull(result);
        assertEquals("updatedSender", result.getSender());
    }

    @Test
    void whenUpdateTransactionWithNonExistingId_thenThrowException() {
        when(transactionRepository.existsById("nonexistent")).thenReturn(false);
        Transaction updatedTransaction = new Transaction("nonexistent", "sender", "receiver", new BigDecimal("100.00"), new Date());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction("nonexistent", updatedTransaction));
    }

    @Test
    void whenDeleteTransactionWithExistingId_thenSuccess() {
        doNothing().when(transactionRepository).deleteById(transaction.getId());
        transactionService.deleteTransaction(transaction.getId());
        verify(transactionRepository, times(1)).deleteById(transaction.getId());
    }
}
