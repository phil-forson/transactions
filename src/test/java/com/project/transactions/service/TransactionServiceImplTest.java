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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction validTransaction;
    private Transaction invalidTransaction;

    @BeforeEach
    void setUp() {
        validTransaction = new Transaction("1", "senderId", "receiverId", new BigDecimal("100.00"), new Date());
        invalidTransaction = new Transaction("2", "", "", new BigDecimal("-100.00"), new Date(System.currentTimeMillis() + 100000));
    }

    @Test
    void getAllTransactions_ReturnsNonEmptyList() {
        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(validTransaction));
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.size());
        verify(transactionRepository).findAll();
    }

    @Test
    void getTransactionById_ExistingId_ReturnsTransaction() {
        when(transactionRepository.findById(validTransaction.getId())).thenReturn(Optional.of(validTransaction));
        Transaction found = transactionService.getTransactionById(validTransaction.getId());
        assertNotNull(found);
        assertEquals(validTransaction.getId(), found.getId());
    }

    @Test
    void getTransactionById_NonExistingId_ThrowsException() {
        when(transactionRepository.findById("nonExistingId")).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById("nonExistingId"));
    }

    @Test
    void saveTransaction_ValidTransaction_SavesSuccessfully() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(validTransaction);
        Transaction saved = transactionService.saveTransaction(validTransaction);
        assertNotNull(saved);
        assertEquals(validTransaction.getAmount(), saved.getAmount());
    }

    @Test
    void saveTransaction_InvalidTransaction_ThrowsException() {
        assertThrows(InvalidTransactionException.class, () -> transactionService.saveTransaction(invalidTransaction));
    }

    @Test
    void updateTransaction_ExistingId_UpdatesSuccessfully() {
        when(transactionRepository.existsById(validTransaction.getId())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(validTransaction);
        Transaction updated = transactionService.updateTransaction(validTransaction.getId(), validTransaction);
        assertNotNull(updated);
    }

    @Test
    void updateTransaction_NonExistingId_ThrowsException() {
        when(transactionRepository.existsById("nonExistingId")).thenReturn(false);
        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction("nonExistingId", validTransaction));
    }

    @Test
    void deleteTransaction_ExistingId_DeletesSuccessfully() {
        doNothing().when(transactionRepository).deleteById(validTransaction.getId());
        transactionService.deleteTransaction(validTransaction.getId());
        verify(transactionRepository).deleteById(validTransaction.getId());
    }
}

