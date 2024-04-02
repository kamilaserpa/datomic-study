(ns ecommerce-datomic.aula-1-04
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.model :as model]))
; Obs: execute core.clj primeiro!

; SNAPSHOT
; Banco (apenas para leitura) no instante em que você executar essa linha
; Como foi executada antes de inserir o celular, ao buscar ele nao encontra o celular
(def db-instant-1 (d/db ecommerce.core/conn))

; FIND / GET
; Buscando nos datoms um em que o primeiro valor pode ser qualqer um (?entidade) e possui valor :produto/nome
(d/q '[:find ?entidade
       :where [?entidade :produto/nome]] db-instant-1)

(let [celular (model/novo-produto "Celular caro", "/celular", 888.10M)]
  (d/transact ecommerce.core/conn [celular]))

; SNAPSHOT
; Nova conexão de leitura em outro momento, outra fotografia do banco
(def db-instant-2 (d/db ecommerce.core/conn))

(d/q '[:find ?entidade ?nome
       :where [?entidade :produto/nome ?nome]] db-instant-2)
