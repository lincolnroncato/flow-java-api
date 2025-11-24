# FLOW - API RESTful

## Equipe STECHIA

**Integrantes:**
- Lincoln Roncato — RM 565944
- Rafael Malaguti — RM 561830
- Natalia Cristina Souza — RM 564099

## Objetivo

FLOW é uma plataforma de padronização e execução de processos operacionais. No MVP backend, vamos modelar processos, suas etapas, usuários e execuções.

## Escopo

API RESTful desenvolvida em Java com Quarkus seguindo o padrão Domain Driven Design (DDD), utilizando apenas tecnologias ensinadas nas aulas de "Domain Driven Design Using Java" da FIAP.

## Tecnologias

- **Java 17+**
- **Quarkus** (RESTEasy/JAX-RS)
- **JDBC Oracle** (ConnectionFactory, PreparedStatement, ResultSet)
- **Maven**
- **Bean Validation** (jakarta.validation: @NotBlank, @NotNull, @PastOrPresent, @Min, @Valid)
- **Jackson** via Quarkus para JSON
- **CORSFilter** conforme aula

## Estrutura do Projeto

```
src/main/java/br/com/fiap/flow/
  ├── to/          # Transfer Objects (Model/DTO)
  ├── dao/         # Data Access Objects (JDBC)
  ├── bo/          # Business Objects (Regras de Negócio)
  ├── resource/    # Endpoints REST (JAX-RS)
  ├── factory/     # ConnectionFactory
  └── exception/   # Exceções de Domínio
```

## Instruções para Rodar Localmente

### Pré-requisitos

1. Java 17 ou superior
2. Maven 3.6+
3. Oracle Database (ou Docker com Oracle)
4. Insomnia (ou Postman) para testes

### Configuração

1. **Configurar variáveis de ambiente:**
   ```bash
   export DB_URL=jdbc:oracle:thin:@localhost:1521:xe
   export DB_USER=system
   export DB_PASSWORD=oracle
   ```

   Ou edite `src/main/resources/application.properties` e descomente as linhas de fallback no `ConnectionFactory.java`.

2. **Criar o banco de dados:**
   Execute o script `docs/sql/ddl.sql` no Oracle para criar as tabelas e sequences.
   
   **Importante:** Se a tabela DDD_EXECUCOES já existir sem a coluna `cod_processo`, execute também o script `docs/sql/alter_table.sql` para adicionar essa coluna.

3. **Popular dados iniciais (opcional):**
   Execute o script `docs/sql/dml.sql` para inserir dados de teste.

4. **Compilar e executar:**
   ```bash
   mvn clean compile quarkus:dev
   ```

5. **Acessar a API:**
   A API estará disponível em `http://localhost:8081`

## Endpoints da API

### Usuários

| Método | URI | Status de Sucesso | Descrição |
|--------|-----|-------------------|-----------|
| GET | `/usuarios` | 200 OK | Lista todos os usuários |
| GET | `/usuarios/{codigo}` | 200 OK | Busca usuário por código |
| POST | `/usuarios` | 201 Created | Cria novo usuário |
| PUT | `/usuarios/{codigo}` | 201 Created | Atualiza usuário |
| DELETE | `/usuarios/{codigo}` | 204 No Content | Remove usuário |

### Processos

| Método | URI | Status de Sucesso | Descrição |
|--------|-----|-------------------|-----------|
| GET | `/processos` | 200 OK | Lista todos os processos |
| GET | `/processos/{codigo}` | 200 OK | Busca processo por código |
| POST | `/processos` | 201 Created | Cria novo processo |
| PUT | `/processos/{codigo}` | 201 Created | Atualiza processo |
| DELETE | `/processos/{codigo}` | 204 No Content | Remove processo |

### Etapas

| Método | URI | Status de Sucesso | Descrição |
|--------|-----|-------------------|-----------|
| GET | `/etapas` | 200 OK | Lista todas as etapas |
| GET | `/etapas/{codigo}` | 200 OK | Busca etapa por código |
| GET | `/etapas/processo/{codProcesso}` | 200 OK | Lista etapas de um processo |
| POST | `/etapas` | 201 Created | Cria nova etapa |
| PUT | `/etapas/{codigo}` | 201 Created | Atualiza etapa |
| DELETE | `/etapas/{codigo}` | 204 No Content | Remove etapa |

### Execuções - CRUD

| Método | URI | Status de Sucesso | Descrição |
|--------|-----|-------------------|-----------|
| GET | `/execucoes` | 200 OK | Lista todas as execuções |
| GET | `/execucoes/{codigo}` | 200 OK | Busca execução por código |
| GET | `/execucoes/usuario/{codUsuario}` | 200 OK | Lista execuções de um usuário |
| POST | `/execucoes` | 201 Created | Cria nova execução |
| PUT | `/execucoes/{codigo}` | 201 Created | Atualiza execução |
| DELETE | `/execucoes/{codigo}` | 204 No Content | Remove execução |

### Execuções - Fluxo de Processo

| Método | URI | Status de Sucesso | Descrição |
|--------|-----|-------------------|-----------|
| POST | `/execucoes/iniciar` | 201 Created | Inicia execução de um processo para um usuário |
| GET | `/execucoes/processo/{codProcesso}/usuario/{codUsuario}` | 200 OK | Busca execução atual de um usuário em um processo |
| PUT | `/execucoes/{codExecucao}/etapas/{codEtapa}/concluir` | 200 OK | Conclui etapa atual e avança para próxima |
| PUT | `/execucoes/{codExecucao}/finalizar` | 200 OK | Finaliza a execução do processo |

## Exemplos de JSON

### UsuarioTO
```json
{
  "codigo": 1,
  "nome": "João Silva",
  "email": "joao.silva@flow.com",
  "cargo": "Analista de Processos"
}
```

### ProcessoTO
```json
{
  "codigo": 1,
  "titulo": "Processo de Onboarding",
  "descricao": "Processo de integração de novos colaboradores",
  "dataCriacao": "2024-01-15"
}
```

### EtapaTO
```json
{
  "codigo": 1,
  "codProcesso": 1,
  "titulo": "Cadastro de Dados",
  "descricao": "Cadastro inicial do novo colaborador no sistema",
  "ordem": 1,
  "tempoEstimadoMin": 30
}
```

### ExecucaoTO
```json
{
  "codigo": 1,
  "codUsuario": 1,
  "codProcesso": 1,
  "codEtapa": 1,
  "dataExecucao": "2024-01-20",
  "status": "CONCLUIDA",
  "observacao": "Cadastro realizado com sucesso"
}
```

### Exemplo - Iniciar Execução
```json
POST /execucoes/iniciar
{
  "codProcesso": 1,
  "codUsuario": 1
}
```

## Modelo Entidade-Relacionamento (MER)

### Descrição

O modelo possui 4 entidades principais:

1. **DDD_USUARIOS**: Armazena informações dos usuários do sistema
2. **DDD_PROCESSOS**: Representa os processos operacionais padronizados
3. **DDD_ETAPAS**: Etapas que compõem um processo (relacionamento 1-N com processos)
4. **DDD_EXECUCOES**: Registra as execuções de etapas por usuários (relacionamento N-N entre usuários e etapas)

### Relacionamentos

- **Processo 1 — N Etapas**: Um processo possui várias etapas
- **Etapa 1 — N Execuções**: Uma etapa pode ser executada várias vezes
- **Usuário 1 — N Execuções**: Um usuário pode executar várias etapas

### Diagrama ASCII

```
┌─────────────────┐
│ DDD_USUARIOS    │
├─────────────────┤
│ codigo (PK)     │
│ nome            │
│ email           │
│ cargo           │
└────────┬────────┘
         │
         │ 1
         │
         │ N
┌────────▼────────┐
│ DDD_EXECUCOES   │
├─────────────────┤
│ codigo (PK)     │
│ cod_usuario (FK)│
│ cod_etapa (FK)  │
│ data_execucao   │
│ status          │
│ observacao      │
└────────┬────────┘
         │
         │ N
         │
         │ 1
┌────────▼────────┐
│ DDD_ETAPAS      │
├─────────────────┤
│ codigo (PK)     │
│ cod_processo(FK)│
│ titulo          │
│ descricao       │
│ ordem           │
│ tempo_estimado  │
└────────┬────────┘
         │
         │ N
         │
         │ 1
┌────────▼────────┐
│ DDD_PROCESSOS   │
├─────────────────┤
│ codigo (PK)     │
│ titulo          │
│ descricao       │
│ data_criacao    │
└─────────────────┘
```

## Diagrama de Classes

### Classes TO (Transfer Objects)
- **UsuarioTO**: Modelo de dados para usuários
- **ProcessoTO**: Modelo de dados para processos
- **EtapaTO**: Modelo de dados para etapas
- **ExecucaoTO**: Modelo de dados para execuções

### Classes DAO (Data Access Objects)
- **UsuarioDAO**: Acesso a dados de usuários
- **ProcessoDAO**: Acesso a dados de processos
- **EtapaDAO**: Acesso a dados de etapas
- **ExecucaoDAO**: Acesso a dados de execuções

### Classes BO (Business Objects)
- **UsuarioBO**: Regras de negócio para usuários
- **ProcessoBO**: Regras de negócio para processos
- **EtapaBO**: Regras de negócio para etapas (valida ordem duplicada)
- **ExecucaoBO**: Regras de negócio para execuções (valida status)

### Classes Resource (Endpoints REST)
- **UsuarioResource**: Endpoints REST para usuários
- **ProcessoResource**: Endpoints REST para processos
- **EtapaResource**: Endpoints REST para etapas
- **ExecucaoResource**: Endpoints REST para execuções

### Classes de Infraestrutura
- **ConnectionFactory**: Factory para conexões JDBC com Oracle
- **CorsFilter**: Filtro CORS para permitir requisições cross-origin
- **BusinessException**: Exceção de domínio para regras de negócio

## Regras de Negócio

### EtapaBO
- **save/update**: Não permite ordem duplicada dentro do mesmo processo. Lança `BusinessException` se violado.

### ExecucaoBO
- **save**: Status inicial deve ser "PENDENTE" ou "EM_ANDAMENTO". Lança `BusinessException` se diferente.
- **update**: Permite mudar para "CONCLUIDA" apenas se já existia e não estiver pendente sem data. Lança `BusinessException` se violado.

## Como Testar no Insomnia

### 1. Configurar Workspace
1. Abra o Insomnia
2. Crie um novo workspace chamado "FLOW API"

### 2. Criar Requisições

#### GET - Listar Usuários
- **Método**: GET
- **URL**: `http://localhost:8081/usuarios`
- **Headers**: Nenhum necessário

#### GET - Buscar Usuário por Código
- **Método**: GET
- **URL**: `http://localhost:8081/usuarios/1`

#### POST - Criar Usuário
- **Método**: POST
- **URL**: `http://localhost:8081/usuarios`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (JSON):
```json
{
  "nome": "Pedro Costa",
  "email": "pedro.costa@flow.com",
  "cargo": "Desenvolvedor"
}
```

#### PUT - Atualizar Usuário
- **Método**: PUT
- **URL**: `http://localhost:8081/usuarios/1`
- **Headers**: 
  - `Content-Type: application/json`
- **Body** (JSON):
```json
{
  "nome": "João Silva Atualizado",
  "email": "joao.silva@flow.com",
  "cargo": "Analista Sênior"
}
```

#### DELETE - Remover Usuário
- **Método**: DELETE
- **URL**: `http://localhost:8081/usuarios/1`

### 3. Testar Outros Endpoints

Repita o mesmo padrão para:
- `/processos`
- `/etapas`
- `/execucoes`

### 4. Testar Endpoints Especiais

#### GET - Etapas por Processo
- **Método**: GET
- **URL**: `http://localhost:8081/etapas/processo/1`

#### GET - Execuções por Usuário
- **Método**: GET
- **URL**: `http://localhost:8081/execucoes/usuario/1`

### 5. Testar Validações

#### Teste de Validação de Bean Validation
Tente criar um usuário sem nome:
```json
{
  "email": "teste@flow.com",
  "cargo": "Teste"
}
```
Deve retornar 400 Bad Request.

#### Teste de Regra de Negócio - Ordem Duplicada
Tente criar duas etapas com a mesma ordem no mesmo processo:
```json
{
  "codProcesso": 1,
  "titulo": "Etapa Teste",
  "ordem": 1,
  "tempoEstimadoMin": 30
}
```
Deve retornar 400 Bad Request com mensagem: "Já existe uma etapa com essa ordem para este processo."

#### Teste de Regra de Negócio - Status Inválido
Tente criar uma execução com status "CONCLUIDA" diretamente:
```json
{
  "codUsuario": 1,
  "codEtapa": 1,
  "dataExecucao": "2024-01-20",
  "status": "CONCLUIDA",
  "observacao": "Teste"
}
```
Deve retornar 400 Bad Request com mensagem: "Status inicial deve ser PENDENTE ou EM_ANDAMENTO."

### 6. Verificar CORS

Teste fazer requisições de um frontend em `http://localhost:3000` (ou outra porta). O CORS deve permitir as requisições devido ao `CorsFilter` configurado.

## Observações Importantes

- Todos os endpoints retornam JSON
- Status HTTP seguem o padrão RESTful (200, 201, 204, 400, 404)
- Validações são feitas via Bean Validation
- Regras de negócio são validadas nos BOs
- CORS está habilitado para todas as origens
- O projeto segue estritamente o padrão visto nas aulas, sem tecnologias extras

