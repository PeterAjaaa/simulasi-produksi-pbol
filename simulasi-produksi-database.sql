-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for simulasi-produksi
CREATE DATABASE IF NOT EXISTS `simulasi-produksi` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `simulasi-produksi`;

-- Dumping structure for table simulasi-produksi.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `req_tool` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prod_time` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table simulasi-produksi.product: ~4 rows (approximately)
INSERT IGNORE INTO `product` (`id`, `name`, `req_tool`, `type`, `prod_time`) VALUES
	('PR001', 'Navy Killer', 'Calibration Equipment', 'Combat Drone', 500),
	('PR002', 'MedEVAC V2', 'Inventory Management System', 'Medical Delivery Drone', 300),
	('PR003', 'DroneBuoy2K', 'Electronic Workbench', 'SAR Drone', 720),
	('PR004', 'AceDefenseSystem Combat Drone', 'Assembly Robots', 'Combat Drone', 1440);

-- Dumping structure for table simulasi-produksi.tool
CREATE TABLE IF NOT EXISTS `tool` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `availability` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `efficiency` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table simulasi-produksi.tool: ~4 rows (approximately)
INSERT IGNORE INTO `tool` (`id`, `name`, `type`, `availability`, `efficiency`) VALUES
	('T001', 'VAssembly', 'Assembly Robots', 'Ready', 75),
	('T002', 'Musashi Corp 3D Printer V2', '3D Printer', 'Maintenance', 85),
	('T003', 'NekoCalibrationSystem', 'Calibration Equipment', 'Ready', 65),
	('T004', 'OuterInc CNC Machine', 'CNC Machine', 'Ready', 80);

-- Dumping structure for table simulasi-produksi.worker
CREATE TABLE IF NOT EXISTS `worker` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `proficiency` varchar(50) NOT NULL,
  `work_hours` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table simulasi-produksi.worker: ~5 rows (approximately)
INSERT IGNORE INTO `worker` (`id`, `name`, `proficiency`, `work_hours`) VALUES
	('P001', 'Jack', 'Advanced', 8),
	('P002', 'Tom', 'Intermediate', 6),
	('P003', 'Ben', 'Beginner', 4),
	('P004', 'Benny', 'Advanced', 12),
	('P005', 'Tommy', 'Beginner', 1);

-- Dumping structure for table simulasi-produksi.work_session
CREATE TABLE IF NOT EXISTS `work_session` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `products_made` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `end_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `worker_involved` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_work_session_product` (`products_made`),
  KEY `FK_work_session_worker` (`worker_involved`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table simulasi-produksi.work_session: ~2 rows (approximately)
INSERT IGNORE INTO `work_session` (`id`, `products_made`, `start_time`, `end_time`, `worker_involved`) VALUES
	('185eb037-07a7-4aab-aba8-32f3428ccf62_P001', 'PR002', '16:36', '18:57', 'P001'),
	('185eb037-07a7-4aab-aba8-32f3428ccf62_P002', 'PR002', '16:36', '18:57', 'P002'),
	('185eb037-07a7-4aab-aba8-32f3428ccf62_P003', 'PR002', '16:36', '18:57', 'P003');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
