package com.project.transactions.repository;

import com.project.transactions.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
