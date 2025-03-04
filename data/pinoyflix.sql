CREATE DATABASE PinoyFlix;
USE PinoyFlix;

CREATE TABLE users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Created DATETIME DEFAULT CURRENT_TIMESTAMP,
    PaymentID INT,
    SubscriptionID INT,
    FOREIGN KEY (PaymentID) REFERENCES PaymentMethod(PaymentID) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    FOREIGN KEY (SubscriptionID) REFERENCES Subscription(SubscriptionID) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT
);

SELECT 
       users.Username, 
       users.Password, 
       users.FirstName, 
       users.LastName, 
       users.Email, 
       users.Created, 
       paymentmethod.PaymentMethod, 
       subscription.PlanType
FROM users 
JOIN paymentmethod 
ON users.PaymentID = paymentmethod.PaymentIDmovies
JOIN subscription 
ON users.SubscriptionID = subscription.SubscriptionID;

SELECT * FROM users;


CREATE TABLE Subscription (
    SubscriptionID INT AUTO_INCREMENT PRIMARY KEY,
    PlanType VARCHAR(50) NOT NULL,
    Price DECIMAL(10,2) NOT NULL
);

SELECT * FROM Subscription

INSERT INTO Subscription (PlanType, Price) VALUES
('Mobile', 149.00),
('Basic', 249.00),
('Standard', 399.00),
('Premium', 549.00);


CREATE TABLE PaymentMethod (
    PaymentID INT AUTO_INCREMENT PRIMARY KEY,
    PaymentMethod VARCHAR(50) NOT NULL
);

SELECT * FROM PaymentMethod

INSERT INTO PaymentMethod (PaymentMethod) VALUES
('Card'),
('Gcash'),
('Maya'),
('PayPal');

CREATE TABLE tvshows (
    ShowID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    ReleaseDate DATE NOT NULL,
    ReleaseYear INT AS (YEAR(ReleaseDate)) STORED,  -- Generated column
    ContentRatingID INT,  
    PopularityScore INT,
    FOREIGN KEY (ContentRatingID) REFERENCES ContentRating(ContentRatingID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
    CONSTRAINT unique_tvshow_title_year UNIQUE (Title, ReleaseYear) -- Unique 
);


SELECT 
    tvshows.ShowID, 
    tvshows.Title, 
    tvshows.ReleaseDate, 
    tvshows.PopularityScore, 
    contentrating.Classification, 
    GROUP_CONCAT(genre.GenreName ORDER BY genre.GenreName SEPARATOR ', ') AS Genre
FROM tvshows
JOIN contentrating 
    ON tvshows.ContentRatingID = contentrating.ContentRatingID
JOIN tvshowgenre 
    ON tvshows.ShowID = tvshowgenre.ShowID
JOIN genre 
    ON tvshowgenre.GenreID = genre.GenreID
GROUP BY 
    tvshows.ShowID, 
    tvshows.Title, 
    tvshows.ReleaseDate, 
    tvshows.PopularityScore, 
    contentrating.Classification;

CREATE TABLE Movies (
    MovieID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    ReleaseDate DATE NOT NULL,
    ReleaseYear INT AS (YEAR(ReleaseDate)) STORED,  
    ContentRatingID INT,  
    PopularityScore INT,
    FOREIGN KEY (ContentRatingID) REFERENCES ContentRating(ContentRatingID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
    CONSTRAINT unique_title_year UNIQUE (Title, ReleaseYear) 
);


SELECT movies.MovieID, -- join
       movies.Title, 
       movies.ReleaseDate, 
       movies.PopularityScore, 
       contentrating.Classification
FROM movies 
JOIN contentrating 
ON movies.ContentRatingID = contentrating.ContentRatingID;

CREATE TABLE ContentRating (
    ContentRatingID INT AUTO_INCREMENT PRIMARY KEY,
    Classification VARCHAR(10) NOT NULL UNIQUE
);

SELECT * FROM contentrating

INSERT INTO ContentRating (Classification) VALUES
('G'),
('PG'),
('PG-13'),
('R-16'),
('R-18');


CREATE TABLE genre (
genreID INT AUTO_INCREMENT PRIMARY KEY,
genrename VARCHAR(100)
);

CREATE TABLE moviegenre (
    MovieID INT,
    GenreID INT,
    FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    FOREIGN KEY (GenreID) REFERENCES Genre(GenreID) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT
);


SELECT 
    movies.MovieID, 
    movies.Title, 
    movies.ReleaseDate, 
    movies.PopularityScore, 
    contentrating.Classification, 
    GROUP_CONCAT(genre.GenreName SEPARATOR ', ') AS Genre 
FROM movies 
JOIN contentrating ON movies.ContentRatingID = contentrating.ContentRatingID 
JOIN moviegenre ON movies.MovieID = moviegenre.MovieID 
JOIN genre ON moviegenre.GenreID = genre.GenreID 
GROUP BY 
    movies.MovieID, 
    movies.Title, 
    movies.ReleaseDate, 
    movies.PopularityScore, 
    contentrating.Classification;


CREATE TABLE tvshowgenre (
    ShowID INT,
    GenreID INT,
    FOREIGN KEY (ShowID) REFERENCES tvshows(ShowID) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    FOREIGN KEY (GenreID) REFERENCES Genre(GenreID) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT
);

INSERT INTO genre (genrename) VALUES
('Action'),
('Adventure'),
('Comedy'),
('Crime'), 
('Drama'),
('Fantasy'),
('Historical'),
('Horror'),
('Mystery'),
('Parody'),
('Romance'),
('Sci-Fi'),
('Sitcom'),
('Superhero'),
('Survival'),
('Thriller');

SELECT * FROM genre

CREATE TABLE Transactions (
    TransactionID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT,  
    SubscriptionID INT NOT NULL,  -- References Subscription table
    PaymentID INT NOT NULL,
    TransactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) 
		ON DELETE SET NULL 
        ON UPDATE CASCADE,
    FOREIGN KEY (SubscriptionID) REFERENCES Subscription(SubscriptionID) 
		ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    FOREIGN KEY (PaymentID) REFERENCES PaymentMethod(PaymentID) 
		ON DELETE RESTRICT 
		ON UPDATE CASCADE
);

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











