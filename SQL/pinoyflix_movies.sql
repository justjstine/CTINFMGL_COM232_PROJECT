-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: pinoyflix
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `MovieID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) NOT NULL,
  `ReleaseDate` date DEFAULT NULL,
  `ContentRatingID` int DEFAULT NULL,
  `PopularityScore` int DEFAULT NULL,
  `GenreID` int DEFAULT NULL,
  PRIMARY KEY (`MovieID`),
  UNIQUE KEY `ReleaseDate` (`ReleaseDate`),
  UNIQUE KEY `ReleaseDate_2` (`ReleaseDate`),
  KEY `ContentRatingID` (`ContentRatingID`),
  KEY `fk_Genre` (`GenreID`),
  CONSTRAINT `fk_Genre` FOREIGN KEY (`GenreID`) REFERENCES `genre` (`GenreID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `movies_ibfk_1` FOREIGN KEY (`ContentRatingID`) REFERENCES `contentrating` (`ContentRatingID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (12,'Avengers','2025-02-28',3,NULL,NULL),(14,'Fortnite Battlepass','2025-02-20',3,NULL,NULL),(21,'dsa','2025-02-05',19,NULL,NULL),(22,'GGS','2025-02-22',21,NULL,NULL),(24,'GGS','2025-02-12',21,NULL,NULL),(26,'YesSir','2025-03-19',3,NULL,NULL),(31,'Avengers','2025-02-02',3,NULL,1),(32,'Fortnite Battlepass','2025-02-10',3,NULL,2),(33,'Yessirrrr','2025-03-29',5,NULL,NULL),(34,'dsadasdasadsa','2025-03-13',5,NULL,NULL),(36,'The Starving Games','2013-01-17',3,1,NULL),(37,'28 Days Later','2002-04-20',4,3,NULL);
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-03 21:16:02
