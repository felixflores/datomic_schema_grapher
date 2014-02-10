(ns datomic-schema-grapher.associated
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

(defn entities
  [attr-name database]
  (map #(d/entity database %) (references-ids attr-name database)))

(defn namepspaces
  [entities]
  (->> (map keys entities)
       (flatten)
       (map namespace)
       (set)))
