package domain;

import java.math.BigDecimal;

public class TransactionValidator {

    public void validateTransaction(Transaction checkTransaction) throws TransactionException {
        try {
            int checkQuantity = checkTransaction.getQuantity();
            if (checkQuantity <= 0) {
                throw new TransactionException("The medicine quantity should be greater than 0\n");
            }
        } catch(IllegalArgumentException i) {
            System.out.println("Wrong input");
        }
    }
}
