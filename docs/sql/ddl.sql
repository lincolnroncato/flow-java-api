----------------------------------------------------------
-- SEQUENCES
----------------------------------------------------------

-- Sequence para DDD_USUARIOS
CREATE SEQUENCE DDD_USUARIOS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Sequence para DDD_PROCESSOS
CREATE SEQUENCE DDD_PROCESSOS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Sequence para DDD_ETAPAS
CREATE SEQUENCE DDD_ETAPAS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Sequence para DDD_EXECUCOES
CREATE SEQUENCE DDD_EXECUCOES_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;



----------------------------------------------------------
-- TABELAS
----------------------------------------------------------

-- Tabela de usuários
CREATE TABLE DDD_USUARIOS (
    codigo NUMBER PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    cargo VARCHAR2(60)
);

-- Tabela de processos
CREATE TABLE DDD_PROCESSOS (
    codigo NUMBER PRIMARY KEY,
    titulo VARCHAR2(120) NOT NULL,
    descricao VARCHAR2(400),
    data_criacao DATE NOT NULL
);

-- Tabela de etapas
CREATE TABLE DDD_ETAPAS (
    codigo NUMBER PRIMARY KEY,
    cod_processo NUMBER NOT NULL,
    titulo VARCHAR2(120) NOT NULL,
    descricao VARCHAR2(300),
    ordem NUMBER NOT NULL,
    tempo_estimado_min NUMBER NOT NULL,
    CONSTRAINT FK_ETAPA_PROCESSO
        FOREIGN KEY (cod_processo)
        REFERENCES DDD_PROCESSOS(codigo)
);



----------------------------------------------------------
-- Tabela de execuções 
-- (CORRIGIDA PARA ALINHAR COM TEU FLUXO DO JAVA)
----------------------------------------------------------

CREATE TABLE DDD_EXECUCOES (
    codigo NUMBER PRIMARY KEY,
    cod_usuario NUMBER NOT NULL,
    cod_processo NUMBER NOT NULL,
    cod_etapa NUMBER NOT NULL,
    data_execucao DATE,  -- pode iniciar como NULL
    status VARCHAR2(20) NOT NULL,
    observacao VARCHAR2(300),

    CONSTRAINT FK_EXECUCAO_USUARIO
        FOREIGN KEY (cod_usuario)
        REFERENCES DDD_USUARIOS(codigo),

    CONSTRAINT FK_EXECUCAO_PROCESSO
        FOREIGN KEY (cod_processo)
        REFERENCES DDD_PROCESSOS(codigo),

    CONSTRAINT FK_EXECUCAO_ETAPA
        FOREIGN KEY (cod_etapa)
        REFERENCES DDD_ETAPAS(codigo),

    CONSTRAINT CK_EXECUCAO_STATUS
        CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA'))
);
