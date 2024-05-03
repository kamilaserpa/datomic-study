(ns ecommerce-datomic.curso-1.aula-2-01
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.core :as ecommerce.core]
            [ecommerce-datomic.db :as ecommerce.db]
            [ecommerce-datomic.model :as model]))

; Inserindo dados sem algumas propriedades
(let [calculadora {:produto/nome "Calculadora com 4 operações"}]
  (d/transact ecommerce.core/conn [calculadora]))

; EXCEPTION - Inserir dados com propriedades NIL resulta em Exception
; Se quiser colocar algo vazio, é só nao inserir a propriedade
;(let [radio-relogio {:produto/nome "Rádio com relógio" :produto/slug nil}]
;   (d/transact conn [radio-relogio])))

(let [celular-barato      (model/novo-produto "Celular barato" "/celular-barato" 350.0M)
      resultado           @(d/transact ecommerce.core/conn [celular-barato]) ; @ indica que vamos esperar a execução do future e capturar o resultado, precisamos derreferenciar
      id-entidade-celular (first (vals (:tempids resultado)))]   ; tempids é um mapa com chave/valor, o valor, para retirar do vetor utilizamos o first
  (pprint ["Resultado: " resultado])
  ; UPDATE
  (pprint @(d/transact ecommerce.core/conn [[:db/add id-entidade-celular :produto/preco 0.1M]]))

  ; DELETE - (retract) a partir desse instante :produto/slug "/celular-barato" não estará presente
  (pprint @(d/transact ecommerce.core/conn [[:db/retract id-entidade-celular :produto/slug "/celular-barato"]])))

(ecommerce.db/deleta-banco)
