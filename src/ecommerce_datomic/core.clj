(ns ecommerce-datomic.core
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as db]
            [ecommerce-datomic.model :as model]))

(def conn (db/abre-conexao))
(pprint conn)

(db/cria-schema conn)

(let [computador
      (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)]
  (d/transact conn [computador]))

