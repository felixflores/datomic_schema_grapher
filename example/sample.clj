(ns example.sample
  (:require [datomic-schema-grapher.dot :as dot]
            [utils.datomic-helpers :refer :all]
            [datomic-schema-grapher.database :refer (schema references)]))

(def uri "datomic:mem://grapher")
(def db-schema "/example/schema.edn")
(def db-fixtures "/example/fixtures.edn")
(prepare-database! uri db-schema db-fixtures)

(def db (database uri))
(dot/show (schema db) (references db))
(delete-database! uri)
