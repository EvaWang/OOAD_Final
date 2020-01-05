-- Database export via SQLPro (https://www.sqlprostudio.com/allapps.html)
-- Exported by wangting at 06-01-2020 04:35.
-- WARNING: This file may contain descructive statements such as DROPs.
-- Please ensure that you are running the script at the proper location.


-- BEGIN TABLE booking
DROP TABLE IF EXISTS booking;
CREATE TABLE `booking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `hotel_id` int(11) NOT NULL,
  `hotel_room_id` int(11) NOT NULL,
  `is_disabled` bit(1) NOT NULL,
  `order_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE booking

-- BEGIN TABLE calendar_table
DROP TABLE IF EXISTS calendar_table;
CREATE TABLE `calendar_table` (
  `dt` date NOT NULL,
  `y` smallint(6) DEFAULT NULL,
  `q` tinyint(4) DEFAULT NULL,
  `m` tinyint(4) DEFAULT NULL,
  `d` tinyint(4) DEFAULT NULL,
  `dw` tinyint(4) DEFAULT NULL,
  `monthName` varchar(9) DEFAULT NULL,
  `dayName` varchar(9) DEFAULT NULL,
  `w` tinyint(4) DEFAULT NULL,
  `isWeekday` binary(1) DEFAULT NULL,
  `isHoliday` binary(1) DEFAULT NULL,
  `holidayDescr` varchar(32) DEFAULT NULL,
  `isPayday` binary(1) DEFAULT NULL,
  PRIMARY KEY (`dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE calendar_table

-- BEGIN TABLE hibernate_sequence
DROP TABLE IF EXISTS hibernate_sequence;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE hibernate_sequence

-- BEGIN TABLE hotel
DROP TABLE IF EXISTS hotel;
CREATE TABLE `hotel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `address` varchar(255) NOT NULL DEFAULT '',
  `json_file_id` int(11) NOT NULL,
  `locality` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `star` varchar(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hotel_unique_index` (`json_file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1501 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE hotel

-- BEGIN TABLE hotel_room
DROP TABLE IF EXISTS hotel_room;
CREATE TABLE `hotel_room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `hotel_id` varchar(255) NOT NULL DEFAULT '0',
  `price` varchar(255) NOT NULL DEFAULT '99999',
  `quantity` varchar(255) NOT NULL DEFAULT '0',
  `room_type` varchar(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hotel_room_unique_index` (`hotel_id`,`room_type`)
) ENGINE=InnoDB AUTO_INCREMENT=4501 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE hotel_room

-- BEGIN TABLE ints
DROP TABLE IF EXISTS ints;
CREATE TABLE `ints` (
  `i` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE ints

-- BEGIN TABLE order_view
DROP TABLE IF EXISTS order_view;
CREATE TABLE `order_view` (
  `hotel_room_id` int(11) NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `discount` double DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `booked_is_disabled` bit(1) DEFAULT NULL,
  `booked_quantity` int(11) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `is_disabled` bit(1) DEFAULT NULL,
  `is_paid` bit(1) DEFAULT NULL,
  `json_file_id` int(11) DEFAULT NULL,
  `locality` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `room_type` int(11) DEFAULT NULL,
  `star` int(11) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`hotel_room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE order_view

-- BEGIN TABLE ordering
DROP TABLE IF EXISTS ordering;
CREATE TABLE `ordering` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `discount` varchar(255) NOT NULL DEFAULT '1',
  `memo` varchar(500) NOT NULL DEFAULT '0',
  `total` varchar(255) NOT NULL DEFAULT '0',
  `end_date` date NOT NULL,
  `is_disabled` bit(1) NOT NULL,
  `is_paid` bit(1) NOT NULL,
  `start_date` date NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE ordering

-- BEGIN TABLE system_config
DROP TABLE IF EXISTS system_config;
CREATE TABLE `system_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `explanation` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `value` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE system_config

-- BEGIN TABLE user
DROP TABLE IF EXISTS user;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `account` varchar(255) NOT NULL DEFAULT '',
  `email` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- END TABLE user

DROP VIEW IF EXISTS booked_hotel_info;
CREATE OR REPLACE booked_hotel_info
AS
select `hotel_info`.`dt` AS `dt`,`hotel_info`.`id` AS `id`,`hotel_info`.`star` AS `star`,`hotel_info`.`locality` AS `locality`,`hotel_info`.`address` AS `address`,`hotel_info`.`json_file_id` AS `json_file_id`,`hotel_info`.`name` AS `name`,`hotel_info`.`room_type` AS `room_type`,`hotel_info`.`quantity` AS `quantity`,`hotel_info`.`price` AS `price`,`booked_room`.`quantity` AS `booked_quantity` from (`BookingHotelTest`.`hotel_info` left join `BookingHotelTest`.`booked_room` on(((`booked_room`.`dt` = `hotel_info`.`dt`) and (`booked_room`.`room_type` = `hotel_info`.`room_type`) and (`booked_room`.`hotel_id` = `hotel_info`.`json_file_id`))));

DROP VIEW IF EXISTS booked_room;
CREATE OR REPLACE booked_room
AS
select `BookingHotelTest`.`calendar_table`.`dt` AS `dt`,`booked`.`hotel_id` AS `hotel_id`,`booked`.`room_type` AS `room_type`,count(`booked`.`bookingId`) AS `quantity` from (`BookingHotelTest`.`calendar_table` left join (select `BookingHotelTest`.`booking`.`id` AS `bookingId`,`BookingHotelTest`.`booking`.`hotel_id` AS `hotel_id`,`BookingHotelTest`.`booking`.`hotel_room_id` AS `hotel_room_id`,`BookingHotelTest`.`ordering`.`start_date` AS `start_date`,`BookingHotelTest`.`ordering`.`end_date` AS `end_date`,`BookingHotelTest`.`booking`.`is_disabled` AS `is_disabled`,`BookingHotelTest`.`hotel_room`.`room_type` AS `room_type` from ((`BookingHotelTest`.`ordering` join `BookingHotelTest`.`booking` on(((`BookingHotelTest`.`ordering`.`id` = `BookingHotelTest`.`booking`.`order_id`) and (`BookingHotelTest`.`booking`.`is_disabled` <> TRUE) and (`BookingHotelTest`.`ordering`.`is_disabled` <> TRUE)))) join `BookingHotelTest`.`hotel_room` on((`BookingHotelTest`.`hotel_room`.`id` = `BookingHotelTest`.`booking`.`hotel_room_id`)))) `booked` on(((cast(`booked`.`start_date` as date) <= `BookingHotelTest`.`calendar_table`.`dt`) and (cast(`booked`.`end_date` as date) >= `BookingHotelTest`.`calendar_table`.`dt`)))) group by `BookingHotelTest`.`calendar_table`.`dt`,`booked`.`hotel_id`,`booked`.`room_type`;

DROP VIEW IF EXISTS hotel_info;
CREATE OR REPLACE hotel_info
AS
select `BookingHotelTestMikie`.`hotel`.`id` AS `id`,`BookingHotelTestMikie`.`hotel`.`star` AS `star`,`BookingHotelTestMikie`.`hotel`.`locality` AS `locality`,`BookingHotelTestMikie`.`hotel`.`address` AS `address`,`BookingHotelTestMikie`.`hotel`.`json_file_id` AS `json_file_id`,`BookingHotelTestMikie`.`hotel`.`name` AS `name`,sum((case when (`BookingHotelTestMikie`.`hotel_room`.`room_type` = 1) then `BookingHotelTestMikie`.`hotel_room`.`quantity` else 0 end)) AS `SingleRoom`,sum((case when (`BookingHotelTestMikie`.`hotel_room`.`room_type` = 1) then `BookingHotelTestMikie`.`hotel_room`.`price` else 0 end)) AS `SingleRoomPrice`,sum((case when (`BookingHotelTestMikie`.`hotel_room`.`room_type` = 2) then `BookingHotelTestMikie`.`hotel_room`.`quantity` else 0 end)) AS `DoubleRoom`,sum((case when (`BookingHotelTestMikie`.`hotel_room`.`room_type` = 2) then `BookingHotelTestMikie`.`hotel_room`.`price` else 0 end)) AS `DoubleRoomPrice`,sum((case when (`BookingHotelTestMikie`.`hotel_room`.`room_type` = 4) then `BookingHotelTestMikie`.`hotel_room`.`quantity` else 0 end)) AS `QuadRoom`,sum((case when (`BookingHotelTestMikie`.`hotel_room`.`room_type` = 4) then `BookingHotelTestMikie`.`hotel_room`.`price` else 0 end)) AS `QuadRoomPrice` from (`BookingHotelTestMikie`.`hotel` join `BookingHotelTestMikie`.`hotel_room` on((`BookingHotelTestMikie`.`hotel_room`.`hotel_id` = `BookingHotelTestMikie`.`hotel`.`json_file_id`))) where (`BookingHotelTestMikie`.`hotel_room`.`quantity` > 0) group by `BookingHotelTestMikie`.`hotel`.`id`,`BookingHotelTestMikie`.`hotel`.`star`,`BookingHotelTestMikie`.`hotel`.`locality`,`BookingHotelTestMikie`.`hotel`.`address`,`BookingHotelTestMikie`.`hotel`.`json_file_id`,`BookingHotelTestMikie`.`hotel`.`name`;

DROP VIEW IF EXISTS order_info;
CREATE OR REPLACE order_info
AS
select `booking_info`.`hotel_room_id` AS `hotel_room_id`,`BookingHotelTest`.`ordering`.`id` AS `id`,`BookingHotelTest`.`ordering`.`create_time` AS `create_time`,`BookingHotelTest`.`ordering`.`update_time` AS `update_time`,`BookingHotelTest`.`ordering`.`discount` AS `discount`,`BookingHotelTest`.`ordering`.`memo` AS `memo`,`BookingHotelTest`.`ordering`.`total` AS `total`,`BookingHotelTest`.`ordering`.`user_id` AS `user_id`,`BookingHotelTest`.`ordering`.`is_disabled` AS `is_disabled`,`BookingHotelTest`.`ordering`.`end_date` AS `end_date`,`BookingHotelTest`.`ordering`.`start_date` AS `start_date`,`BookingHotelTest`.`ordering`.`is_paid` AS `is_paid`,`BookingHotelTest`.`hotel`.`json_file_id` AS `json_file_id`,`BookingHotelTest`.`hotel`.`star` AS `star`,`BookingHotelTest`.`hotel`.`locality` AS `locality`,`BookingHotelTest`.`hotel`.`address` AS `address`,`BookingHotelTest`.`hotel`.`name` AS `name`,`booking_info`.`room_type` AS `room_type`,`booking_info`.`price` AS `price`,`booking_info`.`is_disabled` AS `booked_is_disabled`,count(`BookingHotelTest`.`ordering`.`id`) AS `booked_quantity` from ((`BookingHotelTest`.`ordering` join (select `BookingHotelTest`.`booking`.`id` AS `id`,`BookingHotelTest`.`booking`.`create_time` AS `create_time`,`BookingHotelTest`.`booking`.`update_time` AS `update_time`,`BookingHotelTest`.`booking`.`hotel_id` AS `hotel_id`,`BookingHotelTest`.`booking`.`hotel_room_id` AS `hotel_room_id`,`BookingHotelTest`.`booking`.`is_disabled` AS `is_disabled`,`BookingHotelTest`.`booking`.`order_id` AS `order_id`,`BookingHotelTest`.`hotel_room`.`room_type` AS `room_type`,`BookingHotelTest`.`hotel_room`.`price` AS `price` from (`BookingHotelTest`.`booking` join `BookingHotelTest`.`hotel_room` on((`BookingHotelTest`.`booking`.`hotel_room_id` = `BookingHotelTest`.`hotel_room`.`id`)))) `booking_info` on((`booking_info`.`order_id` = `BookingHotelTest`.`ordering`.`id`))) join `BookingHotelTest`.`hotel` on((`booking_info`.`hotel_id` = `BookingHotelTest`.`hotel`.`json_file_id`))) group by `booking_info`.`hotel_room_id`,`BookingHotelTest`.`ordering`.`id`,`BookingHotelTest`.`ordering`.`create_time`,`BookingHotelTest`.`ordering`.`update_time`,`BookingHotelTest`.`ordering`.`discount`,`BookingHotelTest`.`ordering`.`memo`,`BookingHotelTest`.`ordering`.`total`,`BookingHotelTest`.`ordering`.`user_id`,`BookingHotelTest`.`ordering`.`is_disabled`,`BookingHotelTest`.`ordering`.`end_date`,`BookingHotelTest`.`ordering`.`start_date`,`BookingHotelTest`.`ordering`.`is_paid`,`BookingHotelTest`.`hotel`.`json_file_id`,`BookingHotelTest`.`hotel`.`star`,`BookingHotelTest`.`hotel`.`locality`,`BookingHotelTest`.`hotel`.`address`,`BookingHotelTest`.`hotel`.`name`,`booking_info`.`room_type`,`booking_info`.`price`,`booking_info`.`is_disabled`;

