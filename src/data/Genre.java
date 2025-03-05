package data;

import javafx.beans.property.SimpleStringProperty;


public class Genre {
    private final SimpleStringProperty genregid;
    private final SimpleStringProperty genrenames;

    public Genre(String genreid, String genrenames){
        this.genregid = new SimpleStringProperty(genreid);
        this.genrenames = new SimpleStringProperty(genrenames);
    }

    public String getGenreid(){
        return genregid.get();
    }
    public String getGenrenames() {
        return genrenames.get();
    }
}