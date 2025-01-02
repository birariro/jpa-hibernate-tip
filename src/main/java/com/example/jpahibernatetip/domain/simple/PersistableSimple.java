package com.example.jpahibernatetip.domain.simple;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_no_generated_value")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PersistableSimple implements Persistable<String> {
    @Id
    private String id;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    public PersistableSimple(String id) {
        this.id = id;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
