(ns datomic-schema-grapher.database-adapter.associated
  (:require [datomic.api :as d]))

(defn ref-attrs
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
