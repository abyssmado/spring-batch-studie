# Projeto Spring Batch
## Visão Geral
Este projeto utiliza o Spring Batch para configurar e executar um job que lê dados de um arquivo CSV e os insere em uma tabela de banco de dados PostgreSQL. O processo é dividido em chunks para melhor performance e controle transacional.

## Estrutura do Projeto
### Classes Principais
- BatchConfig: Classe de configuração do Spring Batch.
- User: Classe modelo que representa os dados a serem processados.
- DatasourceConfig: Classe de configuração do banco de dados.
### Dependências e Tecnologias
As principais dependências do projeto são:

`spring-boot-starter-batch`
`spring-boot-starter-jdbc`
`spring-boot-starter-data-jpa`
`postgresql`
<br>
<br>
Tecnologias utilizadas:
- Java 17
- SpringBoot
- Maven

## Configurações
### application.properties
Configurações para a conexão com o banco de dados PostgreSQL.

```
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver
```

## Execução
### Para executar o projeto, siga os passos abaixo:

1. Configure o banco de dados PostgreSQL com as credenciais apropriadas.
2. Note que o arquivo users.csv ja está no diretório files/. (Arquivo utilizado como base para a inserção)
3. Compile e execute o projeto utilizando o Spring Boot.

## Estrutura do Arquivo CSV
O arquivo CSV deve ter o seguinte formato:

```
-- name,age
John Doe,30
Jane Smith,25
```
*O arquivo users.csv que se encontra já na pasta "files" contém 200 linhas para insert*<br>
*Na pasta raiz do projeto existe um script python para geração do csv, podendo ser definida a quantidade de linhas geradas.*

# Banco de dados

Para este projeto utilizei do docker para instanciar meu banco de dados de forma simples.

## Como inicar seu banco?
Para instalar instanciar nosso banco de dados é necessário instalar o Daemon Docker. (*https://docs.docker.com/get-docker*)<br>
Após instalar abriremos nosso terminal e rodaremos os seguintes comandos:<br>
1. Para baixar a imagem do nosso banco de dados.
```
docker pull postgres
```
2. Para iniciar nosso banco de dados.
```
docker run -p 5432:5432 -v /tmp/database:/var/lib/postgresql/data -e POSTGRES_PASSWORD=123456 -d postgres
```
### Explicando o comando "docker run"
- docker run: Executa um novo container a partir da imagem especificada.
- -p 5432:5432: Mapeia a porta 5432 do container para a porta 5432 no host. Isso permite que você acesse o PostgreSQL no container a partir do seu host.
- -v /tmp/database:/var/lib/postgresql/data: Mapeia um diretório no seu sistema host (/tmp/database) para um diretório dentro do container (/var/lib/postgresql/data). Isso é usado para persistir dados do PostgreSQL fora do container, permitindo que eles não sejam perdidos quando o container for parado.
- -e POSTGRES_PASSWORD=123456: Define a variável de ambiente POSTGRES_PASSWORD no container, que configura a senha do usuário postgres do PostgreSQL como 123456.
- -d: Executa o container em segundo plano (modo detached).
- postgres: Nome da imagem do Docker a ser usada. Nesse caso, a imagem padrão do PostgreSQL é usada para criar o container.

*Não usando o comando -e POSTGRES_USER=meu_usuario e declarando o valor, o usuário padrão da intancia do banco de dados se chamara "postgres"*

### Script de criação da tabela "users"
```
create table if not exists public.users
(
    id   integer generated by default as identity
        constraint users_pk
            primary key,
    name varchar(100) not null,
    age  integer
);
```

