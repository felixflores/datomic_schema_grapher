(ns datomic-schema-grapher.database.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.test-helpers :refer :all]
            [utils.datomic-helpers :refer :all]
            [datomic-schema-grapher.database :refer :all]))

(def uri "datomic:mem://database-test")
(def db-schema "/test/datomic_schema_grapher/database/schema.edn")
(def fixtures "/test/datomic_schema_grapher/database/fixtures.edn")

(defn before [] (prepare-database! uri db-schema fixtures))
(defn after [] (delete-database! uri))
(use-fixtures :each (setup-surround before after))

(deftest test-datomic-attribute?
  (testing "Determines if a keyword is a datomic attributes"
    (are [x y] (= (datomic-attribute? x) y)
         :db/db true
         :fressian/tag true
         :something/else false)))

(deftest test-schema
  (testing "Returns all attribute entities of the database"
    (is (= (->> (schema (database uri))
                (map :db/ident))
           '(:entity1/multi :entity1/entity2 :entity2/entity1 :entity2/attr :entity1/self)))))

(deftest test-referencing-namespaces
  (testing "Returns a collection of referenced namespaces"
    (let [db (database uri)]
      (are [x y] (= (ref-entities x db) y)
           :entity1/self #{"entity1"}
           :entity1/multi #{"entity1" "entity2"}
           :entity1/entity2 #{"entity2"}
           :entity2/entity1 #{"entity1"}))))

(deftest test-references
  (testing "Returns a mapping of all references in the database"
    (let [refs (references (database uri))]
      (is (= (references (database uri))
             '((:entity1/multi "entity1" "many")
               (:entity1/multi "entity2" "many")
               (:entity1/entity2 "entity2" "one")
               (:entity2/entity1 "entity1" "many")
               (:entity1/self "entity1" "many")))))))
