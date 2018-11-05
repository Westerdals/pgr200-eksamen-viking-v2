create table if not exists CONFERENCE_TALK (
  id          SERIAL PRIMARY key,
  title       varchar,
  description text,
  topic varchar,
  foreign key (topic) REFERENCES topic(title)
);
