(ns ecommerce-datomic.curso-2.aula-3
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.model :as model]))

(ecommerce.db/deleta-banco)
; ecommerce-datomic.core
(def conn (ecommerce.db/abre-conexao))

(ecommerce.db/cria-schema conn)

(def eletronicos (model/nova-categoria "Eletrônicos"))
(def esporte (model/nova-categoria "Esporte"))
(pprint @(d/transact conn [eletronicos esporte]))
(def categorias (ecommerce.db/todas-as-categorias (d/db conn)))
(println "> Todas as categorias são:")
(pprint categorias)

(def computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M))
(def celular (model/novo-produto "Celular Caro", "/celular", 888888.10M))
(def calculadora {:produto/nome "Calculadora com 4 operações"})
(def celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M))
(def tabuleiro-xadrez (model/novo-produto "Tabuleiro de Xadrez", "/tabuleiro-de-xadrez", 30M))


(pprint @(d/transact conn [computador
                           celular
                           calculadora
                           celular-barato
                           tabuleiro-xadrez]))

(def produtos (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db conn)))
(println "> Todos os produtos são: ")
(pprint produtos)

; Atualiza produto adicionando referência para categoria
(d/transact conn [[:db/add
                   [:produto/id (:produto/id computador)]
                   :produto/categoria
                   [:categoria/id (:categoria/id eletronicos)]]])
(pprint (ecommerce.db/busca-produto-por-uuid (d/db conn) (:produto/id computador)))
(d/transact conn [[:db/add
                   [:produto/id (:produto/id celular)]
                   :produto/categoria
                   [:categoria/id (:categoria/id eletronicos)]]])
(d/transact conn [[:db/add
                   [:produto/id (:produto/id celular-barato)]
                   :produto/categoria
                   [:categoria/id (:categoria/id eletronicos)]]])
(d/transact conn [[:db/add
                   [:produto/id (:produto/id tabuleiro-xadrez)]
                   :produto/categoria
                   [:categoria/id (:categoria/id esporte)]]])


(println "> Todos os produtos são: ")
(def produtos-atualizados (ecommerce.db/todos-os-produtos-com-nome-full-entity-2 (d/db conn)))
(pprint produtos-atualizados)