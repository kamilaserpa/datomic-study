(ns ecommerce-datomic.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn deleta-banco []
  (d/delete-database db-uri))

(def produto-schema [{:db/ident       :produto/nome
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
                      :db/doc         "O preço de um produto com precisão monetária"}])

(defn cria-produto-schema
  [conn]
  (d/transact conn produto-schema))

(defn todos-os-produtos
  [db]
  (d/q '[:find ?entidade ?nome
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

(defn todos-os-produtos-com-preco-e-nome
  "Nomear a entidade como ?produto e passar como variável na próxima linha faz com que as consultas
  se refiram a mesma entidade de produto. Datomic procura qualquer entidade que tenha esse atributo :produto/preco,
  por isso a nomenclatura de colocar o nome da entidade na frente."
  [db]
  (d/q '[:find ?nome, ?preco
         :where [?produto :produto/preco ?preco]
         [?produto :produto/nome ?nome]]
       db))


