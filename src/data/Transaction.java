package data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {
    private final SimpleStringProperty transactionID;
    private final SimpleStringProperty userID;
    private final SimpleStringProperty planType;
    private final SimpleStringProperty paymentMethod;
    private final SimpleIntegerProperty amount;
    private final SimpleStringProperty transactionDate;

    public Transaction(String transactionID, String userID, String planType, String paymentMethod, int amount, String transactionDate)  {
        this.transactionID = new SimpleStringProperty(transactionID);
        this.userID = new SimpleStringProperty(userID);
        this.planType = new SimpleStringProperty(planType);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.amount = new SimpleIntegerProperty(amount);
        this.transactionDate = new SimpleStringProperty(transactionDate);
    }

    public String getTransactionID() {
        return transactionID.get();
    }

    public String getUserID() {
        return userID.get();
    }

    public String getPlanType() {
        return planType.get();
    }

    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }
}