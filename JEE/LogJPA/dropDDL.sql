ALTER TABLE LOG DROP CONSTRAINT FK_LOG_MACHINE_ID
DROP TABLE LOG
DROP TABLE MACHINE
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN'