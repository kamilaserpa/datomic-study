(ns ecommerce-datomic.aula-2-02
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.model :as model]))

(let [computador-dell      (model/novo-produto "Computador Dell" "/computador-dell" 3700.0M)
      celular-motorola     (model/novo-produto "Celular Motorola" "/celular-motorola" 876.0M)
      celular-sansumg      (model/novo-produto "Celular Sansumg" "/celular-sansumg" 1350.0M)
      calculadora-portatil (model/novo-produto "Calculadora Portátil" "/calculadora-portatil" 9.99M)
      mouse-logitech {:produto/nome "Mouse Logitech"}]

  (d/transact ecommerce.core/conn [computador-dell celular-motorola celular-sansumg calculadora-portatil mouse-logitech]))

; Lembrando! (d/db ecommerce.core/conn) é um snapshot, uma fotografia do banco no momento da execução
(pprint (ecommerce.db/todos-os-produtos-com-nome-ids (d/db ecommerce.core/conn)))
