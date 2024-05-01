package assignments.assignment3.payment;
import assignments.assignment3.*;

public class DebitPayment implements DepeFoodPaymentSystem {
    private static final double MINIMUM_TOTAL_PRICE = 50000.0;

    // Check if the payment amount is above the minimum price
    public boolean isPaymentValid(double price) {
        return price >= MINIMUM_TOTAL_PRICE;
    }

    // Process the payment if it meets the minimum price requirement
    @Override
    public long processPayment(long amount) {
        if (isPaymentValid(amount)) {
            if (MainMenu.userLoggedIn.getSaldo() >= amount) {
                return amount;
            }
            else {
                return -1;
            }

    } return 0;
    }

}
