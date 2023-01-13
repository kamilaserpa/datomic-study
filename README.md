# Datomic: ecommerce

Aplicação clojure para estudo do bando de dados Datomic, em acompanhamento da formação Datomic da plataforma Alura.
O formato de negócio é ecommerce.

### Developer
[Kamila Serpa](https://kamilaserpa.github.io)

[1]: https://www.linkedin.com/in/kamila-serpa/
[2]: https://gitlab.com/java-kamila
[3]: https://github.com/kamilaserpa

[![linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)][1]
[![gitlab](https://img.shields.io/badge/GitLab-330F63?style=for-the-badge&logo=gitlab&logoColor=white)][2]
[![github](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)][3]

## Curso 1 - Datomic: um banco cronológico

### 1.1 Instalação

#### Servidor

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

#### Conexão com servidor

##### Forma 1

_Optamos por esta maneira neste projeto!_ Para usar Datomic API e criar um banco de dados vamos utilizar o leiningen conforme a [documentação](https://docs.datomic.com/on-prem/peer/integrating-peer-lib.html#leiningen).

Adicione a dependência no arquivo `project.clj :dependencies` (entre aspas insira a versão utilizada no passo anterior):
 > [com.datomic/datomic-pro "1.0.6269"]

A biblioteca não é disponibilizada de forma aberta, então deve-se acessar a página https://my.datomic.com/downloads/pro. Estando logado no site, clique em _My account_ e veja ao final como baixar via leiningen.
Crie o arquivo `credentials.clj.gpg` e adicione no diretório `.lein` no formato

 > {#"my\.datomic\.com" {:username " ************ " 
 >                       :password " ************ "}}
 
##### Forma 2

Acesse https://www.datomic.com/ e clique em `dev-local`, em seguida clique em "how to get and configure dev-local" e "get the latest version of dev-tools", dessa maneira será enviado por e-mail o link para download.

Após realizado o download, descompacte o arquivo, acesse o terminal dentro da pasta (cognitect-dev-tools-0.9.72) e execute o comando `./install`.

Necessário o maven para instalação. Exporte a variável maven:
> export PATH=$PATH:/Users/kamila.serpa/Documents/Apps/apache-maven-3.8.6/bin
 
Para verificar a versão instalada execute o comando `cd ~/.m2/repository/com/datomic/dev-local/`, `ls` para listar as pastas.

Adicione essa versão no project.clj, segundo a [documentação](https://docs.datomic.com/on-prem/peer/integrating-peer-lib.html#leiningen):
 > [com.datomic/dev-local "1.0.243"]

#### Testando instalação
Vamos rodar nosso project.clj no REPL. 
Vamos rodar esse código que vai requerer a biblioteca datomic. Em seguida definimos um símbolo com os dados da conexão localhost na porta 4334 em um banco de dados chamado "hello" e criamos o banco de dados através do comando `d/create-database` passando o símbolo `db-url`.

```clojure
  (require '[datomic.api :as d])
  (def db-url "datomic:dev://localhost:4334/hello")
  (d/create-database db-url)
```

Será devolvido true, temos um banco de dados criado. Se tentarmos de novo criar esse banco, devolve false porque ele já existe.

### 1.3 Schema e transações

No Datomic, não vamos pensar em várias tabelas, mas sim uma grande tabela. Nela, por exemplo, para o produto cujo ID é 15, o nome será Computador Novo, o slug será /computador_novo, e preco de 31500.10.

Assim estaremos atribuindo ao ID da entidade id_entidade, e teremos o atributo (slug) e o valor. Como podemos ter várias entidades adicionamos o namespace (:produto) para diferenciar sua propriedades.
```clojure
; id_entidade atributo valor
; 15      :produto/nome Computador Novo
; 15      :produto/slug /computador_novo
; 15      :produto/preço 3500.10
```

Ao definir um schema para ser persistido no Datomic, indicamos o identificador (ident), o tipo de valor (valueType) e, no nosso caso, cardinalidade 1, já que um produto tem um nome.

```clojure
(def schema [{:db/ident :produto/nome
              :db/valueType :db.type/string 
              :db/cardinality :db.cardinality/one
              :db/doc "O nome de um produto"}]) 
```

Os tipos de valores possíveis (valueType) podem ser encontrados na doc [Schema | Datomic](https://docs.datomic.com/on-prem/schema/schema.html).

Utilizamos `Cmd + Shift + P` sobre uma função para forçar a sua execução no REPL e transacional esse objeto. O `transact` recebe a conexão e uma sequência, no nosso caso, um vetor com um único item.

![Console com informações após persistir dado](images/console-saved-object.png)
 Observamos que o quarto valor no banco indica o ID da transação "13194139534320", um valor que o Datomic gera para a entidade "1759...5425" e para cada atributo. Ou seja, para a entidade `...5425` o `72` tem valor "Computador Novo".

Além disso o booleano ao final indica se houve inclusão `true`, ou retirada `false` de dados.

