package com.project.transactions.service.impl;

import com.project.transactions.exception.InvalidTransactionException;
import com.project.transactions.exception.TransactionNotFoundException;
import com.project.transactions.model.Transaction;
import com.project.transactions.repository.TransactionRepository;
import com.project.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(String id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + id + " not found."));
    }

    @Override
    public Transaction saveTransaction(Transaction transaction){
        validateTransaction(transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(String id,Transaction transaction){
        if(!transactionRepository.existsById(id)){
            throw(new TransactionNotFoundException("Transaction with ID " + id + " not found."));
        }
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(String id){
        transactionRepository.deleteById(id);
    }

    private void validateTransaction(Transaction transaction) {
        StringBuilder errors = new StringBuilder();

        if (!StringUtils.hasText(transaction.getSender())) {
            errors.append("Sender cannot be empty. ");
        }

        if (!StringUtils.hasText(transaction.getReceiver())) {
            errors.append("Receiver cannot be empty. ");
        }

        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.append("Amount must be greater than 0. ");
        }

        if (transaction.getTransactionDate() == null || transaction.getTransactionDate().after(new Date())) {
            errors.append("Transaction date is invalid. ");
        }

        if (!errors.isEmpty()) {
            throw new InvalidTransactionException(errors.toString());
        }
    }


}
