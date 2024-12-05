package com.example.jpatipsample.domain;

import com.example.jpatipsample.domain.support.Identifiable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_cancel")
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cancel extends Identifiable {

    @Embedded
    private Amount amount;
    @Embedded
    private VAT vat;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private Cancel(Payment payment, Amount amount, VAT vat) {
        this.payment = payment;
        this.amount = amount;
        this.vat = vat;
    }

    public static Cancel of(Payment payment, Amount amount, VAT vat) {
        return new Cancel(payment, amount, vat);
    }
}
