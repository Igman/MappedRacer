-- phpMyAdmin SQL Dump
-- version 3.3.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 23, 2011 at 07:30 PM
-- Server version: 5.5.9
-- PHP Version: 5.3.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `MappedRace`
--

-- --------------------------------------------------------

--
-- Table structure for table `CheckIn`
--

DROP TABLE IF EXISTS `CheckIn`;
CREATE TABLE IF NOT EXISTS `CheckIn` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RaceID` int(11) DEFAULT NULL,
  `Picture` varchar(255) DEFAULT NULL,
  `Comment` varchar(255) DEFAULT NULL,
  `GeoLocation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RaceID` (`RaceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `CheckIn`
--


-- --------------------------------------------------------

--
-- Table structure for table `Item`
--

DROP TABLE IF EXISTS `Item`;
CREATE TABLE IF NOT EXISTS `Item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TypeID` int(11) DEFAULT NULL,
  `Status` tinyint(1) DEFAULT NULL,
  `ValueWeight` int(11) DEFAULT NULL,
  `Geolocation` varchar(255) DEFAULT NULL,
  `RaceID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `RaceID` (`RaceID`),
  KEY `TypeID` (`TypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `Item`
--


-- --------------------------------------------------------

--
-- Table structure for table `ItemType`
--

DROP TABLE IF EXISTS `ItemType`;
CREATE TABLE IF NOT EXISTS `ItemType` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TypeName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `ItemType`
--

INSERT INTO `ItemType` (`ID`, `TypeName`) VALUES(1, 'FinalDestination');
INSERT INTO `ItemType` (`ID`, `TypeName`) VALUES(2, 'Positive');
INSERT INTO `ItemType` (`ID`, `TypeName`) VALUES(3, 'Negative');

-- --------------------------------------------------------

--
-- Table structure for table `Race`
--

DROP TABLE IF EXISTS `Race`;
CREATE TABLE IF NOT EXISTS `Race` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `EndPoint` varchar(255) DEFAULT NULL,
  `CreateDate` date DEFAULT NULL,
  `StartTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `StartDate` date DEFAULT NULL,
  `CreatorID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CreatorID` (`CreatorID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `Race`
--


-- --------------------------------------------------------

--
-- Table structure for table `Racers`
--

DROP TABLE IF EXISTS `Racers`;
CREATE TABLE IF NOT EXISTS `Racers` (
  `RaceID` int(11) NOT NULL DEFAULT '0',
  `UserID` int(11) NOT NULL DEFAULT '0',
  `Attend` smallint(1) DEFAULT NULL,
  `TotalTime` float DEFAULT NULL,
  `Place` int(11) DEFAULT NULL,
  PRIMARY KEY (`RaceID`,`UserID`),
  KEY `UserID` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Racers`
--


-- --------------------------------------------------------

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
CREATE TABLE IF NOT EXISTS `User` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `User`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `CheckIn`
--
ALTER TABLE `CheckIn`
  ADD CONSTRAINT `CheckIn_ibfk_3` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `CheckIn_ibfk_1` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `CheckIn_ibfk_2` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`);

--
-- Constraints for table `Item`
--
ALTER TABLE `Item`
  ADD CONSTRAINT `Item_ibfk_4` FOREIGN KEY (`TypeID`) REFERENCES `ItemType` (`ID`),
  ADD CONSTRAINT `Item_ibfk_1` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `Item_ibfk_2` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `Item_ibfk_3` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`);

--
-- Constraints for table `Race`
--
ALTER TABLE `Race`
  ADD CONSTRAINT `Race_ibfk_3` FOREIGN KEY (`CreatorID`) REFERENCES `User` (`ID`),
  ADD CONSTRAINT `Race_ibfk_1` FOREIGN KEY (`CreatorID`) REFERENCES `User` (`ID`),
  ADD CONSTRAINT `Race_ibfk_2` FOREIGN KEY (`CreatorID`) REFERENCES `User` (`ID`);

--
-- Constraints for table `Racers`
--
ALTER TABLE `Racers`
  ADD CONSTRAINT `Racers_ibfk_1` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `Racers_ibfk_2` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`),
  ADD CONSTRAINT `Racers_ibfk_3` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `Racers_ibfk_4` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`),
  ADD CONSTRAINT `Racers_ibfk_5` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `Racers_ibfk_6` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`);