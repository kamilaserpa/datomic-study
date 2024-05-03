(ns ecommerce-datomic.curso-1.aula-1-03
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.model :as model]))

; INSERT
; d/transact recebe uma conexão e uma sequência com uma entidade
(let [computador (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)]
  (d/transact ecommerce.core/conn [computador]))
