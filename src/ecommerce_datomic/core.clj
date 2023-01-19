(ns ecommerce-datomic.core
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce-datomic.db :as db]
            [ecommerce-datomic.model :as model]))

(def conn (db/abre-conexao))
(pprint conn)

(db/cria-schema conn)

; INSERT
(let [computador (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)]
  (d/transact conn [computador]))

; Banco apenas para leitura no instante em que você executou a linha, antes de inserir o celular
; Por isso ao buscar ele nao encontra o celular
(def db (d/db conn))

; FIND / GET
; Buscando nos datoms um em que o primeiro valor pode ser qualqer um (?entidade) e o segundo é :produto/nome, por exemplo
; 15 :produto/nome "Computador Novo" ID_TX
(d/q '[:find ?entidade
       :where [?entidade :produto/nome]] db)

(let [celular (model/novo-produto "Celular caro", "/celular", 888.10M)]
  (d/transact conn [celular]))

; SNAPSHOT
(def db (d/db conn))                                        ; Nova conexão de leitura em outro momento, outra fotografia do banco

(d/q '[:find ?entidade ?nome
       :where [?entidade :produto/nome ?nome]] db)

; Inserindo dados sem algumas propriedades
(let [calculadora {:produto/nome "Calculadora 4 operações"}]
  (d/transact conn [calculadora]))

; Inserir dados com propriedades NIL resulta em Exception
; Se quiser colocar algo vazio, é só nao inserir a propriedade
;(let [radio-relogio {:produto/nome "Rádio com relógio" :produto/slug nil}]
;   (d/transact conn [radio-relogio])))


(let [celular-barato (model/novo-produto "Celular barato" "/celular-barato" 350.0M)
      resultado @(d/transact conn [celular-barato])         ; @ indica que vamos esperar a execução do future e capturar o resultado, precisamos derreferenciar
      id-entidade (first (vals (:tempids resultado)))]      ; tempids é um mapa com chave/valor, o valor, para retirar do vetor utilizamos o first
  ;UPDATE
  (pprint @(d/transact conn [[:db/add id-entidade :produto/preco 0.1M]]))
  ;DELETE - a partir desse instante :produto/slug "/celular-barato" não estará presente
  (pprint @(d/transact conn [[:db/retract id-entidade :produto/slug "/celular-barato"]])))




