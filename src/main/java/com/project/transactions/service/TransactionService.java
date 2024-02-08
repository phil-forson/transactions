package com.project.transactions.service;

import com.project.transactions.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface TransactionService {
    Page<Transaction> getAllTransactions(Pageable pageable);
    Transaction getTransactionById(String id);
    Transaction saveTransaction(Transaction transaction);

    Transaction updateTransaction(String id, Transaction transaction);
    void deleteTransaction(String id);
}
