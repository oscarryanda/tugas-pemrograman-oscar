package assignments.assignment3.payment;

public interface DepeFoodPaymentSystem {
    //TODO implementasikan interface di sini
    default long processPayment(long amount) {
        return amount;
    }
}
