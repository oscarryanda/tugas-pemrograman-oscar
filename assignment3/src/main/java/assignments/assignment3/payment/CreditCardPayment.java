package assignments.assignment3.payment;

public class CreditCardPayment implements DepeFoodPaymentSystem {
    private static final double TRANSACTION_FEE_PERCENTAGE = 0.02;

    public static long countTransactionFee(long amount) {
        return Math.round(amount * TRANSACTION_FEE_PERCENTAGE);
    }
}
