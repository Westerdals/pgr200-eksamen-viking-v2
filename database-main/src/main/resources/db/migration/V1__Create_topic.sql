create table if not exists topic (
  id          SERIAL PRIMARY key,
  title VARCHAR not NULL UNIQUE
);

INSERT INTO topic VALUES
                         (DEFAULT , 'Science'),
                         (DEFAULT , 'Programming'),
                         (DEFAULT , 'Hacking');