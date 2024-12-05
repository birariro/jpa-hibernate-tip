package com.example.jpatipsample.domain;

import com.example.jpatipsample.domain.support.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "tb_payment_card")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
public class PaymentCard extends Identifiable {

    @Column(name = "owner_id", unique = true)
    private String ownerId;

    @NaturalId
    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "cvc")
    private String cvc;

    @Column(name = "expiryDate")
    private String expiryDate;

    public static PaymentCard of(String ownerId, String number, String cvc, String expiryDate) {
        return new PaymentCard(ownerId, number, cvc, expiryDate);
    }
}
