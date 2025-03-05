package data;

import javafx.beans.property.SimpleStringProperty;

public class Subscription {
    private final SimpleStringProperty subscriptionID;
    private final SimpleStringProperty subscriptionType;
    private final SimpleStringProperty subscriptionPrice;

    public Subscription(String subscriptionID, String subscriptionType, String subscriptionPrice) {
        this.subscriptionID = new SimpleStringProperty(subscriptionID);
        this.subscriptionType = new SimpleStringProperty(subscriptionType);
        this.subscriptionPrice = new SimpleStringProperty(subscriptionPrice);
    }

    public String getSubscriptionID() {
        return subscriptionID.get();
    }

    public String getSubscriptionType() {
        return subscriptionType.get();
    }

    public String getSubscriptionPrice() {
        return subscriptionPrice.get();
    }
}
