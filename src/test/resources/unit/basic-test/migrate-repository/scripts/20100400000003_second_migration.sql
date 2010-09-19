--
-- Copyright 2010 The myBatis Team
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- version: $Id: 20100400000003_second_migration.sql 107 2010-06-26 20:04:42Z simone.tripodi $

--// First migration.
-- Migration SQL that makes the change goes here.

CREATE TABLE pluto (
    ID NUMERIC(20,0) NOT NULL
);

--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE pluto;
