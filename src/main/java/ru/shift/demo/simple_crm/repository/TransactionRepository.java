package ru.shift.demo.simple_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shift.demo.simple_crm.domain.entity.Seller;
import ru.shift.demo.simple_crm.domain.entity.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySellerId(Long sellerId);

    //    Native sql для включения в аналитику транзакций, совершённых удалённым продавцом
    @Query(value = """
                SELECT s.* FROM sellers s
                JOIN transactions t ON s.id = t.seller_id
                WHERE t.transaction_date BETWEEN :start AND :end
                GROUP BY s.id
                ORDER BY SUM(t.amount) DESC
                LIMIT 1
            """, nativeQuery = true)
    Optional<Seller> findTopSellerInPeriod(Instant start, Instant end);

    @Query(value = """
                SELECT s.* FROM sellers s
                JOIN transactions t ON s.id = t.seller_id
                WHERE t.transaction_date BETWEEN :start AND :end
                GROUP BY s.id
                HAVING SUM(t.amount) < :maxTotalAmount
            """, nativeQuery = true)
    List<Seller> findSellersWithTotalTransactionsAmountLessThan(Instant start, Instant end, BigDecimal maxTotalAmount);
}
