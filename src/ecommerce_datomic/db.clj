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

(defn todos-os-produtos [db]
  (d/q '[:find ?entidade ?nome
         :where [?entidade :produto/nome ?nome]]
       db))

(defn todos-os-produtos-por-slug-fixo [db]
  (d/q '[:find ?entidade
         :where [?entidade :produto/slug "/computador-dell"]]
       db))

; Pode ser extraída a query para ser reaproveitada em outra função
; "-q" notação húngara informanto o tipo, não recomendada, muito menos abreviada
(def todos-os-produtos-por-slug-query
  '[:find ?entidade
    :where [?entidade :produto/slug slug-param]])

(defn todos-os-produtos-por-slug [db slug-param]
  (d/q '[:find ?entidade
         :in $ ?slug
         :where [?entidade :produto/slug ?slug]]
       db slug-param))


