(ns ecommerce-datomic.model
  (:import (java.util UUID)))

(defn generate-uuid
  []
  (UUID/randomUUID))

(defn novo-produto
  ([nome slug preco]
   (novo-produto (generate-uuid) nome slug preco))
  ([uuid nome slug preco]
   {:produto/id    uuid
    :produto/nome  nome
    :produto/slug  slug
    :produto/preco preco}))

