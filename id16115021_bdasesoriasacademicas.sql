-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 02, 2022 at 03:15 AM
-- Server version: 10.8.3-MariaDB
-- PHP Version: 7.3.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id16115021_bdasesoriasacademicas`
--

-- --------------------------------------------------------

--
-- Table structure for table `CLASE`
--

CREATE TABLE `CLASE` (
  `id_clase` int(11) NOT NULL,
  `fecha` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `hora` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `duracion` int(11) NOT NULL,
  `id_tutoria` int(11) NOT NULL,
  `id_estudiante` int(11) NOT NULL,
  `id_profesor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `CLASE`
--

INSERT INTO `CLASE` (`id_clase`, `fecha`, `hora`, `duracion`, `id_tutoria`, `id_estudiante`, `id_profesor`) VALUES
(127, '08/11/2021', '8:0 AM', 3, 132, 57, 16),
(128, '11/11/2021', '6:00 PM', 1, 133, 58, 16),
(129, '11/11/2021', '6:0 PM', 2, 134, 59, 16),
(130, '18/11/2021', '9:0 AM', 1, 135, 59, 18),
(131, '16/11/2021', '3:00 PM', 2, 136, 59, 17),
(132, '15/11/2021', '9:0 AM', 1, 137, 59, 18),
(134, '19/11/2021', '9:0 AM', 1, 139, 60, 17),
(135, '17/11/2021', '6:00 PM', 3, 140, 56, 16),
(136, '19/11/2021', '10:00 AM', 2, 141, 61, 16),
(137, '21/11/2021', '9:0 AM', 2, 142, 60, 16),
(138, '21/11/2021', '9:0 AM', 2, 143, 60, 16),
(139, '23/11/2021', '10:0 AM', 2, 144, 62, 16),
(140, '26/11/2021', '9:0 AM', 6, 145, 60, 16),
(141, '22/11/2021', '3:30 PM', 2, 146, 63, 16),
(142, '29/11/2021', '3:00 PM', 2, 147, 60, 16),
(143, '14/12/2021', '6:0 PM', 6, 148, 60, 16),
(144, '22/04/2022', '5:16 PM', 2, 149, 58, 16);

-- --------------------------------------------------------

--
-- Table structure for table `ESTUDIANTE`
--

CREATE TABLE `ESTUDIANTE` (
  `id_estudiante` int(11) NOT NULL,
  `id_persona` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `ESTUDIANTE`
--

INSERT INTO `ESTUDIANTE` (`id_estudiante`, `id_persona`) VALUES
(56, 73),
(57, 74),
(58, 75),
(59, 76),
(60, 79),
(61, 82),
(62, 83),
(63, 84);

-- --------------------------------------------------------

--
-- Table structure for table `PAGO`
--

CREATE TABLE `PAGO` (
  `id_pago` int(11) NOT NULL,
  `id_clase` int(11) NOT NULL,
  `valor_ganacia` int(11) NOT NULL,
  `valor_fondo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `PAGO`
--

INSERT INTO `PAGO` (`id_pago`, `id_clase`, `valor_ganacia`, `valor_fondo`) VALUES
(59, 127, 30000, 2100),
(60, 128, 15000, 700),
(61, 129, 27000, 1400),
(62, 130, 12000, 700),
(63, 131, 24000, 1400),
(64, 132, 10000, 700),
(66, 134, 12000, 700),
(67, 135, 35000, 2100),
(68, 136, 24000, 1400),
(69, 137, 18800, 1400),
(70, 138, 18800, 1400),
(71, 139, 35000, 1400),
(72, 140, 60000, 4200),
(73, 141, 24000, 2100),
(74, 142, 35000, 1400),
(75, 143, 60000, 4200),
(76, 144, 12000, 1400);

-- --------------------------------------------------------

--
-- Table structure for table `PERSONA`
--

CREATE TABLE `PERSONA` (
  `id_persona` int(11) NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `telefono` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `direccion` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `PERSONA`
--

INSERT INTO `PERSONA` (`id_persona`, `nombre`, `email`, `telefono`, `direccion`, `password`) VALUES
(72, 'John Alexander Hernández', '90jalexhernandez09@gmail.com', '3137632643', 'Cra 18 # 3-04', 'npY/ix7ltvyGVZ83aFYRcg=='),
(73, 'Manuel Gómez', 'manuelgomez@gmail.com', '3122050078', 'Villa docente', ''),
(74, 'María Paula Betancourt', 'mariapaula@gmail.com', '3218564950', 'Campamento', ''),
(75, 'Gladis Zambrano', 'gladis@gmail.com', '3184654220', 'Calle 66 # 0-58 Vereda Gonzáles', ''),
(76, 'Reina Ruiz', 'reina@gmail.com', '3006329770', 'Cra 10 # 1-72 San Francisco', ''),
(77, 'Fernanda Lozada', 'ferlozadabolanos@gmail.com', '', '', '0Kp05l9oj3YtqUv0JiwXaw=='),
(78, 'Piedad Hernández', 'piedadhernandez@gmail.com', '3116302643', 'Cra 18 #3-04 Pandiguando', 'ogKAUdv2Pu/P/SIRDjbdEg=='),
(79, 'Estudiante generico', 'estudantex@gmail.com', '3137632643', 'Cra 18 # 3-04', ''),
(80, 'Juan Camilo villaquiran Lugo', 'jcamilovillaquiran@gmail.com', '3117657191', 'calle 2E No 58-43', 'rtHtkk5T5qK5hWp2ZWYSvA=='),
(81, 'DAVID GIOVANNI MUESES VILLAMARIN', 'davidm1048@hotmail.com', '', '', 'C/CPcaLyVdnwpVT0hZzU8g=='),
(82, 'Jessica llanos', 'estudiante@gmail.com', '3207696063', 'Calle 2A #32 - 48 Junín', ''),
(83, 'Alejandra Administración', 'alejandra@gmail.com', '3148472466', 'Cra 18 # 3-04', ''),
(84, 'Juan David Caicedo', 'juancaicedo@gmail.com', '3128518227', 'Calle 61 N # 3-51 los Angeles', '');

-- --------------------------------------------------------

--
-- Table structure for table `PROFESOR`
--

CREATE TABLE `PROFESOR` (
  `id_profesor` int(11) NOT NULL,
  `id_persona` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `PROFESOR`
--

INSERT INTO `PROFESOR` (`id_profesor`, `id_persona`) VALUES
(16, 72),
(17, 77),
(18, 78),
(19, 80),
(20, 81);

-- --------------------------------------------------------

--
-- Table structure for table `TUTORIA`
--

CREATE TABLE `TUTORIA` (
  `id_tutoria` int(11) NOT NULL,
  `materia` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `tema` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `inquietudes` varchar(200) COLLATE utf8mb3_unicode_ci NOT NULL,
  `estado` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `TUTORIA`
--

INSERT INTO `TUTORIA` (`id_tutoria`, `materia`, `tema`, `inquietudes`, `estado`) VALUES
(132, 'Matemáticas 7°', 'Proporcionalidad', 'Preparación examen final', 'activo'),
(133, 'Probabilidad y Estadística', 'Estadística descriptiva', 'Solución taller razones', 'activo'),
(134, 'Matemáticas 5°', 'Operaciones con fracciones', 'Solución taller operacion con fracciones', 'activo'),
(135, 'Física', 'Mov. uniforme rectilíneo acelerado (MURA)', 'Solución trabajo x', 'activo'),
(136, 'Física', 'Conservación de la energía', 'Revisión de conceptos y ejercicios de práctica', 'activo'),
(137, 'Física', 'Magnitudes física y derivadas', 'x', 'activo'),
(139, 'Matemáticas 5°', 'Lectura y escritura de fracciones', 'Conceptos y ejercicios', 'activo'),
(140, 'Matemáticas 10°', 'Razones trigonométricas', 'Solución nivelación trígonometría', 'activo'),
(141, 'Matemáticas 7°', 'Regla de tres simple', 'Preparatorio exámen final', 'activo'),
(142, 'Matemáticas 7°', 'Operaciones con numeros racionales', 'Preparación recuperación matemáticas', 'activo'),
(143, 'Matemáticas 7°', 'Operaciones con numeros racionales', 'Preparación recuperación matemáticas', 'inactivo'),
(144, 'Matemáticas 11°', 'Derivadas, propiedades de las derivadas', 'Solución parcial Cálculo', 'activo'),
(145, 'Matemáticas 11°', 'Límites y continuidad', 'Solución taller cálculo de límites', 'activo'),
(146, 'Matemáticas 7°', 'Regla de tres simple', 'Preparatorio exámen de recuperación', 'activo'),
(147, 'Matemáticas 11°', 'Límites y continuidad', 'Solución parcial cálculo de límites', 'activo'),
(148, 'Precálculo', 'Función y relación', 'Solución taller aplicaciones funciones', 'activo'),
(149, 'Precálculo', 'Dominio y rango de una función', 'estudio examen', 'activo');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `CLASE`
--
ALTER TABLE `CLASE`
  ADD PRIMARY KEY (`id_clase`),
  ADD KEY `id_tutoria` (`id_tutoria`),
  ADD KEY `id_estudiante` (`id_estudiante`),
  ADD KEY `id_profesor` (`id_profesor`);

--
-- Indexes for table `ESTUDIANTE`
--
ALTER TABLE `ESTUDIANTE`
  ADD PRIMARY KEY (`id_estudiante`),
  ADD KEY `id_persona` (`id_persona`);

--
-- Indexes for table `PAGO`
--
ALTER TABLE `PAGO`
  ADD PRIMARY KEY (`id_pago`),
  ADD KEY `id_clase` (`id_clase`);

--
-- Indexes for table `PERSONA`
--
ALTER TABLE `PERSONA`
  ADD PRIMARY KEY (`id_persona`);

--
-- Indexes for table `PROFESOR`
--
ALTER TABLE `PROFESOR`
  ADD PRIMARY KEY (`id_profesor`),
  ADD KEY `id_persona` (`id_persona`);

--
-- Indexes for table `TUTORIA`
--
ALTER TABLE `TUTORIA`
  ADD PRIMARY KEY (`id_tutoria`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `CLASE`
--
ALTER TABLE `CLASE`
  MODIFY `id_clase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=145;

--
-- AUTO_INCREMENT for table `ESTUDIANTE`
--
ALTER TABLE `ESTUDIANTE`
  MODIFY `id_estudiante` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `PAGO`
--
ALTER TABLE `PAGO`
  MODIFY `id_pago` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

--
-- AUTO_INCREMENT for table `PERSONA`
--
ALTER TABLE `PERSONA`
  MODIFY `id_persona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=85;

--
-- AUTO_INCREMENT for table `PROFESOR`
--
ALTER TABLE `PROFESOR`
  MODIFY `id_profesor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `TUTORIA`
--
ALTER TABLE `TUTORIA`
  MODIFY `id_tutoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=150;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `CLASE`
--
ALTER TABLE `CLASE`
  ADD CONSTRAINT `CLASE_ibfk_1` FOREIGN KEY (`id_tutoria`) REFERENCES `TUTORIA` (`id_tutoria`) ON DELETE CASCADE,
  ADD CONSTRAINT `CLASE_ibfk_2` FOREIGN KEY (`id_estudiante`) REFERENCES `ESTUDIANTE` (`id_estudiante`) ON DELETE CASCADE,
  ADD CONSTRAINT `CLASE_ibfk_3` FOREIGN KEY (`id_profesor`) REFERENCES `PROFESOR` (`id_profesor`);

--
-- Constraints for table `ESTUDIANTE`
--
ALTER TABLE `ESTUDIANTE`
  ADD CONSTRAINT `ESTUDIANTE_ibfk_1` FOREIGN KEY (`id_persona`) REFERENCES `PERSONA` (`id_persona`) ON DELETE CASCADE;

--
-- Constraints for table `PAGO`
--
ALTER TABLE `PAGO`
  ADD CONSTRAINT `PAGO_ibfk_1` FOREIGN KEY (`id_clase`) REFERENCES `CLASE` (`id_clase`);

--
-- Constraints for table `PROFESOR`
--
ALTER TABLE `PROFESOR`
  ADD CONSTRAINT `PROFESOR_ibfk_1` FOREIGN KEY (`id_persona`) REFERENCES `PERSONA` (`id_persona`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
