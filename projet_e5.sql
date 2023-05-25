-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- 主機： localhost
-- 產生時間： 2023 年 04 月 04 日 01:29
-- 伺服器版本： 10.4.24-MariaDB
-- PHP 版本： 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `projet_e5`
--

-- --------------------------------------------------------

--
-- 資料表結構 `appointments`
--

CREATE TABLE `appointments` (
  `id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `doctor_availability_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `appointments`
--

INSERT INTO `appointments` (`id`, `patient_id`, `doctor_availability_id`) VALUES
(1, 11, 1),
(2, 3, 2),
(3, 10, 3),
(4, 3, 8);

-- --------------------------------------------------------

--
-- 資料表結構 `doctors`
--

CREATE TABLE `doctors` (
  `id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `specialty` varchar(100) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `office_address` varchar(255) DEFAULT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `doctors`
--

INSERT INTO `doctors` (`id`, `first_name`, `last_name`, `specialty`, `phone_number`, `email`, `office_address`, `password`) VALUES
(1, 'John', 'Smith', 'Cardiology', '555-123-4567', 'johnsmith@example.com', '123 Main St', 'Jiojio000608.'),
(2, 'Jane', 'Doe', 'Dermatology', '555-234-5678', 'janedoe@example.com', '456 Elm St', 'Jiojio000608.'),
(3, 'Michael', 'Johnson', 'Neurology', '555-345-6789', 'michaeljohnson@example.com', '789 Oak St', 'Jiojio000608.'),
(4, 'Emily', 'Davis', 'Orthopedics', '555-456-7890', 'emilydavis@example.com', '321 Pine St', 'Jiojio000608.'),
(5, 'David', 'Williams', 'Pediatrics', '555-567-8901', 'davidwilliams@example.com', '654 Maple St', 'Jiojio000608.'),
(6, 'Sophia', 'Brown', 'Radiology', '000122333', 'sophiabrown@example.com', '3333333', 'Jiojio000608.'),
(7, 'Daniel', 'Jones', 'Oncology', '555-789-0123', 'danieljones@example.com', '147 Cedar St', 'Jiojio000608.'),
(8, 'Isabella', 'Garcia', 'Psychiatry', '555-890-1234', 'isabellagarcia@example.com', '258 Willow St', 'Jiojio000608.'),
(9, 'William', 'Miller', 'Urology', '555-901-2345', 'williammiller@example.com', '369 Spruce St', 'Jiojio000608.'),
(10, 'Olivia', 'Wilson', 'Gastroenterology', '555-012-3456', 'oliviawilson@example.com', '480 Aspen St', 'Jiojio000104.');

-- --------------------------------------------------------

--
-- 資料表結構 `doctor_availabilities`
--

CREATE TABLE `doctor_availabilities` (
  `id` int(11) NOT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `available_from` datetime DEFAULT NULL,
  `available_to` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `doctor_availabilities`
--

INSERT INTO `doctor_availabilities` (`id`, `doctor_id`, `available_from`, `available_to`) VALUES
(1, 1, '2023-04-01 09:00:00', '2023-04-01 12:00:00'),
(2, 1, '2023-04-02 14:00:00', '2023-04-02 18:00:00'),
(3, 2, '2023-04-03 10:00:00', '2023-04-03 15:00:00'),
(4, 2, '2023-04-04 09:00:00', '2023-04-04 12:00:00'),
(5, 3, '2023-04-05 14:00:00', '2023-04-05 17:00:00'),
(6, 3, '2023-04-06 10:00:00', '2023-04-06 13:00:00'),
(7, 4, '2023-04-07 15:00:00', '2023-04-07 19:00:00'),
(8, 4, '2023-04-08 09:00:00', '2023-04-08 11:00:00'),
(9, 5, '2023-04-09 13:00:00', '2023-04-09 17:00:00'),
(10, 5, '2023-04-10 10:00:00', '2023-04-10 14:00:00');

-- --------------------------------------------------------

--
-- 資料表結構 `patients`
--

CREATE TABLE `patients` (
  `id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `birthdate` date NOT NULL,
  `gender` enum('M','F') NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `patients`
--

INSERT INTO `patients` (`id`, `first_name`, `last_name`, `birthdate`, `gender`, `phone_number`, `email`, `address`, `password`) VALUES
(1, 'John', 'Doe', '1980-01-01', 'M', '555-123-4567', 'john.doe@example.com', '123 Main St', 'Jiojio000608.'),
(2, 'Jane', 'Smith', '1985-05-15', 'F', '555-987-6543', 'jane.smith@example.com', '456 Oak St', 'Jiojio000608.'),
(3, 'Michael', 'Johnson', '1975-10-30', 'M', '555-555-5555', 'michael.johnson@example.com', '789 Elm St', 'Jiojio000608.'),
(4, 'Emily', 'Brown', '1990-04-25', 'F', '555-777-7777', 'emily.brown@example.com', '321 Birch St', 'Jiojio000608.'),
(5, 'David', 'Jones', '1982-08-18', 'M', '555-888-8888', 'david.jones@example.com', '654 Maple St', 'Jiojio000608.'),
(6, 'Sara', 'Miller', '1989-12-24', 'F', '555-666-6666', 'sara.miller@example.com', '987 Cedar St', 'Jiojio000608.'),
(7, 'James', 'Wilson', '1978-06-12', 'M', '555-444-4444', 'james.wilson@example.com', '159 Willow St', 'Jiojio000608.'),
(8, 'Sophia', 'Taylor', '1995-03-10', 'F', '555-333-3333', 'sophia.taylor@example.com', '963 Pine St', 'Jiojio000608.'),
(9, 'Daniel', 'Anderson', '1998-07-05', 'M', '555-222-2222', 'daniel.anderson@example.com', '741 Spruce St', 'Jiojio000608.'),
(10, 'Mia', 'Thomas', '1992-11-20', 'F', '555-111-1111', 'mia.thomas@example.com', '528 Beech St', 'Jiojio000608.'),
(11, 'Zeyudsvds', 'Hou', '1998-05-12', 'M', '0695867276', 'houzeyu7@gmail.com', '33 rue louise 75013 paris', 'Jiojio000104.');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `doctor_availability_id` (`doctor_availability_id`);

--
-- 資料表索引 `doctors`
--
ALTER TABLE `doctors`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `doctor_availabilities`
--
ALTER TABLE `doctor_availabilities`
  ADD PRIMARY KEY (`id`),
  ADD KEY `doctor_id` (`doctor_id`);

--
-- 資料表索引 `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `appointments`
--
ALTER TABLE `appointments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `doctors`
--
ALTER TABLE `doctors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `doctor_availabilities`
--
ALTER TABLE `doctor_availabilities`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `appointments`
--
ALTER TABLE `appointments`
  ADD CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  ADD CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`doctor_availability_id`) REFERENCES `doctor_availabilities` (`id`);

--
-- 資料表的限制式 `doctor_availabilities`
--
ALTER TABLE `doctor_availabilities`
  ADD CONSTRAINT `doctor_availabilities_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
