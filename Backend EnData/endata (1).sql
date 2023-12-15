-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 15 Jun 2023 pada 09.14
-- Versi server: 10.4.27-MariaDB
-- Versi PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `endata`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `files`
--

CREATE TABLE `files` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_user_send` int(11) NOT NULL,
  `nama_file` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL,
  `is_send` int(11) NOT NULL,
  `created` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `files`
--

INSERT INTO `files` (`id`, `id_user`, `id_user_send`, `nama_file`, `description`, `token`, `path`, `is_send`, `created`) VALUES
(39, 3, 0, 'data faiz amrulloh.pdf', '123', '6378781e3f5dd50af45bfa43ef0179ec', '1686298870549-data-faiz-amrulloh.pdf', 0, '2023-06-14'),
(40, 3, 1, 'data faiz amrulloh.pdf', '123', '6378781e3f5dd50af45bfa43ef0179ec', '1686298870549-data-faiz-amrulloh.pdf', 1, '2023-06-14'),
(41, 4, 0, 'coba.jpg', 'coba file 1', 'df2654bfdbb520ade08edffb3e0b1df8', '1686812688006-coba.jpg', 0, '2023-06-15'),
(42, 4, 0, 'coba_1.jpg', 'coba', 'df2654bfdbb520ade08edffb3e0b1df8', '1686812962476-coba_1.jpg', 0, '2023-06-15'),
(43, 2, 4, 'coba_1.jpg', 'coba', 'df2654bfdbb520ade08edffb3e0b1df8', '1686812962476-coba_1.jpg', 1, '2023-06-15');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `nama`, `email`, `password`) VALUES
(1, 'Rumah Sakit Coba', 'coba@gmail.com', '$2b$10$zqxSHWxDE2MKTftWa51.9.nV651nN8dM0QXTcZTfJGSymITj2j5AS'),
(2, 'Rumah Sakit Coba2', 'wahyudirudhi@gmail.com', '$2b$10$XUEMvM.a/PV/YzjYotjZX.saxGht7R3fkoEJm6MU4JcGWlkWB6Z8e'),
(3, 'rumah sakit hati', 'faizn890@gmail.com', '$2b$10$48Zy0i9qUJaGXoW4ahn1e.FBm19OK8Rl73pELlQvHDcMDLhXSDQ2y'),
(4, 'coba', 'rudhiwahyudi0@gmail.com', '$2b$10$9ObfW73U0fJiHKDymJ11dukuyHg9WzxkBmHH7LilsBtmAC.S5C8mm');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `files`
--
ALTER TABLE `files`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
