(ns ecommerce-datomic.curso-2.aula-2
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.model :as model]))

(ecommerce.db/deleta-banco)

(def conn (ecommerce.db/abre-conexao))

(ecommerce.db/cria-schema conn)

(def computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M))
(def celular (model/novo-produto "Celular Caro", "/celular", 888888.10M))
(def calculadora {:produto/nome "Calculadora com 4 operações"})
(def celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M))


(pprint @(d/transact conn [computador, celular, calculadora, celular-barato]))

(def produtos (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db conn)))
(println "Todos os produtos são: ")
(pprint produtos)


; Novo objeto utilizando id (db/unique) já persistido, ao ser transacionado atualiza a entidade daquele :id já existente

(def celular-barato-2 (model/novo-produto (:produto/id celular-barato) "Celular Barato!!", "/celular-barato-2", 0.01M))
(println "Celular barato 2:" celular-barato-2)
(pprint @(d/transact conn [celular-barato-2]))


(def produtos (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db conn)))
(pprint produtos)

