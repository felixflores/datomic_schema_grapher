(ns datomic-schema-grapher.core
  (:require [dorothy.core :as dor]
            [datomic.api :as d]
            [datomic-schema-grapher.dot :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer :all]))

(def uri "datomic:mem://grapher")
(def schema "/resources/sample.edn")
(prepare-database! uri schema)

(show-graph (schema/namespaces (database uri)))
