package data;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Show {
    private final SimpleStringProperty showID;
    private final SimpleStringProperty title;
    private final SimpleStringProperty releaseDate;
    private final SimpleStringProperty contentRating;
    private final List<String> genres;
    private final SimpleIntegerProperty popularityScore;
    private final SimpleBooleanProperty voted;

    public Show(String showID, String title, String releaseDate, String contentRating, List<String> genres, int popularityScore) {
        this.showID = new SimpleStringProperty(showID);
        this.title = new SimpleStringProperty(title);
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.contentRating = new SimpleStringProperty(contentRating);
        this.genres = genres;
        this.popularityScore = new SimpleIntegerProperty(popularityScore);
        this.voted = new SimpleBooleanProperty(false);
    }

    public String getShowID() {
        return showID.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getReleaseDate() {
        return releaseDate.get();
    }

    public String getContentRating() {
        return contentRating.get();
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getPopularityScore() {
        return popularityScore.get();
    }

    public void setPopularityScore(int popularityScore) {
        this.popularityScore.set(popularityScore);
    }

    public IntegerProperty popularityScoreProperty() {
        return popularityScore;
    }

    public boolean isVoted() {
        return voted.get();
    }

    public void setVoted(boolean voted) {
        this.voted.set(voted);
    }

    public SimpleBooleanProperty votedProperty() {
        return voted;
    }
}