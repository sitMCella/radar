CREATE OR REPLACE FUNCTION notify_devices_event() RETURNS TRIGGER AS
$$
BEGIN
    PERFORM pg_notify('devices_notification', json_build_object(
      'id', NEW.id,
      'latitude', NEW.latitude,
      'longitude', NEW.longitude,
      'radius', NEW.radius
    )::text);
    RETURN NULL;
END;
$$
LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION notify_signals_event() RETURNS TRIGGER AS
$$
BEGIN
    PERFORM pg_notify('signals_notification', json_build_object(
      'id', NEW.id,
      'creationtime', to_char(NEW.creationtime, 'YYYY-MM-DD"T"HH24:MI:SS.US"Z"'),
      'deviceId', NEW.device_id,
      'objId', NEW.obj_id,
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

DROP TRIGGER IF EXISTS notify_devices ON devices;;

CREATE TRIGGER notify_devices
    AFTER INSERT
    ON devices
    FOR EACH ROW
EXECUTE PROCEDURE notify_devices_event();;

DROP TRIGGER IF EXISTS notify_signals ON signals;;

CREATE TRIGGER notify_signals
    AFTER INSERT
    ON signals
    FOR EACH ROW
EXECUTE PROCEDURE notify_signals_event();;
