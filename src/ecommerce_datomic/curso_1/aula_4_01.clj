(ns ecommerce-datomic.curso-1.aula-4-01
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.core :as ecommerce.core]))

; Return as map [#:produto{:nome "Celular Motorola", :preco 876.0M}...]
(pprint (ecommerce.db/todos-os-produtos-com-preco-e-nome-map (d/db ecommerce.core/conn)))

; Return all listed attributes
(pprint (ecommerce.db/todos-os-produtos-com-nome-full-entity-1 (d/db ecommerce.core/conn)))

; Return all attributes form entity
(pprint (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db ecommerce.core/conn)))
