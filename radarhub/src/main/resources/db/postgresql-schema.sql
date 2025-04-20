CREATE TABLE if not exists devices (
  id varchar (36) primary key,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  radius DOUBLE PRECISION NOT NULL
);

CREATE TABLE if not exists signals (
  id bigserial primary key,
  creationtime timestamp NOT NULL,
  device_id varchar (36) NOT NULL,
  obj_id bigserial NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL
)
