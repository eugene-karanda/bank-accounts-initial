package org.overnind.bankaccounts;

public enum TransactionResult {
    OK,
    OVERFLOW,
    DUPLICATED_ACCOUNT,
    NOT_ENOUGH_MONEY
}