-------------------------------------------------------------------------------------------------
DROP SCHEMA MappedRace;
-------------------------------------------------------------------------------------------------
CREATE SCHEMA MappedRace;
-------------------------------------------------------------------------------------------------
CREATE TABLE User(
	Username varchar(255)
);
CREATE TABLE Race(
	Name varchar(255),
	EndPoint varchar(255),
	CreateDate DATE,
	StartTime TIMESTAMP,
	StartDate DATE,
	CreatorID int
);
CREATE TABLE Racers(
	RaceID int,
	UserID int,
	Attend smallint(1),
	TotalTime float,
	Place int
);
CREATE TABLE CheckIn(
	RaceID int,
	Picture varchar(255),
	Comment varchar(255),
	GeoLocation varchar(255)
);
CREATE TABLE Item(
	TypeID int,
	Status boolean,
	ValueWeight int,
	Geolocation varchar(255),
	RaceID int
);
CREATE TABLE ItemType(
	TypeName varchar(255)
);
-------------------------------------------------------------------------------------------------
ALTER TABLE User 
ADD ID INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (ID);

ALTER TABLE Race 
ADD ID INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (ID);

ALTER TABLE CheckIn 
ADD ID INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (ID);

ALTER TABLE Item
ADD ID INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY(ID);

ALTER TABLE ItemType
ADD ID INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY(ID);
-------------------------------------------------------------------------------------------------
ALTER TABLE Race
ADD FOREIGN KEY (CreatorID) REFERENCES User(ID);

ALTER TABLE CheckIn
ADD FOREIGN KEY (RaceID) REFERENCES Race(ID);

ALTER TABLE Racers
ADD FOREIGN KEY (RaceID) REFERENCES Race(ID);

ALTER TABLE Racers
ADD FOREIGN KEY (UserID) REFERENCES User(ID);

ALTER TABLE Item
ADD FOREIGN KEY (RaceId) REFERENCES Race(ID);

ALTER TABLE Item
ADD FOREIGN KEY (TypeID) REFERENCES ItemType(ID);

-------------------------------------------------------------------------------------------------
ALTER TABLE Racers
ADD PRIMARY KEY (RaceID,UserID);
-------------------------------------------------------------------------------------------------

INSERT INTO ItemType(TypeName) VALUES('FinalDestination');
INSERT INTO ItemType(TypeName) VALUES('Positive');
INSERT INTO ItemType(TypeName) VALUES('Negative');

-------------------------------------------------------------------------------------------------