CREATE TABLE LOG (ID NUMBER(19) NOT NULL, CLASSE VARCHAR, LEVEL VARCHAR, MSG VARCHAR, ANNEE NUMBER(19), HEURE NUMBER(19), JOUR NUMBER(19), MILLISECONDE NUMBER(19), MINUTE NUMBER(19), MOIS NUMBER(19), SECONDE NUMBER(19), MACHINE_ID NUMBER(19), PRIMARY KEY (ID))
CREATE TABLE MACHINE (ID NUMBER(19) NOT NULL, NOM VARCHAR, PRIMARY KEY (ID))
ALTER TABLE LOG ADD CONSTRAINT FK_LOG_MACHINE_ID FOREIGN KEY (MACHINE_ID) REFERENCES MACHINE (ID)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMBER(19), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)
