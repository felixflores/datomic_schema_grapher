(ns datomic-schema-grapher.database-adapter.attributes
  (:require [datomic.api :as d]))

(defn datomic-attribute?
  [identifier]
  ((complement empty?) (re-matches #"(db|fressian).*" (namespace identifier))))

(defn all
  [database]
  (->> (d/q '[:find ?attr ?name
              :where
              [_ :db.install/attribute ?attr]
              [?attr :db/ident ?name]]
            database)
       (remove #(datomic-attribute? (last %)))
       (map #(d/entity database (first %)))))

(defn namespaced
  [attributes]
  (group-by #(namespace (:db/ident %)) attributes))
