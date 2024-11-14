package domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionEngineTest {
    TransactionEngine transactionEngine;
    @BeforeEach
    void setup()
    {
        transactionEngine = new TransactionEngine();
    }
    Transaction getTransaction(int id, int accountID , int amount)
    {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(id);
        transaction.setAmount(amount);
        transaction.setAccountId(accountID);
        transaction.setDebit(false);
        return transaction;
    }
    private void addSomeTransactionToGetAverageOf200()
    {
        for (int i = 3 ; i<6; i ++)
        {
            transactionEngine.addTransactionAndDetectFraud(getTransaction(i,1,(i-2)*100));
        }
        Assertions.assertEquals(transactionEngine.getAverageTransactionAmountByAccount(1),200);
    }
    @Test
    void addTransactionAndDetectFraud_returns0_whenFirstTransactionAdd()
    {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setAmount(200);
        transaction.setAccountId(1);
        transaction.setDebit(false);
        int result = transactionEngine.addTransactionAndDetectFraud(transaction);
        Assertions.assertEquals(result,0);
    }
    @Test
    void addTransaction_returns0_whenDuplicateTransactionAdding()
    {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setAmount(200);
        transaction.setAccountId(1);
        transaction.setDebit(false);
        int result = transactionEngine.addTransactionAndDetectFraud(transaction);
        int result2 = transactionEngine.addTransactionAndDetectFraud(transaction);
        Assertions.assertEquals(result2, 0);

    }
    @Test
    void addTransactionAndDetectFraud_returnsANumberAboveZero_whenDebitIsTrueAndAmountIsAboveAvg()
    {
        addSomeTransactionToGetAverageOf200();
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(2000);
        newTransaction.setDebit(true);
        newTransaction.setAccountId(1);
        newTransaction.setTransactionId(50);
        Assertions.assertEquals(transactionEngine.addTransactionAndDetectFraud(newTransaction), 1600);
    }
    @Test
    void addTransactionAndDetectFraud_returnsZero_whenTransactionWithAmountOfLessThanAverageAndDebitTrueAdd()
    {
        addSomeTransactionToGetAverageOf200();
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(100);
        newTransaction.setDebit(true);
        newTransaction.setAccountId(1);
        newTransaction.setTransactionId(50);
        Assertions.assertEquals(transactionEngine.addTransactionAndDetectFraud(newTransaction), 0);
    }
    @Test
    void addTransactionAndDetectFraud_returnsZero_whenTransactionIsValidAndOtherAccountAreIn()
    {
        addSomeTransactionToGetAverageOf200();
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(100);
        newTransaction.setDebit(false);
        newTransaction.setAccountId(1);
        newTransaction.setTransactionId(50);
        transactionEngine.addTransactionAndDetectFraud(getTransaction(49,2,100));
        int result = transactionEngine.addTransactionAndDetectFraud(newTransaction);
        Assertions.assertEquals(result,0);
    }
    @Test
    void addTransactionAndDetectFraud_returnsFraudOfMaxMinusTransactionTransactionAmount_whenAmountAboveThresholdExist()
    {
        addSomeTransactionToGetAverageOf200();
        Transaction transactionAbove = getTransaction(49,1 , 1000000);
        transactionEngine.addTransactionAndDetectFraud(transactionAbove);
        Transaction newTransaction = getTransaction(50,1,100);
        Assertions.assertEquals(transactionEngine.addTransactionAndDetectFraud(newTransaction),999900);
    }
    @Test
    void addTransactionAndDetectFraud_returnZero_whenMultiTransactionAboveThresholdExistWithSameAmount()
    {
        addSomeTransactionToGetAverageOf200();
        Transaction transactionAbove1 = getTransaction(49,1 , 1000000);
        Transaction transactionAbove2 = getTransaction(67,1 , 1000000);
        transactionEngine.addTransactionAndDetectFraud(transactionAbove1);
        transactionEngine.addTransactionAndDetectFraud(transactionAbove2);
        Transaction newTransaction = getTransaction(50,1,100);
        Assertions.assertEquals(transactionEngine.addTransactionAndDetectFraud(newTransaction),0);
    }
    @Test
    void addTransactionAndDetectFraud_returnsDiffOfTwoBiggestThreshold_whenDifferenceOfAddedTransactionAndSecondMaxEqualToDiffOfTwoMax()
    {
        addSomeTransactionToGetAverageOf200();
        Transaction transactionAbove1 = getTransaction(49,1 , 50000);
        Transaction transactionAbove2 = getTransaction(67,1 , 99900);
        transactionEngine.addTransactionAndDetectFraud(transactionAbove1);
        transactionEngine.addTransactionAndDetectFraud(transactionAbove2);
        Transaction newTransaction = getTransaction(50,1,100);
        Assertions.assertEquals(transactionEngine.addTransactionAndDetectFraud(newTransaction),49900);
    }

}
