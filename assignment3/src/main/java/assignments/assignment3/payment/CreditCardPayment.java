package assignments.assignment3.payment;

public class CreditCardPayment implements DepeFoodPaymentSystem {
    private static final double TRANSACTION_FEE_PERCENTAGE = 0.02;

    // Calculate the transaction fee based on the amount
    public static long countTransactionFee(long amount) {
        return Math.round(amount * TRANSACTION_FEE_PERCENTAGE);
    }

    // Process payment with transaction fee
    @Override
    public long processPayment(long amount) {
        long fee = countTransactionFee(amount);
        return fee;
    }
}
