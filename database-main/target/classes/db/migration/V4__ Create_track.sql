create table if not exists track(
  id serial PRIMARY KEY,
  day varchar(8) NOT NULL,
  conference_id int NOT NULL,
  FOREIGN KEY (day) REFERENCES days(weekday_name),
  FOREIGN KEY (conference_id) references conference(id)
);