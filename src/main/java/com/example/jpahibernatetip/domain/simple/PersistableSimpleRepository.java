package com.example.jpahibernatetip.domain.simple;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistableSimpleRepository extends JpaRepository<PersistableSimple, Long> {
}
