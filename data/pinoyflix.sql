CREATE TABLE Movies (
    MovieID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    ReleaseDate YEAR UNIQUE,
    ContentRatingID INT,  -- Foreign key references ContentRating table
    PopularityScore INT,
    FOREIGN KEY (ContentRatingID) REFERENCES ContentRating(ContentRatingID)
    ON UPDATE CASCADE -- Function update agad
    ON DELETE RESTRICT --
);

INSERT INTO Movies (Title, ReleaseDate, ContentRatingID, PopularityScore) VALUES
('The Starving Games', 2013, 4, 42),  -- R-16
('28 Days Later', 2002, 5, 16),      -- R-18
('Avengers: Age of Ultron', 2015, 3, 20), -- PG-13
('Superhero Movie', 2008, 3, 79),    -- PG-13
('Titanic', 1998, 2, 15);            -- PG

SELECT movies.MovieID, -- join
       movies.Title, 
       movies.ReleaseDate, 
       movies.PopularityScore, 
       contentrating.Classification
FROM movies 
JOIN contentrating 
ON movies.ContentRatingID = contentrating.ContentRatingID;

ALTER TABLE movies MODIFY COLUMN ReleaseDate DATE;


CREATE TABLE ContentRating (
    ContentRatingID INT AUTO_INCREMENT PRIMARY KEY,
    Classification VARCHAR(10) NOT NULL UNIQUE
);

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

SELECT * FROM tvshowgenre

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

