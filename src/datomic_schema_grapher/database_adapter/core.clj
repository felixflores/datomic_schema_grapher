(ns datomic-schema-grapher.database-adapter.core
  (:require [datomic-schema-grapher.database-adapter.associated :as associated]
            [datomic-schema-grapher.database-adapter.attributes :as attributes]))

(defn all-attributes
  "Returns all user defined datomic attribute as entities,
  grouped by their common namespace."
  [database]
  (attributes/all database))

(defn referenced-namespaces
  "Returns all entities the references a given datomic attribute."
  [attribute database]
  (associated/namespaces attribute database))
