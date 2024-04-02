(ns ecommerce-datomic.aula-3-01
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.core :as ecommerce.core]))

(pprint (ecommerce.db/todos-os-produtos-por-slug-fixo (d/db ecommerce.core/conn)))

(pprint (ecommerce.db/todos-os-produtos-por-slug (d/db ecommerce.core/conn) "/computador-dell"))
