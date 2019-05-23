package org.overnind.bankaccounts;

import java.util.Objects;

public class Transaction {

    private final Account from;
    
    private final Account to;
    
    private final int amount;

    public static Transaction of(Account from, Account to, int amount) {
        if(from == null) {
            throw new IllegalArgumentException("'from' must be not null");
        }

        if(to == null) {
            throw new IllegalArgumentException("'to' must be not null");
        }


        if(amount <= 0) {
            throw new IllegalArgumentException("'amount' must be positive");
        }
        
        return new Transaction(from, to, amount);
    }
    
    private Transaction(Account from, Account to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public TransactionResult execute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return amount == that.amount &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, amount);
    }
}
