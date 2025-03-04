package data;

import javafx.beans.property.SimpleStringProperty;

public class ContentRating { // Private fields to store content rating ID and classification
    private final SimpleStringProperty contentratingid;
    private final SimpleStringProperty classification;

    public ContentRating(String crid, String cf){ // Initialize content rating ID and classification fields
        this.contentratingid = new SimpleStringProperty(crid);
        this.classification = new SimpleStringProperty(cf);
    }

    public String getContentratingid(){  // Getter methods
        return contentratingid.get();
    }
    public String getClassification() {
        return classification.get();
    }
}
