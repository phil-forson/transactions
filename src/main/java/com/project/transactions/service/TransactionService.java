package com.project.transactions.service;

import com.project.transactions.model.Transaction;
import java.util.List;


public interface TransactionService {
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(String id);
    Transaction saveTransaction(Transaction transaction);

    Transaction updateTransaction(String id, Transaction transaction);
    void deleteTransaction(String id);
}
