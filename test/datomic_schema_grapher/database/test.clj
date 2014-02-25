(ns datomic-schema-grapher.database.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.test-helpers :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer :all]
            [datomic-schema-grapher.database :refer :all]))

(def uri "datomic:mem://database-test")
(def db-schema "/test/datomic_schema_grapher/database/schema.edn")
(def fixtures "/test/datomic_schema_grapher/database/fixtures.edn")

(defn before [] (prepare-database! uri db-schema fixtures))
(defn after [] (delete-database! uri))
(use-fixtures :each (setup-surround before after))

(deftest test-datomic-attribute?
  (testing "Determines if a keyword is a datomic attributes"
    (is (= (datomic-attribute? :db/db) true))
    (is (= (datomic-attribute? :fressian/tag) true))
    (is (= (datomic-attribute? :something/else) false))))

(deftest test-referencing-namespaces
  (testing "Returns a collection of referenced namespaces"
    (let [db (database uri)]
      (is (= (ref-attrs :entity1/self db) #{"entity1"}))
      (is (= (ref-attrs :entity1/multi db) #{"entity1" "entity2"}))
      (is (= (ref-attrs :entity1/entity2 db) #{"entity2"}))
      (is (= (ref-attrs :entity2/entity1 db) #{"entity1"})))))

(deftest test-schema
  (testing "Returns all attribute entities of the database"
    (let [s ((schema (database uri)) :db/ident)]
      (is (= (sort (map :db/ident (s "entity1"))) [:entity1/entity2 :entity1/multi :entity1/self]))
      (is (= (sort (map :db/ident (s "entity2"))) [:entity2/attr :entity2/entity1])))))
