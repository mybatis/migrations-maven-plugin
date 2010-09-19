--// First migration.
-- Migration SQL that makes the change goes here.

CREATE TABLE test3 (
ID NUMERIC(20,0) NOT NULL,
);


--//@UNDO

DROP TABLE test3;
