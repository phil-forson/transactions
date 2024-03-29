package com.project.transactions.controller;

import com.project.transactions.model.Transaction;
import com.project.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping
    public Page<Transaction> getAllTransactions(Pageable pageable){
        return transactionService.getAllTransactions(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id){
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/create")
    public Transaction createTransaction(@RequestBody Transaction transaction){
        return transactionService.saveTransaction(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id,@RequestBody Transaction transaction){
        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

}
