(ns ecommerce-datomic.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn deleta-banco []
  (d/delete-database db-uri))

(def db-schema [
                ; Produto
                {:db/ident       :produto/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity
                 :db/doc         "Identificador único do produto"}

                {:db/ident       :produto/nome
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db/doc         "O nome de um produto"}

                {:db/ident       :produto/slug
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one
                 :db/doc         "O caminho para acessar um produto via http"}

                {:db/ident       :produto/preco
                 :db/valueType   :db.type/bigdec
                 :db/cardinality :db.cardinality/one
                 :db/doc         "O preço de um produto com precisão monetária"}

                {:db/ident       :produto/palavra-chave
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/many
                 :db/doc         "Palavras-chave com características do produto"}

                {:db/ident       :produto/categoria
                 :db/valueType   :db.type/ref
                 :db/cardinality :db.cardinality/one
                 :db/doc         "Categoria do produto"}

                ; Categoria
                {:db/ident       :categoria/nome
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}

                {:db/ident       :categoria/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}])

(defn cria-schema
  [conn]
  (d/transact conn db-schema))

(defn todos-os-produtos-com-nome-ids
  [db]
  (d/q '[:find ?entidade
         :where [?entidade :produto/nome ?nome]]
       db))

(defn todos-os-produtos-com-nome-full-entity-1
  "Pull explícito"
  [db]
  (d/q '[:find (pull ?entidade [:produto/nome :produto/preco :produto/slug])
         :where [?entidade :produto/nome ?nome]]
       db))

(defn todos-os-produtos-com-nome-full-entity-2
  "Pull genérico, retorna todos os atributos, inclusive o :id"
  [db]
  (d/q '[:find (pull ?entidade [*])
         :where [?entidade :produto/nome ?nome]]
       db))

(defn todos-os-produtos-por-slug-fixo
  [db]
  (d/q '[:find ?entidade
         :where [?entidade :produto/slug "/computador-dell"]]
       db))

; Pode ser extraída a query para ser reaproveitada em outra função
; "-q" notação húngara informanto o tipo, não recomendada, muito menos abreviada
(def todos-os-produtos-por-slug-query
  '[:find ?entidade
    :where [?entidade :produto/slug slug-param]])

(defn todos-os-produtos-por-slug
  [db slug-param]
  (d/q '[:find ?entidade
         :in $ ?slug-param                                  ; Proprositalmente nomeado de forma diferente para não ser confundido com "slug-param" sem interrogação
         :where [?entidade :produto/slug ?slug-param]]
       db slug-param))

(defn todos-os-slugs
  "Busca entidade (?e) produto que possui algum valor em :produto/slug, como entidade não está sendo retornada substituímos por undderscore"
  [db]
  (d/q '[:find ?qualquer-slug
         :where [_ :produto/slug ?qualquer-slug]]
       db))

; Retorna tuplas com valores, sem nome de atributo, não é boa prática: #{["Celular Motorola" 876.0M] ["Calculadora Portátil" 9.99M] ...}
(defn todos-os-produtos-com-preco-e-nome
  "Nomear a entidade como ?produto e passar como variável na próxima linha faz com que as consultas
  se refiram a mesma entidade de produto. Datomic procura qualquer entidade que tenha esse atributo :produto/preco,
  por isso a nomenclatura de colocar o nome da entidade na frente."
  [db]
  (d/q '[:find ?nome, ?preco
         :where [?produto :produto/preco ?preco]
         [?produto :produto/nome ?nome]]
       db))

(defn todos-os-produtos-com-preco-e-nome-map
  [db]
  (d/q '[:find ?nome, ?preco
         :keys produto/nome, produto/preco
         :where [?produto :produto/preco ?preco]
         [?produto :produto/nome ?nome]]
       db))

(defn produtos-com-preco-maior-que
  "Em geral deixar as condições da mais restritiva para a menos restritiva"
  [db preco-minimo]
  (d/q '[:find ?nome, ?preco
         :in $, ?preco-minimo
         :keys produto/nome, produto/preco
         :where [?produto :produto/preco ?preco] ; pegar preco
                [(> ?preco ?preco-minimo)]       ; filtrar preco
                [?produto :produto/nome ?nome]]  ; só então pegar o nome
       db preco-minimo))

(defn produtos-por-palavra-chave
  [db palavra-chave-param]
  (d/q '[:find (pull ?produto [*])
         :in $, ?palavra-chave
         :where [?produto :produto/palavra-chave ?palavra-chave]]
       db palavra-chave-param))

(defn busca-produto-por-dbid [db produto-db-id]
  "Por padrão o pull usa o identificador do banco (:db/id)"
  (d/pull db '[*] produto-db-id))

(defn busca-produto-por-uuid [db produto-id]
  (d/pull db '[*] [:produto/id produto-id]))

(defn todas-as-categorias [db]
  (d/q '[:find (pull ?categoria [*])
         :where [?categoria :categoria/id]]
       db))


