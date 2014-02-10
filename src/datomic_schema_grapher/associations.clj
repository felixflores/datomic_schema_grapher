(ns datomic-schema-grapher.associations
  (:require [datomic.api :as d]))

(defn- references-ids
  [attr-name database]
  (-> (d/q '[:find ?refs
             :in $ ?attr-name
             :where
             [_ ?attr-name ?refs]]
           database
           attr-name)
      first))

(defn- entities
  [attr-name database]
  (map #(d/entity database %) (references-ids attr-name database)))

(defn referencing-namepspaces
  [attr-name database]
  (->> (entities attr-name database)
       (map keys)
       (flatten)
       (map namespace)
       (set)))
