-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: May 01, 2025 at 04:36 PM
-- Server version: 8.0.35
-- PHP Version: 8.2.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fitness_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `category_id` int NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `gender` enum('Nam','Nữ') NOT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`category_id`, `category_name`, `gender`, `image_url`) VALUES
(1, 'Giảm Cân ', 'Nữ', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVLZFd3ENrt22CNWWgYUpXQ4svF47SWAZyPA&s'),
(2, 'Giảm Cân', 'Nam', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRP_vI3WDBKK--cihgGFUE0CF5jfmZJSWXYHiS4cuBb6fYXRKGFj_pqFFoa2NhfvGPNZMc&usqp=CAU'),
(3, 'Cardio', 'Nữ', 'https://file.hstatic.net/1000308068/file/bai-tap-cardio-tai-nha-cho-nu-hieu-qua-nhat-npt_0fbb1997819f42f282dc53a63c7d68fa_grande.jpg'),
(4, 'Cardio', 'Nam', 'https://thethaonamviet.vn/wp-content/uploads/2022/04/2-bai-tap-cardio-cho-nam.png'),
(5, 'Siết Cơ', 'Nữ', 'https://ptsportvietnam.vn/assets/tin-tuc/2023_06/siet-co-bung-la-gi.jpg'),
(6, 'Siết Cơ', 'Nam', 'https://abcsport.com.vn/image/catalog/2023/T02/siet-co-la-gi-1.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `exercise`
--

CREATE TABLE `exercise` (
  `id` int NOT NULL,
  `exercise_name` varchar(255) NOT NULL,
  `exercise_level_id` int NOT NULL,
  `exercise_category_id` int NOT NULL,
  `gender` enum('nam','nữ') NOT NULL,
  `note` text,
  `description` text,
  `day` int NOT NULL,
  `link` varchar(500) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `muscle_group` varchar(155) NOT NULL,
  `unlock_order` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `exercise`
--

INSERT INTO `exercise` (`id`, `exercise_name`, `exercise_level_id`, `exercise_category_id`, `gender`, `note`, `description`, `day`, `link`, `image_url`, `muscle_group`, `unlock_order`) VALUES
(1, 'Full Body', 1, 2, 'nam', 'Lưu ý: Tập đều đặn mỗi ngày.', 'Bài tập toàn thân giúp giảm cân.', 1, 'https://www.youtube.com/watch?v=2pLT-olgUJs', 'https://kickfit.com.vn/wp-content/uploads/2023/11/Lich-tap-Gym-cho-nam-Full-body-5-buoi-1-tuan.jpg', 'Full Body', 1),
(2, 'Squats', 1, 2, 'nam', 'Lưu ý: Tập đúng kỹ thuật để tránh chấn thương.', 'Squats giúp phát triển cơ mông và đùi.', 2, 'https://www.youtube.com/watch?v=aclHkVaku9U', 'https://cali.vn/storage/app/media/Editors/62e778c7392eftap-squat-nam-3.jpg', 'Mông, Đùi', 2),
(3, 'Push-ups', 1, 2, 'nam', 'Lưu ý: Giữ thẳng người khi tập.', 'Bài tập đẩy tay giúp phát triển cơ ngực và tay.', 3, 'https://www.youtube.com/watch?v=_l3ySVKYVJ8', 'https://thethaonamviet.vn/wp-content/uploads/2021/10/su-dung-tap-chong-day-perfect-push-up.jpg', 'Ngực, Tay', 3),
(4, 'Plank', 1, 2, 'nam', 'Lưu ý: Giữ thẳng người khi tập.', 'Bài tập giữ cơ bụng để giảm mỡ.', 4, 'https://www.youtube.com/watch?v=pSHjTRCQxIw', 'https://file.hstatic.net/200000783295/file/bai-tap-plank-thap_1598601c4f8242998f8bd0808eedce84_1024x1024.jpg', 'Bụng', 4),
(5, 'Mountain Climbers', 1, 2, 'nam', 'Lưu ý: Tập nhanh và đều.', 'Bài tập chạy tại chỗ giúp đốt calo nhanh.', 5, 'https://www.youtube.com/watch?v=nmwgirgXLYM', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxGkj67P4LOn9q8jY4ccA9CeDfNJDjV6LWbzB7KKWgdJR0EwfvyVo9JS5avgBtOr87F_o&usqp=CAU', 'Body', 5),
(6, '\'Full Body', 4, 1, 'nữ', 'Lưu ý: Tập đều đặn mỗi ngày.', 'Bài tập toàn thân giúp giảm cân.', 1, 'https://www.youtube.com/watch?v=2pLT-olgUJs', 'https://cdn.unityfitness.vn/2024/04/co-nen-tap-full-body-moi-ngay-4.jpg', 'Body', 1),
(7, 'Squats', 4, 1, 'nữ', 'Lưu ý: Tập đúng kỹ thuật để tránh chấn thương.', 'Squats giúp phát triển cơ mông và đùi.', 2, 'https://www.youtube.com/watch?v=aclHkVaku9U', 'https://cdn.tgdd.vn/Files/2023/05/09/1529280/squat-la-gi-loi-ich-suc-khoe-khi-tap-squat-dung-cach-202305092356359690.jpg', 'Mông, Đùi', 2),
(8, 'Push-ups', 4, 2, 'nữ', 'Lưu ý: Giữ thẳng người khi tập.', 'Bài tập đẩy tay giúp phát triển cơ ngực và tay.', 3, 'https://www.youtube.com/watch?v=_l3ySVKYVJ8', 'https://opstudiohk.com/wp-content/uploads/2021/02/Guide-to-Push-ups-Main-Image.jpg', 'Ngực, Tay', 3),
(9, 'Plank', 4, 2, 'nữ', 'Lưu ý: Giữ thẳng người khi tập.', 'Bài tập giữ cơ bụng để giảm mỡ.', 4, 'https://www.youtube.com/watch?v=pSHjTRCQxIw', 'https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2023/12/12/co-bung-san-chac-1-1702346745847631805980.jpg', 'Bụng', 4),
(10, 'Mountain Climbers', 4, 2, 'nữ', 'Lưu ý: Tập nhanh và đều.', 'Bài tập chạy tại chỗ giúp đốt calo nhanh.', 5, 'https://www.youtube.com/watch?v=nmwgirgXLYM', 'https://mekongsport.com/uploads/2023/10/5-bai-tap-moutain-climber.jpg', 'Body', 5),
(11, 'Cardio HIIT', 2, 2, 'nam', 'Thực hiện 3 hiệp, nghỉ 30s giữa hiệp.', 'Bài cardio cường độ cao đốt mỡ nhanh chóng.', 1, 'https://www.youtube.com/watch?v=ml6cT4AZdqI', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAUF3p_hBVzP-DMWkEmpKD7zckHWnIIdH8-Q&s', 'Body', 1),
(12, 'Burpees', 2, 2, 'nam', 'Thực hiện 4 hiệp, mỗi hiệp 10 lần.', 'Tăng nhịp tim, đốt cháy calo toàn thân.', 2, 'https://www.youtube.com/watch?v=TU8QYVW0gDU', 'https://bizweb.dktcdn.net/100/517/390/files/25-bai-tap-bung-giam-mo-cho-nam-tai-nha-vo-cung-donhiit-burpees.jpg?v=1724223603820', 'Body', 2),
(13, 'Mountain Climbers', 2, 2, 'nam', 'Thực hiện 3 hiệp x 30 giây.', 'Tập trung vào cơ bụng và chân', 3, 'https://i.imgur.com/zmtnclimbers1.jpg', 'https://nuedu.vn/cdn/upload/images/moutain-climbers-3.jpg', 'Bụng, Chân', 3),
(14, 'Jump Squats', 2, 2, 'nam', '3 hiệp, 12 lần.', 'Kết hợp cardio và sức mạnh chân.', 4, 'https://www.youtube.com/watch?v=U4s4mEQ5VqU', 'https://manup.vn/wp-content/uploads/2024/08/bai-tap-mong-chan-cho-nam-tai-phong-gym-15.jpg', 'Chân', 4),
(15, 'Push Up Variation', 2, 2, 'nam', '4 hiệp x 15 lần.', 'Tăng cơ ngực và tay.', 5, 'https://www.youtube.com/watch?v=IODxDxX7oi4', 'https://i.ytimg.com/vi/-0eYiItN2D8/hqdefault.jpg', 'Ngực, Tay', 5);

-- --------------------------------------------------------

--
-- Table structure for table `level`
--

CREATE TABLE `level` (
  `id` int NOT NULL,
  `level_name` varchar(255) NOT NULL,
  `description` text,
  `image_url` varchar(500) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `level`
--

INSERT INTO `level` (`id`, `level_name`, `description`, `image_url`, `gender`) VALUES
(1, 'Cơ bản', 'Dành cho người mới bắt đầu.', 'https://hdfitness.vn/wp-content/uploads/2022/03/cac-bai-tap-gym-cho-nam-17-min-1024x614.jpg', 'nam'),
(2, 'Trung cấp', 'Dành cho người đã có kinh nghiệm luyện tập.', 'https://cdn.unityfitness.vn/2024/04/cac-bai-tap-gym-cho-nam-4.webp', 'nam'),
(3, 'Nâng cao', 'Dành cho người tập luyện chuyên sâu.', 'https://gymaster.vn/wp-content/uploads/2023/02/lich-tap-gym-6-ngay-1-tuan-cho-nam-gymaster.vn_.webp', 'nam'),
(4, 'Cơ bản', 'Dành cho người mới bắt đầu.', 'https://cdn.unityfitness.vn/2024/04/tap-gym-nu-1.jpg', 'nữ'),
(5, 'Trung cấp', 'Dành cho người đã có kinh nghiệm luyện tập.', 'https://fitcenter.vn/wp-content/uploads/2024/08/bai-tap-lung.jpg', 'nữ'),
(6, 'Nâng cao', 'Dành cho người tập luyện chuyên sâu.', 'https://fitcenter.vn/wp-content/uploads/2024/08/bai-tap-cardio.jpg', 'nữ');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `fullname` varchar(100) NOT NULL,
  `password` varchar(155) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `fullname`, `password`) VALUES
(1, 'kimngan12', 'kimngan@gmail.com', 'kimngan', '$2y$10$W.pPlQJmGTw7ShjVxa28ouPHIQ8WYU3Js0yOQ9urOpT1/qvkCaXBy'),
(2, '', '', '', '$2y$10$H0euIvmgRIld6gMQxsZYrOUNqLrrtaoRVi1QYCBMb6ULDtPdAqH.W'),
(3, 'kimngan23', 'kimngannguyenanh@gmail.com', 'Nguyen Anh Kim Ngan', '$2y$10$cCUG5khLiTTYlvl9daAjSOVkoBusY7AgC/OxqtegWMlANyywxf5ki'),
(4, 'kimngan24', 'kimngan2@gmail.com', 'nguyen anh ki ngan', '$2y$10$QRTLzGZHFc9CJqWjyEIGO.fVhejYIjpFh5fJF3NNypmIyUWaUjBTW'),
(7, 'kimngan45', 'kimngan5@gmail.com', 'kimngan45', '$2y$10$5UNqgaFmyUVS5orRqa9m2e7zQkpq7k77unjHiP6.FncxFShYqiVYS'),
(8, 'kimngan34', 'kimngan6@gmail.com', 'kimngan34', '$2y$10$WEWc9WhZY794a2eheqOSXeB5aC/clGHoG.qiwKYUTAjS//2p9o7uO');

-- --------------------------------------------------------

--
-- Table structure for table `user_data`
--

CREATE TABLE `user_data` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `weight` decimal(5,2) NOT NULL,
  `height` int NOT NULL,
  `age` int NOT NULL,
  `medical_condition` varchar(255) DEFAULT NULL,
  `workout_level` varchar(50) DEFAULT NULL,
  `improvement_goal` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user_data`
--

INSERT INTO `user_data` (`id`, `user_id`, `weight`, `height`, `age`, `medical_condition`, `workout_level`, `improvement_goal`) VALUES
(1, 7, 40.00, 162, 21, 'khong co', 'Tập luyện nhẹ nhàng 1-2 lần/tuần', 'mo bung'),
(2, 8, 40.00, 162, 21, 'khong', 'Tập luyện vừa phải 3-4 lần/tuần', 'Giảm cân');

-- --------------------------------------------------------

--
-- Table structure for table `user_exercise_progress`
--

CREATE TABLE `user_exercise_progress` (
  `id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `exercise_id` int DEFAULT NULL,
  `is_completed` tinyint(1) DEFAULT '0',
  `completed_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `exercise`
--
ALTER TABLE `exercise`
  ADD PRIMARY KEY (`id`),
  ADD KEY `exercise_level_id` (`exercise_level_id`),
  ADD KEY `exercise_category_id` (`exercise_category_id`);

--
-- Indexes for table `level`
--
ALTER TABLE `level`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_data`
--
ALTER TABLE `user_data`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `user_exercise_progress`
--
ALTER TABLE `user_exercise_progress`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `exercise_id` (`exercise_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `category_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `exercise`
--
ALTER TABLE `exercise`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `level`
--
ALTER TABLE `level`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `user_data`
--
ALTER TABLE `user_data`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user_exercise_progress`
--
ALTER TABLE `user_exercise_progress`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `exercise`
--
ALTER TABLE `exercise`
  ADD CONSTRAINT `exercise_ibfk_1` FOREIGN KEY (`exercise_level_id`) REFERENCES `level` (`id`),
  ADD CONSTRAINT `exercise_ibfk_2` FOREIGN KEY (`exercise_category_id`) REFERENCES `category` (`category_id`);

--
-- Constraints for table `user_data`
--
ALTER TABLE `user_data`
  ADD CONSTRAINT `user_data_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `user_exercise_progress`
--
ALTER TABLE `user_exercise_progress`
  ADD CONSTRAINT `user_exercise_progress_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `user_exercise_progress_ibfk_2` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
