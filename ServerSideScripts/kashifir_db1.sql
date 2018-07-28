-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 28, 2018 at 08:37 AM
-- Server version: 10.1.31-MariaDB-cll-lve
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kashifir_db1`
--
CREATE DATABASE IF NOT EXISTS `kashifir_db1` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `kashifir_db1`;


--
-- Table structure for table `AndroidProjects_Projects`
--

DROP TABLE IF EXISTS `AndroidProjects_Projects`;
CREATE TABLE `AndroidProjects_Projects` (
  `Id` int(11) NOT NULL,
  `Story` text,
  `FilePaths` text,
  `EstimatedHrs` varchar(500) DEFAULT NULL,
  `EstimateCost` varchar(500) DEFAULT NULL,
  `DeliveryDate` datetime DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `UpdatedAt` datetime DEFAULT NULL,
  `UserId` int(11) DEFAULT NULL,
  `ParentId` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


--
-- Table structure for table `AndroidProjects_Requests`
--

DROP TABLE IF EXISTS `AndroidProjects_Requests`;
CREATE TABLE `AndroidProjects_Requests` (
  `Id` int(11) NOT NULL,
  `Request` text,
  `Response` text NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `AndroidProjects_Users`
--

DROP TABLE IF EXISTS `AndroidProjects_Users`;
CREATE TABLE `AndroidProjects_Users` (
  `Id` int(11) NOT NULL,
  `FirstName` varchar(500) DEFAULT NULL,
  `MiddleName` varchar(500) DEFAULT NULL,
  `LastName` varchar(500) DEFAULT NULL,
  `EmailAddress` varchar(500) NOT NULL,
  `SkypeId` varchar(500) DEFAULT NULL,
  `WatsAppNo` varchar(500) DEFAULT NULL,
  `AddressLine1` varchar(1000) DEFAULT NULL,
  `AddressLine2` varchar(1000) DEFAULT NULL,
  `City` varchar(500) DEFAULT NULL,
  `State` varchar(500) DEFAULT NULL,
  `Country` varchar(500) DEFAULT NULL,
  `UnReadOnly` int(11) DEFAULT NULL,
  `SyncDuration` int(11) DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `UpdatedAt` datetime DEFAULT NULL,
  `IsLocked` int(11) NOT NULL DEFAULT '0',
  `Password` varchar(255) NOT NULL,
  `IsEmailVerified` int(11) NOT NULL,
  `IsLoggedIn` int(11) NOT NULL,
  `Token` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

ALTER TABLE `AndroidProjects_Users`  ADD  CHECK (EmailAddress <> '')
--
-- Table structure for table `EmailTokens`
--

DROP TABLE IF EXISTS `EmailTokens`;
CREATE TABLE `EmailTokens` (
  `Id` int(11) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Date` date NOT NULL,
  `Token` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


--
-- Indexes for table `AndroidProjects_Projects`
--
ALTER TABLE `AndroidProjects_Projects`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `AndroidProjects_Requests`
--
ALTER TABLE `AndroidProjects_Requests`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `AndroidProjects_Users`
--
ALTER TABLE `AndroidProjects_Users`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `uq_email` (`EmailAddress`);

--
-- Indexes for table `EmailTokens`
--
ALTER TABLE `EmailTokens`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Email` (`Email`,`Date`),
  ADD KEY `Email_2` (`Email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `AndroidProjects_Projects`
--
ALTER TABLE `AndroidProjects_Projects`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `AndroidProjects_Requests`
--
ALTER TABLE `AndroidProjects_Requests`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `AndroidProjects_Users`
--
ALTER TABLE `AndroidProjects_Users`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `EmailTokens`
--
ALTER TABLE `EmailTokens`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
