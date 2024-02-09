CREATE TABLE AirQualityRecord (
                                  id SERIAL PRIMARY KEY,
                                  overallaqi INT NOT NULL,
                                  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Pollutant (
                           id SERIAL PRIMARY KEY,
                           recordid INT NOT NULL,
                           name VARCHAR(50) NOT NULL,
                           concentration FLOAT NOT NULL,
                           aqi INT NOT NULL,
                           FOREIGN KEY (RecordID) REFERENCES AirQualityRecord(id)
);