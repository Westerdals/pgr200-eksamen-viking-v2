create table if not exists topic (
  id          SERIAL PRIMARY key,
  title VARCHAR not NULL UNIQUE
);

create table if not exists CONFERENCE_TALK (
  id          SERIAL PRIMARY key,
  title       varchar,
  description text,
  topic varchar null,
  foreign key (topic) REFERENCES topic(title)
);

