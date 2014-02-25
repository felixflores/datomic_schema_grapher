(ns datomic-schema-grapher.core
  (:require [datomic-schema-grapher.dot :as dot]
            [datomic-schema-grapher.datomic-test-helpers :refer :all]
            [datomic-schema-grapher.database :as db]))

(def uri "datomic:mem://grapher")
(def schema "/resources/sample.edn")
(prepare-database! uri schema)

(->> (db/schema (database uri))
     dot/show)


(delete-database! uri)
