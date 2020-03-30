DROP TABLE IF EXISTS SOCIO_TORCEDOR;
CREATE TABLE SOCIO_TORCEDOR
(
    ID                       INT                NOT NULL    IDENTITY    PRIMARY KEY,
    NOME_COMPLETO            VARCHAR(255)       NOT NULL,
    EMAIL                    VARCHAR(255)       NOT NULL,
    DATA_NASCIMENTO          DATE               NULL,
    TIME_CORACAO             VARCHAR(255)
);

INSERT INTO SOCIO_TORCEDOR VALUES (1, 'joao', 'joao@teste.com', TO_DATE('2020/05/01', 'yyyy/mm/dd'), 'Corinthians');