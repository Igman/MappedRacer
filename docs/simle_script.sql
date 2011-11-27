-------------------------------------------------------------------------------------------------
DROP SCHEMA MappedRace;
-------------------------------------------------------------------------------------------------
CREATE SCHEMA MappedRace;
-------------------------------------------------------------------------------------------------
CREATE TABLE Users(
uname VARCHAR(50) UNIQUE,
passcode VARCHAR(50),
oauth_token VARCHAR(90),
oauth_token_secret VARCHAR(90)
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
ALTER TABLE Users
ADD uid INT NOT NULL AUTO_INCREMENT FIRST,
ADD PRIMARY KEY (uid);

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
ADD FOREIGN KEY (CreatorID) REFERENCES Users(uid);

ALTER TABLE CheckIn
ADD FOREIGN KEY (RaceID) REFERENCES Race(ID);

ALTER TABLE Racers
ADD FOREIGN KEY (RaceID) REFERENCES Race(ID);

ALTER TABLE Racers
ADD FOREIGN KEY (UserID) REFERENCES Users(uid);

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