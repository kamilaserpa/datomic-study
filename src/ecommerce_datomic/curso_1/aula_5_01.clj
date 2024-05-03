(ns ecommerce-datomic.curso-1.aula-5-01
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.model :as model]))

;(ecommerce.db/deleta-banco)

(let [calculadora-lg      (model/novo-produto "Calculadora LG" "/calcualdora-lg" 129.99M)
      tv-philips          (model/novo-produto "TV Philips" "/tv-philips" 3500.0M)
      resultado-transacao @(d/transact ecommerce.core/conn [calculadora-lg tv-philips])]
  (pprint resultado-transacao))

(def snapshot-do-passado (d/db ecommerce.core/conn))

(let [computador-acer     (model/novo-produto "Computador Acer" "/computador-acer" 2700.0M)
      celular-xiaomi      (model/novo-produto "Celular Xiaomi" "/celular-xiaomi" 1500.0M)
      resultado-transacao @(d/transact ecommerce.core/conn [computador-acer celular-xiaomi])]
  (pprint resultado-transacao))

(pprint "Snapshot em instante atual do d/db")
(pprint (count (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db ecommerce.core/conn))))

(pprint "Snapshot em instante no passado do d/db (para finsd e auditoria, investigação de logs por exemplo). Ambos retornam apenas 2 produtos")
(pprint (count (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 snapshot-do-passado)))

(pprint (count (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/as-of (d/db ecommerce.core/conn)
                                                                               #inst "2024-04-03T17:45:26.512-00:00")))) ; datetime capturado dos primeiros logs de criação de entidade
