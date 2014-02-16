(ns datomic-schema-grapher.database_adapter.associated.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer (prepare-database! delete-database! database)]
            [datomic-schema-grapher.database-adapter.associated :as associated]
            [datomic.api :as d]))

(def uri "datomic:mem://database-associated-test")
(def schema "/test/datomic_schema_grapher/database_adapter/associated/schema.edn")
(def fixtures "/test/datomic_schema_grapher/database_adapter/associated/fixtures.edn")

(defn setup
  [test-case]
  (do
    (prepare-database! uri schema fixtures)
    (test-case)
    (delete-database! uri)))

(use-fixtures :each setup)

(defn entity2 []
  (let [db (database uri)]
    (->> (d/q '[:find ?e
                :where [?e :entity2/attr "Attribute 2"]]
              db)
         (ffirst)
         (d/entity db))))

(deftest test-get-entities
  (testing "Returns a collection of referenced entities"
    (let [db (database uri)]
      (is (= (first (associated/entities :entity1/entity2 db)) (entity2)))
      (is (= (:db:id (first (associated/entities :entity2/attr db))) nil)))))

(deftest test-referencing-namespaces
  (testing "Returns a collection of referenced namespaces"
    (let [db (database uri)]
      (is (= (associated/namespaces :entity1/self db) #{"entity1"}))
      (is (= (associated/namespaces :entity1/multi db) #{"entity1" "entity2"}))
      (is (= (associated/namespaces :entity1/entity2 db) #{"entity2"}))
      (is (= (associated/namespaces :entity2/entity1 db) #{"entity1"}))
      (is (= (associated/namespaces :entity2/attr db) #{})))))
