(ns datomic-schema-grapher.associated.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer (prepare-database! delete-database! database)]
            [datomic-schema-grapher.associated :refer (namespaces entities)]
            [datomic.api :as d]))

(def uri "datomic:mem://associated-test")
(def schema "/test/datomic_schema_grapher/associated/schema.edn")
(def fixtures "/test/datomic_schema_grapher/associated/fixtures.edn")

(use-fixtures :each (fn [test-case]
                      (do
                        (prepare-database! uri schema fixtures)
                        (test-case)
                        (delete-database! uri))))

(defn entity2 []
  (let [db (database uri)]
    (->> (d/q '[:find ?e
                :where [?e :entity2/attr "Attribute 2"]]
              db)
         (ffirst)
         (d/entity db))))

(deftest test-entities
  (testing "Returns a collection of referenced entities"
    (let [db (database uri)]
      (is (= (first (entities :entity1/entity2 db)) (entity2)))
      (is (= (:db:id (first (entities :entity2/attr db))) nil)))))

(deftest test-namespaces
  (testing "Returns a collection of referenced namespaces"
    (let [db (database uri)]
      (is (= (namespaces (entities :entity1/self db)) #{"entity1"}))
      (is (= (namespaces (entities :entity1/multi db)) #{"entity1" "entity2"}))
      (is (= (namespaces (entities :entity1/entity2 db)) #{"entity2"}))
      (is (= (namespaces (entities :entity2/entity1 db)) #{"entity1"}))
      (is (= (namespaces (entities :entity2/attr db)) #{})))))
