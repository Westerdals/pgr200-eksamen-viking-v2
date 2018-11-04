create table if not exists days (
  id          SERIAL PRIMARY key,
  weekday_name        VARCHAR(9) NOT NULL UNIQUE,
  conference_id int /*NOT SURE IF THIS IS SUPPOSED TO BE NULL*/,
  FOREIGN KEY (conference_id) REFERENCES conference(id)
);

  INSERT INTO days VALUES
                            (DEFAULT, 'Monday'),
                            (DEFAULT, 'Tuesday'),
                            (DEFAULT, 'Wednesday'),
                            (DEFAULT, 'Thursday'),
                            (DEFAULT, 'Friday'),
                            (DEFAULT, 'Saturday'),
                            (DEFAULT, 'Sunday');
