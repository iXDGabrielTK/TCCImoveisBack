-- Tabela Usuario
CREATE TABLE usuario (
                         ID SERIAL PRIMARY KEY,
                         NOME VARCHAR(100) NOT NULL,
                         LOGIN VARCHAR(50) UNIQUE NOT NULL,
                         SENHA VARCHAR(100) NOT NULL,
                         TELEFONE VARCHAR(20),
                         TIPO_USUARIO BOOLEAN
);

-- Tabela Endereco
CREATE TABLE endereco (
                          ID SERIAL PRIMARY KEY,
                          RUA VARCHAR(255) NOT NULL,
                          NUMERO VARCHAR(10) NOT NULL,
                          COMPLEMENTO VARCHAR(50),
                          BAIRRO VARCHAR(100) NOT NULL,
                          CIDADE VARCHAR(100) NOT NULL,
                          ESTADO VARCHAR(2) NOT NULL,
                          CEP VARCHAR(10) NOT NULL
);

-- Tabela Imovel (agora antes das demais tabelas que a referenciam)
CREATE TABLE imovel (
                        ID SERIAL PRIMARY KEY,
                        TIPO_IMOVEL VARCHAR(50) NOT NULL,
                        TAMANHO_IMOVEL FLOAT,
                        PRECO_IMOVEL DECIMAL(15,2) NOT NULL,
                        STATUS_IMOVEL BOOLEAN NOT NULL,
                        DESCRICAO_IMOVEL TEXT,
                        ENDERECO_ID INT REFERENCES endereco(ID) ON DELETE SET NULL,
                        HISTORICO_MANUTENCAO TEXT,
                        USUARIO_ID INT REFERENCES usuario(ID) ON DELETE SET NULL,
                        FUNCINARIO_ID INT REFERENCES funcionario(ID) ON DELETE SET NULL
);

-- Tabela Visitante
CREATE TABLE visitante (
                           ID SERIAL PRIMARY KEY,
                           usuario_id INT REFERENCES usuario(ID) ON DELETE CASCADE
);

-- Tabela Funcionario
CREATE TABLE funcionario (
                             ID SERIAL PRIMARY KEY,
                             usuario_id INT REFERENCES usuario(ID) ON DELETE CASCADE,
                             CPF VARCHAR(11) UNIQUE NOT NULL
);

-- Tabela Favoritos
CREATE TABLE favoritos (
                           visitante_id INT REFERENCES visitante(ID) ON DELETE CASCADE,
                           imovel_id INT REFERENCES imovel(ID) ON DELETE CASCADE,
                           PRIMARY KEY (visitante_id, imovel_id)
);

-- Tabela FotosImovel
CREATE TABLE fotos_imovel (
                              ID SERIAL PRIMARY KEY,
                              IMOVEL_ID INT REFERENCES imovel(ID) ON DELETE CASCADE,
                              URL_FOTO_IMOVEL VARCHAR(255) NOT NULL
);

-- Tabela Agendamento
CREATE TABLE agendamento (
                             ID SERIAL PRIMARY KEY,
                             DATA_AGENDAMENTO DATE NOT NULL,
                             NOME_VISITANTE VARCHAR(100) NOT NULL,
                             IMOVEL_ID INT REFERENCES imovel(ID) ON DELETE CASCADE
);

-- Tabela Vistoria
CREATE TABLE vistoria (
                          ID SERIAL PRIMARY KEY,
                          imovel_id INT REFERENCES imovel(ID) ON DELETE CASCADE,
                          TIPO_VISTORIA VARCHAR(255),
                          LAUDO_VISTORIA TEXT,
                          FOTO_VISTORIA TEXT[],
                          DATA_VISTORIA DATE NOT NULL
);

-- Tabela FotosVistoria
CREATE TABLE fotos_vistoria (
                                ID SERIAL PRIMARY KEY,
                                vistoria_id INT REFERENCES vistoria(ID) ON DELETE CASCADE,
                                URL_FOTO_VISTORIA VARCHAR(255) NOT NULL
);


