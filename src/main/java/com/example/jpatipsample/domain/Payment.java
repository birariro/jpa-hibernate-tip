package com.example.jpatipsample.domain;

import com.example.jpatipsample.domain.support.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tb_payment")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Payment extends Identifiable {

    @Embedded
    private Amount amount;

    @Embedded
    private VAT vat;

    @Column(name = "installment_month")
    private int installmentMonth;

    @ManyToOne
    @JoinColumn(name = "payment_card_id")
    private PaymentCard paymentCard;

    public static Payment of(PaymentCard paymentCard, Amount amount, VAT vat, int installmentMonth) {
        if (installmentMonth < 0 || installmentMonth > 12) {
            throw new IllegalArgumentException("InstallmentMonth must be between 0 and 12");
        }
        if (amount.getValue() < vat.getValue()) {
            throw new IllegalArgumentException("The VAT cannot be greater than the amount paid");
        }
        return new Payment(amount, vat, installmentMonth, paymentCard);
    }

    public Amount getAmount() {
        return amount;
    }

    public VAT getVat() {
        return vat;
    }

    public int getInstallmentMonth() {
        return installmentMonth;
    }

    public PaymentCard getPaymentCard() {
        return paymentCard;
    }
}
