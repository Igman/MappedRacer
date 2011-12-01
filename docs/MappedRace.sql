-- phpMyAdmin SQL Dump
-- version 3.3.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 27, 2011 at 09:18 PM
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
-- Creation: Nov 27, 2011 at 09:17 PM
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
-- Creation: Nov 27, 2011 at 09:17 PM
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
-- Creation: Nov 27, 2011 at 09:17 PM
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

INSERT INTO `ItemType` VALUES(1, 'FinalDestination');
INSERT INTO `ItemType` VALUES(2, 'Positive');
INSERT INTO `ItemType` VALUES(3, 'Negative');

-- --------------------------------------------------------

--
-- Table structure for table `Race`
--
-- Creation: Nov 27, 2011 at 09:17 PM
--

DROP TABLE IF EXISTS `Race`;
CREATE TABLE IF NOT EXISTS `Race` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
-- Creation: Nov 27, 2011 at 09:18 PM
--

DROP TABLE IF EXISTS `Racers`;
CREATE TABLE IF NOT EXISTS `Racers` (
  `RaceID` int(11) NOT NULL DEFAULT '0',
  `UserID` int(11) NOT NULL DEFAULT '0',
  `Attend` smallint(1) DEFAULT NULL,
  `Score` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`RaceID`,`UserID`),
  KEY `UserID` (`UserID`),
  KEY `RaceID` (`RaceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Racers`
--


-- --------------------------------------------------------

--
-- Table structure for table `Users`
--
-- Creation: Nov 27, 2011 at 09:17 PM
--

DROP TABLE IF EXISTS `Users`;
CREATE TABLE IF NOT EXISTS `Users` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(50) DEFAULT NULL,
  `passcode` varchar(50) DEFAULT NULL,
  `oauth_token` varchar(90) DEFAULT NULL,
  `oauth_token_secret` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `Users`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `CheckIn`
--
ALTER TABLE `CheckIn`
  ADD CONSTRAINT `CheckIn_ibfk_1` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`);

--
-- Constraints for table `Item`
--
ALTER TABLE `Item`
  ADD CONSTRAINT `Item_ibfk_2` FOREIGN KEY (`TypeID`) REFERENCES `ItemType` (`ID`),
  ADD CONSTRAINT `Item_ibfk_1` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`);

--
-- Constraints for table `Race`
--
ALTER TABLE `Race`
  ADD CONSTRAINT `Race_ibfk_1` FOREIGN KEY (`CreatorID`) REFERENCES `Users` (`uid`);

--
-- Constraints for table `Racers`
--
ALTER TABLE `Racers`
  ADD CONSTRAINT `Racers_ibfk_1` FOREIGN KEY (`RaceID`) REFERENCES `Race` (`ID`),
  ADD CONSTRAINT `Racers_ibfk_2` FOREIGN KEY (`UserID`) REFERENCES `Users` (`uid`);
