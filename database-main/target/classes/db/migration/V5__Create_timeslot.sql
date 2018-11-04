create table if not exists timeslot (
  id SERIAL PRIMARY KEY,
  time time NOT NULL UNIQUE,
  track int NOT NULL,
  day varchar(9) NOT NULL,
  FOREIGN KEY (track) references track(id),
  FOREIGN KEY (day) references days(weekday_name)
);