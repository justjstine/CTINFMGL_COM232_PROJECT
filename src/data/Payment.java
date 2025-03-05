package data;

import javafx.beans.property.SimpleStringProperty;

public class Payment {
    private final SimpleStringProperty paymentID;
    private final SimpleStringProperty paymentMethod;

    public Payment(String paymentID, String paymentMethod) {
        this.paymentID = new SimpleStringProperty(paymentID);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
    }

    public String getPaymentID() {
        return paymentID.get();
    }
    public String getPaymentMethod() {
        return paymentMethod.get();
    }
}
