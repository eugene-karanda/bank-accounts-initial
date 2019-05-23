package org.overnind.bankaccounts;

import java.util.Objects;

public class Account {

    private final int id;

    private int amount;

    public static Account of(int id, int amount) {
        if(amount < 0) {
            throw new IllegalStateException("'amount' must be not negative");
        }

        return new Account(id, amount);
    }

    private Account(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int id() {
        return id;
    }

    public int amount() {
        return amount;
    }

    public boolean has(int amount) {
        return this.amount >= amount;
    }

    public void increase(int amount) {
        this.amount += amount;
    }

    public void decrease(int amount) {
        this.amount -= amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                amount == account.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }
}
