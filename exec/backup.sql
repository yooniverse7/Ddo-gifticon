-- MySQL dump 10.13  Distrib 9.2.0, for Linux (x86_64)
--
-- Host: localhost    Database: ddopay
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `ddo_pay_ddopay_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9kc9xwfr8kgj2m250inkusbu4` (`ddo_pay_ddopay_id`),
  CONSTRAINT `FK9kc9xwfr8kgj2m250inkusbu4` FOREIGN KEY (`ddo_pay_ddopay_id`) REFERENCES `ddo_pay` (`ddopay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,1,'9995258783782222'),(2,2,'9996049338336744'),(3,3,'9992771090893649');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agreement`
--

DROP TABLE IF EXISTS `agreement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agreement` (
  `marketing` bit(1) DEFAULT NULL,
  `personal_info` bit(1) DEFAULT NULL,
  `push_alarm` bit(1) DEFAULT NULL,
  `agreement_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`agreement_id`),
  UNIQUE KEY `UKpmtyl2cwsnhnpsui15lax7did` (`user_id`),
  CONSTRAINT `FKqj8sg6nwb66uswagi8wl5jerc` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agreement`
--

LOCK TABLES `agreement` WRITE;
/*!40000 ALTER TABLE `agreement` DISABLE KEYS */;
/*!40000 ALTER TABLE `agreement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarm`
--

DROP TABLE IF EXISTS `alarm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm` (
  `alarm_id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`alarm_id`),
  KEY `FKd6g1gp6sn8nt3ku8y2mgu41vs` (`user_id`),
  CONSTRAINT `FKd6g1gp6sn8nt3ku8y2mgu41vs` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm`
--

LOCK TABLES `alarm` WRITE;
/*!40000 ALTER TABLE `alarm` DISABLE KEYS */;
/*!40000 ALTER TABLE `alarm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_menu`
--

DROP TABLE IF EXISTS `custom_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom_menu` (
  `custom_menu_price` int DEFAULT NULL,
  `custom_menu_id` bigint NOT NULL AUTO_INCREMENT,
  `user_restaurant_id` bigint DEFAULT NULL,
  `custom_menu_image` varchar(255) DEFAULT NULL,
  `custom_menu_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`custom_menu_id`),
  KEY `FK3bsu5g1hhnbp31rxeha15u1ix` (`user_restaurant_id`),
  CONSTRAINT `FK3bsu5g1hhnbp31rxeha15u1ix` FOREIGN KEY (`user_restaurant_id`) REFERENCES `user_restaurant` (`user_restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_menu`
--

LOCK TABLES `custom_menu` WRITE;
/*!40000 ALTER TABLE `custom_menu` DISABLE KEYS */;
INSERT INTO `custom_menu` VALUES (20000,1,2,NULL,'가마솥 계란찜'),(15000,2,3,NULL,'ㄱㅈㄷ'),(100000000,3,4,NULL,'돼지국밥');
/*!40000 ALTER TABLE `custom_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ddo_pay`
--

DROP TABLE IF EXISTS `ddo_pay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ddo_pay` (
  `balance` int NOT NULL,
  `point` int NOT NULL,
  `ddopay_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `pay_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ddopay_id`),
  UNIQUE KEY `UKr8qac8x3mavy1uhune9j63oym` (`user_id`),
  CONSTRAINT `FKr9m5mvnwcv1swfavrhhpf21dg` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ddo_pay`
--

LOCK TABLES `ddo_pay` WRITE;
/*!40000 ALTER TABLE `ddo_pay` DISABLE KEYS */;
INSERT INTO `ddo_pay` VALUES (10844400,50,1,1,'123456'),(10000000,50,2,2,'123456'),(10000000,50,3,3,'123456');
/*!40000 ALTER TABLE `ddo_pay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `followee`
--

DROP TABLE IF EXISTS `followee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `followee` (
  `followee_id` bigint NOT NULL AUTO_INCREMENT,
  `followee_user_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `phone_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`followee_id`),
  KEY `FK4cm36myhtu8edc1o5pc4i6yin` (`user_id`),
  CONSTRAINT `FK4cm36myhtu8edc1o5pc4i6yin` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `followee`
--

LOCK TABLES `followee` WRITE;
/*!40000 ALTER TABLE `followee` DISABLE KEYS */;
/*!40000 ALTER TABLE `followee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gift`
--

DROP TABLE IF EXISTS `gift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift` (
  `amount` int DEFAULT NULL,
  `expiration_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `restaurant_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `menu_combination` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `phone_num` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `used_status` enum('AFTER_USE','BEFORE_USE','CANCLE','EXPIRED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpsl9nektrfok8bounkyryl7e3` (`restaurant_id`),
  KEY `FKkc0fi6necdyfsc3hqoskjub5j` (`user_id`),
  CONSTRAINT `FKkc0fi6necdyfsc3hqoskjub5j` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKpsl9nektrfok8bounkyryl7e3` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift`
--

LOCK TABLES `gift` WRITE;
/*!40000 ALTER TABLE `gift` DISABLE KEYS */;
INSERT INTO `gift` VALUES (73000,'2025-07-10 11:31:39.569437',1,1,1,'https://ddopay.s3.ap-northeast-2.amazonaws.com/uploads/bad4439f-3bdd-40cd-a094-5b698eab4ec4_custom_menu_0.jpg',NULL,'ㅊㅌㅋㅍㅋㅌㅊ','010-8765-4321','ㅊㅍㅋㅊㅌㅍ','CANCLE'),(113000,'2025-07-10 14:17:14.227310',2,1,1,'https://ddopay.s3.ap-northeast-2.amazonaws.com/uploads/a368ef17-95ea-4ec1-9755-03942561ce26_custom_menu_0.jpg',NULL,'re','010-8765-4321','re','CANCLE'),(87000,'2025-07-10 15:26:30.819473',3,1,1,'https://ddopay.s3.ap-northeast-2.amazonaws.com/uploads/a640dda4-52fc-4835-b880-7b067a8ca939_custom_menu_0.jpg',NULL,'ㅎㅎ','','ㅗㅗ','BEFORE_USE');
/*!40000 ALTER TABLE `gift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gift_box`
--

DROP TABLE IF EXISTS `gift_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift_box` (
  `gift_id` bigint DEFAULT NULL,
  `giftbox_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`giftbox_id`),
  UNIQUE KEY `UKjva334int80x1g6mws1fmkmx` (`gift_id`),
  KEY `FK8h7b2s8yr8fm80e0lt4dng81d` (`user_id`),
  CONSTRAINT `FK8h7b2s8yr8fm80e0lt4dng81d` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FK9g8u83u6e1uorg0mdrpuygoml` FOREIGN KEY (`gift_id`) REFERENCES `gift` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift_box`
--

LOCK TABLES `gift_box` WRITE;
/*!40000 ALTER TABLE `gift_box` DISABLE KEYS */;
/*!40000 ALTER TABLE `gift_box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `history` (
  `in_out_amount` int NOT NULL,
  `ddo_pay_ddopay_id` bigint DEFAULT NULL,
  `history_id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` enum('BALANCE','POINT') DEFAULT NULL,
  PRIMARY KEY (`history_id`),
  KEY `FKte1603ty3dan8khs71uqvlucj` (`ddo_pay_ddopay_id`),
  CONSTRAINT `FKte1603ty3dan8khs71uqvlucj` FOREIGN KEY (`ddo_pay_ddopay_id`) REFERENCES `ddo_pay` (`ddopay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES (-73000,1,1,'2025-04-10 11:31:39.271325','기프티콘 생성','BALANCE'),(-73000,1,2,'2025-04-10 11:31:39.271325','기프티콘 생성','BALANCE'),(-113000,1,3,'2025-04-10 14:17:14.096328','기프티콘 생성','BALANCE'),(-113000,1,4,'2025-04-10 14:17:14.096328','기프티콘 생성','BALANCE'),(73000,1,5,'2025-04-10 14:18:35.577772','기프티콘 환불','BALANCE'),(73000,1,6,'2025-04-10 14:18:35.577772','기프티콘 환불','BALANCE'),(50000,1,8,'2025-04-10 14:24:31.738044','또페이 충전','BALANCE'),(900000,1,9,'2025-04-10 14:24:45.699861','또페이 충전','BALANCE'),(113000,1,10,'2025-04-10 14:25:11.314442','기프티콘 환불','BALANCE'),(113000,1,11,'2025-04-10 14:25:11.314442','기프티콘 환불','BALANCE'),(-87000,1,12,'2025-04-10 15:26:30.678759','기프티콘 생성','BALANCE'),(-87000,1,13,'2025-04-10 15:26:30.678759','기프티콘 생성','BALANCE');
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `menu_price` int DEFAULT NULL,
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `restaurant_id` bigint DEFAULT NULL,
  `menu_image` varchar(255) DEFAULT NULL,
  `menu_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`menu_id`),
  KEY `FKblwdtxevpl4mrds8a12q0ohu6` (`restaurant_id`),
  CONSTRAINT `FKblwdtxevpl4mrds8a12q0ohu6` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (87000,1,1,NULL,'본가 시그니처 700g'),(59000,2,1,NULL,'본가 시그니처 500g'),(14000,3,1,NULL,'우삼겹(150g)'),(16000,4,1,NULL,'깍둑등심(150g)'),(17000,5,1,NULL,'마늘깍둑등심(150g)'),(14000,6,1,NULL,'숙성 돼지갈비(200g)'),(14000,7,1,NULL,'한돈 생삼겹살(150g)'),(17000,8,1,NULL,'LA양념갈비(200g)'),(19000,9,1,NULL,'갈비살(150g)'),(26000,10,1,NULL,'LA통갈비'),(28000,11,1,NULL,'꽃살(150g)'),(30000,12,1,NULL,'소양념왕갈비(250g)'),(24000,13,1,NULL,'농협안심한우육회'),(16000,14,1,NULL,'우삼겹정식(점심한정)'),(8000,15,1,NULL,'차돌된장찌개'),(9000,16,1,NULL,'뚝배기불고기'),(9000,17,1,NULL,'본가비빔밥'),(10000,18,1,NULL,'한우우거지해장국'),(14500,19,1,NULL,'본가 갈비탕'),(7000,20,1,NULL,'본가 냉면'),(7000,21,1,NULL,'비빔냉면'),(10000,22,2,NULL,'숯불닭다리살'),(10000,23,2,NULL,'숯불무뼈닭발'),(10000,24,2,NULL,'숯불닭똥집'),(4500,25,2,NULL,'날치알마요밥'),(5000,26,2,NULL,'심야우동'),(6000,27,2,NULL,'가마솥계란찜');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `star_rating` decimal(3,2) DEFAULT NULL,
  `restaurant_id` bigint NOT NULL AUTO_INCREMENT,
  `address_name` varchar(255) DEFAULT NULL,
  `main_image_url` varchar(255) DEFAULT NULL,
  `place_id` varchar(255) DEFAULT NULL,
  `place_name` varchar(255) DEFAULT NULL,
  `user_intro` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (35.097841,128.9108217,4.50,1,'부산광역시 강서구 명지동 3432-3 대방디엠시티 2층 B동 201호~205호','https://ldb-phinf.pstatic.net/20230901_137/1693562011023vRaKI_JPEG/KakaoTalk_20230711_150131819_17.jpg',NULL,'본가 부산명지점','직접 끓인 사골 국물이 일품!'),(35.11113,128.9632915,4.50,2,'부산광역시 사하구 하단동 500-4 1층','https://ldb-phinf.pstatic.net/20201023_276/1603424015305CdC4w_JPEG/yyDdjexjV__50xn4BHRoh5sw.jpg',NULL,'하단끝집 하단점','직접 끓인 사골 국물이 일품!'),(35.0912665,128.9684452,4.50,3,'부산광역시 사하구 신평동 343-2',NULL,NULL,'용강식당','직접 끓인 사골 국물이 일품!'),(35.1430086,128.87927,4.50,4,'부산광역시 강서구 범방동 1915-6',NULL,NULL,'영진블루텍 미음공장','직접 끓인 사골 국물이 일품!');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `birthday` datetime(6) DEFAULT NULL,
  `kakao_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `login_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_num` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK4tp32nb01jmfcirpipti37lfs` (`kakao_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (NULL,4210061904,1,'',NULL,'정영한',NULL,'010-0000-0001','MbYYwtnepzmFodzfhlsbzq1lxtjTcdsSAAAAAgoNG5oAAAGWHXWvDs6SpOckXrb0'),(NULL,NULL,2,'minsangi@example.com',NULL,'이상혁','password123','010-0000-0002',NULL),(NULL,4008036910,3,'',NULL,'이상혁',NULL,'','X5gK9DvnHNUlW2l99KqB-vUmpmMl79qqAAAAAgoNGZAAAAGWHYkEttQ0RDl69jWm'),(NULL,4210324827,4,'',NULL,'권은채',NULL,'','xH-gonZ_EIOtdPtMnM1jhVGvrkFxCtOGAAAAAgoNFZsAAAGWHiDdbq-b-4epDDEo'),(NULL,4210333580,5,'',NULL,'윤예리',NULL,'','N6_mRH0y87zcGWkIz4PuUcgx_A9gSwYwAAAAAgoXEi0AAAGWHiZ4lMO6S6yUo1la'),(NULL,4210369837,6,'',NULL,'박준호',NULL,'','Y6o8QraUEG9gdu1A9RLRdVFuXWCEn4ZZAAAAAgoXAVAAAAGWHj5ze90Jz_1t7hqp'),(NULL,4200290229,7,'',NULL,'최진문',NULL,'','IO3rJikkDqSMTZmlNf2sHASvfQPdzN8iAAAAAgoNIBsAAAGWHmf36eZNgpjs3oAL');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_restaurant`
--

DROP TABLE IF EXISTS `user_restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_restaurant` (
  `visited_count` int NOT NULL DEFAULT '0',
  `restaurant_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `user_restaurant_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`user_restaurant_id`),
  KEY `FKcw5qhotbd6n915bkpha54se2l` (`restaurant_id`),
  KEY `FK308myc6bojgkm0eeiyvbawmci` (`user_id`),
  CONSTRAINT `FK308myc6bojgkm0eeiyvbawmci` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKcw5qhotbd6n915bkpha54se2l` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_restaurant`
--

LOCK TABLES `user_restaurant` WRITE;
/*!40000 ALTER TABLE `user_restaurant` DISABLE KEYS */;
INSERT INTO `user_restaurant` VALUES (0,1,1,1),(0,2,1,2),(0,3,1,3),(0,4,1,4);
/*!40000 ALTER TABLE `user_restaurant` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-10 15:40:13
