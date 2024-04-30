package assignments.assignment3.payment;

public class DebitPayment implements DepeFoodPaymentSystem{
    //TODO implementasikan class di sini
    // Anda dibebaskan untuk membuat method yang diperlukan
    private static final double MINIMUM_TOTAL_PRICE = 50000.0;

    public static boolean isPaymentValid(double price) {
        return price >= MINIMUM_TOTAL_PRICE;
    }
    
}
