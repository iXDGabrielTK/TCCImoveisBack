-- Tabela Usuario
CREATE TABLE usuario (
                         ID SERIAL PRIMARY KEY,
                         NOME VARCHAR(100) NOT NULL,
                         LOGIN VARCHAR(50) UNIQUE NOT NULL,
                         SENHA VARCHAR(100) NOT NULL,
                         TELEFONE VARCHAR(20),
                         TIPO_USUARIO VARCHAR(31) -- Corrigido o tipo do campo
);

-- Tabela Endereco
CREATE TABLE endereco (
                          ID SERIAL PRIMARY KEY,
                          RUA VARCHAR(255) NOT NULL,
                          NUMERO VARCHAR(255) NOT NULL, -- Corrigido o tamanho
                          COMPLEMENTO VARCHAR(255),
                          BAIRRO VARCHAR(255) NOT NULL,
                          CIDADE VARCHAR(255) NOT NULL,
                          ESTADO VARCHAR(2) NOT NULL,
                          CEP VARCHAR(255) NOT NULL
);

-- Tabela Imovel
CREATE TABLE imovel (
                        ID SERIAL PRIMARY KEY,
                        TIPO_IMOVEL VARCHAR(255) NOT NULL,
                        TAMANHO_IMOVEL FLOAT,
                        PRECO_IMOVEL DECIMAL(15,2) NOT NULL,
                        STATUS_IMOVEL BOOLEAN NOT NULL, -- Adicionado o campo
                        DESCRICAO_IMOVEL VARCHAR(255), -- Adicionado o campo
                        ENDERECO_ID INT REFERENCES endereco(ID) ON DELETE SET NULL,
                        HISTORICO_MANUTENCAO VARCHAR(255), -- Corrigido o tipo
                        USUARIO_ID INT REFERENCES usuario(ID) ON DELETE SET NULL,
                        FUNCIONARIO_ID INT REFERENCES funcionario(ID) ON DELETE SET NULL
);

-- Tabela Visitante
CREATE TABLE visitante (
                           ID SERIAL PRIMARY KEY,
                           USUARIO_ID INT REFERENCES usuario(ID) ON DELETE CASCADE,
                           DATA_ACESSO TIMESTAMP, -- Adicionado o campo
                           DATA_CADASTRO TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Adicionado o campo
);

-- Tabela Funcionario
CREATE TABLE funcionario (
                             ID SERIAL PRIMARY KEY,
                             USUARIO_ID INT REFERENCES usuario(ID) ON DELETE CASCADE,
                             CPF VARCHAR(11) UNIQUE NOT NULL
);

-- Tabela Favoritos
CREATE TABLE favoritos (
                           VISITANTE_ID INT REFERENCES visitante(ID) ON DELETE CASCADE,
                           IMOVEL_ID INT REFERENCES imovel(ID) ON DELETE CASCADE,
                           PRIMARY KEY (VISITANTE_ID, IMOVEL_ID)
);

-- Tabela FotosImovel
CREATE TABLE fotos_imovel (
                              ID SERIAL PRIMARY KEY,
                              IMOVEL_ID INT REFERENCES imovel(ID) ON DELETE CASCADE,
                              URL_FOTO_IMOVEL VARCHAR(1000) NOT NULL -- Corrigido o tamanho
);

-- Tabela Agendamento
CREATE TABLE agendamento (
                             ID SERIAL PRIMARY KEY,
                             DATA_AGENDAMENTO DATE NOT NULL,
                             NOME_VISITANTE VARCHAR(100) NOT NULL,
                             IMOVEL_ID INT REFERENCES imovel(ID) ON DELETE CASCADE,
                             USUARIO_ID BIGINT REFERENCES usuario(ID),
                             HORARIO_MARCADO BOOLEAN DEFAULT FALSE NOT NULL, -- Corrigido
                             CANCELADO BOOLEAN DEFAULT FALSE -- Adicionado o campo
);

-- Tabela Vistoria
CREATE TABLE vistoria (
                          ID SERIAL PRIMARY KEY,
                          IMOVEL_ID INT REFERENCES imovel(ID) ON DELETE CASCADE,
                          TIPO_VISTORIA VARCHAR(255),
                          LAUDO_VISTORIA TEXT,
                          FOTO_VISTORIA TEXT[],
                          DATA_VISTORIA DATE NOT NULL
);

-- Tabela FotosVistoria
CREATE TABLE fotos_vistoria (
                                ID SERIAL PRIMARY KEY,
                                VISTORIA_ID INT REFERENCES vistoria(ID) ON DELETE CASCADE,
                                URL_FOTO_VISTORIA VARCHAR(1000) NOT NULL -- Corrigido o tamanho
);

-- Tabela LogAcesso
CREATE TABLE log_acesso (
                            ID BIGSERIAL PRIMARY KEY,
                            USUARIO_ID BIGINT NOT NULL REFERENCES usuario(ID) ON DELETE CASCADE,
                            DATA_HORA TIMESTAMP NOT NULL,
                            ACAO VARCHAR(50) NOT NULL
);
