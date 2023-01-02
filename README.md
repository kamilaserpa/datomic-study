# Datomic: ecommerce

Aplicação clojure para estudo do bando de dados Datomic, em acompanhamento da formação Datomic da plataforma Alura. Formato de negócio ecommerce.

## Instalação

### Servidor

O download da versão Starter pode ser feito em https://www.datomic.com/get-datomic.html, uma conta deve ser criada. A versão aqui utilizada foi `1.0.6269`, pois as mais atuais não foram compatíveis.

Ao realizar o download recebemos a licença por e-mail, devemos inserir a licença no arquivo `config/samples/dev-transactor-template.properties`.
Após inserir a licença deve-se salvar o arquivo e copiá-lo na pasta `datomic-pro-x.x.xxx/config`.

Necessário o maven para instalação. Exporte a variável maven com:
> export PATH=$PATH:/Users/kamila.serpa/Documents/Apps/apache-maven-3.8.6/bin

Para instalar execute o comando `bin/maven-install` na pasta do Datomic. Para verificar a versão instalada execute o comando `cd ~/.m2/repository/com/datomic/dev-local/`, `ls` para listar as pastas, deve estar presemte a pasta com o nome da versão instalada.

Para executar o servidor Datomic acesse o terminal na pasta do Datomic e execute `bin transactor <LOCAL/NOME-DO-ARQUIVO.properties>`, por exemplo:

 > bin/transactor config/dev-transactor-template.properties

Espera-se que seja exibida a porta em que o banco de dados servidor está sendo executado:
![Datomic servidor em execução](./images/bin-transactor.png)

### Conexão com servidor

### Forma 1
Para usar Datomic API e criar um banco de dados vamos utilizar o leiningen conforme a [documentação](https://docs.datomic.com/on-prem/peer/integrating-peer-lib.html#leiningen).

Adicione a dependência no arquivo `project.clj :dependencies` (entre aspas insira a versão utilizada no passo anterior):
 > [com.datomic/datomic-pro "1.0.6269"]

A biblioteca não é disponibilizada de forma aberta, então deve-se acessar a página https://my.datomic.com/downloads/pro. Estando logado no site, clique em _My account_ e veja ao final como baixar via leiningen.
Crie o arquivo `credentials.clj.gpg` e adicione no diretório `.lein` no formato

 > {#"my\.datomic\.com" {:username " ************ " 
 >                       :password " ************ "}}

<b>Optamos por esta maneira neste projeto.</b>

### Forma 2

Acesse https://www.datomic.com/ e clique em `dev-local`, em seguida clique em "how to get and configure dev-local" e "get the latest version of dev-tools", dessa maneira será enviado por e-mail o link para download.

Após realizado o download, descompacte o arquivo, acesse o terminal dentro da pasta (cognitect-dev-tools-0.9.72) e execute o comando `./install`.

Necessário o maven para instalação. Exporte a variável maven:
> export PATH=$PATH:/Users/kamila.serpa/Documents/Apps/apache-maven-3.8.6/bin
 
Para verificar a versão instalada execute o comando `cd ~/.m2/repository/com/datomic/dev-local/`, `ls` para listar as pastas.

Adicione essa versão no project.clj, segundo a [documentação](https://docs.datomic.com/on-prem/peer/integrating-peer-lib.html#leiningen):
 > [com.datomic/dev-local "1.0.243"]

### Testando instalação
Vamos rodar nosso project.clj no REPL. 
Vamos rodar esse código que vai requerer a biblioteca datomic. Em seguida definimos um símbolo com os dados da conexão localhost na porta 4334 em um banco de dados chamado "hello" e criamos o banco de dados através do comando `d/create-database` passando o símbolo `db-url`.

```clojure
  (require '[datomic.api :as d])
  (def db-url "datomic:dev://localhost:4334/hello")
  (d/create-database db-url)
```

Será devolvido true, temos um banco de dados criado. Se tentarmos de novo criar esse banco, devolve false porque ele já existe.
