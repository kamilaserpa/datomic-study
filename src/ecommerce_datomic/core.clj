(ns ecommerce-datomic.core
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as db]
            [ecommerce-datomic.model :as model]))

(def conn (db/abre-conexao))
(pprint conn)

(db/cria-schema conn)

; INSERT
(let [computador
      (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)]
  (d/transact conn [computador]))

; Banco apenas para leitura no instante em que você executou a linha, antes de inserir o celular
; Por isso ao buscar ele nao encontra o celular
(def db (d/db conn))

; Buscando nos datoms um em que o primeiro valor pode ser qualqer um (?entidade) e o segundo é :produto/nome, por exemplo
; 15 :produto/nome "Computador Novo" ID_TX
(d/q '[:find ?entidade
       :where [?entidade :produto/nome]] db)

(let [celular
      (model/novo-produto "Celular caro", "/celular", 888.10M)]
  (d/transact conn [celular]))

; SNAPSHOT
(def db (d/db conn)) ; Nova conexão de leitura em outro momento, outra fotografia do banco

(d/q '[:find ?entidade ?nome
       :where [?entidade :produto/nome ?nome]] db)


