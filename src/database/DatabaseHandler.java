package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import data.ContentRating;
import data.Genre;
import data.Movie;
import data.Payment;
import data.Show;
import data.Subscription;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;
    
    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public static Connection getDBConnection()
    {
        Connection connection = null;

        String dburl = "jdbc:mysql://localhost:3306/pinoyflix?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String userName = "root";
        String password = "password";

        try
        {
            connection = DriverManager.getConnection(dburl, userName, password);

        } catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    public ResultSet execQuery(String query) {
        
        ResultSet result;

        try {
            stmt = getDBConnection().createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }

    public static boolean validateLogin(String username, String password){

        getInstance();
        String query = "SELECT * FROM users WHERE Username = '" + username + "' AND Password = '" + password + "'";
        
        System.out.println(query);

        ResultSet result = handler.execQuery(query);
        try {
            if (result.next()) {
                return true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getUsers() {
        ResultSet result = null;
        try {
            String query = "SELECT users.UserID, users.Username, users.Password, users.FirstName, users.LastName, users.Email, users.Created, paymentmethod.PaymentMethod, subscription.PlanType, subscription.Price " +
                           "FROM users " +
                           "JOIN paymentmethod ON users.PaymentID = paymentmethod.PaymentID " +
                           "JOIN subscription ON users.SubscriptionID = subscription.SubscriptionID";
            PreparedStatement stmt = getDBConnection().prepareStatement(query);
            System.out.println("Executing query: " + query);
            result = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean addUser(User user) {
        String userQuery = "INSERT INTO users (Username, Password, FirstName, LastName, Email, PaymentID, SubscriptionID) " +
                           "VALUES (?, ?, ?, ?, ?, " +
                           "(SELECT PaymentID FROM paymentmethod WHERE PaymentMethod = ?), " +
                           "(SELECT SubscriptionID FROM subscription WHERE PlanType = ?))";
    
        String transactionQuery = "INSERT INTO transactions (UserID, SubscriptionID, PaymentID) " +
                                  "VALUES (?, " +
                                  "(SELECT SubscriptionID FROM subscription WHERE PlanType = ?), " +
                                  "(SELECT PaymentID FROM paymentmethod WHERE PaymentMethod = ?))";
    
        try (Connection conn = getDBConnection();
             PreparedStatement userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement transactionStmt = conn.prepareStatement(transactionQuery)) {
    
            conn.setAutoCommit(false);
    
            userStmt.setString(1, user.getUsername());
            userStmt.setString(2, user.getPassword());
            userStmt.setString(3, user.getFirstName());
            userStmt.setString(4, user.getLastName());
            userStmt.setString(5, user.getEmail());
            userStmt.setString(6, user.getPaymentMethod());
            userStmt.setString(7, user.getPlanType());
    
            if (userStmt.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }
    
            try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    conn.rollback();
                    return false;
                }
    
                int userID = generatedKeys.getInt(1);
    
                transactionStmt.setInt(1, userID);
                transactionStmt.setString(2, user.getPlanType());
                transactionStmt.setString(3, user.getPaymentMethod());
    
                if (transactionStmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }
    
            conn.commit(); 
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean deleteUser(User user) {

        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM `users` WHERE  Username = ?");
            pstatement.setString(1, user.getUsername());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }

    public static boolean updateUser(User user) {
        try {
            String updateStatement = "UPDATE users SET Username = ?, Password = ?, FirstName = ?, LastName = ?, Email = ?, " +
            "PaymentID = (SELECT PaymentID FROM paymentmethod WHERE PaymentMethod = ?), " +
            "SubscriptionID = (SELECT SubscriptionID FROM subscription WHERE PlanType = ?) " +
            "WHERE UserID = ?";
    
            pstatement = getDBConnection().prepareStatement(updateStatement);
            pstatement.setString(1, user.getUsername());
            pstatement.setString(2, user.getPassword());
            pstatement.setString(3, user.getFirstName());
            pstatement.setString(4, user.getLastName());
            pstatement.setString(5, user.getEmail());
            pstatement.setString(6, user.getPaymentMethod());
            pstatement.setString(7, user.getPlanType());
            pstatement.setInt(8, user.getUserID());
    
            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false;
    }

    public static boolean updateUUser(User user) {
        try {
            String updateStatement = "UPDATE users SET Password = ?, FirstName = ?, LastName = ?, Email = ?, " +
            "PaymentID = (SELECT PaymentID FROM paymentmethod WHERE PaymentMethod = ?), " +
            "SubscriptionID = (SELECT SubscriptionID FROM subscription WHERE PlanType = ?) " +
            "WHERE Username = ?";
    
            pstatement = getDBConnection().prepareStatement(updateStatement);
            pstatement.setString(1, user.getPassword());
            pstatement.setString(2, user.getFirstName());
            pstatement.setString(3, user.getLastName());
            pstatement.setString(4, user.getEmail());
            pstatement.setString(5, user.getPaymentMethod());
            pstatement.setString(6, user.getPlanType());
            pstatement.setString(7, user.getUsername());
    
            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getMovies() { 
        ResultSet result = null;
    
        try {
            Connection conn = getDBConnection(); 
            String query = "SELECT movies.MovieID, " +
                       "movies.Title, " +
                       "movies.ReleaseDate, " +
                       "movies.PopularityScore, " +
                       "contentrating.Classification, " +
                       "GROUP_CONCAT(genre.GenreName SEPARATOR ', ') AS Genres " +
                       "FROM movies " +
                       "JOIN contentrating ON movies.ContentRatingID = contentrating.ContentRatingID " +
                       "JOIN moviegenre ON movies.MovieID = moviegenre.MovieID " +
                       "JOIN genre ON moviegenre.GenreID = genre.GenreID " +
                       "GROUP BY movies.MovieID, movies.Title, movies.ReleaseDate, movies.PopularityScore, contentrating.Classification";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static boolean addMovie(Movie movie) {
        try (Connection conn = getDBConnection()) {

            String checkQuery = "SELECT ContentRatingID FROM contentrating WHERE Classification = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, movie.getContentRating());
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Error: ContentRating does not exist!");
                return false;
            }
            int contentRatingID = rs.getInt("ContentRatingID");
    
   
            String insertMovieQuery = "INSERT INTO movies (Title, ReleaseDate, ContentRatingID) VALUES (?, ?, ?)";
            PreparedStatement insertMovieStmt = conn.prepareStatement(insertMovieQuery, Statement.RETURN_GENERATED_KEYS);
            insertMovieStmt.setString(1, movie.getTitle());
            insertMovieStmt.setString(2, movie.getReleaseDate()); // Ensure this is in the correct format
            insertMovieStmt.setInt(3, contentRatingID);
            int affectedRows = insertMovieStmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Creating movie failed, no rows affected.");
            }
    
            ResultSet generatedKeys = insertMovieStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int movieID = generatedKeys.getInt(1);

                String insertGenreQuery = "INSERT INTO moviegenre (MovieID, GenreID) VALUES (?, ?)";
                PreparedStatement insertGenreStmt = conn.prepareStatement(insertGenreQuery);
    
                for (String genre : movie.getGenres()) {

                    String checkGenreQuery = "SELECT GenreID FROM genre WHERE GenreName = ?";
                    PreparedStatement checkGenreStmt = conn.prepareStatement(checkGenreQuery);
                    checkGenreStmt.setString(1, genre);
                    ResultSet genreRs = checkGenreStmt.executeQuery();
    
                    if (!genreRs.next()) {
                        System.out.println("Error: Genre does not exist!");
                        return false;
                    }
                    int genreID = genreRs.getInt("GenreID");
    
                    insertGenreStmt.setInt(1, movieID);
                    insertGenreStmt.setInt(2, genreID);
                    insertGenreStmt.addBatch();
                }
    
                insertGenreStmt.executeBatch();
                return true;
            } else {
                throw new SQLException("Creating movie failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

     public static List<String> getMovieContentRatings() {
        List<String> classifications = new ArrayList<>();
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT Classification FROM contentrating");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                classifications.add(rs.getString("Classification"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classifications;
    }

    public static List<String> getMovieGenres() {
        List<String> genres = new ArrayList<>();
        String query = "SELECT GenreName FROM genre";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                genres.add(rs.getString("GenreName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }
    

    public static boolean deleteMovie(int movieID) {
        try {

            pstatement = getDBConnection().prepareStatement("DELETE FROM moviegenre WHERE MovieID = ?");
            pstatement.setInt(1, movieID);
            pstatement.executeUpdate();
    
            pstatement = getDBConnection().prepareStatement("DELETE FROM movies WHERE MovieID = ?");
            pstatement.setInt(1, movieID);
            return pstatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateMovie(Movie movie) {
        try (Connection conn = getDBConnection()) {

            String checkQuery = "SELECT ContentRatingID FROM contentrating WHERE Classification = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, movie.getContentRating());
                ResultSet rs = checkStmt.executeQuery();
    
                if (!rs.next()) {
                    System.out.println("Error: ContentRating does not exist!");
                    return false;
                }
    
                int contentRatingID = rs.getInt("ContentRatingID"); 
    
                String updateQuery = "UPDATE movies SET Title = ?, ReleaseDate = ?, ContentRatingID = ? WHERE MovieID = ?";
                try (PreparedStatement pstatement = conn.prepareStatement(updateQuery)) {
                    pstatement.setString(1, movie.getTitle());
                    pstatement.setString(2, movie.getReleaseDate());
                    pstatement.setInt(3, contentRatingID);  
                    pstatement.setString(4, movie.getMovieID()); 
    
                    pstatement.executeUpdate();
                }
    
                String deleteGenreQuery = "DELETE FROM moviegenre WHERE MovieID = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteGenreQuery)) {
                    deleteStmt.setString(1, movie.getMovieID());
                    deleteStmt.executeUpdate();
                }

                String addGenreQuery = "INSERT INTO moviegenre (MovieID, GenreID) VALUES (?, ?)";
                try (PreparedStatement addStmt = conn.prepareStatement(addGenreQuery)) {
                    for (String genre : movie.getGenres()) {
      
                        String checkGenreQuery = "SELECT GenreID FROM genre WHERE GenreName = ?";
                        try (PreparedStatement checkGenreStmt = conn.prepareStatement(checkGenreQuery)) {
                            checkGenreStmt.setString(1, genre);
                            ResultSet genreRs = checkGenreStmt.executeQuery();
    
                            if (!genreRs.next()) {
                                System.out.println("Error: Genre does not exist!");
                                return false;
                            }
    
                            int genreID = genreRs.getInt("GenreID");
    
                            addStmt.setString(1, movie.getMovieID());
                            addStmt.setInt(2, genreID);
                            addStmt.executeUpdate();
                        }
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getContentRating(){ 
        ResultSet result = null;
 
        try {
             Connection conn = getDBConnection(); 
             String query = "SELECT * FROM contentrating"; 
             PreparedStatement stmt = conn.prepareStatement(query);
             result = stmt.executeQuery();
        }
        catch (Exception e){
            e.printStackTrace();
        }
      
     return result;
     }
     public static boolean addClassification(ContentRating contentrating) { 

        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO `contentrating` (Classification) VALUES (?)");
            pstatement.setString(1, contentrating.getClassification());

            return pstatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return false;
    }
    public static boolean deleteClassification(ContentRating contentrating) {
        try (Connection conn = getDBConnection()) {
            
            String checkQuery = "SELECT * FROM movies WHERE ContentRatingID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, contentrating.getContentratingid());
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    System.out.println("Error: Cannot delete. ContentRating is used by a movie.");
                    return false;
                }
    
                String deleteQuery = "DELETE FROM contentrating WHERE ContentRatingID = ?";
                try (PreparedStatement pstatement = conn.prepareStatement(deleteQuery)) {
                    pstatement.setString(1, contentrating.getContentratingid());
                    return pstatement.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateClassification(ContentRating contentrating, String newClassification) {
        try (Connection conn = getDBConnection()) {
       
            String checkQuery = "SELECT * FROM contentrating WHERE Classification = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, newClassification);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    System.out.println("Error: New classification already exists!");
                    return false;
                }
    
                String updateQuery = "UPDATE contentrating SET Classification = ? WHERE Classification = ?";
                try (PreparedStatement pstatement = conn.prepareStatement(updateQuery)) {
                    pstatement.setString(1, newClassification);
                    pstatement.setString(2, contentrating.getClassification());
                    return pstatement.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static ResultSet getGenres() {
        ResultSet result = null;
        try {
            Connection conn = getDBConnection();
            String query = "SELECT * FROM genre";
            PreparedStatement stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean addGenre(Genre genre) {
    try (Connection conn = getDBConnection()) {
        String query = "INSERT INTO genre (GenreName) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, genre.getGenrenames());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public static boolean updateGenre(Genre genre, String newGenreName) {
    try (Connection conn = getDBConnection()) {
        String query = "UPDATE genre SET GenreName = ? WHERE GenreID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, newGenreName);
        stmt.setString(2, genre.getGenreid());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public static boolean deleteGenre(Genre genre) {
    try (Connection conn = getDBConnection()) {

        String checkMovieQuery = "SELECT * FROM moviegenre WHERE GenreID = ?";
        try (PreparedStatement checkMovieStmt = conn.prepareStatement(checkMovieQuery)) {
            checkMovieStmt.setString(1, genre.getGenreid());
            ResultSet movieRs = checkMovieStmt.executeQuery();

            if (movieRs.next()) {
                System.out.println("Error: Cannot delete. Genre is used by a movie.");
                return false;
            }
        }

        String checkShowQuery = "SELECT * FROM tvshowgenre WHERE GenreID = ?";
        try (PreparedStatement checkShowStmt = conn.prepareStatement(checkShowQuery)) {
            checkShowStmt.setString(1, genre.getGenreid());
            ResultSet showRs = checkShowStmt.executeQuery();

            if (showRs.next()) {
                System.out.println("Error: Cannot delete. Genre is used by a show.");
                return false;
            }
        }

        String deleteQuery = "DELETE FROM genre WHERE GenreID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, genre.getGenreid());
            return stmt.executeUpdate() > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    public static ResultSet getShows() { 
        ResultSet result = null;
    
        try {
            Connection conn = getDBConnection();
            String query = "SELECT tvshows.ShowID, " +
                       "tvshows.Title, " +
                       "tvshows.ReleaseDate, " +
                       "tvshows.PopularityScore, " +
                       "contentrating.Classification, " +
                       "GROUP_CONCAT(genre.GenreName SEPARATOR ', ') AS Genre " +
                       "FROM tvshows " +
                       "JOIN contentrating ON tvshows.ContentRatingID = contentrating.ContentRatingID " +
                       "JOIN tvshowgenre ON tvshows.ShowID = tvshowgenre.ShowID " +
                       "JOIN genre ON tvshowgenre.GenreID = genre.GenreID " +
                       "GROUP BY tvshows.ShowID, tvshows.Title, tvshows.ReleaseDate, tvshows.PopularityScore, contentrating.Classification";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean addShow(Show show) {
        try (Connection conn = getDBConnection()) {
            String checkQuery = "SELECT ContentRatingID FROM contentrating WHERE Classification = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, show.getContentRating());
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Error: ContentRating does not exist!");
                return false;
            }
            int contentRatingID = rs.getInt("ContentRatingID");
    
            String insertShowQuery = "INSERT INTO tvshows (Title, ReleaseDate, ContentRatingID) VALUES (?, ?, ?)";
            PreparedStatement insertShowStmt = conn.prepareStatement(insertShowQuery, Statement.RETURN_GENERATED_KEYS);
            insertShowStmt.setString(1, show.getTitle());
            insertShowStmt.setString(2, show.getReleaseDate()); 
            insertShowStmt.setInt(3, contentRatingID);
            int affectedRows = insertShowStmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Creating show failed, no rows affected.");
            }
    
            ResultSet generatedKeys = insertShowStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int showID = generatedKeys.getInt(1);

                String insertGenreQuery = "INSERT INTO tvshowgenre (ShowID, GenreID) VALUES (?, ?)";
                PreparedStatement insertGenreStmt = conn.prepareStatement(insertGenreQuery);
    
                for (String genre : show.getGenres()) {
           
                    String checkGenreQuery = "SELECT GenreID FROM genre WHERE GenreName = ?";
                    PreparedStatement checkGenreStmt = conn.prepareStatement(checkGenreQuery);
                    checkGenreStmt.setString(1, genre);
                    ResultSet genreRs = checkGenreStmt.executeQuery();
    
                    if (!genreRs.next()) {
                        System.out.println("Error: Genre does not exist!");
                        return false;
                    }
                    int genreID = genreRs.getInt("GenreID");
    
                    insertGenreStmt.setInt(1, showID);
                    insertGenreStmt.setInt(2, genreID);
                    insertGenreStmt.addBatch();
                }
    
                insertGenreStmt.executeBatch();
                return true;
            } else {
                throw new SQLException("Creating show failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<String> getTvContentRatings() {
    List<String> classifications = new ArrayList<>();
    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT Classification FROM contentrating");
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            classifications.add(rs.getString("Classification"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return classifications;
}
public static List<String> getTvGenres() {
    List<String> genres = new ArrayList<>();
    String query = "SELECT GenreName FROM genre";
    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            genres.add(rs.getString("GenreName"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return genres;
}


public static boolean deleteShow(int showID) {
    try {
        pstatement = getDBConnection().prepareStatement("DELETE FROM tvshowgenre WHERE ShowID = ?");
        pstatement.setInt(1, showID);
        pstatement.executeUpdate();

        pstatement = getDBConnection().prepareStatement("DELETE FROM tvshows WHERE ShowID = ?");
        pstatement.setInt(1, showID);
        return pstatement.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public static boolean updateShow(Show show) {
    try (Connection conn = getDBConnection()) {
        String checkQuery = "SELECT ContentRatingID FROM contentrating WHERE Classification = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, show.getContentRating());
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Error: ContentRating does not exist!");
                return false;
            }

            int contentRatingID = rs.getInt("ContentRatingID"); 

            String updateQuery = "UPDATE tvshows SET Title = ?, ReleaseDate = ?, ContentRatingID = ? WHERE ShowID = ?";
            try (PreparedStatement pstatement = conn.prepareStatement(updateQuery)) {
                pstatement.setString(1, show.getTitle());
                pstatement.setString(2, show.getReleaseDate());
                pstatement.setInt(3, contentRatingID);  
                pstatement.setString(4, show.getShowID()); 

                pstatement.executeUpdate();
            }

            String deleteGenreQuery = "DELETE FROM tvshowgenre WHERE ShowID = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteGenreQuery)) {
                deleteStmt.setString(1, show.getShowID());
                deleteStmt.executeUpdate();
            }

            String addGenreQuery = "INSERT INTO tvshowgenre (ShowID, GenreID) VALUES (?, ?)";
            try (PreparedStatement addStmt = conn.prepareStatement(addGenreQuery)) {
                for (String genre : show.getGenres()) {

                    String checkGenreQuery = "SELECT GenreID FROM genre WHERE GenreName = ?";
                    try (PreparedStatement checkGenreStmt = conn.prepareStatement(checkGenreQuery)) {
                        checkGenreStmt.setString(1, genre);
                        ResultSet genreRs = checkGenreStmt.executeQuery();

                        if (!genreRs.next()) {
                            System.out.println("Error: Genre does not exist!");
                            return false;
                        }

                        int genreID = genreRs.getInt("GenreID");

                        addStmt.setString(1, show.getShowID());
                        addStmt.setInt(2, genreID);
                        addStmt.executeUpdate();
                    }
                }
            }

            return true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public static ResultSet getPayment(){ 
        ResultSet result = null;
 
        try {
             Connection conn = getDBConnection(); 
             String query = "SELECT * FROM paymentmethod"; 
             PreparedStatement stmt = conn.prepareStatement(query);
             result = stmt.executeQuery();
            }
            catch (Exception e){
                e.printStackTrace();
            }
         return result;
        }

        public static List<String> getPaymentMethods() {
            List<String> paymentMethods = new ArrayList<>();
            try (Connection conn = getDBConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT PaymentMethod FROM paymentmethod");
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paymentMethods.add(rs.getString("PaymentMethod"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return paymentMethods;
        }

    public static boolean addPayment(Payment payment) { 

        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO `paymentmethod` (PaymentMethod) VALUES (?)");
            pstatement.setString(1, payment.getPaymentMethod());

            return pstatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deletePayment(Payment payment) {
        try (Connection conn = getDBConnection()) {
     
            String checkQuery = "SELECT * FROM users WHERE PaymentID = (SELECT PaymentID FROM paymentmethod WHERE PaymentMethod = ?)";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, payment.getPaymentMethod());
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    System.out.println("Error: Cannot delete. PaymentMethod is used by a user.");
                    return false;
                }
    
                String deleteQuery = "DELETE FROM paymentmethod WHERE PaymentMethod = ?";
                try (PreparedStatement pstatement = conn.prepareStatement(deleteQuery)) {
                    pstatement.setString(1, payment.getPaymentMethod());
                    return pstatement.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updatePayment(Payment payment, String newPaymentMethod) {
        try (Connection conn = getDBConnection()) {
  
            String checkQuery = "SELECT * FROM paymentmethod WHERE PaymentMethod = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, newPaymentMethod);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    System.out.println("Error: New payment method already exists!");
                    return false;
                }

                String updateQuery = "UPDATE paymentmethod SET PaymentMethod = ? WHERE PaymentMethod = ?";
                try (PreparedStatement pstatement = conn.prepareStatement(updateQuery)) {
                    pstatement.setString(1, newPaymentMethod);
                    pstatement.setString(2, payment.getPaymentMethod());
                    return pstatement.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getSubscription(){ 
        ResultSet result = null;
 
        try {
             Connection conn = getDBConnection(); 
             String query = "SELECT * FROM subscription"; 
             PreparedStatement stmt = conn.prepareStatement(query);
             result = stmt.executeQuery();
            }
            catch (Exception e){
                e.printStackTrace();
            }
         return 
         result;
        }
        public static List<String> getSubscriptionTypes() {
            List<String> subscriptionTypes = new ArrayList<>();
            try (Connection conn = getDBConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT PlanType FROM subscription");
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subscriptionTypes.add(rs.getString("PlanType"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return subscriptionTypes;
        }

    public static boolean addSubscription(Subscription subscription) { 
        
        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO `subscription` (PlanType, Price) VALUES (?,?)");
            pstatement.setString(1, subscription.getSubscriptionType());
            pstatement.setString(2, subscription.getSubscriptionPrice());

            return pstatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean deleteSubscription(Subscription subscription) {
        try (Connection conn = getDBConnection()) {
            String deleteQuery = "DELETE FROM subscription WHERE PlanType = ?";
            try (PreparedStatement pstatement = conn.prepareStatement(deleteQuery)) {
                pstatement.setString(1, subscription.getSubscriptionType());
                return pstatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateSubscription(Subscription subscription, String newSubscriptionType, String newPrice) {
        try (Connection conn = getDBConnection()) {
            String updateQuery = "UPDATE subscription SET PlanType = ?, Price = ? WHERE PlanType = ?";
            try (PreparedStatement pstatement = conn.prepareStatement(updateQuery)) {
                pstatement.setString(1, newSubscriptionType);
                pstatement.setString(2, newPrice);
                pstatement.setString(3, subscription.getSubscriptionType());
                return pstatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getTransactions() {
        String query = """
            SELECT 
                Transactions.TransactionID, 
                Transactions.UserID, 
                Subscription.PlanType AS Subscription, 
                PaymentMethod.PaymentMethod AS PaymentMethod, 
                Subscription.Price, 
                Transactions.TransactionDate
                FROM Transactions
                JOIN PaymentMethod ON Transactions.PaymentID = PaymentMethod.PaymentID
                JOIN Subscription ON Transactions.SubscriptionID = Subscription.SubscriptionID;
        """;
        try {
            PreparedStatement stmt = getDBConnection().prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /////////////////////////////////////////////////////////////////////////// USER SIDE //////////////////////////////////////////////////////////////////////////////
    
    public static void updateMoviePopularity(String movieID, int newPopularityScore) {
        String query = "UPDATE Movies SET PopularityScore = ? WHERE MovieID = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, newPopularityScore);
            pstmt.setString(2, movieID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateShowPopularity(String showID, int newPopularityScore) {
        String query = "UPDATE tvshows SET PopularityScore = ? WHERE ShowID = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, newPopularityScore);
            pstmt.setString(2, showID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Movie> searchMovies(String searchTerm) {
        ObservableList<Movie> movieList = FXCollections.observableArrayList();
        String query = "SELECT movies.MovieID, " +
                       "movies.Title, " +
                       "movies.ReleaseDate, " +
                       "contentrating.Classification, " +
                       "movies.PopularityScore, " +
                       "GROUP_CONCAT(genre.GenreName SEPARATOR ', ') AS Genres " +
                       "FROM movies " +
                       "JOIN contentrating " +
                       "ON movies.ContentRatingID = contentrating.ContentRatingID " +
                       "JOIN moviegenre " +
                       "ON movies.MovieID = moviegenre.MovieID " +
                       "JOIN genre " +
                       "ON moviegenre.GenreID = genre.GenreID " +
                       "WHERE movies.Title LIKE ? OR genre.GenreName LIKE ? " +
                       "GROUP BY movies.MovieID, movies.Title, movies.ReleaseDate, contentrating.Classification, movies.PopularityScore";
        
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<String> genres = List.of(rs.getString("Genres").split(", "));
                    movieList.add(new Movie(
                            rs.getString("MovieID"),
                            rs.getString("Title"),
                            rs.getString("ReleaseDate"),
                            rs.getString("Classification"),
                            genres,
                            rs.getInt("PopularityScore")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    public static ObservableList<Show> searchShows(String searchTerm) {
        ObservableList<Show> showList = FXCollections.observableArrayList();
        String query = "SELECT tvshows.ShowID, " +
                       "tvshows.Title, " +
                       "tvshows.ReleaseDate, " +
                       "contentrating.Classification, " +
                       "tvshows.PopularityScore, " +
                       "GROUP_CONCAT(genre.GenreName SEPARATOR ', ') AS Genres " +
                       "FROM tvshows " +
                       "JOIN contentrating " +
                       "ON tvshows.ContentRatingID = contentrating.ContentRatingID " +
                       "JOIN tvshowgenre " +
                       "ON tvshows.ShowID = tvshowgenre.ShowID " +
                       "JOIN genre " +
                       "ON tvshowgenre.GenreID = genre.GenreID " +
                       "WHERE tvshows.Title LIKE ? OR genre.GenreName LIKE ? " +
                       "GROUP BY tvshows.ShowID, tvshows.Title, tvshows.ReleaseDate, contentrating.Classification, tvshows.PopularityScore";
        
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<String> genres = List.of(rs.getString("Genres").split(", "));
                    showList.add(new Show(
                            rs.getString("ShowID"),
                            rs.getString("Title"),
                            rs.getString("ReleaseDate"),
                            rs.getString("Classification"),
                            genres,
                            rs.getInt("PopularityScore")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showList;
    }
}

