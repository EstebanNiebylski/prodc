CREATE TABLE teams (
  name VARCHAR(128) PRIMARY KEY,
  image VARCHAR(128),
  created_at DATETIME,
  updated_at DATETIME
)ENGINE=InnoDB;

ALTER TABLE teams ADD group_letter VARCHAR(1);