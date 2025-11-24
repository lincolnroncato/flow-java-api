-- FLOW - Script para adicionar a coluna cod_processo na tabela DDD_EXECUCOES
-- Execute este script APENAS se a coluna ainda não existir.

-- 1) Adicionar coluna cod_processo (permite NULL no início para evitar erro)
ALTER TABLE DDD_EXECUCOES ADD (cod_processo NUMBER);

-- 2) Criar Foreign Key para DDD_PROCESSOS
ALTER TABLE DDD_EXECUCOES
ADD CONSTRAINT FK_EXECUCAO_PROCESSO
FOREIGN KEY (cod_processo) REFERENCES DDD_PROCESSOS(codigo);

-- 3) (Opcional) Preencher cod_processo para execuções já existentes
-- Este UPDATE associa automaticamente o processo baseado na etapa atual
-- Caso você tenha execuções antigas no banco.
-- Descomente se precisar.

-- UPDATE DDD_EXECUCOES e
--    SET e.cod_processo = (
--        SELECT et.cod_processo
--        FROM DDD_ETAPAS et
--        WHERE et.codigo = e.cod_etapa
--      )
-- WHERE e.cod_processo IS NULL;

-- 4) (Opcional) Somente após atualizar os registros existentes:
-- ALTER TABLE DDD_EXECUCOES MODIFY (cod_processo NOT NULL);
