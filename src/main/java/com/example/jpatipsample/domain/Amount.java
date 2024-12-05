package com.example.jpatipsample.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Amount {

    private static final int MIN_LIMIT = 100;
    private static final int MAX_LIMIT = 1_000_000_000;

    @Column(name = "amount")
    private int value;

    private Amount(int value) {
        if (value < MIN_LIMIT) {
            throw new IllegalArgumentException("The minimum amount is 100");
        }
        if (value > MAX_LIMIT) {
            throw new IllegalArgumentException("The maximum amount is billion");
        }
        this.value = value;
    }

    public static Amount of(int value) {
        return new Amount(value);
    }
}
