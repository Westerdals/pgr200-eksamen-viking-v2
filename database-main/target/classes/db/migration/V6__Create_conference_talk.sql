create table if not exists CONFERENCE_TALK (
  id          SERIAL PRIMARY key,
  title       varchar,
  description text,
  topic varchar,
  timeslot time NOT NULL,
  foreign key (timeslot) REFERENCES timeslot(time),
  foreign key (topic) REFERENCES topic(title)
);
