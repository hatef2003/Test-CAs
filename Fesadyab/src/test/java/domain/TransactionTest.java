package domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {
    Transaction transaction ;
    @BeforeEach
    void setup()
    {
        transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setAccountId(1);
        transaction.setAmount(23);
        transaction.setDebit(true);
    }
    @Test
    void equal_returnsTrue_whenIdAreEqual()
    {
        Transaction other  = new Transaction();
        other.setTransactionId(1);
        Assertions.assertTrue(transaction.equals(other));
    }
    @Test
    void equal_returnsFalse_whenIdAreNotEqual()
    {
        Transaction other  = new Transaction();

        other.setTransactionId(2);
        Assertions.assertFalse(transaction.equals(other));
    } @Test
    void equal_returnsFalse_whenOtherIsNotTransaction()
    {
        String other  = new String();
        Assertions.assertFalse(transaction.equals(other));
    }


}
