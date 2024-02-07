package com.project.transactions.service.impl;

import com.project.transactions.model.Transaction;
import com.project.transactions.repository.TransactionRepository;
import com.project.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransactionById(String id){
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(String id,Transaction transaction){
        if(!transactionRepository.existsById(id)){
            return null;
        }
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(String id){
        transactionRepository.deleteById(id);
    }


}
