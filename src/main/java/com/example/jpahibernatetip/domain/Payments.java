package com.example.jpahibernatetip.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Payments extends JpaRepository<Payment, Long> {
}
