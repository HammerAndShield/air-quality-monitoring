CREATE TABLE AirQualityRecord (
                                  ID SERIAL PRIMARY KEY,
                                  OverallAQI INT NOT NULL,
                                  Timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Pollutant (
                           ID SERIAL PRIMARY KEY,
                           RecordID INT NOT NULL,
                           Name VARCHAR(50) NOT NULL,
                           Concentration FLOAT NOT NULL,
                           AQI INT NOT NULL,
                           FOREIGN KEY (RecordID) REFERENCES AirQualityRecord(ID)
);