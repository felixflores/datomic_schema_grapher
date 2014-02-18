(ns datomic-schema-grapher.database-adapter.attributes.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.test-helpers :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer (prepare-database! delete-database! database)]
            [datomic-schema-grapher.database-adapter.attributes :as attributes]
            [datomic.api :as d]))

(def uri "datomic:mem://associated-test")
(def schema "/test/datomic_schema_grapher/database_adapter/attributes/schema.edn")

(defn before [] (prepare-database! uri schema))
(defn after [] (delete-database! uri))
(use-fixtures :each (setup-surround before after))

(deftest test-datomic-attribute?
  (testing "Determines if a keyword is a datomic attributes"
    (is (= (attributes/datomic-attribute? :db/db) true))
    (is (= (attributes/datomic-attribute? :fressian/tag) true))
    (is (= (attributes/datomic-attribute? :something/else) false))))

(deftest test-all
  (testing "Returns all attribute entities of the database"
    (let [attrs (attributes/all (database uri))]
      (is (= (sort (map :db/ident (attrs "entity1"))) [:entity1/entity2 :entity1/multi :entity1/self]))
      (is (= (sort (map :db/ident (attrs "entity2"))) [:entity2/attr :entity2/entity1])))))
