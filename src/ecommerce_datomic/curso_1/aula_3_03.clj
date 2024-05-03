(ns ecommerce-datomic.curso-1.aula-3-03
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.core :as ecommerce.core]))

(pprint (ecommerce.db/todos-os-produtos-com-preco-e-nome (d/db ecommerce.core/conn)))
