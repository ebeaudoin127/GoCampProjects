-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (x86_64)
--
-- Host: localhost    Database: reservecamping
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `reservecamping`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `reservecamping` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `reservecamping`;

--
-- Table structure for table `accommodation_type`
--

DROP TABLE IF EXISTS `accommodation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accommodation_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_accommodation_type_code` (`code`),
  UNIQUE KEY `uq_accommodation_type_name_fr` (`name_fr`),
  KEY `idx_accommodation_type_active` (`is_active`),
  KEY `idx_accommodation_type_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accommodation_type`
--

LOCK TABLES `accommodation_type` WRITE;
/*!40000 ALTER TABLE `accommodation_type` DISABLE KEYS */;
INSERT INTO `accommodation_type` VALUES (1,'TENT','Tente','Tent',NULL,NULL,'CAMPING','tent',10,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(2,'RV','Véhicule récréatif','Recreational vehicle',NULL,NULL,'CAMPING','rv',20,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(3,'READY_TO_CAMP','Prêt-à-camper','Ready-to-camp',NULL,NULL,'READY_TO_CAMP','ready-to-camp',30,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(4,'YURT','Yourte','Yurt',NULL,NULL,'READY_TO_CAMP','yurt',40,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(5,'BUBBLE','Bulle','Bubble',NULL,NULL,'READY_TO_CAMP','bubble',50,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(6,'TREEHOUSE','Hébergement en arbre','Treehouse',NULL,NULL,'READY_TO_CAMP','treehouse',60,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(7,'TIPI','Tipi','Tipi',NULL,NULL,'READY_TO_CAMP','tipi',70,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(8,'CARAVAN_RENTAL','Roulotte en location','Caravan rental',NULL,NULL,'READY_TO_CAMP','caravan-rental',80,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(9,'CABIN','Cabine','Cabin',NULL,NULL,'READY_TO_CAMP','cabin',90,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(10,'SHELTER','Refuge','Shelter',NULL,NULL,'READY_TO_CAMP','shelter',100,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(11,'CHALET','Chalet','Chalet',NULL,NULL,'READY_TO_CAMP','chalet',110,1,'2026-04-17 09:35:24','2026-04-17 09:35:24');
/*!40000 ALTER TABLE `accommodation_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_activity_code` (`code`),
  UNIQUE KEY `uq_activity_name_fr` (`name_fr`),
  KEY `idx_activity_active` (`is_active`),
  KEY `idx_activity_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'BEACH','Plage','Beach',NULL,NULL,'beach',10,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(2,'SWIMMING_POOL','Piscine','Swimming pool',NULL,NULL,'pool',20,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(3,'HEATED_POOL','Piscine chauffée','Heated pool',NULL,NULL,'heated-pool',30,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(4,'WADING_POOL','Pataugeoire','Wading pool',NULL,NULL,'wading-pool',40,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(5,'WATER_GAMES','Jeux d’eau','Water games',NULL,NULL,'water-games',50,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(6,'WATER_SLIDES','Glissades d’eau','Water slides',NULL,NULL,'water-slides',60,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(7,'SPA','SPA','Spa',NULL,NULL,'spa',70,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(8,'PLAYGROUND','Terrain de jeux','Playground',NULL,NULL,'playground',80,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(9,'ORGANIZED_ACTIVITIES','Loisirs organisés','Organized activities',NULL,NULL,'organized-activities',90,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(10,'GAME_ROOM','Salle de jeux','Game room',NULL,NULL,'game-room',100,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(11,'MOVIE_PROJECTION','Projection de films','Movie screening',NULL,NULL,'movie-screening',110,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(12,'LIBRARY','Bibliothèque','Library',NULL,NULL,'library',120,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(13,'INTERPRETATION_CENTER','Centre d’interprétation','Interpretation center',NULL,NULL,'interpretation-center',130,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(14,'PETANQUE','Fer/Pétanque/Croquet','Horseshoes/Petanque/Croquet',NULL,NULL,'petanque',140,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(15,'SKATEPARK','Planche à roulettes','Skatepark',NULL,NULL,'skatepark',150,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(16,'TRAMPOLINE','Trampoline','Trampoline',NULL,NULL,'trampoline',160,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(17,'BADMINTON','Badminton','Badminton',NULL,NULL,'badminton',170,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(18,'BASKETBALL','Basketball','Basketball',NULL,NULL,'basketball',180,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(19,'FITNESS_CENTER','Centre de conditionnement physique','Fitness centre',NULL,NULL,'fitness',190,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(20,'HOCKEY','Hockey / dek hockey','Hockey / dek hockey',NULL,NULL,'hockey',200,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(21,'TENNIS','Tennis','Tennis',NULL,NULL,'tennis',210,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(22,'PICKLEBALL','Pickleball','Pickleball',NULL,NULL,'pickleball',220,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(23,'BASEBALL','Terrain de balle','Baseball field',NULL,NULL,'baseball',230,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(24,'SOCCER','Terrain de soccer','Soccer field',NULL,NULL,'soccer',240,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(25,'VOLLEYBALL','Volleyball','Volleyball',NULL,NULL,'volleyball',250,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(26,'CLIMBING','Escalade','Climbing',NULL,NULL,'climbing',260,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(27,'LOOKOUT','Belvédère','Lookout',NULL,NULL,'lookout',270,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(28,'BIKE_RENTAL','Location de vélo','Bike rental',NULL,NULL,'bike-rental',280,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(29,'MINIGOLF','Minigolf','Mini golf',NULL,NULL,'minigolf',290,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(30,'BMX','Piste de BMX','BMX track',NULL,NULL,'bmx',300,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(31,'BIKE_PATH','Piste cyclable','Cycling path',NULL,NULL,'cycling-path',310,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(32,'HORSEBACK_RIDING','Équitation','Horseback riding',NULL,NULL,'horseback-riding',320,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(33,'GOLF','Golf','Golf',NULL,NULL,'golf',330,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(34,'FISHING','Pêche','Fishing',NULL,NULL,'fishing',340,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(35,'ATV','VTT','ATV',NULL,NULL,'atv',350,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(36,'HIKING','Randonnée pédestre','Hiking trails',NULL,NULL,'hiking',360,1,'2026-04-17 09:35:24','2026-04-17 09:35:24');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground`
--

DROP TABLE IF EXISTS `campground`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `short_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `long_description` tinytext COLLATE utf8mb4_general_ci,
  `address_line_1` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `address_line_2` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `city` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `province_state_id` bigint NOT NULL,
  `country_id` bigint NOT NULL,
  `postal_code` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone_main` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone_secondary` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gps_latitude` decimal(10,7) DEFAULT NULL,
  `gps_longitude` decimal(10,7) DEFAULT NULL,
  `opening_date` date DEFAULT NULL,
  `closing_date` date DEFAULT NULL,
  `check_in_time` time DEFAULT NULL,
  `check_out_time` time DEFAULT NULL,
  `total_sites` int DEFAULT '0',
  `sites_3_services` int DEFAULT '0',
  `sites_2_services` int DEFAULT '0',
  `sites_1_service` int DEFAULT '0',
  `sites_no_service` int DEFAULT '0',
  `traveler_sites_count` int DEFAULT '0',
  `shade_percentage` int DEFAULT NULL,
  `has_wifi` tinyint(1) NOT NULL DEFAULT '0',
  `is_winter_camping` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campground_name_city` (`name`,`city`),
  KEY `idx_campground_name` (`name`),
  KEY `idx_campground_city` (`city`),
  KEY `idx_campground_active` (`is_active`),
  KEY `idx_campground_country` (`country_id`),
  KEY `idx_campground_province_state` (`province_state_id`),
  CONSTRAINT `fk_campground_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`),
  CONSTRAINT `fk_campground_province_state` FOREIGN KEY (`province_state_id`) REFERENCES `province_state` (`id`),
  CONSTRAINT `chk_campground_shade_percentage` CHECK (((`shade_percentage` is null) or ((`shade_percentage` >= 0) and (`shade_percentage` <= 100)))),
  CONSTRAINT `chk_campground_sites_1_service` CHECK ((`sites_1_service` >= 0)),
  CONSTRAINT `chk_campground_sites_2_services` CHECK ((`sites_2_services` >= 0)),
  CONSTRAINT `chk_campground_sites_3_services` CHECK ((`sites_3_services` >= 0)),
  CONSTRAINT `chk_campground_sites_no_service` CHECK ((`sites_no_service` >= 0)),
  CONSTRAINT `chk_campground_total_sites` CHECK ((`total_sites` >= 0)),
  CONSTRAINT `chk_campground_traveler_sites` CHECK ((`traveler_sites_count` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground`
--

LOCK TABLES `campground` WRITE;
/*!40000 ALTER TABLE `campground` DISABLE KEYS */;
INSERT INTO `campground` VALUES (1,'Remous de la chaudiere','Venez Découvrir Notre Havre De Paix. c\'est parfait','Situé à Lévis sur la rive-sud de Québec, à seulement 15 minutes des ponts, le camping est bordé par la Rivière Chaudière et à proximité de plusieurs attraits touristiques.','406 Route Saint-andré',NULL,'Lévis',9,1,'G6J1E8','4188312554',NULL,'campingremous@outlook.com',NULL,46.6248854,-71.2423720,'2026-05-08','2026-10-12','13:30:00','13:00:00',61,61,0,0,0,30,90,1,0,1,'2026-04-17 15:57:46','2026-04-17 16:12:15');
/*!40000 ALTER TABLE `campground` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_accommodation_type`
--

DROP TABLE IF EXISTS `campground_accommodation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_accommodation_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `accommodation_type_id` bigint NOT NULL,
  `quantity` int DEFAULT NULL,
  `notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campground_accommodation_type` (`campground_id`,`accommodation_type_id`),
  KEY `idx_campground_accommodation_type_campground` (`campground_id`),
  KEY `idx_campground_accommodation_type_type` (`accommodation_type_id`),
  CONSTRAINT `fk_campground_accommodation_type_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campground_accommodation_type_type` FOREIGN KEY (`accommodation_type_id`) REFERENCES `accommodation_type` (`id`),
  CONSTRAINT `chk_campground_accommodation_quantity` CHECK (((`quantity` is null) or (`quantity` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_accommodation_type`
--

LOCK TABLES `campground_accommodation_type` WRITE;
/*!40000 ALTER TABLE `campground_accommodation_type` DISABLE KEYS */;
INSERT INTO `campground_accommodation_type` VALUES (14,1,2,NULL,NULL,1,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(15,1,5,NULL,NULL,1,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(16,1,7,NULL,NULL,1,'2026-05-05 01:08:43','2026-05-05 01:08:43');
/*!40000 ALTER TABLE `campground_accommodation_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_activity`
--

DROP TABLE IF EXISTS `campground_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_activity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `activity_id` bigint NOT NULL,
  `notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_included` tinyint(1) NOT NULL DEFAULT '1',
  `extra_fee` decimal(10,2) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campground_activity` (`campground_id`,`activity_id`),
  KEY `idx_campground_activity_campground` (`campground_id`),
  KEY `idx_campground_activity_activity` (`activity_id`),
  CONSTRAINT `fk_campground_activity_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `fk_campground_activity_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_campground_activity_extra_fee` CHECK (((`extra_fee` is null) or (`extra_fee` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_activity`
--

LOCK TABLES `campground_activity` WRITE;
/*!40000 ALTER TABLE `campground_activity` DISABLE KEYS */;
INSERT INTO `campground_activity` VALUES (23,1,2,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(24,1,5,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(25,1,11,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(26,1,18,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43');
/*!40000 ALTER TABLE `campground_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_map`
--

DROP TABLE IF EXISTS `campground_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_map` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `background_image_path` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image_width` int DEFAULT NULL,
  `image_height` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campground_map_campground` (`campground_id`),
  UNIQUE KEY `uq_campground_map` (`campground_id`),
  KEY `idx_campground_map_active` (`is_active`),
  CONSTRAINT `fk_campground_map_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_campground_map_image_height` CHECK (((`image_height` is null) or (`image_height` > 0))),
  CONSTRAINT `chk_campground_map_image_width` CHECK (((`image_width` is null) or (`image_width` > 0)))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_map`
--

LOCK TABLES `campground_map` WRITE;
/*!40000 ALTER TABLE `campground_map` DISABLE KEYS */;
INSERT INTO `campground_map` VALUES (1,1,'/maps/camping-1.png',1024,768,1,'2026-04-18 10:44:09','2026-04-18 10:44:09');
/*!40000 ALTER TABLE `campground_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_map_element`
--

DROP TABLE IF EXISTS `campground_map_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_map_element` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `map_element_type_id` bigint NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `map_polygon_json` json DEFAULT NULL,
  `label_x` decimal(10,2) DEFAULT NULL,
  `label_y` decimal(10,2) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `labelx` int DEFAULT NULL,
  `labely` int DEFAULT NULL,
  `polygon_json` text COLLATE utf8mb4_general_ci,
  `element_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_campground_map_element_campground` (`campground_id`),
  KEY `idx_campground_map_element_type` (`map_element_type_id`),
  KEY `idx_campground_map_element_active` (`is_active`),
  KEY `FKfl0kmj4wbepx6tfjro87ybw8v` (`element_type_id`),
  CONSTRAINT `fk_campground_map_element_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campground_map_element_type` FOREIGN KEY (`map_element_type_id`) REFERENCES `map_element_type` (`id`),
  CONSTRAINT `FKfl0kmj4wbepx6tfjro87ybw8v` FOREIGN KEY (`element_type_id`) REFERENCES `map_element_type` (`id`),
  CONSTRAINT `chk_campground_map_element_label_x` CHECK (((`label_x` is null) or (`label_x` >= 0))),
  CONSTRAINT `chk_campground_map_element_label_y` CHECK (((`label_y` is null) or (`label_y` >= 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_map_element`
--

LOCK TABLES `campground_map_element` WRITE;
/*!40000 ALTER TABLE `campground_map_element` DISABLE KEYS */;
/*!40000 ALTER TABLE `campground_map_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_map_element_photo`
--

DROP TABLE IF EXISTS `campground_map_element_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_map_element_photo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_map_element_id` bigint NOT NULL,
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `thumbnail_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_primary` tinyint(1) NOT NULL DEFAULT '0',
  `display_order` int NOT NULL DEFAULT '0',
  `caption_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `caption_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_map_element_photo_element` (`campground_map_element_id`),
  KEY `idx_map_element_photo_primary` (`campground_map_element_id`,`is_primary`),
  KEY `idx_map_element_photo_display_order` (`campground_map_element_id`,`display_order`),
  KEY `idx_map_element_photo_active` (`is_active`),
  CONSTRAINT `fk_map_element_photo_element` FOREIGN KEY (`campground_map_element_id`) REFERENCES `campground_map_element` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_map_element_photo_display_order` CHECK ((`display_order` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_map_element_photo`
--

LOCK TABLES `campground_map_element_photo` WRITE;
/*!40000 ALTER TABLE `campground_map_element_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `campground_map_element_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_photo`
--

DROP TABLE IF EXISTS `campground_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_photo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `thumbnail_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_primary` tinyint(1) NOT NULL DEFAULT '0',
  `display_order` int NOT NULL DEFAULT '0',
  `caption_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `caption_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_campground_photo_campground` (`campground_id`),
  KEY `idx_campground_photo_primary` (`campground_id`,`is_primary`),
  KEY `idx_campground_photo_display_order` (`campground_id`,`display_order`),
  KEY `idx_campground_photo_active` (`is_active`),
  CONSTRAINT `fk_campground_photo_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_campground_photo_display_order` CHECK ((`display_order` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_photo`
--

LOCK TABLES `campground_photo` WRITE;
/*!40000 ALTER TABLE `campground_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `campground_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_promotion`
--

DROP TABLE IF EXISTS `campground_promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_promotion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` tinytext COLLATE utf8mb4_general_ci,
  `promo_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `discount_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `discount_value` decimal(10,2) DEFAULT NULL,
  `conditions_text` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_campground_promotion_campground` (`campground_id`),
  KEY `idx_campground_promotion_active` (`is_active`),
  KEY `idx_campground_promotion_dates` (`start_date`,`end_date`),
  CONSTRAINT `fk_campground_promotion_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_campground_promotion_discount_type` CHECK (((`discount_type` is null) or (`discount_type` in (_utf8mb4'PERCENT',_utf8mb4'AMOUNT',_utf8mb4'FIXED_PRICE',_utf8mb4'NIGHTS_FOR_PRICE',_utf8mb4'OTHER')))),
  CONSTRAINT `chk_campground_promotion_discount_value` CHECK (((`discount_value` is null) or (`discount_value` >= 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_promotion`
--

LOCK TABLES `campground_promotion` WRITE;
/*!40000 ALTER TABLE `campground_promotion` DISABLE KEYS */;
/*!40000 ALTER TABLE `campground_promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_reservation_period`
--

DROP TABLE IF EXISTS `campground_reservation_period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_reservation_period` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_campground_reservation_period_campground` (`campground_id`),
  KEY `idx_campground_reservation_period_dates` (`start_date`,`end_date`),
  KEY `idx_campground_reservation_period_active` (`active`),
  CONSTRAINT `fk_campground_reservation_period_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_campground_reservation_period_dates` CHECK ((`end_date` >= `start_date`))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_reservation_period`
--

LOCK TABLES `campground_reservation_period` WRITE;
/*!40000 ALTER TABLE `campground_reservation_period` DISABLE KEYS */;
INSERT INTO `campground_reservation_period` VALUES (1,1,'2026-04-01','2026-10-01',1,'2026-05-05 01:08:42','2026-05-05 01:08:42');
/*!40000 ALTER TABLE `campground_reservation_period` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_service`
--

DROP TABLE IF EXISTS `campground_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_service` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `service_id` bigint NOT NULL,
  `notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_included` tinyint(1) NOT NULL DEFAULT '1',
  `extra_fee` decimal(10,2) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campground_service` (`campground_id`,`service_id`),
  KEY `idx_campground_service_campground` (`campground_id`),
  KEY `idx_campground_service_service` (`service_id`),
  CONSTRAINT `fk_campground_service_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campground_service_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`),
  CONSTRAINT `chk_campground_service_extra_fee` CHECK (((`extra_fee` is null) or (`extra_fee` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_service`
--

LOCK TABLES `campground_service` WRITE;
/*!40000 ALTER TABLE `campground_service` DISABLE KEYS */;
INSERT INTO `campground_service` VALUES (37,1,2,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(38,1,3,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(39,1,4,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(40,1,5,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(41,1,6,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(42,1,9,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(43,1,10,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(44,1,17,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43'),(45,1,19,NULL,1,NULL,'2026-05-05 01:08:43','2026-05-05 01:08:43');
/*!40000 ALTER TABLE `campground_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campground_site_pricing_option`
--

DROP TABLE IF EXISTS `campground_site_pricing_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campground_site_pricing_option` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_cg_site_pricing_option_campground` (`campground_id`),
  CONSTRAINT `fk_cg_site_pricing_option_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campground_site_pricing_option`
--

LOCK TABLES `campground_site_pricing_option` WRITE;
/*!40000 ALTER TABLE `campground_site_pricing_option` DISABLE KEYS */;
INSERT INTO `campground_site_pricing_option` VALUES (1,1,'Bord de l\'eau',1,'2026-04-22 01:14:30','2026-04-22 01:14:30'),(4,1,'Standard XL à entrée directe',1,'2026-04-30 02:51:22','2026-04-30 02:51:22'),(5,1,'Premium Bord de l\'eau XL',1,'2026-05-04 23:52:11','2026-05-04 23:52:11'),(6,1,'Standard',1,'2026-05-06 01:28:41','2026-05-06 01:28:41'),(7,1,'Standard XL',1,'2026-05-06 01:31:38','2026-05-06 01:31:38'),(8,1,'Premium Bord de l\'eau',1,'2026-05-06 01:39:06','2026-05-06 01:39:06'),(9,1,'Premium',1,'2026-05-06 01:42:10','2026-05-06 01:42:10');
/*!40000 ALTER TABLE `campground_site_pricing_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite`
--

DROP TABLE IF EXISTS `campsite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `site_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `site_type_id` bigint DEFAULT NULL,
  `site_service_type_id` bigint DEFAULT NULL,
  `site_amperage_id` bigint DEFAULT NULL,
  `width_feet` decimal(38,2) DEFAULT NULL,
  `length_feet` decimal(38,2) DEFAULT NULL,
  `max_equipment_length_feet` decimal(38,2) DEFAULT NULL,
  `is_pull_through` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `notes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `map_polygon_json` text COLLATE utf8mb4_general_ci,
  `label_x` int DEFAULT NULL,
  `label_y` int DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campsite_campground_site_code` (`campground_id`,`site_code`),
  KEY `idx_campsite_campground` (`campground_id`),
  KEY `idx_campsite_site_code` (`site_code`),
  KEY `idx_campsite_active` (`is_active`),
  KEY `idx_campsite_site_type` (`site_type_id`),
  KEY `idx_campsite_service_type` (`site_service_type_id`),
  KEY `idx_campsite_amperage` (`site_amperage_id`),
  CONSTRAINT `fk_campsite_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campsite_site_amperage` FOREIGN KEY (`site_amperage_id`) REFERENCES `site_amperage` (`id`),
  CONSTRAINT `fk_campsite_site_service_type` FOREIGN KEY (`site_service_type_id`) REFERENCES `site_service_type` (`id`),
  CONSTRAINT `fk_campsite_site_type` FOREIGN KEY (`site_type_id`) REFERENCES `site_type` (`id`),
  CONSTRAINT `chk_campsite_label_x` CHECK (((`label_x` is null) or (`label_x` >= 0))),
  CONSTRAINT `chk_campsite_label_y` CHECK (((`label_y` is null) or (`label_y` >= 0))),
  CONSTRAINT `chk_campsite_length_feet` CHECK (((`length_feet` is null) or (`length_feet` >= 0))),
  CONSTRAINT `chk_campsite_max_equipment_length_feet` CHECK (((`max_equipment_length_feet` is null) or (`max_equipment_length_feet` >= 0))),
  CONSTRAINT `chk_campsite_width_feet` CHECK (((`width_feet` is null) or (`width_feet` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite`
--

LOCK TABLES `campsite` WRITE;
/*!40000 ALTER TABLE `campsite` DISABLE KEYS */;
INSERT INTO `campsite` VALUES (1,1,'1',1,4,3,25.00,60.00,45.00,0,1,NULL,'[{\"x\":311,\"y\":642},{\"x\":288,\"y\":593},{\"x\":311,\"y\":581},{\"x\":335,\"y\":627}]',311,611,'2026-04-18 13:43:55','2026-04-19 17:42:50'),(2,1,'2',1,4,4,25.00,30.00,25.00,0,1,NULL,'[{\"x\":337,\"y\":626},{\"x\":325,\"y\":603},{\"x\":363,\"y\":581},{\"x\":375,\"y\":605}]',350,604,'2026-04-18 16:12:54','2026-04-18 19:46:49'),(3,1,'3',1,4,4,30.00,70.00,50.00,0,1,NULL,'[{\"x\":523,\"y\":518},{\"x\":493,\"y\":471},{\"x\":523,\"y\":450},{\"x\":559,\"y\":495}]',525,484,'2026-04-18 16:38:43','2026-04-18 16:39:28'),(4,1,'4',1,4,4,30.00,70.00,45.00,0,1,NULL,'[{\"x\":525,\"y\":449},{\"x\":557,\"y\":426},{\"x\":592,\"y\":474},{\"x\":559,\"y\":497},{\"x\":559,\"y\":497}]',558,469,'2026-04-26 21:28:11','2026-04-28 01:49:53'),(5,1,'5',1,4,4,20.00,50.00,30.00,1,1,NULL,'[{\"x\":595,\"y\":474},{\"x\":559,\"y\":424},{\"x\":593,\"y\":402},{\"x\":631,\"y\":455}]',595,439,'2026-04-28 01:53:10','2026-04-28 01:55:50'),(6,1,'6',1,4,4,30.00,64.00,39.62,0,1,NULL,'[{\"x\":633,\"y\":453},{\"x\":595,\"y\":401},{\"x\":631,\"y\":379},{\"x\":671,\"y\":431}]',633,416,'2026-04-28 01:53:55','2026-04-28 01:56:18'),(7,1,'7',1,4,4,30.00,70.00,50.00,0,1,NULL,'[{\"x\":634,\"y\":378},{\"x\":674,\"y\":363},{\"x\":709,\"y\":410},{\"x\":673,\"y\":430}]',673,395,'2026-04-28 01:54:40','2026-04-28 01:56:52'),(8,1,'8',1,4,4,30.00,60.00,40.00,0,1,NULL,'[{\"x\":676,\"y\":362},{\"x\":714,\"y\":346},{\"x\":745,\"y\":388},{\"x\":710,\"y\":409}]',711,376,'2026-04-28 02:09:04','2026-04-28 02:10:32'),(9,1,'9',1,4,4,30.00,60.00,40.00,0,1,NULL,'[{\"x\":748,\"y\":387},{\"x\":716,\"y\":345},{\"x\":754,\"y\":326},{\"x\":783,\"y\":368}]',750,357,'2026-04-28 02:09:35','2026-04-28 02:11:09'),(10,1,'10',1,4,4,25.00,50.00,30.00,0,1,NULL,'[{\"x\":419,\"y\":403},{\"x\":417,\"y\":347},{\"x\":434,\"y\":344},{\"x\":451,\"y\":401}]',430,374,'2026-04-28 02:12:40','2026-04-28 02:16:34'),(11,1,'11',1,4,4,25.00,40.00,25.00,0,1,NULL,'[{\"x\":436,\"y\":344},{\"x\":465,\"y\":335},{\"x\":484,\"y\":400},{\"x\":453,\"y\":401}]',460,370,'2026-04-28 02:13:30','2026-04-28 02:16:59'),(12,1,'12',1,4,4,30.00,40.00,25.00,0,1,NULL,'[{\"x\":468,\"y\":335},{\"x\":495,\"y\":327},{\"x\":515,\"y\":396},{\"x\":486,\"y\":400}]',491,365,'2026-04-28 02:14:27','2026-04-28 02:17:24'),(13,1,'13',1,4,4,30.00,50.00,35.00,0,1,NULL,'[{\"x\":497,\"y\":328},{\"x\":524,\"y\":318},{\"x\":542,\"y\":383},{\"x\":533,\"y\":392},{\"x\":518,\"y\":397}]',523,364,'2026-04-28 02:15:02','2026-04-28 02:17:51'),(14,1,'14',1,4,4,35.00,60.00,40.00,0,1,NULL,'[{\"x\":526,\"y\":318},{\"x\":540,\"y\":313},{\"x\":589,\"y\":301},{\"x\":590,\"y\":336},{\"x\":583,\"y\":352},{\"x\":567,\"y\":367},{\"x\":544,\"y\":382}]',563,338,'2026-04-28 02:15:44','2026-04-28 02:18:29'),(15,1,'15',1,4,4,30.00,60.00,45.00,0,1,NULL,'[{\"x\":390,\"y\":345},{\"x\":355,\"y\":350},{\"x\":345,\"y\":310},{\"x\":350,\"y\":300},{\"x\":360,\"y\":295},{\"x\":380,\"y\":285}]',363,314,'2026-04-30 01:44:18','2026-04-30 02:53:48'),(16,1,'16',1,4,4,25.00,50.00,35.00,0,1,NULL,'[{\"x\":430,\"y\":340},{\"x\":415,\"y\":345},{\"x\":390,\"y\":345},{\"x\":380,\"y\":285},{\"x\":420,\"y\":275}]',407,318,'2026-04-30 02:27:22','2026-04-30 03:22:00'),(17,1,'17',1,4,4,30.00,50.00,25.00,0,1,NULL,'[{\"x\":465,\"y\":335},{\"x\":435,\"y\":340},{\"x\":420,\"y\":275},{\"x\":450,\"y\":270}]',443,305,'2026-04-30 02:27:58','2026-04-30 03:22:49'),(18,1,'18',1,4,4,30.00,70.00,50.00,0,1,NULL,'[{\"x\":495,\"y\":325},{\"x\":465,\"y\":335},{\"x\":450,\"y\":270},{\"x\":480,\"y\":260}]',473,298,'2026-04-30 02:43:38','2026-04-30 03:23:37'),(19,1,'19',1,4,4,40.00,60.00,45.00,0,1,NULL,'[{\"x\":530,\"y\":315},{\"x\":500,\"y\":325},{\"x\":480,\"y\":260},{\"x\":515,\"y\":250}]',506,288,'2026-04-30 02:44:46','2026-04-30 03:25:03'),(20,1,'20',1,4,4,20.00,40.00,25.00,0,1,NULL,'[{\"x\":560,\"y\":305},{\"x\":530,\"y\":315},{\"x\":520,\"y\":250},{\"x\":545,\"y\":245}]',539,279,'2026-04-30 02:45:23','2026-04-30 03:25:56'),(21,1,'21',1,4,4,30.00,65.00,50.00,0,1,NULL,'[{\"x\":590,\"y\":300},{\"x\":565,\"y\":305},{\"x\":550,\"y\":245},{\"x\":565,\"y\":245},{\"x\":575,\"y\":245},{\"x\":585,\"y\":250},{\"x\":590,\"y\":260}]',574,264,'2026-04-30 02:45:56','2026-04-30 03:26:53'),(22,1,'22',1,4,2,30.00,60.00,40.00,1,1,NULL,'[{\"x\":637,\"y\":334},{\"x\":612,\"y\":344},{\"x\":606,\"y\":341},{\"x\":611,\"y\":247},{\"x\":617,\"y\":239},{\"x\":644,\"y\":240}]',621,291,'2026-04-30 02:46:38','2026-05-06 01:25:44'),(23,1,'23',1,4,4,30.00,80.00,60.00,1,1,NULL,'[{\"x\":668,\"y\":321},{\"x\":638,\"y\":334},{\"x\":645,\"y\":240},{\"x\":675,\"y\":242}]',657,284,'2026-04-30 02:47:32','2026-05-06 01:35:14'),(24,1,'24',1,4,4,40.00,70.00,55.00,1,1,NULL,'[{\"x\":696,\"y\":311},{\"x\":669,\"y\":321},{\"x\":676,\"y\":242},{\"x\":694,\"y\":246},{\"x\":704,\"y\":251}]',688,274,'2026-04-30 02:49:18','2026-05-06 01:35:52'),(25,1,'25',1,4,4,40.00,70.00,55.00,1,1,NULL,'[{\"x\":705,\"y\":252},{\"x\":716,\"y\":260},{\"x\":725,\"y\":269},{\"x\":732,\"y\":278},{\"x\":734,\"y\":287},{\"x\":730,\"y\":295},{\"x\":723,\"y\":301},{\"x\":714,\"y\":306},{\"x\":697,\"y\":311}]',720,284,'2026-04-30 02:49:57','2026-05-06 01:36:31'),(26,1,'26',1,4,3,30.00,40.00,30.00,0,1,NULL,NULL,NULL,NULL,'2026-05-04 23:49:39','2026-05-04 23:49:39'),(27,1,'27',1,4,3,30.00,45.00,40.00,0,1,NULL,NULL,NULL,NULL,'2026-05-04 23:50:19','2026-05-04 23:50:19'),(28,1,'35',1,4,4,40.00,70.00,60.00,0,1,NULL,'[{\"x\":555,\"y\":140},{\"x\":585,\"y\":130},{\"x\":595,\"y\":150},{\"x\":595,\"y\":165},{\"x\":595,\"y\":180},{\"x\":595,\"y\":195},{\"x\":595,\"y\":210},{\"x\":585,\"y\":220},{\"x\":575,\"y\":215},{\"x\":570,\"y\":205}]',585,181,'2026-05-04 23:52:47','2026-05-04 23:56:16'),(29,1,'36',1,4,4,40.00,80.00,55.00,0,1,NULL,'[{\"x\":525,\"y\":155},{\"x\":555,\"y\":140},{\"x\":570,\"y\":215},{\"x\":540,\"y\":225}]',548,184,'2026-05-04 23:53:30','2026-05-04 23:56:47'),(30,1,'37',1,4,4,50.00,80.00,60.00,0,1,NULL,'[{\"x\":500,\"y\":165},{\"x\":525,\"y\":155},{\"x\":540,\"y\":225},{\"x\":515,\"y\":230}]',520,194,'2026-05-04 23:54:11','2026-05-04 23:57:23'),(31,1,'38',1,4,4,40.00,60.00,45.00,0,1,NULL,'[{\"x\":495,\"y\":165},{\"x\":515,\"y\":235},{\"x\":490,\"y\":240},{\"x\":470,\"y\":170}]',493,203,'2026-05-04 23:54:45','2026-05-04 23:57:59');
/*!40000 ALTER TABLE `campsite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite_equipment_allowed`
--

DROP TABLE IF EXISTS `campsite_equipment_allowed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite_equipment_allowed` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campsite_id` bigint NOT NULL,
  `equipment_allowed_type_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campsite_equipment_allowed` (`campsite_id`,`equipment_allowed_type_id`),
  KEY `idx_campsite_equipment_allowed_campsite` (`campsite_id`),
  KEY `idx_campsite_equipment_allowed_type` (`equipment_allowed_type_id`),
  CONSTRAINT `fk_campsite_equipment_allowed_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campsite_equipment_allowed_type` FOREIGN KEY (`equipment_allowed_type_id`) REFERENCES `equipment_allowed_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=263 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite_equipment_allowed`
--

LOCK TABLES `campsite_equipment_allowed` WRITE;
/*!40000 ALTER TABLE `campsite_equipment_allowed` DISABLE KEYS */;
INSERT INTO `campsite_equipment_allowed` VALUES (137,22,1,'2026-04-30 02:51:28'),(138,22,3,'2026-04-30 02:51:28'),(139,22,4,'2026-04-30 02:51:28'),(140,22,5,'2026-04-30 02:51:28'),(141,23,1,'2026-04-30 02:51:45'),(142,23,3,'2026-04-30 02:51:45'),(143,23,4,'2026-04-30 02:51:45'),(144,23,5,'2026-04-30 02:51:45'),(145,24,1,'2026-04-30 02:51:58'),(146,24,3,'2026-04-30 02:51:58'),(147,24,4,'2026-04-30 02:51:58'),(148,24,5,'2026-04-30 02:51:58'),(149,25,1,'2026-04-30 02:52:13'),(150,25,3,'2026-04-30 02:52:13'),(151,25,4,'2026-04-30 02:52:13'),(152,25,5,'2026-04-30 02:52:13'),(161,28,1,'2026-05-04 23:52:47'),(162,28,4,'2026-05-04 23:52:47'),(163,28,5,'2026-05-04 23:52:47'),(164,28,3,'2026-05-04 23:52:47'),(165,29,1,'2026-05-04 23:53:30'),(166,29,4,'2026-05-04 23:53:30'),(167,29,5,'2026-05-04 23:53:30'),(168,29,3,'2026-05-04 23:53:30'),(169,30,1,'2026-05-04 23:54:11'),(170,30,4,'2026-05-04 23:54:11'),(171,30,5,'2026-05-04 23:54:11'),(172,30,3,'2026-05-04 23:54:11'),(173,31,1,'2026-05-04 23:54:45'),(174,31,4,'2026-05-04 23:54:45'),(175,31,5,'2026-05-04 23:54:45'),(176,31,3,'2026-05-04 23:54:45'),(177,1,1,'2026-05-06 01:28:49'),(178,1,3,'2026-05-06 01:28:49'),(179,1,4,'2026-05-06 01:28:49'),(180,1,5,'2026-05-06 01:28:49'),(181,2,1,'2026-05-06 01:28:59'),(182,3,1,'2026-05-06 01:29:09'),(183,4,1,'2026-05-06 01:29:23'),(184,4,3,'2026-05-06 01:29:23'),(185,4,4,'2026-05-06 01:29:23'),(186,4,5,'2026-05-06 01:29:23'),(187,5,1,'2026-05-06 01:29:32'),(188,5,3,'2026-05-06 01:29:32'),(189,5,4,'2026-05-06 01:29:32'),(190,5,5,'2026-05-06 01:29:32'),(191,6,1,'2026-05-06 01:29:45'),(192,6,3,'2026-05-06 01:29:45'),(193,6,4,'2026-05-06 01:29:45'),(194,6,5,'2026-05-06 01:29:45'),(195,7,1,'2026-05-06 01:29:59'),(196,7,3,'2026-05-06 01:29:59'),(197,7,4,'2026-05-06 01:29:59'),(198,7,5,'2026-05-06 01:29:59'),(199,9,1,'2026-05-06 01:30:10'),(200,9,3,'2026-05-06 01:30:10'),(201,9,4,'2026-05-06 01:30:10'),(202,9,5,'2026-05-06 01:30:10'),(203,8,1,'2026-05-06 01:30:39'),(204,8,3,'2026-05-06 01:30:39'),(205,8,4,'2026-05-06 01:30:39'),(206,8,5,'2026-05-06 01:30:39'),(207,10,1,'2026-05-06 01:31:43'),(208,10,3,'2026-05-06 01:31:43'),(209,10,4,'2026-05-06 01:31:43'),(210,10,5,'2026-05-06 01:31:43'),(211,11,1,'2026-05-06 01:32:13'),(212,11,3,'2026-05-06 01:32:13'),(213,11,4,'2026-05-06 01:32:13'),(214,11,5,'2026-05-06 01:32:13'),(215,12,1,'2026-05-06 01:32:36'),(216,12,3,'2026-05-06 01:32:36'),(217,12,4,'2026-05-06 01:32:36'),(218,12,5,'2026-05-06 01:32:36'),(219,13,1,'2026-05-06 01:32:52'),(220,13,3,'2026-05-06 01:32:52'),(221,13,4,'2026-05-06 01:32:52'),(222,13,5,'2026-05-06 01:32:52'),(223,14,1,'2026-05-06 01:33:06'),(224,14,3,'2026-05-06 01:33:06'),(225,14,4,'2026-05-06 01:33:06'),(226,14,5,'2026-05-06 01:33:06'),(227,15,1,'2026-05-06 01:39:11'),(228,15,3,'2026-05-06 01:39:11'),(229,15,4,'2026-05-06 01:39:11'),(230,15,5,'2026-05-06 01:39:11'),(231,16,1,'2026-05-06 01:39:26'),(232,16,3,'2026-05-06 01:39:26'),(233,16,4,'2026-05-06 01:39:26'),(234,16,5,'2026-05-06 01:39:26'),(235,17,1,'2026-05-06 01:39:40'),(236,17,3,'2026-05-06 01:39:40'),(237,17,4,'2026-05-06 01:39:40'),(238,17,5,'2026-05-06 01:39:40'),(239,18,1,'2026-05-06 01:41:21'),(240,18,3,'2026-05-06 01:41:21'),(241,18,4,'2026-05-06 01:41:21'),(242,18,5,'2026-05-06 01:41:21'),(243,19,1,'2026-05-06 01:41:35'),(244,19,3,'2026-05-06 01:41:35'),(245,19,4,'2026-05-06 01:41:35'),(246,19,5,'2026-05-06 01:41:35'),(247,20,1,'2026-05-06 01:42:13'),(248,20,3,'2026-05-06 01:42:13'),(249,20,4,'2026-05-06 01:42:13'),(250,20,5,'2026-05-06 01:42:13'),(251,21,1,'2026-05-06 01:42:29'),(252,21,3,'2026-05-06 01:42:29'),(253,21,4,'2026-05-06 01:42:29'),(254,21,5,'2026-05-06 01:42:29'),(255,26,1,'2026-05-06 01:44:02'),(256,26,3,'2026-05-06 01:44:02'),(257,26,4,'2026-05-06 01:44:02'),(258,26,5,'2026-05-06 01:44:02'),(259,27,1,'2026-05-06 01:44:24'),(260,27,3,'2026-05-06 01:44:24'),(261,27,4,'2026-05-06 01:44:24'),(262,27,5,'2026-05-06 01:44:24');
/*!40000 ALTER TABLE `campsite_equipment_allowed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite_photo`
--

DROP TABLE IF EXISTS `campsite_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite_photo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campsite_id` bigint NOT NULL,
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `thumbnail_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_primary` tinyint(1) NOT NULL DEFAULT '0',
  `display_order` int NOT NULL DEFAULT '0',
  `caption_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `caption_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_campsite_photo_campsite` (`campsite_id`),
  KEY `idx_campsite_photo_primary` (`campsite_id`,`is_primary`),
  KEY `idx_campsite_photo_display_order` (`campsite_id`,`display_order`),
  KEY `idx_campsite_photo_active` (`is_active`),
  CONSTRAINT `fk_campsite_photo_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_campsite_photo_display_order` CHECK ((`display_order` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite_photo`
--

LOCK TABLES `campsite_photo` WRITE;
/*!40000 ALTER TABLE `campsite_photo` DISABLE KEYS */;
INSERT INTO `campsite_photo` VALUES (1,3,'/uploads/campsites/3/original/10db5f07-17ea-4144-9c6e-ceabf192b001.jpg','/uploads/campsites/3/thumb/10db5f07-17ea-4144-9c6e-ceabf192b001_thumb.jpg',0,1,NULL,NULL,1,'2026-04-18 20:31:05','2026-04-19 17:48:15'),(2,3,'/uploads/campsites/3/original/eedcfb53-ad3a-4b69-848b-d7dafac05000.jpg','/uploads/campsites/3/thumb/eedcfb53-ad3a-4b69-848b-d7dafac05000_thumb.jpg',1,2,NULL,NULL,1,'2026-04-19 17:45:37','2026-04-19 17:48:15'),(3,3,'/uploads/campsites/3/original/9e432f53-a402-495e-95c8-47931b079542.jpg','/uploads/campsites/3/thumb/9e432f53-a402-495e-95c8-47931b079542_thumb.jpg',0,3,NULL,NULL,1,'2026-04-19 17:48:05','2026-04-19 17:48:05');
/*!40000 ALTER TABLE `campsite_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite_pricing_assignment`
--

DROP TABLE IF EXISTS `campsite_pricing_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite_pricing_assignment` (
  `campsite_id` bigint NOT NULL,
  `pricing_option_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`campsite_id`),
  KEY `idx_campsite_pricing_assignment_option` (`pricing_option_id`),
  CONSTRAINT `fk_campsite_pricing_assignment_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campsite_pricing_assignment_option` FOREIGN KEY (`pricing_option_id`) REFERENCES `campground_site_pricing_option` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite_pricing_assignment`
--

LOCK TABLES `campsite_pricing_assignment` WRITE;
/*!40000 ALTER TABLE `campsite_pricing_assignment` DISABLE KEYS */;
INSERT INTO `campsite_pricing_assignment` VALUES (1,6,'2026-05-06 01:28:49','2026-05-06 01:28:49'),(2,6,'2026-05-06 01:28:59','2026-05-06 01:28:59'),(3,6,'2026-05-06 01:29:09','2026-05-06 01:29:09'),(4,6,'2026-05-06 01:29:23','2026-05-06 01:29:23'),(5,6,'2026-05-06 01:29:32','2026-05-06 01:29:32'),(6,6,'2026-05-06 01:29:45','2026-05-06 01:29:45'),(7,6,'2026-05-06 01:29:59','2026-05-06 01:29:59'),(8,6,'2026-05-06 01:30:39','2026-05-06 01:30:39'),(9,6,'2026-05-06 01:30:10','2026-05-06 01:30:10'),(10,7,'2026-05-06 01:31:43','2026-05-06 01:31:43'),(11,7,'2026-05-06 01:32:13','2026-05-06 01:32:13'),(12,7,'2026-05-06 01:32:36','2026-05-06 01:32:36'),(13,7,'2026-05-06 01:32:52','2026-05-06 01:32:52'),(14,7,'2026-05-06 01:33:06','2026-05-06 01:33:06'),(15,8,'2026-05-06 01:39:11','2026-05-06 01:39:11'),(16,8,'2026-05-06 01:39:26','2026-05-06 01:39:26'),(17,8,'2026-05-06 01:39:40','2026-05-06 01:39:40'),(18,1,'2026-05-06 01:41:21','2026-05-06 01:41:21'),(19,1,'2026-05-06 01:41:35','2026-05-06 01:41:35'),(20,9,'2026-05-06 01:42:13','2026-05-06 01:42:13'),(21,9,'2026-05-06 01:42:29','2026-05-06 01:42:29'),(22,4,'2026-04-30 02:51:28','2026-04-30 02:51:28'),(23,4,'2026-04-30 02:51:45','2026-04-30 02:51:45'),(24,4,'2026-04-30 02:51:58','2026-04-30 02:51:58'),(25,4,'2026-04-30 02:52:13','2026-04-30 02:52:13'),(26,6,'2026-05-06 01:44:02','2026-05-06 01:44:02'),(27,6,'2026-05-06 01:44:24','2026-05-06 01:44:24'),(28,5,'2026-05-04 23:52:47','2026-05-04 23:52:47'),(29,5,'2026-05-04 23:53:30','2026-05-04 23:53:30'),(30,5,'2026-05-04 23:54:11','2026-05-04 23:54:11'),(31,5,'2026-05-04 23:54:45','2026-05-04 23:54:45');
/*!40000 ALTER TABLE `campsite_pricing_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite_pricing_rule`
--

DROP TABLE IF EXISTS `campsite_pricing_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite_pricing_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `target_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `pricing_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `campsite_id` bigint DEFAULT NULL,
  `pricing_option_id` bigint DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `fixed_price` decimal(10,2) DEFAULT NULL,
  `dynamic_min_price` decimal(10,2) DEFAULT NULL,
  `dynamic_base_price` decimal(10,2) DEFAULT NULL,
  `dynamic_max_price` decimal(10,2) DEFAULT NULL,
  `minimum_nights` int DEFAULT NULL,
  `label` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `notes` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_campsite_pricing_rule_campground` (`campground_id`),
  KEY `idx_campsite_pricing_rule_campsite` (`campsite_id`),
  KEY `idx_campsite_pricing_rule_pricing_option` (`pricing_option_id`),
  KEY `idx_campsite_pricing_rule_dates` (`start_date`,`end_date`),
  CONSTRAINT `fk_campsite_pricing_rule_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campsite_pricing_rule_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campsite_pricing_rule_pricing_option` FOREIGN KEY (`pricing_option_id`) REFERENCES `campground_site_pricing_option` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite_pricing_rule`
--

LOCK TABLES `campsite_pricing_rule` WRITE;
/*!40000 ALTER TABLE `campsite_pricing_rule` DISABLE KEYS */;
INSERT INTO `campsite_pricing_rule` VALUES (13,1,'GROUP','FIXED',NULL,6,'2026-05-08','2026-06-07',53.55,NULL,NULL,NULL,NULL,'Standard basse saison',NULL,1,'2026-05-06 01:48:39','2026-05-06 01:57:12'),(14,1,'GROUP','FIXED',NULL,7,'2026-05-08','2026-06-07',57.80,NULL,NULL,NULL,NULL,'Standard XL basse saison',NULL,1,'2026-05-06 01:50:27','2026-05-06 01:59:01'),(15,1,'GROUP','FIXED',NULL,4,'2026-05-08','2026-06-07',62.05,NULL,NULL,NULL,NULL,'Standard XL à entrée directe basse saison',NULL,1,'2026-05-06 01:51:37','2026-05-06 02:00:23'),(16,1,'GROUP','FIXED',NULL,1,'2026-05-08','2026-06-07',63.75,NULL,NULL,NULL,NULL,'Bord de l’eau basse saison',NULL,1,'2026-05-06 01:52:42','2026-05-06 02:01:16'),(17,1,'GROUP','FIXED',NULL,9,'2026-05-08','2026-06-07',63.75,NULL,NULL,NULL,NULL,'Premium basse saison',NULL,1,'2026-05-06 01:53:35','2026-05-06 02:02:16'),(18,1,'GROUP','FIXED',NULL,8,'2026-05-08','2026-06-07',69.70,NULL,NULL,NULL,NULL,'Premium bord de l’eau basse saison',NULL,1,'2026-05-06 01:54:32','2026-05-06 02:03:35'),(19,1,'GROUP','FIXED',NULL,5,'2026-05-08','2026-06-07',72.25,NULL,NULL,NULL,NULL,'Premium bord de l’eau XL basse saison',NULL,1,'2026-05-06 01:55:30','2026-05-06 02:04:55'),(20,1,'GROUP','FIXED',NULL,6,'2026-09-15','2026-10-12',53.55,NULL,NULL,NULL,NULL,'Standard basse saison',NULL,1,'2026-05-06 01:57:12','2026-05-06 01:57:12'),(21,1,'GROUP','FIXED',NULL,6,'2026-06-08','2026-09-14',63.00,NULL,NULL,NULL,NULL,'Standard haute saison',NULL,1,'2026-05-06 01:57:12','2026-05-06 01:57:12'),(22,1,'GROUP','FIXED',NULL,7,'2026-09-15','2026-10-12',57.80,NULL,NULL,NULL,NULL,'Standard XL basse saison',NULL,1,'2026-05-06 01:59:01','2026-05-06 01:59:01'),(23,1,'GROUP','FIXED',NULL,7,'2026-06-08','2026-09-14',68.00,NULL,NULL,NULL,NULL,'Standard XL haute saison',NULL,1,'2026-05-06 01:59:01','2026-05-06 01:59:01'),(24,1,'GROUP','FIXED',NULL,4,'2026-09-15','2026-10-12',62.05,NULL,NULL,NULL,NULL,'Standard XL à entrée directe basse saison',NULL,1,'2026-05-06 02:00:23','2026-05-06 02:00:23'),(25,1,'GROUP','FIXED',NULL,4,'2026-06-08','2026-09-14',73.00,NULL,NULL,NULL,NULL,'Standard XL à entrée directe haute saison',NULL,1,'2026-05-06 02:00:23','2026-05-06 02:00:23'),(26,1,'GROUP','FIXED',NULL,1,'2026-09-15','2026-10-12',63.75,NULL,NULL,NULL,NULL,'Bord de l’eau basse saison',NULL,1,'2026-05-06 02:01:16','2026-05-06 02:01:16'),(27,1,'GROUP','FIXED',NULL,1,'2026-06-08','2026-09-14',75.00,NULL,NULL,NULL,NULL,'Bord de l’eau haute saison',NULL,1,'2026-05-06 02:01:16','2026-05-06 02:01:16'),(28,1,'GROUP','FIXED',NULL,9,'2026-09-15','2026-10-12',63.75,NULL,NULL,NULL,NULL,'Premium basse saison',NULL,1,'2026-05-06 02:02:16','2026-05-06 02:02:16'),(29,1,'GROUP','FIXED',NULL,9,'2026-06-08','2026-09-14',75.00,NULL,NULL,NULL,NULL,'Premium haute saison',NULL,1,'2026-05-06 02:02:16','2026-05-06 02:02:16'),(30,1,'GROUP','FIXED',NULL,8,'2026-09-15','2026-10-12',69.70,NULL,NULL,NULL,NULL,'Premium bord de l’eau basse saison',NULL,1,'2026-05-06 02:03:35','2026-05-06 02:03:35'),(31,1,'GROUP','FIXED',NULL,8,'2026-06-08','2026-09-14',82.00,NULL,NULL,NULL,NULL,'Premium bord de l’eau haute saison',NULL,1,'2026-05-06 02:03:35','2026-05-06 02:03:35'),(32,1,'GROUP','FIXED',NULL,5,'2026-09-15','2026-10-12',72.25,NULL,NULL,NULL,NULL,'Premium bord de l’eau XL basse saison',NULL,1,'2026-05-06 02:04:55','2026-05-06 02:04:55'),(33,1,'GROUP','FIXED',NULL,5,'2026-06-08','2026-09-14',85.00,NULL,NULL,NULL,NULL,'Premium bord de l’eau XL haute saison',NULL,1,'2026-05-06 02:04:55','2026-05-06 02:04:55');
/*!40000 ALTER TABLE `campsite_pricing_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite_pricing_rule_day`
--

DROP TABLE IF EXISTS `campsite_pricing_rule_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite_pricing_rule_day` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pricing_rule_id` bigint NOT NULL,
  `day_of_week` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_campsite_pricing_rule_day_rule` (`pricing_rule_id`),
  KEY `idx_campsite_pricing_rule_day_day` (`day_of_week`),
  CONSTRAINT `fk_campsite_pricing_rule_day_rule` FOREIGN KEY (`pricing_rule_id`) REFERENCES `campsite_pricing_rule` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite_pricing_rule_day`
--

LOCK TABLES `campsite_pricing_rule_day` WRITE;
/*!40000 ALTER TABLE `campsite_pricing_rule_day` DISABLE KEYS */;
/*!40000 ALTER TABLE `campsite_pricing_rule_day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite_surface_type`
--

DROP TABLE IF EXISTS `campsite_surface_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite_surface_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campsite_id` bigint NOT NULL,
  `site_surface_type_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_campsite_surface_type` (`campsite_id`,`site_surface_type_id`),
  KEY `idx_campsite_surface_type_campsite` (`campsite_id`),
  KEY `idx_campsite_surface_type_type` (`site_surface_type_id`),
  CONSTRAINT `fk_campsite_surface_type_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_campsite_surface_type_type` FOREIGN KEY (`site_surface_type_id`) REFERENCES `site_surface_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite_surface_type`
--

LOCK TABLES `campsite_surface_type` WRITE;
/*!40000 ALTER TABLE `campsite_surface_type` DISABLE KEYS */;
INSERT INTO `campsite_surface_type` VALUES (53,22,2,'2026-04-30 02:51:28'),(54,23,2,'2026-04-30 02:51:45'),(55,24,2,'2026-04-30 02:51:58'),(56,25,2,'2026-04-30 02:52:13'),(59,28,2,'2026-05-04 23:52:47'),(60,29,2,'2026-05-04 23:53:30'),(61,30,2,'2026-05-04 23:54:11'),(62,31,2,'2026-05-04 23:54:45'),(63,1,1,'2026-05-06 01:28:49'),(64,1,2,'2026-05-06 01:28:49'),(65,1,3,'2026-05-06 01:28:49'),(66,2,4,'2026-05-06 01:28:59'),(67,3,2,'2026-05-06 01:29:09'),(68,4,2,'2026-05-06 01:29:23'),(69,5,2,'2026-05-06 01:29:32'),(70,6,2,'2026-05-06 01:29:45'),(71,7,2,'2026-05-06 01:29:59'),(72,9,2,'2026-05-06 01:30:10'),(73,8,2,'2026-05-06 01:30:39'),(74,10,2,'2026-05-06 01:31:43'),(75,11,2,'2026-05-06 01:32:13'),(76,12,2,'2026-05-06 01:32:36'),(77,13,2,'2026-05-06 01:32:52'),(78,14,2,'2026-05-06 01:33:06'),(79,15,2,'2026-05-06 01:39:11'),(80,16,2,'2026-05-06 01:39:26'),(81,17,2,'2026-05-06 01:39:40'),(82,18,2,'2026-05-06 01:41:21'),(83,19,2,'2026-05-06 01:41:35'),(84,20,2,'2026-05-06 01:42:13'),(85,21,2,'2026-05-06 01:42:29'),(86,26,2,'2026-05-06 01:44:02'),(87,27,2,'2026-05-06 01:44:24');
/*!40000 ALTER TABLE `campsite_surface_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campsite_unavailability`
--

DROP TABLE IF EXISTS `campsite_unavailability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campsite_unavailability` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campsite_id` bigint NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `reason` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `is_blocking` tinyint(1) NOT NULL DEFAULT '1',
  `notes` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_campsite_unavailability_campsite` (`campsite_id`),
  KEY `idx_campsite_unavailability_dates` (`start_date`,`end_date`),
  KEY `idx_campsite_unavailability_campsite_dates` (`campsite_id`,`start_date`,`end_date`),
  CONSTRAINT `fk_campsite_unavailability_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campsite_unavailability`
--

LOCK TABLES `campsite_unavailability` WRITE;
/*!40000 ALTER TABLE `campsite_unavailability` DISABLE KEYS */;
INSERT INTO `campsite_unavailability` VALUES (1,2,'2026-04-21','2026-04-24','gh',1,'ergerther','2026-04-21 03:14:36','2026-04-21 03:14:36'),(2,2,'2026-05-12','2026-05-21','reregre',1,'regergergerg','2026-04-21 03:19:57','2026-04-21 03:19:57');
/*!40000 ALTER TABLE `campsite_unavailability` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `country` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,'Canada','CA'),(2,'États-Unis','US'),(3,'Mexique','MX');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipement_vr`
--

DROP TABLE IF EXISTS `equipement_vr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipement_vr` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `marque` varchar(60) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `modele` varchar(60) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `longueur` int DEFAULT NULL,
  `no_serie` varchar(60) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `no_plaque` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `has_extension` tinyint(1) DEFAULT '0',
  `extension_conducteur` int DEFAULT NULL,
  `extension_passager` int DEFAULT NULL,
  `actif` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_equipement_user` (`user_id`),
  CONSTRAINT `fk_equipement_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipement_vr`
--

LOCK TABLES `equipement_vr` WRITE;
/*!40000 ALTER TABLE `equipement_vr` DISABLE KEYS */;
INSERT INTO `equipement_vr` VALUES (1,11,'Grey Wolf','301BH',37,'1X876','R6754',1,2,0,1),(2,11,'shasta','30QB',25,'1B7C','X8754',1,1,NULL,0);
/*!40000 ALTER TABLE `equipement_vr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_allowed_type`
--

DROP TABLE IF EXISTS `equipment_allowed_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_allowed_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_equipment_allowed_type_code` (`code`),
  UNIQUE KEY `uq_equipment_allowed_type_name_fr` (`name_fr`),
  KEY `idx_equipment_allowed_type_active` (`is_active`),
  KEY `idx_equipment_allowed_type_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_allowed_type`
--

LOCK TABLES `equipment_allowed_type` WRITE;
/*!40000 ALTER TABLE `equipment_allowed_type` DISABLE KEYS */;
INSERT INTO `equipment_allowed_type` VALUES (1,'RV','VR','RV',NULL,NULL,10,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(2,'TENT','Tente','Tent',NULL,NULL,20,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(3,'VAN','Van','Van',NULL,NULL,30,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(4,'FIFTH_WHEEL','Fifth wheel','Fifth wheel',NULL,NULL,40,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(5,'POPUP','Tente-roulotte','Pop-up trailer',NULL,NULL,50,1,'2026-04-18 08:41:10','2026-04-18 08:41:10');
/*!40000 ALTER TABLE `equipment_allowed_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment_compatibility_issues`
--

DROP TABLE IF EXISTS `equipment_compatibility_issues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment_compatibility_issues` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reservation_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `issue_type` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `message` text COLLATE utf8mb4_general_ci NOT NULL,
  `resolved` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resolved_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_equipment_issue_user` (`user_id`),
  KEY `idx_equipment_issue_reservation` (`reservation_id`),
  KEY `idx_equipment_issue_resolved` (`resolved`),
  KEY `idx_equipment_issue_type` (`issue_type`),
  CONSTRAINT `fk_equipment_issue_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`id`),
  CONSTRAINT `fk_equipment_issue_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment_compatibility_issues`
--

LOCK TABLES `equipment_compatibility_issues` WRITE;
/*!40000 ALTER TABLE `equipment_compatibility_issues` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipment_compatibility_issues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `map_element_type`
--

DROP TABLE IF EXISTS `map_element_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `map_element_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name_fr` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_map_element_type_name_fr` (`name_fr`),
  UNIQUE KEY `uq_map_element_type_code` (`code`),
  KEY `idx_map_element_type_active` (`is_active`),
  KEY `idx_map_element_type_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `map_element_type`
--

LOCK TABLES `map_element_type` WRITE;
/*!40000 ALTER TABLE `map_element_type` DISABLE KEYS */;
INSERT INTO `map_element_type` VALUES (1,'RECEPTION','Accueil','Reception',NULL,NULL,10,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(2,'POOL','Piscine','Pool',NULL,NULL,20,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(3,'ROAD','Route','Road',NULL,NULL,30,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(4,'PARKING','Stationnement','Parking',NULL,NULL,40,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(5,'SANITARY_BLOCK','Bloc sanitaire','Sanitary block',NULL,NULL,50,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(6,'PLAYGROUND','Aire de jeux','Playground',NULL,NULL,60,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(7,'BEACH','Plage','Beach',NULL,NULL,70,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(8,'DUMP_STATION','Station de vidange','Dump station',NULL,NULL,80,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(9,'MARINA','Marina','Marina',NULL,NULL,90,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(10,'OTHER','Autre','Other',NULL,NULL,100,1,'2026-04-18 08:41:10','2026-04-18 08:41:10');
/*!40000 ALTER TABLE `map_element_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pricing_promotion`
--

DROP TABLE IF EXISTS `pricing_promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pricing_promotion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `campground_id` bigint NOT NULL,
  `target_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `application_mode` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `promotion_type` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `campsite_id` bigint DEFAULT NULL,
  `pricing_option_id` bigint DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `name` varchar(120) COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fixed_price` decimal(10,2) DEFAULT NULL,
  `discount_percent` decimal(5,2) DEFAULT NULL,
  `discount_amount` decimal(10,2) DEFAULT NULL,
  `buy_nights` int DEFAULT NULL,
  `pay_nights` int DEFAULT NULL,
  `package_nights` int DEFAULT NULL,
  `package_price` decimal(10,2) DEFAULT NULL,
  `required_consecutive_weekends` int DEFAULT NULL,
  `min_nights` int DEFAULT NULL,
  `max_nights` int DEFAULT NULL,
  `priority` int NOT NULL DEFAULT '100',
  `combinable` tinyint(1) NOT NULL DEFAULT '0',
  `promo_code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `requires_promo_code` tinyint(1) NOT NULL DEFAULT '0',
  `booking_before_date` date DEFAULT NULL,
  `arrival_within_days` int DEFAULT NULL,
  `required_arrival_day` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_pricing_promotion_campground` (`campground_id`),
  KEY `idx_pricing_promotion_campsite` (`campsite_id`),
  KEY `idx_pricing_promotion_pricing_option` (`pricing_option_id`),
  KEY `idx_pricing_promotion_dates` (`start_date`,`end_date`),
  KEY `idx_pricing_promotion_priority` (`priority`),
  CONSTRAINT `fk_pricing_promotion_campground` FOREIGN KEY (`campground_id`) REFERENCES `campground` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pricing_promotion_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pricing_promotion_pricing_option` FOREIGN KEY (`pricing_option_id`) REFERENCES `campground_site_pricing_option` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pricing_promotion`
--

LOCK TABLES `pricing_promotion` WRITE;
/*!40000 ALTER TABLE `pricing_promotion` DISABLE KEYS */;
INSERT INTO `pricing_promotion` VALUES (3,1,'GROUP','ADJUSTMENT','FIXED_PRICE',NULL,6,'2026-05-11','2026-05-15','test 1',NULL,45.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,100,0,NULL,0,NULL,NULL,NULL,1,'2026-05-06 02:27:33','2026-05-06 02:31:22'),(4,1,'ALL_CAMPGROUND','ADJUSTMENT','PERCENT_DISCOUNT',NULL,NULL,'2026-06-08','2026-09-14','test 1',NULL,NULL,10.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,100,0,NULL,0,NULL,NULL,NULL,1,'2026-05-06 03:19:11','2026-05-06 03:19:11'),(5,1,'GROUP','ADJUSTMENT','AMOUNT_DISCOUNT',NULL,6,'2026-05-08','2026-10-12','test 2',NULL,NULL,NULL,25.00,NULL,NULL,NULL,NULL,NULL,2,NULL,100,0,NULL,0,NULL,NULL,NULL,1,'2026-05-06 03:23:26','2026-05-06 03:23:26'),(6,1,'GROUP','ADJUSTMENT','X_NIGHTS_FOR_AMOUNT',NULL,7,'2026-05-08','2026-10-12','test 3',NULL,NULL,NULL,NULL,NULL,NULL,3,150.00,NULL,NULL,NULL,100,0,NULL,0,NULL,NULL,NULL,1,'2026-05-06 03:26:34','2026-05-06 03:27:14');
/*!40000 ALTER TABLE `pricing_promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pricing_promotion_campsite`
--

DROP TABLE IF EXISTS `pricing_promotion_campsite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pricing_promotion_campsite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pricing_promotion_id` bigint NOT NULL,
  `campsite_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_pricing_promotion_campsite` (`pricing_promotion_id`,`campsite_id`),
  KEY `idx_ppc_promotion` (`pricing_promotion_id`),
  KEY `idx_ppc_campsite` (`campsite_id`),
  CONSTRAINT `fk_ppc_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ppc_promotion` FOREIGN KEY (`pricing_promotion_id`) REFERENCES `pricing_promotion` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pricing_promotion_campsite`
--

LOCK TABLES `pricing_promotion_campsite` WRITE;
/*!40000 ALTER TABLE `pricing_promotion_campsite` DISABLE KEYS */;
/*!40000 ALTER TABLE `pricing_promotion_campsite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pricing_promotion_day`
--

DROP TABLE IF EXISTS `pricing_promotion_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pricing_promotion_day` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pricing_promotion_id` bigint NOT NULL,
  `day_of_week` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_pricing_promotion_day_promotion` (`pricing_promotion_id`),
  KEY `idx_pricing_promotion_day_day` (`day_of_week`),
  CONSTRAINT `fk_pricing_promotion_day_promotion` FOREIGN KEY (`pricing_promotion_id`) REFERENCES `pricing_promotion` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pricing_promotion_day`
--

LOCK TABLES `pricing_promotion_day` WRITE;
/*!40000 ALTER TABLE `pricing_promotion_day` DISABLE KEYS */;
/*!40000 ALTER TABLE `pricing_promotion_day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `province_state`
--

DROP TABLE IF EXISTS `province_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `province_state` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `country_id` bigint NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `country_id` (`country_id`),
  CONSTRAINT `province_state_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `province_state`
--

LOCK TABLES `province_state` WRITE;
/*!40000 ALTER TABLE `province_state` DISABLE KEYS */;
INSERT INTO `province_state` VALUES (1,1,'Alberta','AB'),(2,1,'Colombie-Britannique','BC'),(3,1,'Île-du-Prince-Édouard','PE'),(4,1,'Manitoba','MB'),(5,1,'Nouveau-Brunswick','NB'),(6,1,'Nouvelle-Écosse','NS'),(7,1,'Nunavut','NU'),(8,1,'Ontario','ON'),(9,1,'Québec','QC'),(10,1,'Saskatchewan','SK'),(11,1,'Terre-Neuve-et-Labrador','NL'),(12,1,'Territoires du Nord-Ouest','NT'),(13,1,'Yukon','YT'),(14,2,'Alabama','AL'),(15,2,'Alaska','AK'),(16,2,'Arizona','AZ'),(17,2,'Arkansas','AR'),(18,2,'Californie','CA'),(19,2,'Caroline du Nord','NC'),(20,2,'Caroline du Sud','SC'),(21,2,'Colorado','CO'),(22,2,'Connecticut','CT'),(23,2,'Dakota du Nord','ND'),(24,2,'Dakota du Sud','SD'),(25,2,'Delaware','DE'),(26,2,'Floride','FL'),(27,2,'Géorgie','GA'),(28,2,'Hawaï','HI'),(29,2,'Idaho','ID'),(30,2,'Illinois','IL'),(31,2,'Indiana','IN'),(32,2,'Iowa','IA'),(33,2,'Kansas','KS'),(34,2,'Kentucky','KY'),(35,2,'Louisiane','LA'),(36,2,'Maine','ME'),(37,2,'Maryland','MD'),(38,2,'Massachusetts','MA'),(39,2,'Michigan','MI'),(40,2,'Minnesota','MN'),(41,2,'Mississippi','MS'),(42,2,'Missouri','MO'),(43,2,'Montana','MT'),(44,2,'Nebraska','NE'),(45,2,'Nevada','NV'),(46,2,'New Hampshire','NH'),(47,2,'New Jersey','NJ'),(48,2,'New York','NY'),(49,2,'Nouveau-Mexique','NM'),(50,2,'Ohio','OH'),(51,2,'Oklahoma','OK'),(52,2,'Oregon','OR'),(53,2,'Pennsylvanie','PA'),(54,2,'Rhode Island','RI'),(55,2,'Tennessee','TN'),(56,2,'Texas','TX'),(57,2,'Utah','UT'),(58,2,'Vermont','VT'),(59,2,'Virginie','VA'),(60,2,'Virginie-Occidentale','WV'),(61,2,'Washington','WA'),(62,2,'Wisconsin','WI'),(63,2,'Wyoming','WY'),(64,3,'Aguascalientes','AGS'),(65,3,'Baja California','BC'),(66,3,'Baja California Sur','BCS'),(67,3,'Campeche','CAM'),(68,3,'Chiapas','CHIS'),(69,3,'Chihuahua','CHIH'),(70,3,'Ciudad de México','CDMX'),(71,3,'Coahuila','COAH'),(72,3,'Colima','COL'),(73,3,'Durango','DGO'),(74,3,'Guanajuato','GTO'),(75,3,'Guerrero','GRO'),(76,3,'Hidalgo','HID'),(77,3,'Jalisco','JAL'),(78,3,'México','MEX'),(79,3,'Michoacán','MICH'),(80,3,'Morelos','MOR'),(81,3,'Nayarit','NAY'),(82,3,'Nuevo León','NL'),(83,3,'Oaxaca','OAX'),(84,3,'Puebla','PUE'),(85,3,'Querétaro','QRO'),(86,3,'Quintana Roo','QROO'),(87,3,'San Luis Potosí','SLP'),(88,3,'Sinaloa','SIN'),(89,3,'Sonora','SON'),(90,3,'Tabasco','TAB'),(91,3,'Tamaulipas','TAMPS'),(92,3,'Tlaxcala','TLAX'),(93,3,'Veracruz','VER'),(94,3,'Yucatán','YUC'),(95,3,'Zacatecas','ZAC');
/*!40000 ALTER TABLE `province_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `campsite_id` bigint NOT NULL,
  `arrival_date` date NOT NULL,
  `departure_date` date NOT NULL,
  `number_of_nights` int NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_reservation_user` (`user_id`),
  KEY `idx_reservation_campsite` (`campsite_id`),
  KEY `idx_reservation_dates` (`arrival_date`,`departure_date`),
  KEY `idx_reservation_status` (`status`),
  CONSTRAINT `fk_reservation_campsite` FOREIGN KEY (`campsite_id`) REFERENCES `campsite` (`id`),
  CONSTRAINT `fk_reservation_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (1,11,1,'2026-07-10','2026-07-15',5,'PENDING','2026-05-07 03:24:54','2026-05-07 03:24:54');
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'CAMPING_ADMIN'),(3,'GESTIONNAIRE'),(1,'SUPER_ADMIN'),(4,'UTILISATEUR');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `icon_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_service_code` (`code`),
  UNIQUE KEY `uq_service_name_fr` (`name_fr`),
  KEY `idx_service_active` (`is_active`),
  KEY `idx_service_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (1,'DRINKING_WATER','Eau potable','Drinking water',NULL,NULL,'drinking-water',10,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(2,'ELECTRICITY','Prise avec électricité','Electric hook-up',NULL,NULL,'electricity',20,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(3,'TOILETS','Toilettes','Toilets',NULL,NULL,'toilets',30,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(4,'SHOWERS','Douches','Showers',NULL,NULL,'showers',40,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(5,'LAUNDRY','Buanderie','Laundry',NULL,NULL,'laundry',50,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(6,'WOOD','Bois disponible','Wood available',NULL,NULL,'wood',60,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(7,'ICE','Glace disponible','Ice available',NULL,NULL,'ice',70,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(8,'PROPANE','Propane disponible','Propane available',NULL,NULL,'propane',80,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(9,'WIFI','Wi-Fi disponible','Wi-Fi available',NULL,NULL,'wifi',90,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(10,'WIFI_FREE','Wi-Fi gratuit','Free Wi-Fi',NULL,NULL,'wifi-free',100,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(11,'RECYCLING','Collecte de matières recyclables','Recycling collection',NULL,NULL,'recycling',110,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(12,'ORGANIC_WASTE','Collecte de matières organiques','Organic waste collection',NULL,NULL,'organic-waste',120,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(13,'EV_CHARGING','Borne de recharge électrique','Electric vehicle charging',NULL,NULL,'ev-charging',130,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(14,'DUMP_STATION','Station de vidange','Dump station',NULL,NULL,'dump-station',140,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(15,'ACCESSIBLE','Accessible','Accessible',NULL,NULL,'accessible',150,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(16,'PARTIALLY_ACCESSIBLE','Partiellement accessible','Partially accessible',NULL,NULL,'partially-accessible',160,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(17,'CONVENIENCE_STORE','Dépanneur sur place','Convenience store on site',NULL,NULL,'convenience-store',170,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(18,'RESTAURANT','Restaurant sur place','Restaurant on site',NULL,NULL,'restaurant',180,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(19,'SNACK_BAR','Casse-croûte sur place','Snack bar on site',NULL,NULL,'snack-bar',190,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(20,'BOAT_RAMP','Rampe de mise à l’eau','Boat ramp',NULL,NULL,'boat-ramp',200,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(21,'MARINA_GAS','Marina avec essence','Marina with gas',NULL,NULL,'marina-gas',210,1,'2026-04-17 09:35:24','2026-04-17 09:35:24'),(22,'PICNIC_AREA','Aire de pique-nique','Picnic area',NULL,NULL,'picnic-area',220,1,'2026-04-17 09:35:24','2026-04-17 09:35:24');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_amperage`
--

DROP TABLE IF EXISTS `site_amperage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site_amperage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `amps` int DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_site_amperage_code` (`code`),
  UNIQUE KEY `uq_site_amperage_name_fr` (`name_fr`),
  KEY `idx_site_amperage_active` (`is_active`),
  KEY `idx_site_amperage_display_order` (`display_order`),
  CONSTRAINT `chk_site_amperage_amps` CHECK (((`amps` is null) or (`amps` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_amperage`
--

LOCK TABLES `site_amperage` WRITE;
/*!40000 ALTER TABLE `site_amperage` DISABLE KEYS */;
INSERT INTO `site_amperage` VALUES (1,'AMP_15','15 Amp','15 Amp',15,10,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(2,'AMP_20','20 Amp','20 Amp',20,20,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(3,'AMP_30','30 Amp','30 Amp',30,30,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(4,'AMP_50','50 Amp','50 Amp',50,40,1,'2026-04-18 08:41:10','2026-04-18 08:41:10');
/*!40000 ALTER TABLE `site_amperage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_service_type`
--

DROP TABLE IF EXISTS `site_service_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site_service_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_site_service_type_code` (`code`),
  UNIQUE KEY `uq_site_service_type_name_fr` (`name_fr`),
  KEY `idx_site_service_type_active` (`is_active`),
  KEY `idx_site_service_type_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_service_type`
--

LOCK TABLES `site_service_type` WRITE;
/*!40000 ALTER TABLE `site_service_type` DISABLE KEYS */;
INSERT INTO `site_service_type` VALUES (1,'NO_SERVICE','Sans service','No service',NULL,NULL,10,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(2,'WATER_ONLY','Eau seulement','Water only',NULL,NULL,20,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(3,'WATER_ELECTRICITY','Eau + électricité','Water + electricity',NULL,NULL,30,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(4,'WATER_ELECTRICITY_SEWER','Eau + électricité + égouts','Water + electricity + sewer',NULL,NULL,40,1,'2026-04-18 08:41:10','2026-04-18 08:41:10');
/*!40000 ALTER TABLE `site_service_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_surface_type`
--

DROP TABLE IF EXISTS `site_surface_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site_surface_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_site_surface_type_code` (`code`),
  UNIQUE KEY `uq_site_surface_type_name_fr` (`name_fr`),
  KEY `idx_site_surface_type_active` (`is_active`),
  KEY `idx_site_surface_type_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_surface_type`
--

LOCK TABLES `site_surface_type` WRITE;
/*!40000 ALTER TABLE `site_surface_type` DISABLE KEYS */;
INSERT INTO `site_surface_type` VALUES (1,'GRAVEL','Gravier','Gravel',NULL,NULL,10,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(2,'GRASS','Pelouse','Grass',NULL,NULL,20,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(3,'SAND','Sable','Sand',NULL,NULL,30,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(4,'CONCRETE','Béton','Concrete',NULL,NULL,40,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(5,'CONCRETE_PATIO','Patio béton','Concrete patio',NULL,NULL,50,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(6,'DIRT','Terre','Dirt',NULL,NULL,60,1,'2026-04-18 08:41:10','2026-04-18 08:41:10');
/*!40000 ALTER TABLE `site_surface_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_type`
--

DROP TABLE IF EXISTS `site_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_fr` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_en` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_fr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `display_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_site_type_code` (`code`),
  UNIQUE KEY `uq_site_type_name_fr` (`name_fr`),
  KEY `idx_site_type_active` (`is_active`),
  KEY `idx_site_type_display_order` (`display_order`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_type`
--

LOCK TABLES `site_type` WRITE;
/*!40000 ALTER TABLE `site_type` DISABLE KEYS */;
INSERT INTO `site_type` VALUES (1,'RV','VR','RV',NULL,NULL,10,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(2,'TENT','Tente','Tent',NULL,NULL,20,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(3,'READY_TO_CAMP','Prêt-à-camper','Ready-to-camp',NULL,NULL,30,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(4,'CHALET','Chalet','Chalet',NULL,NULL,40,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(5,'YURT','Yourte','Yurt',NULL,NULL,50,1,'2026-04-18 08:41:10','2026-04-18 08:41:10'),(6,'SEASONAL_SITE','Saisonnier','Seasonal site',NULL,NULL,60,1,'2026-04-20 22:19:31','2026-04-20 22:19:31');
/*!40000 ALTER TABLE `site_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `firstname` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
  `lastname` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(120) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `city` varchar(60) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `postalCode` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `equipmentType` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `equipmentLength` int DEFAULT NULL,
  `hasSlideOut` tinyint(1) DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `country_id` bigint DEFAULT NULL,
  `province_state_id` bigint DEFAULT NULL,
  `postal_code` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `equipment_length` int DEFAULT NULL,
  `equipment_type` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `has_slide_out` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  KEY `country_id` (`country_id`),
  KEY `province_state_id` (`province_state_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`),
  CONSTRAINT `users_ibfk_3` FOREIGN KEY (`province_state_id`) REFERENCES `province_state` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (11,'a','a','a@a.com','$2a$10$V8jtgpAZDpuS2CcwvVa/5e.OjRStQ7ErBtUZdSJqe9K9LH/zYMfiG',NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,1,9,NULL,NULL,NULL,NULL),(12,'b','b','b@b.com','$2a$10$1N7F.LcpP/uavQAyNOxVb.BrfeCST0bkjYBpp/1yk3cUAAorqgoLO','1234567890','991 de la rue','levis',NULL,NULL,NULL,NULL,4,1,9,'g6z3b2',NULL,NULL,NULL),(14,'Eric','Beaudoin','super@super.com','$2a$10$sQUnrWWgdSCqQK0FGkN/j.0Kf483os/1xdRfh6S7UoEd1TVCREjwa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,1,9,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vw_campground_accommodations`
--

DROP TABLE IF EXISTS `vw_campground_accommodations`;
/*!50001 DROP VIEW IF EXISTS `vw_campground_accommodations`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_campground_accommodations` AS SELECT 
 1 AS `id`,
 1 AS `campground_id`,
 1 AS `campground_name`,
 1 AS `accommodation_type_id`,
 1 AS `accommodation_code`,
 1 AS `accommodation_name_fr`,
 1 AS `accommodation_name_en`,
 1 AS `category`,
 1 AS `quantity`,
 1 AS `notes`,
 1 AS `is_available`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_campground_activities`
--

DROP TABLE IF EXISTS `vw_campground_activities`;
/*!50001 DROP VIEW IF EXISTS `vw_campground_activities`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_campground_activities` AS SELECT 
 1 AS `id`,
 1 AS `campground_id`,
 1 AS `campground_name`,
 1 AS `activity_id`,
 1 AS `activity_code`,
 1 AS `activity_name_fr`,
 1 AS `activity_name_en`,
 1 AS `notes`,
 1 AS `is_included`,
 1 AS `extra_fee`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_campground_services`
--

DROP TABLE IF EXISTS `vw_campground_services`;
/*!50001 DROP VIEW IF EXISTS `vw_campground_services`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_campground_services` AS SELECT 
 1 AS `id`,
 1 AS `campground_id`,
 1 AS `campground_name`,
 1 AS `service_id`,
 1 AS `service_code`,
 1 AS `service_name_fr`,
 1 AS `service_name_en`,
 1 AS `notes`,
 1 AS `is_included`,
 1 AS `extra_fee`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_campground_summary`
--

DROP TABLE IF EXISTS `vw_campground_summary`;
/*!50001 DROP VIEW IF EXISTS `vw_campground_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_campground_summary` AS SELECT 
 1 AS `id`,
 1 AS `name`,
 1 AS `city`,
 1 AS `email`,
 1 AS `phone_main`,
 1 AS `is_active`,
 1 AS `total_sites`,
 1 AS `sites_3_services`,
 1 AS `sites_2_services`,
 1 AS `sites_1_service`,
 1 AS `sites_no_service`,
 1 AS `traveler_sites_count`,
 1 AS `has_wifi`,
 1 AS `is_winter_camping`,
 1 AS `created_at`,
 1 AS `updated_at`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'reservecamping'
--

--
-- Dumping routines for database 'reservecamping'
--

--
-- Current Database: `reservecamping`
--

USE `reservecamping`;

--
-- Final view structure for view `vw_campground_accommodations`
--

/*!50001 DROP VIEW IF EXISTS `vw_campground_accommodations`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_campground_accommodations` AS select `cat`.`id` AS `id`,`cat`.`campground_id` AS `campground_id`,`c`.`name` AS `campground_name`,`cat`.`accommodation_type_id` AS `accommodation_type_id`,`at`.`code` AS `accommodation_code`,`at`.`name_fr` AS `accommodation_name_fr`,`at`.`name_en` AS `accommodation_name_en`,`at`.`category` AS `category`,`cat`.`quantity` AS `quantity`,`cat`.`notes` AS `notes`,`cat`.`is_available` AS `is_available` from ((`campground_accommodation_type` `cat` join `campground` `c` on((`c`.`id` = `cat`.`campground_id`))) join `accommodation_type` `at` on((`at`.`id` = `cat`.`accommodation_type_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_campground_activities`
--

/*!50001 DROP VIEW IF EXISTS `vw_campground_activities`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_campground_activities` AS select `ca`.`id` AS `id`,`ca`.`campground_id` AS `campground_id`,`c`.`name` AS `campground_name`,`ca`.`activity_id` AS `activity_id`,`a`.`code` AS `activity_code`,`a`.`name_fr` AS `activity_name_fr`,`a`.`name_en` AS `activity_name_en`,`ca`.`notes` AS `notes`,`ca`.`is_included` AS `is_included`,`ca`.`extra_fee` AS `extra_fee` from ((`campground_activity` `ca` join `campground` `c` on((`c`.`id` = `ca`.`campground_id`))) join `activity` `a` on((`a`.`id` = `ca`.`activity_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_campground_services`
--

/*!50001 DROP VIEW IF EXISTS `vw_campground_services`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_campground_services` AS select `cs`.`id` AS `id`,`cs`.`campground_id` AS `campground_id`,`c`.`name` AS `campground_name`,`cs`.`service_id` AS `service_id`,`s`.`code` AS `service_code`,`s`.`name_fr` AS `service_name_fr`,`s`.`name_en` AS `service_name_en`,`cs`.`notes` AS `notes`,`cs`.`is_included` AS `is_included`,`cs`.`extra_fee` AS `extra_fee` from ((`campground_service` `cs` join `campground` `c` on((`c`.`id` = `cs`.`campground_id`))) join `service` `s` on((`s`.`id` = `cs`.`service_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_campground_summary`
--

/*!50001 DROP VIEW IF EXISTS `vw_campground_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_campground_summary` AS select `c`.`id` AS `id`,`c`.`name` AS `name`,`c`.`city` AS `city`,`c`.`email` AS `email`,`c`.`phone_main` AS `phone_main`,`c`.`is_active` AS `is_active`,`c`.`total_sites` AS `total_sites`,`c`.`sites_3_services` AS `sites_3_services`,`c`.`sites_2_services` AS `sites_2_services`,`c`.`sites_1_service` AS `sites_1_service`,`c`.`sites_no_service` AS `sites_no_service`,`c`.`traveler_sites_count` AS `traveler_sites_count`,`c`.`has_wifi` AS `has_wifi`,`c`.`is_winter_camping` AS `is_winter_camping`,`c`.`created_at` AS `created_at`,`c`.`updated_at` AS `updated_at` from `campground` `c` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-07  0:46:26
