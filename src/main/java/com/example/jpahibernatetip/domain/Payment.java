package com.example.jpahibernatetip.domain;

import com.example.jpahibernatetip.domain.support.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_payment")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Setter
public class Payment extends Identifiable {

    @Embedded
    private Amount amount;

    @Embedded
    private VAT vat;

    @Column(name = "installment_month")
    private int installmentMonth;

    @Column(name = "payment_card_id")
    private Long paymentCard;

    public static Payment of(Long paymentCardId, Amount amount, VAT vat, int installmentMonth) {
        return new Payment(amount, vat, installmentMonth, paymentCardId);
    }

    public static Payment of(PaymentCard paymentCard, Amount amount, VAT vat, int installmentMonth) {
        return new Payment(amount, vat, installmentMonth, paymentCard.getId());
    }
}
