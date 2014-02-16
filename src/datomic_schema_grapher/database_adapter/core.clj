(ns datomic-schema-grapher.database_adapter.core
  (:require [datomic-schema-grapher.database_adapter.associated :as associated]
            [datomic-schema-grapher.database_adapter.attributes :as attributes]))

(defn all-attributes
  [database]
  (attributes/all database))

(defn referenced-namespaces
  [attribute database]
  (associated/namespaces attribute database))
