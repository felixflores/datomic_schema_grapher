(ns datomic-schema-grapher.database-adapter.attributes.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer (prepare-database! delete-database! database)]
            [datomic-schema-grapher.database-adapter.attributes :as attributes]
            [datomic.api :as d]))

(def uri "datomic:mem://associated-test")
(def schema "/test/datomic_schema_grapher/database_adapter/attributes/schema.edn")

(use-fixtures :each (fn [test-case]
                      (do
                        (prepare-database! uri schema)
                        (test-case)
                        (delete-database! uri))))

(deftest test-datomic-attribute?
  (testing "Determines if a keyword is a datomic attributes"
    (is (= (attributes/datomic-attribute? :db/db) true))
    (is (= (attributes/datomic-attribute? :something/else) false))))

(deftest test-all
  (testing "Returns all attribute entities of the database"
    (is (= (sort (map :db/ident (attributes/all (database uri))))
           [:entity1/entity2 :entity1/multi :entity1/self :entity2/attr :entity2/entity1]))))
