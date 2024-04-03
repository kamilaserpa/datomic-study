(ns ecommerce-datomic.aula-6-01
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.model :as model]))

;(ecommerce.db/deleta-banco)

(let [calculadora-lg      (model/novo-produto "Calculadora LG" "/calculadora-lg" 129.99M)
      tv-philips          (model/novo-produto "TV Philips" "/tv-philips" 3500.0M)
      computador-acer     (model/novo-produto "Computador Acer" "/computador-acer" 2700.0M)
      celular-xiaomi      (model/novo-produto "Celular Xiaomi" "/celular-xiaomi" 1500.0M)
      resultado-transacao @(d/transact ecommerce.core/conn [calculadora-lg tv-philips computador-acer celular-xiaomi])]
  (pprint resultado-transacao))

(ecommerce.db/produtos-com-preco-maior-que (d/db ecommerce.core/conn) 2000)


