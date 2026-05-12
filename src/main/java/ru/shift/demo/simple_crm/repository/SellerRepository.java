package ru.shift.demo.simple_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shift.demo.simple_crm.domain.entity.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
