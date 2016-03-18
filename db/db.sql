drop table t_proj;
create table t_proj(
	ID INTEGER  GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
    PROJ_CODE VARCHAR(32),
    PROJ_NAME VARCHAR(64),
    CREATE_TIME DATETIME,
    UPDATE_TIME DATETIME,
    VERSION INTEGER
);
select * from t_proj;
INSERT INTO t_proj (PROJ_CODE, PROJ_NAME, CREATE_TIME, UPDATE_TIME, VERSION) 
 	VALUES ('test_proj_0', 'test project name 0', current_timestamp,current_timestamp,1);
drop table t_tab;
create table t_tab(
	ID INTEGER  GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
    ID_PROJ INTEGER,
	TAB_CODE VARCHAR(32),
	TAB_NAME VARCHAR(128),
	DISPLAY TINYINT,
	EDITABLE TINYINT,
    CREATE_TIME DATETIME,
    UPDATE_TIME DATETIME,
    VERSION INTEGER
);
select * from t_tab;
drop table t_col;
create table t_col(
	ID INTEGER  GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
    ID_TAB INTEGER,
	COL_CODE VARCHAR(32),
	COL_NAME VARCHAR(128),
	COL_TYPE VARCHAR(4),
	COL_COLLECTION_TYPE VARCHAR(4),
	COL_LEN INTEGER,
	NULLABLE TINYINT,
	QUERYABLE TINYINT,
	COL_REG VARCHAR(128),
	COL_RANGE VARCHAR(128),
	COL_MEMO VARCHAR(128),
	DISPLAY TINYINT,
	EDITABLE TINYINT,
    CREATE_TIME DATETIME,
    UPDATE_TIME DATETIME,
    VERSION INTEGER
);
select * from t_col;