(ns ecommerce-datomic.core
  (:use clojure.pprint)
  (:require [ecommerce-datomic.db :as ecommerce.db]))

(def conn (ecommerce.db/abre-conexao))
(pprint conn)

(ecommerce.db/cria-schema conn)


