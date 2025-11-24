----------------------------------------------------------
-- DML - FLOW
-- Inserts de dados fictícios para testes
----------------------------------------------------------

-- Inserindo usuários
INSERT INTO DDD_USUARIOS (codigo, nome, email, cargo)
VALUES (DDD_USUARIOS_SEQ.NEXTVAL, 'João Silva', 'joao.silva@flow.com', 'Analista de Processos');

INSERT INTO DDD_USUARIOS (codigo, nome, email, cargo)
VALUES (DDD_USUARIOS_SEQ.NEXTVAL, 'Maria Santos', 'maria.santos@flow.com', 'Coordenadora de Operações');


----------------------------------------------------------
-- Inserindo processos
----------------------------------------------------------

INSERT INTO DDD_PROCESSOS (codigo, titulo, descricao, data_criacao)
VALUES (DDD_PROCESSOS_SEQ.NEXTVAL, 'Processo de Onboarding',
        'Processo de integração de novos colaboradores',
        TO_DATE('2024-01-15', 'YYYY-MM-DD'));

INSERT INTO DDD_PROCESSOS (codigo, titulo, descricao, data_criacao)
VALUES (DDD_PROCESSOS_SEQ.NEXTVAL, 'Processo de Vendas',
        'Processo completo de vendas desde o contato até o fechamento',
        TO_DATE('2024-02-01', 'YYYY-MM-DD'));


----------------------------------------------------------
-- Inserindo etapas (2 para cada processo)
-- Processo 1 = onboarding
-- Processo 2 = vendas
----------------------------------------------------------

-- Etapas do Processo 1
INSERT INTO DDD_ETAPAS (codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min)
VALUES (DDD_ETAPAS_SEQ.NEXTVAL, 1, 'Cadastro de Dados',
        'Cadastro inicial do novo colaborador no sistema', 1, 30);

INSERT INTO DDD_ETAPAS (codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min)
VALUES (DDD_ETAPAS_SEQ.NEXTVAL, 1, 'Treinamento Inicial',
        'Treinamento sobre políticas e procedimentos da empresa', 2, 120);

-- Etapas do Processo 2
INSERT INTO DDD_ETAPAS (codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min)
VALUES (DDD_ETAPAS_SEQ.NEXTVAL, 2, 'Qualificação do Lead',
        'Análise e qualificação do potencial cliente', 1, 45);

INSERT INTO DDD_ETAPAS (codigo, cod_processo, titulo, descricao, ordem, tempo_estimado_min)
VALUES (DDD_ETAPAS_SEQ.NEXTVAL, 2, 'Apresentação de Proposta',
        'Apresentação da proposta comercial ao cliente', 2, 60);


----------------------------------------------------------
-- Inserindo execuções (AGORA CORRETAS COM cod_processo)
----------------------------------------------------------

-- Execução concluída no Processo 1 (etapa 1)
INSERT INTO DDD_EXECUCOES (codigo, cod_usuario, cod_processo, cod_etapa, data_execucao, status, observacao)
VALUES (DDD_EXECUCOES_SEQ.NEXTVAL,
        1,               -- usuário 1
        1,               -- processo 1
        1,               -- etapa 1
        TO_DATE('2024-01-20', 'YYYY-MM-DD'),
        'CONCLUIDA',
        'Cadastro realizado com sucesso');

-- Execução em andamento no Processo 2 (etapa 3 ou etapa 1 do processo 2)
INSERT INTO DDD_EXECUCOES (codigo, cod_usuario, cod_processo, cod_etapa, data_execucao, status, observacao)
VALUES (DDD_EXECUCOES_SEQ.NEXTVAL,
        2,               -- usuário 2
        2,               -- processo 2
        3,               -- etapa 3 = primeira etapa do processo 2
        TO_DATE('2024-02-10', 'YYYY-MM-DD'),
        'EM_ANDAMENTO',
        'Lead em análise');


COMMIT;
