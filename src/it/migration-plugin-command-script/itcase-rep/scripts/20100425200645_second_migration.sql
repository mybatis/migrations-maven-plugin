--// First migration.
-- Migration SQL that makes the change goes here.

CREATE TABLE test2 (
ID NUMERIC(20,0) NOT NULL,
);


--//@UNDO

DROP TABLE test2;
