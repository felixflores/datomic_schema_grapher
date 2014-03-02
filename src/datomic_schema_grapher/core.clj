(ns datomic-schema-grapher.core
  (:require [datomic-schema-grapher.dot :as dot]
            [datomic-schema-grapher.datomic-test-helpers :refer :all]
            [datomic-schema-grapher.database :refer (schema references)]))

(def uri "datomic:mem://grapher")
(def db-schema "/resources/sample.edn")
(def db-fixtures "/resources/fixtures.edn")
(prepare-database! uri db-schema db-fixtures)

(def db (database uri))
(dot/show (schema db) (references db))
(delete-database! uri)
