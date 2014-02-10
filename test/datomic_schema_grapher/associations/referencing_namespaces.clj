(ns datomic-schema-grapher.associations.referencing-namespaces
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer (prepare-database! delete-database! database)]
            [datomic-schema-grapher.associations :refer (referencing-namepspaces)]
            [datomic.api :as d]))

(def uri "datomic:mem://associations-test")
(def schema "/test/datomic_schema_grapher/associations/schema.edn")
(def fixtures "/test/datomic_schema_grapher/associations/fixtures.edn")

(use-fixtures :each (fn [test-case]
                      (do
                        (prepare-database! uri schema fixtures)
                        (test-case)
                        (delete-database! uri))))

(deftest a-test
  (testing "Returns a vector of referenced entity ids"
    (let [db (database uri)]
      (is (= ((referencing-namepspaces :entity1/entity2 db) :entity2)))
      (is (= ((referencing-namepspaces :entity1/self db) :entity1)))
      (is (= ((referencing-namepspaces :entity1/multi db) [:entity1 :entity2])))
      (is (= ((referencing-namepspaces :entity2/entity1 db) :entity1))))))
