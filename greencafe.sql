-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 14, 2022 at 06:42 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `greencafe`
--
CREATE DATABASE IF NOT EXISTS `greencafe` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `greencafe`;

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

CREATE TABLE `log` (
  `id_log` int(11) NOT NULL,
  `id_pegawai` int(11) NOT NULL,
  `aktivitas` text NOT NULL,
  `tanggal` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `id_login` int(11) NOT NULL,
  `id_pegawai` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`id_login`, `id_pegawai`, `username`, `password`) VALUES
(1, 1, 'admin', 'admin'),
(3, 2, 'manager', 'manager'),
(4, 3, 'kasir', 'kasir');

-- --------------------------------------------------------

--
-- Table structure for table `meja`
--

CREATE TABLE `meja` (
  `id_meja` int(11) NOT NULL,
  `no_meja` varchar(3) NOT NULL,
  `status_meja` enum('TERSEDIA','DIGUNAKAN') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `meja`
--

INSERT INTO `meja` (`id_meja`, `no_meja`, `status_meja`) VALUES
(1, 'M1', 'TERSEDIA'),
(2, 'M2', 'TERSEDIA'),
(3, 'M3', 'TERSEDIA'),
(4, 'M4', 'TERSEDIA'),
(5, 'M5', 'TERSEDIA'),
(6, 'M6', 'TERSEDIA'),
(7, 'M7', 'TERSEDIA'),
(8, 'M8', 'TERSEDIA'),
(9, 'M9', 'TERSEDIA'),
(10, 'M10', 'TERSEDIA');

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `id_menu` int(11) NOT NULL,
  `nama_menu` varchar(150) NOT NULL,
  `jenis_menu` enum('MAKANAN','MINUMAN') NOT NULL,
  `harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id_menu`, `nama_menu`, `jenis_menu`, `harga`) VALUES
(1, 'Nasi Goreng', 'MAKANAN', 20000),
(2, 'Mie Goreng', 'MAKANAN', 15000),
(3, 'Kentang Goreng', 'MAKANAN', 10000),
(4, 'Kopi Hitam', 'MINUMAN', 15000),
(5, 'White Coffe', 'MAKANAN', 15000),
(6, 'Espresso', 'MINUMAN', 20000);

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `id_order` int(11) NOT NULL,
  `id_menu` int(11) NOT NULL,
  `no_transaksi` varchar(9) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `subtotal` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `pegawai`
--

CREATE TABLE `pegawai` (
  `id_pegawai` int(11) NOT NULL,
  `nama_pegawai` varchar(100) NOT NULL,
  `jenkel` enum('Laki-Laki','Perempuan') NOT NULL,
  `alamat` text NOT NULL,
  `telepon` varchar(30) NOT NULL,
  `jabatan` enum('ADMIN','MANAGER','KASIR') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pegawai`
--

INSERT INTO `pegawai` (`id_pegawai`, `nama_pegawai`, `jenkel`, `alamat`, `telepon`, `jabatan`) VALUES
(1, 'Bill Gates', 'Laki-Laki', 'Jl. Gates No.1', '081234567890', 'ADMIN'),
(2, 'Valencia', 'Perempuan', 'Jl. Manager no.2', '082233334444', 'MANAGER'),
(3, 'Asep Suherman', 'Laki-Laki', 'Jl. Kejayaan no.3', '082211110000', 'KASIR');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `id_pegawai` int(11) NOT NULL,
  `no_transaksi` varchar(11) NOT NULL,
  `total_transaksi` int(11) NOT NULL,
  `total_tunai` int(11) NOT NULL,
  `id_meja` int(3) NOT NULL,
  `tgl_transaksi` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Triggers `transaksi`
--
DELIMITER $$
CREATE TRIGGER `t_transaksi_gunakan_meja` AFTER INSERT ON `transaksi` FOR EACH ROW UPDATE meja SET meja.status_meja = "DIGUNAKAN" WHERE NEW.id_meja = meja.id_meja
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_akun`
-- (See below for the actual view)
--
CREATE TABLE `view_akun` (
`id_pegawai` int(11)
,`username` varchar(32)
,`password` varchar(32)
,`nama_pegawai` varchar(100)
,`jenkel` enum('Laki-Laki','Perempuan')
,`alamat` text
,`telepon` varchar(30)
,`jabatan` enum('ADMIN','MANAGER','KASIR')
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_log`
-- (See below for the actual view)
--
CREATE TABLE `view_log` (
`id_pegawai` int(11)
,`nama_pegawai` varchar(100)
,`jabatan` enum('ADMIN','MANAGER','KASIR')
,`aksi` text
,`tanggal` timestamp
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_transaksi`
-- (See below for the actual view)
--
CREATE TABLE `view_transaksi` (
);

-- --------------------------------------------------------

--
-- Structure for view `view_akun`
--
DROP TABLE IF EXISTS `view_akun`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_akun`  AS SELECT `login`.`id_pegawai` AS `id_pegawai`, `login`.`username` AS `username`, `login`.`password` AS `password`, `pegawai`.`nama_pegawai` AS `nama_pegawai`, `pegawai`.`jenkel` AS `jenkel`, `pegawai`.`alamat` AS `alamat`, `pegawai`.`telepon` AS `telepon`, `pegawai`.`jabatan` AS `jabatan` FROM (`login` join `pegawai` on(`login`.`id_pegawai` = `pegawai`.`id_pegawai`))  ;

-- --------------------------------------------------------

--
-- Structure for view `view_log`
--
DROP TABLE IF EXISTS `view_log`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_log`  AS SELECT `log`.`id_pegawai` AS `id_pegawai`, `pegawai`.`nama_pegawai` AS `nama_pegawai`, `pegawai`.`jabatan` AS `jabatan`, `log`.`aktivitas` AS `aksi`, `log`.`tanggal` AS `tanggal` FROM (`log` join `pegawai` on(`log`.`id_pegawai` = `pegawai`.`id_pegawai`)) ORDER BY `log`.`tanggal` AS `DESCdesc` ASC  ;

-- --------------------------------------------------------

--
-- Structure for view `view_transaksi`
--
DROP TABLE IF EXISTS `view_transaksi`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_transaksi`  AS SELECT `transaksi`.`id_transaksi` AS `id_transaksi`, `transaksi`.`id_pegawai` AS `id_pegawai`, `pegawai`.`nama_pegawai` AS `nama_pegawai`, `transaksi`.`no_transaksi` AS `no_transaksi`, `transaksi`.`total_transaksi` AS `total_transaksi`, `transaksi`.`total_tunai` AS `total_tunai`, `meja`.`nomor_meja` AS `nomor_meja`, `transaksi`.`tgl_transaksi` AS `tgl_transaksi` FROM ((`transaksi` join `pegawai` on(`transaksi`.`id_pegawai` = `pegawai`.`id_pegawai`)) join `meja` on(`transaksi`.`id_meja` = `meja`.`id_meja`))  ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `log`
--
ALTER TABLE `log`
  ADD PRIMARY KEY (`id_log`),
  ADD KEY `id_pegawai` (`id_pegawai`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id_login`),
  ADD KEY `id_pegawai` (`id_pegawai`),
  ADD KEY `id_pegawai_2` (`id_pegawai`);

--
-- Indexes for table `meja`
--
ALTER TABLE `meja`
  ADD PRIMARY KEY (`id_meja`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id_menu`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`id_order`),
  ADD KEY `id_menu` (`id_menu`),
  ADD KEY `no_transaksi` (`no_transaksi`);

--
-- Indexes for table `pegawai`
--
ALTER TABLE `pegawai`
  ADD PRIMARY KEY (`id_pegawai`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD UNIQUE KEY `no_transaksi` (`no_transaksi`),
  ADD KEY `id_pegawai` (`id_pegawai`),
  ADD KEY `id_meja` (`id_meja`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `log`
--
ALTER TABLE `log`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `id_login` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `meja`
--
ALTER TABLE `meja`
  MODIFY `id_meja` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id_menu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `id_order` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `pegawai`
--
ALTER TABLE `pegawai`
  MODIFY `id_pegawai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `log_ibfk_1` FOREIGN KEY (`id_pegawai`) REFERENCES `pegawai` (`id_pegawai`) ON DELETE CASCADE;

--
-- Constraints for table `login`
--
ALTER TABLE `login`
  ADD CONSTRAINT `login_ibfk_1` FOREIGN KEY (`id_pegawai`) REFERENCES `pegawai` (`id_pegawai`) ON DELETE CASCADE;

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`id_menu`) REFERENCES `menu` (`id_menu`),
  ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`no_transaksi`) REFERENCES `transaksi` (`no_transaksi`) ON DELETE CASCADE;

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`id_meja`) REFERENCES `meja` (`id_meja`),
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`id_pegawai`) REFERENCES `pegawai` (`id_pegawai`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
