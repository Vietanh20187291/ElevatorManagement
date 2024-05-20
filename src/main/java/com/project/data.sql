-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: it_project_management
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'A','',1),(2,'B','',1),(3,'C','',1),(4,'D','',1),(5,'E','',1),(6,'Anh','f',NULL);
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `building`
--

LOCK TABLES `building` WRITE;
/*!40000 ALTER TABLE `building` DISABLE KEYS */;
INSERT INTO `building` VALUES (1,'Keungnam','VN','Hanoi','','');
/*!40000 ALTER TABLE `building` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `check_in`
--

LOCK TABLES `check_in` WRITE;
/*!40000 ALTER TABLE `check_in` DISABLE KEYS */;
/*!40000 ALTER TABLE `check_in` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `elevator`
--

LOCK TABLES `elevator` WRITE;
/*!40000 ALTER TABLE `elevator` DISABLE KEYS */;
INSERT INTO `elevator` VALUES (1,'E0001','/1/E0001',10,1),(6,'E0006','/1/E0006',10,2),(7,'E0007','/1/E0007',10,2),(8,'E0008','/1/E0008',10,2),(9,'E0009','/1/E0009',10,2),(10,'E0010','/1/E0010',10,2),(11,'E0011','/1/E0011',10,3),(12,'E0012','/1/E0012',10,3),(13,'E0013','/1/E0013',10,3),(14,'E0014','/1/E0014',10,3),(16,'E0016','/1/E0016',10,4),(17,'E0017','/1/E0017',10,4),(18,'E0018','/1/E0018',10,4),(19,'E0019','/1/E0019',10,4),(20,'E0020','/1/E0020',10,4);
/*!40000 ALTER TABLE `elevator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `feature`
--

LOCK TABLES `feature` WRITE;
/*!40000 ALTER TABLE `feature` DISABLE KEYS */;
INSERT INTO `feature` VALUES (1,'Card Management','Description for Card Management',1,'2023-01-01','2023-01-05',67,'2023-01-05',1,'COMPLETED'),(2,'Device Management','Description for Device Management',1,'2023-01-06','2023-01-10',85,'2023-01-10',1,'COMPLETED'),(3,'Transaction Management','Description for Transaction Management',1,'2023-02-01',NULL,80,NULL,1,'PROCESSING'),(4,'Page Navigation','Description for Page Navigation',2,'2023-02-01','2023-02-10',70,'2023-02-10',1,'COMPLETED'),(5,'Search Functionality','Description for Search Functionality',2,'2023-02-11','2023-02-20',100,'2023-02-20',1,'COMPLETED'),(6,'User Authentication','Description for User Authentication',2,'2023-02-21',NULL,65,NULL,1,'PROCESSING'),(7,'Student Registration','Description for Student Registration',3,'2023-03-01','2023-03-10',100,'2023-03-10',1,'COMPLETED'),(8,'Course Enrollment','Description for Course Enrollment',3,'2023-03-11','2023-03-15',35,'2023-03-15',1,'PENDING');
/*!40000 ALTER TABLE `feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `floor`
--

LOCK TABLES `floor` WRITE;
/*!40000 ALTER TABLE `floor` DISABLE KEYS */;
/*!40000 ALTER TABLE `floor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `issue`
--

LOCK TABLES `issue` WRITE;
/*!40000 ALTER TABLE `issue` DISABLE KEYS */;
INSERT INTO `issue` VALUES (1,1,'REQUEST','HIGH','Issue 1 for Task 1',2,'2023-01-02'),(2,1,'COMMENT','LOW','Issue 2 for Task 1',3,'2023-01-02'),(3,1,'NOTE','MEDIUM','Issue 3 for Task 1',2,'2023-01-03'),(4,1,'BUG','CRITICAL','Issue 4 for Task 1',3,'2023-01-04'),(5,1,'REQUEST','MEDIUM','Issue 5 for Task 1',2,'2023-01-05'),(6,2,'COMMENT','LOW','Issue 1 for Task 2',3,'2023-01-03'),(7,2,'NOTE','MEDIUM','Issue 2 for Task 2',2,'2023-01-03'),(8,2,'BUG','HIGH','Issue 3 for Task 2',3,'2023-01-04'),(9,2,'REQUEST','MEDIUM','Issue 4 for Task 2',2,'2023-01-05'),(10,2,'COMMENT','LOW','Issue 5 for Task 2',3,'2023-01-06'),(11,3,'NOTE','MEDIUM','Issue 1 for Task 3',2,'2023-01-06'),(12,3,'BUG','HIGH','Issue 2 for Task 3',3,'2023-01-07'),(13,3,'REQUEST','LOW','Issue 3 for Task 3',2,'2023-01-08'),(14,3,'COMMENT','MEDIUM','Issue 4 for Task 3',3,'2023-01-09'),(15,3,'NOTE','HIGH','Issue 5 for Task 3',2,'2023-01-10'),(16,4,'BUG','MEDIUM','Issue 1 for Task 4',2,'2023-01-06'),(17,4,'REQUEST','LOW','Issue 2 for Task 4',3,'2023-01-07'),(18,4,'COMMENT','MEDIUM','Issue 3 for Task 4',2,'2023-01-08'),(19,4,'NOTE','HIGH','Issue 4 for Task 4',3,'2023-01-09'),(20,4,'BUG','MEDIUM','Issue 5 for Task 4',2,'2023-01-10'),(21,5,'REQUEST','MEDIUM','Issue 1 for Task 5',2,'2023-01-06'),(22,5,'COMMENT','LOW','Issue 2 for Task 5',3,'2023-01-07'),(23,5,'NOTE','HIGH','Issue 3 for Task 5',2,'2023-01-08'),(24,5,'BUG','MEDIUM','Issue 4 for Task 5',3,'2023-01-09'),(25,5,'REQUEST','LOW','Issue 5 for Task 5',2,'2023-01-10'),(26,6,'COMMENT','HIGH','Issue 1 for Task 6',3,'2023-01-07'),(27,6,'NOTE','MEDIUM','Issue 2 for Task 6',2,'2023-01-08'),(28,6,'BUG','LOW','Issue 3 for Task 6',3,'2023-01-09'),(29,6,'REQUEST','MEDIUM','Issue 4 for Task 6',2,'2023-01-10'),(30,6,'COMMENT','HIGH','Issue 5 for Task 6',3,'2023-01-11'),(31,7,'NOTE','LOW','Issue 1 for Task 7',2,'2023-01-08'),(32,7,'BUG','MEDIUM','Issue 2 for Task 7',3,'2023-01-09'),(33,7,'REQUEST','HIGH','Issue 3 for Task 7',2,'2023-01-10'),(34,7,'COMMENT','MEDIUM','Issue 4 for Task 7',3,'2023-01-11'),(35,7,'NOTE','LOW','Issue 5 for Task 7',2,'2023-01-12'),(36,8,'BUG','MEDIUM','Issue 1 for Task 8',3,'2023-01-09'),(37,8,'REQUEST','LOW','Issue 2 for Task 8',2,'2023-01-10'),(38,8,'COMMENT','MEDIUM','Issue 3 for Task 8',3,'2023-01-11'),(39,8,'NOTE','HIGH','Issue 4 for Task 8',2,'2023-01-12'),(40,8,'BUG','MEDIUM','Issue 5 for Task 8',3,'2023-01-13'),(41,9,'BUG','HIGH','Issue 4 for Task 9',3,'2023-01-13'),(42,9,'REQUEST','MEDIUM','Issue 5 for Task 9',2,'2023-01-14'),(43,10,'COMMENT','MEDIUM','Issue 1 for Task 10',3,'2023-01-11'),(44,10,'NOTE','LOW','Issue 2 for Task 10',2,'2023-01-12'),(45,10,'BUG','HIGH','Issue 3 for Task 10',3,'2023-01-13'),(46,10,'REQUEST','MEDIUM','Issue 4 for Task 10',2,'2023-01-14'),(47,10,'COMMENT','HIGH','Issue 5 for Task 10',3,'2023-01-15');
/*!40000 ALTER TABLE `issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jwt_response`
--

LOCK TABLES `jwt_response` WRITE;
/*!40000 ALTER TABLE `jwt_response` DISABLE KEYS */;
/*!40000 ALTER TABLE `jwt_response` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `mqttconnection`
--

LOCK TABLES `mqttconnection` WRITE;
/*!40000 ALTER TABLE `mqttconnection` DISABLE KEYS */;
INSERT INTO `mqttconnection` VALUES (1,'tkevn.ddns.net',8001,'user1','minh',NULL);
/*!40000 ALTER TABLE `mqttconnection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `noti`
--

LOCK TABLES `noti` WRITE;
/*!40000 ALTER TABLE `noti` DISABLE KEYS */;
INSERT INTO `noti` VALUES (1,'manager updated area with id: 1','2024-03-30 15:25:01',_binary '\0',2),(2,'manager updated area with id: 2','2024-03-30 15:25:39',_binary '\0',2),(3,'manager updated area with id: 3','2024-03-30 15:26:21',_binary '\0',2),(4,'manager updated area with id: 4','2024-03-30 15:30:17',_binary '\0',2),(5,'manager updated area with id: 5','2024-03-30 15:31:30',_binary '\0',2),(6,'manager updated area with id: 6','2024-03-30 15:37:34',_binary '\0',2),(7,'manager updated area with id: 6','2024-03-30 15:41:09',_binary '\0',2),(8,'manager updated area with id: 2','2024-03-30 15:45:11',_binary '\0',2),(9,'manager updated area with id: 1','2024-03-30 15:46:01',_binary '\0',2),(10,'manager updated area with id: 3','2024-03-30 15:47:04',_binary '\0',2),(11,'manager updated area with id: 3','2024-03-30 15:47:06',_binary '\0',2),(12,'manager updated area with id: 3','2024-03-30 15:47:16',_binary '\0',2),(13,'manager updated area with id: 3','2024-03-30 15:48:55',_binary '\0',2),(14,'manager updated area with id: 3','2024-03-30 15:50:58',_binary '\0',2),(15,'manager updated area with id: 3','2024-03-30 15:51:52',_binary '\0',2),(16,'manager updated area with id: 3','2024-03-30 15:52:18',_binary '\0',2),(17,'manager updated area with id: 3','2024-03-30 15:52:19',_binary '\0',2),(18,'manager updated area with id: 3','2024-03-30 15:52:59',_binary '\0',2),(19,'manager updated area with id: 3','2024-03-30 15:53:47',_binary '\0',2),(20,'manager updated area with id: 3','2024-03-30 15:54:29',_binary '\0',2),(21,'manager updated area with id: 3','2024-03-30 15:55:08',_binary '\0',2),(22,'manager updated area with id: 3','2024-03-30 15:56:44',_binary '\0',2),(23,'manager updated area with id: 4','2024-03-30 15:58:13',_binary '\0',2),(24,'manager updated area E with ID 0','2024-03-30 16:06:29',_binary '\0',2),(25,'manager updated area E with ID 0','2024-03-30 16:08:55',_binary '\0',2),(26,'manager updated area E with ID 0','2024-03-30 16:13:57',_binary '\0',2),(27,'manager updated area Anh with ID 0','2024-03-30 16:50:32',_binary '\0',2),(28,'manager updated area Anh with ID 0','2024-03-30 16:58:29',_binary '\0',2),(29,'manager updated area E with ID 0','2024-03-30 17:00:01',_binary '\0',2),(30,'manager updated area A with ID 0','2024-04-01 10:55:09',_binary '\0',2),(31,'manager updated area B with ID 0','2024-04-01 10:55:20',_binary '\0',2),(32,'manager updated area D with ID 0','2024-04-01 10:56:57',_binary '\0',2),(33,'manager updated area Data Management with ID 0','2024-04-01 10:57:06',_binary '\0',2),(34,'manager updated area Data Management with ID 0','2024-04-01 10:58:40',_binary '\0',2),(35,'manager updated area Data Management with ID 0','2024-04-01 11:03:02',_binary '\0',2),(36,'manager updated area Data Management with ID 0','2024-04-01 11:04:58',_binary '\0',2),(37,'manager updated area Data Management with ID 0','2024-04-01 11:07:14',_binary '\0',2),(38,'manager updated area D with ID 0','2024-04-01 11:08:46',_binary '\0',2),(39,'manager updated area C with ID 0','2024-04-01 11:09:41',_binary '\0',2),(40,'manager updated area Card with ID 0','2024-04-01 11:10:36',_binary '\0',2),(41,'manager updated area card 12 with ID 0','2024-04-01 11:21:22',_binary '\0',2),(42,'manager updated area card 12 with ID 0','2024-04-01 11:22:11',_binary '\0',2),(43,'manager updated area card 12 with ID 0','2024-04-01 11:23:40',_binary '\0',2),(44,'manager updated area Card with ID 0','2024-04-01 11:23:49',_binary '\0',2),(45,'manager updated area Card with ID 0','2024-04-01 14:08:59',_binary '\0',2),(46,'manager updated area Card with ID 0','2024-04-01 14:09:03',_binary '\0',2),(47,'manager updated area D with ID 0','2024-04-01 14:53:21',_binary '\0',2),(48,'manager updated area Card with ID 0','2024-04-01 16:03:25',_binary '\0',2),(49,'manager updated your permission in version 1.0 of Project 2','2024-04-01 16:19:14',_binary '',5),(50,'manager updated your permission in version 1.0 of Project 2','2024-04-01 16:19:19',_binary '',5),(51,'manager updated your permission in version 1.0 of Project 2','2024-04-01 16:19:23',_binary '',5),(52,'manager updated your permission in version 1.0 of Project 2','2024-04-01 16:19:26',_binary '',4),(53,'manager updated your permission in version 1.0 of Project 2','2024-04-01 16:19:29',_binary '',4),(54,'manager updated your permission in version 1.0 of Project 2','2024-04-01 16:22:24',_binary '',4),(55,'manager updated your permission in version 1.0 of Project 2','2024-04-01 16:22:27',_binary '',4),(56,'manager updated area Card with ID 0','2024-04-01 16:44:17',_binary '\0',2),(57,'manager updated your permission in version 1.0 of Project 2','2024-04-02 15:48:33',_binary '',5),(58,'manager updated your permission in version 1.0 of Project 2','2024-04-02 15:48:45',_binary '',5),(59,'manager updated your permission in version 1.0 of Project 2','2024-04-02 15:48:50',_binary '',5),(60,'manager updated your permission in version 1.0 of Project 2','2024-04-02 15:48:59',_binary '',5),(61,'manager updated area A with ID 0','2024-04-03 22:39:47',_binary '\0',2),(62,'manager updated area A with ID 0','2024-04-03 22:39:50',_binary '\0',2),(63,'manager updated area E with ID 0','2024-04-04 10:10:29',_binary '\0',2),(64,'manager updated area E with ID 0','2024-04-04 10:10:35',_binary '\0',2),(65,'manager updated area C with ID 0','2024-04-04 17:25:08',_binary '\0',2),(66,'manager updated area B with ID 0','2024-04-04 17:25:20',_binary '\0',2),(67,'manager updated area A with ID 0','2024-04-04 17:25:30',_binary '\0',2),(68,'manager updated area B with ID 0','2024-04-04 17:27:55',_binary '\0',2),(69,'manager updated area B with ID 0','2024-04-04 21:51:54',_binary '\0',2),(70,'manager updated area A with ID 0','2024-04-04 22:04:58',_binary '\0',2),(71,'manager updated area A with ID 0','2024-04-05 17:12:29',_binary '\0',2),(72,'manager updated area A with ID 0','2024-04-05 17:12:32',_binary '\0',2),(73,'manager updated area A with ID 0','2024-04-15 19:19:17',_binary '\0',2),(74,'manager updated area A with ID 0','2024-04-15 19:19:20',_binary '\0',2),(75,'manager updated area A with ID 0','2024-04-19 16:04:58',_binary '\0',2),(76,'manager updated area A with ID 0','2024-04-19 16:05:00',_binary '\0',2),(77,'manager updated version 2.0 of Project Transaction Management','2024-05-07 15:21:42',_binary '\0',2),(78,'manager updated version 2.0 of Project Transaction Management','2024-05-07 15:22:43',_binary '\0',2);
/*!40000 ALTER TABLE `noti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'Transaction Management','This is Transaction Management',1),(2,'Student Management','Description for Student Management',1);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `project_version`
--

LOCK TABLES `project_version` WRITE;
/*!40000 ALTER TABLE `project_version` DISABLE KEYS */;
INSERT INTO `project_version` VALUES (1,1,'1.0','Basic Function for Transaction Management','2023-01-01','2023-01-10','2023-01-15','PROCESSING',77,1),(2,1,'2.0','Pageable and advance Function for Transaction Management','2023-02-01',NULL,'2024-05-22','PROCESSING',78,1),(3,2,'1.0','Version 1.0 for Student Management','2023-03-01','2023-03-15','2023-03-20','PENDING',67,1);
/*!40000 ALTER TABLE `project_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'Add Card','Description for Task 1',1,4,'2023-01-01','2023-05-05','2023-01-02',100,'COMPLETED','HIGH',1),(2,'Delete Card','Description for Task 2',1,3,'2023-01-02','2023-05-20',NULL,100,'COMPLETED','MEDIUM',1),(3,'Update Card','Description for Task 3',1,5,'2023-01-06','2023-06-12',NULL,30,'POSTPONED','MEDIUM',1),(4,'Find All Card','Description for Task 4',1,5,'2023-01-06','2023-05-29',NULL,40,'PROCESSING','MEDIUM',1),(5,'Add Device','Description for Task 1',2,6,'2023-01-01','2023-06-10','2023-01-02',100,'COMPLETED','HIGH',1),(6,'Delete Device','Description for Task 2',2,4,'2023-01-02','2023-07-01','2023-01-03',100,'COMPLETED','MEDIUM',1),(7,'Update Device','Description for Task 3',2,3,'2023-01-06','2023-09-15','2023-01-03',80,'PROCESSING','MEDIUM',1),(8,'Find All Device','Description for Task 4',2,6,'2023-01-06','2023-06-18',NULL,60,'PROCESSING','MEDIUM',1),(9,'Add Transaction','Description for Task 1',3,5,'2023-01-01','2023-05-15','2023-01-02',100,'COMPLETED','HIGH',1),(10,'Delete Transaction','Description for Task 2',3,4,'2023-01-02','2023-06-30',NULL,100,'PROCESSING','MEDIUM',1),(11,'Update Transaction','Description for Task 3',3,NULL,'2023-01-06','2023-04-05',NULL,50,'PROCESSING','MEDIUM',1),(12,'Find All Transaction','Description for Task 4',3,5,'2023-01-06','2023-05-17',NULL,70,'PROCESSING','MEDIUM',1),(13,'Implement Page Navigation','Description for Task 1',4,3,'2023-02-01','2023-05-05','2023-02-02',100,'COMPLETED','HIGH',1),(14,'Refine Search Functionality','Description for Task 2',5,2,'2023-02-02','2023-05-20',NULL,100,'COMPLETED','MEDIUM',1),(15,'Set Up User Authentication','Description for Task 3',6,1,'2023-02-06','2023-06-12',NULL,30,'POSTPONED','MEDIUM',1),(16,'Test Page Navigation','Description for Task 4',4,1,'2023-02-06','2023-05-29',NULL,40,'PROCESSING','MEDIUM',1),(17,'Test Search Functionality','Description for Task 5',5,4,'2023-02-01','2023-06-10','2023-02-02',100,'COMPLETED','HIGH',1),(18,'Implement User Authentication','Description for Task 6',6,4,'2023-02-02','2023-07-01','2023-02-03',100,'COMPLETED','MEDIUM',1),(19,'Implement Student Registration','Description for Task 1',7,5,'2023-03-01','2023-03-05','2023-03-02',100,'COMPLETED','HIGH',1),(20,'Test Student Registration','Description for Task 2',7,3,'2023-03-02','2023-03-10',NULL,100,'COMPLETED','MEDIUM',1),(21,'Prepare Course Enrollment','Description for Task 3',8,4,'2023-03-11','2023-03-12',NULL,30,'POSTPONED','MEDIUM',1),(22,'Implement Course Enrollment','Description for Task 4',8,5,'2023-03-13','2023-03-15',NULL,40,'PROCESSING','MEDIUM',1);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','Binhta123','ADMIN',1,NULL),(2,'manager','manager','MANAGER',1,1),(3,'user1','tke@123','USER',1,1),(4,'user2','tke@123','USER',1,1),(5,'user3','tke@123','USER',1,1),(6,'user4','tke@123','USER',1,1),(7,'user5','user5','USER',0,1),(8,'user6','user6','USER',1,NULL),(10,'user9','Hanoi123','USER',1,1),(11,'user10','Hanoi123','USER',0,1),(12,'user11','tke@123','USER',1,1),(17,'user12','tke@123','USER',1,1),(18,'user15','tke@123','USER',1,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_area`
--

LOCK TABLES `user_area` WRITE;
/*!40000 ALTER TABLE `user_area` DISABLE KEYS */;
INSERT INTO `user_area` VALUES (1,3,1),(2,4,2),(3,5,3),(6,NULL,NULL),(7,NULL,NULL),(8,NULL,NULL),(13,7,4),(14,4,5),(20,5,2),(25,5,4),(27,4,1),(28,5,1),(29,6,2);
/*!40000 ALTER TABLE `user_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_old`
--

LOCK TABLES `user_old` WRITE;
/*!40000 ALTER TABLE `user_old` DISABLE KEYS */;
INSERT INTO `user_old` VALUES (1,'admin','admin','ADMIN',1,NULL),(2,'manager','manager','MANAGER',1,NULL),(3,'user1','user1','USER',1,NULL),(4,'user2','user2','USER',1,NULL),(5,'user3','user3','USER',1,NULL),(6,'user4','user4','USER',1,NULL);
/*!40000 ALTER TABLE `user_old` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_project_version`
--

LOCK TABLES `user_project_version` WRITE;
/*!40000 ALTER TABLE `user_project_version` DISABLE KEYS */;
INSERT INTO `user_project_version` VALUES (1,3,1,1,0,1),(2,4,2,1,1,0),(3,5,3,1,0,1),(4,6,1,0,0,0),(5,3,2,1,1,1),(6,2,2,1,1,0),(7,1,2,0,1,1),(8,4,3,1,1,1),(9,1,3,1,1,0),(10,2,3,1,0,1);
/*!40000 ALTER TABLE `user_project_version` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-21  0:01:55
