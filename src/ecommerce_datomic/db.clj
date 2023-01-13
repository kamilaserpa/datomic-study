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

(defn cria-schema
  [conn]
  (d/transact conn produto-schema))

