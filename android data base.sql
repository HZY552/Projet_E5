-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2023 年 06 月 18 日 14:51
-- 伺服器版本： 10.4.22-MariaDB
-- PHP 版本： 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫: `projet_e5`
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

-- --------------------------------------------------------

--
-- 資料表結構 `doctors`
--

CREATE TABLE `doctors` (
  `id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `specialty` int(11) DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `office_address` varchar(255) DEFAULT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `doctors`
--

INSERT INTO `doctors` (`id`, `first_name`, `last_name`, `specialty`, `phone_number`, `email`, `office_address`, `password`) VALUES
(86, 'raffi', 'gtk', 7, '069581423', 'raffi@gmail.com', '733 rue nsl spk 75013 paris', '$2y$10$FxTG9ONUPNxsw/ZjcR.AnOpunfXwKlopi5EzNXgoShJOVGqmqlU9S'),
(87, 'rouman', 'jj', 1, '06 95 86 72 74', 'houzeyu77@gmail.com', '733 rue nsl spk 75013 paris', '$2y$10$K29C4A1HeUFAg/00fKsdaee9BW82MLvSBa7rai.nyl3bNl/Lm3dDW');

-- --------------------------------------------------------

--
-- 資料表結構 `doctor_availabilities`
--

CREATE TABLE `doctor_availabilities` (
  `id` int(11) NOT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `available_from` datetime DEFAULT NULL,
  `available_to` datetime DEFAULT NULL,
  `patient_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `doctor_availabilities`
--

INSERT INTO `doctor_availabilities` (`id`, `doctor_id`, `available_from`, `available_to`, `patient_id`) VALUES
(28, 86, '2023-06-05 14:00:00', '2023-06-05 15:00:00', 37),
(29, 86, '2023-06-12 14:00:00', '2023-06-12 15:00:00', 37),
(30, 86, '2023-06-15 14:00:00', '2023-06-15 15:00:00', 37),
(31, 87, '2023-06-05 16:00:00', '2023-06-05 17:00:00', 37),
(32, 86, '2023-06-05 12:21:12', '2023-06-05 12:21:12', 37);

-- --------------------------------------------------------

--
-- 資料表結構 `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `title` text NOT NULL,
  `contenu` text NOT NULL,
  `id_patient` int(11) NOT NULL,
  `id_doctor` int(11) NOT NULL,
  `date_public` datetime NOT NULL,
  `etat` tinyint(1) NOT NULL DEFAULT 0,
  `last_review` datetime NOT NULL,
  `sender` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `message`
--

INSERT INTO `message` (`id`, `title`, `contenu`, `id_patient`, `id_doctor`, `date_public`, `etat`, `last_review`, `sender`) VALUES
(12, 'Test', 'test', 37, 86, '2023-06-01 18:05:59', 1, '2023-06-01 18:05:59', 0),
(13, 'zeyu', 'zeyu', 37, 86, '2023-06-01 18:07:12', 1, '2023-06-01 18:07:12', 0),
(14, '12345678', 'ayan', 38, 86, '2023-06-01 18:07:27', 0, '2023-06-01 18:07:27', 0),
(15, 'Rendez-vous confirmé', '2023-06-05 12', 37, 86, '2023-06-07 14:48:56', 0, '2023-06-07 14:48:56', 0),
(16, 'Rendez-vous confirmé', '2023-06-05 12', 37, 86, '2023-06-07 14:49:37', 0, '2023-06-07 14:49:37', 0),
(17, 'Rendez-vous confirmé', '2023-06-05 16', 37, 87, '2023-06-07 14:55:41', 1, '2023-06-07 14:55:41', 0);

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
(37, 'HOU', 'ZEYU', '1977-11-11', 'M', '06 95 86 72 74', 'houzeyu7@gmail.com', '733 rue nsl spk 75013 paris', '$2y$10$Sgm05akuNAA6/d7TmqeSXuUfqH6XJMoVdQZ2X0efRmqMWI5hoeUMm'),
(38, 'ayan', 'wouchunchong', '1977-11-11', 'M', '98784451234', 'ayan@gmail.com', '733 rue nsl spk 75013 paris', '$2y$10$zdR8uS64bz/Dqrwb.XNJOu.rf0q0Ayksun60Nh4HRKQTwnUd/VL3G');

-- --------------------------------------------------------

--
-- 資料表結構 `specialty`
--

CREATE TABLE `specialty` (
  `id` int(11) NOT NULL,
  `specialty` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `specialty`
--

INSERT INTO `specialty` (`id`, `specialty`) VALUES
(1, 'Médecin généraliste'),
(2, 'Cardiologue'),
(3, 'Dermatologue'),
(4, 'Gynécologue'),
(5, 'Ophtalmologue'),
(6, 'Pédiatre'),
(7, 'Chirurgien orthopédiste'),
(8, 'Psychiatre'),
(9, 'Radiologue'),
(10, 'Oto-rhino-laryngologiste (ORL)'),
(11, 'null');

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
  ADD PRIMARY KEY (`id`),
  ADD KEY `specialty` (`specialty`);

--
-- 資料表索引 `doctor_availabilities`
--
ALTER TABLE `doctor_availabilities`
  ADD PRIMARY KEY (`id`),
  ADD KEY `doctor_id` (`doctor_id`),
  ADD KEY `patient_id` (`patient_id`);

--
-- 資料表索引 `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_doctor` (`id_doctor`),
  ADD KEY `id_patient` (`id_patient`);

--
-- 資料表索引 `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `specialty`
--
ALTER TABLE `specialty`
  ADD PRIMARY KEY (`id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `appointments`
--
ALTER TABLE `appointments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `doctors`
--
ALTER TABLE `doctors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=88;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `doctor_availabilities`
--
ALTER TABLE `doctor_availabilities`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `message`
--
ALTER TABLE `message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `specialty`
--
ALTER TABLE `specialty`
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
-- 資料表的限制式 `doctors`
--
ALTER TABLE `doctors`
  ADD CONSTRAINT `doctors_ibfk_1` FOREIGN KEY (`specialty`) REFERENCES `specialty` (`id`);

--
-- 資料表的限制式 `doctor_availabilities`
--
ALTER TABLE `doctor_availabilities`
  ADD CONSTRAINT `doctor_availabilities_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  ADD CONSTRAINT `doctor_availabilities_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`);

--
-- 資料表的限制式 `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `message_ibfk_1` FOREIGN KEY (`id_doctor`) REFERENCES `doctors` (`id`),
  ADD CONSTRAINT `message_ibfk_2` FOREIGN KEY (`id_patient`) REFERENCES `patients` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
