package com.example.jpahibernatetip.domain.simple;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_no_generated_value")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Simple {
    @Id
    private String id;

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    public Simple(String id) {
        this.id = id;
    }
}
