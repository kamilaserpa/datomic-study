(ns ecommerce-datomic.curso-2.aula-1-03
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.model :as model]))

; (ecommerce.db/deleta-banco)

(def conn (ecommerce.db/abre-conexao))

(ecommerce.db/cria-produto-schema conn)

(let [computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M)
      celular (model/novo-produto "Celular Caro", "/celular", 888888.10M)
      calculadora {:produto/nome "Calculadora com 4 operações"}
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M)]
  (pprint @(d/transact conn [computador, celular, calculadora, celular-barato])))

(def produtos (ecommerce.db/todos-os-produtos-com-nome-full-entity-2  (d/db conn)))
(def primeiro-produto-id (-> produtos
                             first
                             first
                             :db/id))
(println "O id do primeiro produto encontrado é:" primeiro-produto-id)
(pprint (ecommerce.db/find-produto (d/db conn) primeiro-produto-id))

