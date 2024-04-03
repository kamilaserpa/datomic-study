(ns ecommerce-datomic.aula-6-02
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.model :as model]))

;(ecommerce.db/deleta-banco)
; After delete database Load core and db

(let [calculadora-lg      (model/novo-produto "Calculadora LG" "/calculadora-lg" 129.99M)
      tv-philips          (model/novo-produto "TV Philips" "/tv-philips" 3500.0M)
      computador-acer     (model/novo-produto "Computador Acer" "/computador-acer" 2700.0M)
      celular-xiaomi      (model/novo-produto "Celular Xiaomi" "/celular-xiaomi" 1500.0M)
      celular-sansumg     (model/novo-produto "Celular Sansumg" "/celular-sansumg" 1350.0M)
      resultado-transacao @(d/transact ecommerce.core/conn [calculadora-lg tv-philips computador-acer celular-xiaomi celular-sansumg])]
  (pprint resultado-transacao))

(defn filtra-produtos-por-nome
  [produtos
   nome]
  (->> produtos
       flatten
       (filter #(= nome (:produto/nome %)))
       first))

(def produtos (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db ecommerce.core/conn)))

(let [produto-tv-philips      (filtra-produtos-por-nome produtos "TV Philips")
      produto-computador-acer (filtra-produtos-por-nome produtos "Computador Acer")
      produto-celular-xiaomi  (filtra-produtos-por-nome produtos "Celular Xiaomi")
      produto-celular-sansumg (filtra-produtos-por-nome produtos "Celular Sansumg")]
  (pprint produtos)

  ; UPDATE - Adiciona valor em atributo de entidade existente
  (d/transact ecommerce.core/conn [[:db/add (:db/id produto-computador-acer) :produto/palavra-chave "computador"]
                                   [:db/add (:db/id produto-computador-acer) :produto/palavra-chave "desktop"]
                                   [:db/add (:db/id produto-tv-philips) :produto/palavra-chave "smart"]
                                   [:db/add (:db/id produto-celular-xiaomi) :produto/palavra-chave "smart"]
                                   [:db/add (:db/id produto-celular-sansumg) :produto/palavra-chave "smart"]]))

(ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db ecommerce.core/conn))

(pprint [">> Retract pro palavra chave: smart"
         (ecommerce.db/produtos-por-palavra-chave (d/db ecommerce.core/conn) "smart")])

; UPDATE - Remove valor de atributo em entidade existente
(let [produto-tv-philips      (filtra-produtos-por-nome produtos "TV Philips")]
  (d/transact ecommerce.core/conn [[:db/retract (:db/id produto-tv-philips) :produto/palavra-chave "smart"]]))

(pprint [">> Retract pro palavra chave: smart"
         (ecommerce.db/produtos-por-palavra-chave (d/db ecommerce.core/conn) "smart")])

