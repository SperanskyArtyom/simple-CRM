package ru.shift.demo.simple_crm.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.domain.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"seller"})
    Optional<Transaction> findById(@NonNull Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"seller"})
    List<Transaction> findAll();

    @EntityGraph(attributePaths = {"seller"})
    List<Transaction> findAllBySellerId(Long sellerId);

    @Query("""
                SELECT t.seller FROM Transaction t
                WHERE t.transactionDate BETWEEN :start AND :end
                GROUP BY t.seller
                ORDER BY SUM(t.amount) DESC
                LIMIT 1
            """)
    Optional<Seller> findTopSellerInPeriod(LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT t.seller FROM Transaction t
                WHERE t.transactionDate BETWEEN :start AND :end
                GROUP BY t.seller
                HAVING SUM(t.amount) < :maxTotalAmount
            """)
    List<Seller> findSellersWithTotalTransactionsAmountLessThan(LocalDateTime start, LocalDateTime end, BigDecimal maxTotalAmount);
}
