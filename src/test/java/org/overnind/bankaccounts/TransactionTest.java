package org.overnind.bankaccounts;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTest {

    private static final int TRANSACTION_COUNT = 1000000;

    private static final int AMOUNT_LIMIT = Integer.MAX_VALUE / 2;

    private Random random;

    @Before
    public void setUp() {
        this.random = new Random(System.currentTimeMillis());
    }

    @Test
    public void totalMoneyAfterTransactions_should_be_equal_to_totalMoney() {
        final List<Account> accounts = Arrays.asList(
                Account.of(0, Integer.MAX_VALUE / 2),
                Account.of(1, Integer.MAX_VALUE / 3),
                Account.of(2, 0),
                Account.of(3, Integer.MAX_VALUE / 2)
        );

        final long totalMoney = accounts.stream()
                .mapToLong(Account::amount)
                .sum();

        final List<Transaction> transactions = generateTransactions(accounts, AMOUNT_LIMIT, TRANSACTION_COUNT);

        transactions.stream()
                .parallel()
                .forEach(Transaction::execute);

        final long totalMoneyAfterTransactions = accounts.stream()
                .mapToLong(Account::amount)
                .sum();

        assertThat(totalMoneyAfterTransactions)
                .isEqualTo(totalMoney);
    }

    @Test
    public void testOk() {
        Account from = Account.of(0, 50);
        Account to = Account.of(1, 70);

        Transaction transaction = Transaction.of(from, to, 10);

        assertThat(transaction.execute())
                .isEqualTo(TransactionResult.OK);

        assertThat(from)
                .isEqualTo(Account.of(0, 40));

        assertThat(to)
                .isEqualTo(Account.of(1, 80));
    }

    @Test
    public void testOverflow() {
        Account from = Account.of(0, Integer.MAX_VALUE - 50);
        Account to = Account.of(1, 500);

        Transaction transaction = Transaction.of(from, to, 100);

        assertThat(transaction.execute())
                .isEqualTo(TransactionResult.OVERFLOW);

        assertThat(from)
                .isEqualTo(Account.of(0, Integer.MAX_VALUE - 50));

        assertThat(to)
                .isEqualTo(Account.of(1, 500));
    }

    @Test
    public void testDuplicatedAccount() {
        Account account = Account.of(0, 200);

        Transaction transaction = Transaction.of(account, account, 100);

        assertThat(transaction.execute())
                .isEqualTo(TransactionResult.DUPLICATED_ACCOUNT);
    }

    @Test
    public void testNotEnoughMoney() {
        Account from = Account.of(0, 250);
        Account to = Account.of(1, 0);

        Transaction transaction = Transaction.of(from, to, 300);

        assertThat(transaction.execute())
                .isEqualTo(TransactionResult.NOT_ENOUGH_MONEY);

        assertThat(from)
                .isEqualTo(Account.of(0, 250));

        assertThat(to)
                .isEqualTo(Account.of(1, 0));
    }

    private List<Transaction> generateTransactions(List<Account> accounts, int amountLimit, int transactionCount) {
        return Stream.generate(() -> generateTransaction(accounts, amountLimit))
                .limit(transactionCount)
                .collect(Collectors.toList());
    }

    private Transaction generateTransaction(List<Account> accounts, int amountLimit) {
        final Account from = accounts.get(random.nextInt(accounts.size()));
        final Account to = accounts.get(random.nextInt(accounts.size()));
        final int amount = random.nextInt(amountLimit) + 1;

        return Transaction.of(from, to, amount);
    }
}