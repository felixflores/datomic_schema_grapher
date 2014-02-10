(ns datomic-schema-grapher.associated
  (:require [datomic.api :as d]))

(defn entities
  [attr-name database]
  (->> (d/q '[:find ?refs
              :in $ ?attr-name
              :where
              [_ ?attr-name ?refs]]
            database
            attr-name)
       (first)
       (map #(d/entity database %))))


(defn namepspaces
  [entities]
  (->> (map keys entities)
       (flatten)
       (map namespace)
       (set)))
