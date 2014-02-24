(ns datomic-schema-grapher.database
  (:require [datomic.api :as d]))

(defn datomic-attribute?
  [identifier]
  ((complement empty?) (re-matches #"(db|fressian).*" (namespace identifier))))

(defn namespaces
  "Returns all user defined datomic attribute as entities,
  grouped by their common namespace."
  [database]
  (->> (d/q '[:find ?attr ?name
              :where
              [_ :db.install/attribute ?attr]
              [?attr :db/ident ?name]]
            database)
       (remove #(datomic-attribute? (last %)))
       (map #(d/entity database (first %)))
       (group-by #(namespace (:db/ident %)))))

(defn ref-attrs
  "Returns all entities the references a given datomic attribute."
  [attr-name database]
  (->>  (d/q '[:find ?ref-name
               :in $ ?attr-name
               :where
               [_ ?attr-name ?ref]
               [?ref ?ref-attr]
               [?ref-attr :db/ident ?ref-name]]
             database
             attr-name)
       (map first)
       (map namespace)
       (set)))
