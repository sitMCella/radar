CREATE OR REPLACE FUNCTION notify_event() RETURNS TRIGGER AS
$$
BEGIN
    PERFORM pg_notify('signals_notification', json_build_object(
      'id', NEW.id,
      'creationtime', to_char(NEW.creationtime, 'YYYY-MM-DD"T"HH24:MI:SS.US"Z"'),
      'device_id', NEW.device_id,
      'obj_id', NEW.obj_id,
      'latitude', NEW.latitude,
      'longitude', NEW.longitude
    )::text);
    RETURN NULL;
END;
$$
LANGUAGE plpgsql;;

CREATE TABLE if not exists devices (
  id varchar (36) primary key,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  radius DOUBLE PRECISION NOT NULL
);;

CREATE TABLE if not exists signals (
  id bigserial primary key,
  creationtime timestamp NOT NULL,
  device_id varchar (36) NOT NULL,
  obj_id bigserial NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL
);;

DROP TRIGGER IF EXISTS notify_signals ON signals;;

CREATE TRIGGER notify_signals
    AFTER INSERT
    ON signals
    FOR EACH ROW
EXECUTE PROCEDURE notify_event();;
