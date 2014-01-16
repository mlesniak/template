CREATE TABLE logging_event_property (
  event_id BIGINT ,
  mapped_key  VARCHAR(254) ,
  mapped_value LONG VARCHAR,
  PRIMARY KEY(event_id, mapped_key),
  FOREIGN KEY (event_id) REFERENCES logging_event(event_id));

CREATE TABLE logging_event_exception (
  event_id BIGINT ,
  i SMALLINT ,
  trace_line VARCHAR(256) ,
  PRIMARY KEY(event_id, i),
  FOREIGN KEY (event_id) REFERENCES logging_event(event_id));