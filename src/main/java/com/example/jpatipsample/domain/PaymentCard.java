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
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "tb_payment_card")
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCard extends Identifiable {

    @Column(name = "owner_id", unique = true)
    private String ownerId;

    /**
     * todo tip
     */
    @NaturalId
    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "cvc")
    private String cvc;

    @Column(name = "expiryDate")
    private String expiryDate;

    public static PaymentCard of(String ownerId, String number, String cvc, String expiryDate) {
        if (number.length() < 10 || number.length() > 16) {
            throw new IllegalArgumentException("The 'card number' is 10 to 16 length");
        }
        if (cvc.length() != 3) {
            throw new IllegalArgumentException("The 'card cvc' is 3 length");
        }
        if (expiryDate.length() != 4) {
            throw new IllegalArgumentException("The 'card expiry date' is 4 length");
        }
        return new PaymentCard(ownerId, number, cvc, expiryDate);
    }
}
