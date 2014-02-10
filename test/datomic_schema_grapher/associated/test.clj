(ns datomic-schema-grapher.associated.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.datomic-test-helpers :refer (prepare-database! delete-database! database)]
            [datomic-schema-grapher.associated :refer (namepspaces entities)]
            [datomic.api :as d]))

(def uri "datomic:mem://associated-test")
(def schema "/test/datomic_schema_grapher/associated/schema.edn")
(def fixtures "/test/datomic_schema_grapher/associated/fixtures.edn")

(use-fixtures :each (fn [test-case]
                      (do
                        (prepare-database! uri schema fixtures)
                        (test-case)
                        (delete-database! uri))))

(deftest a-test
  (testing "Returns a vector of referenced entity ids"
    (let [db (database uri)]
      (is (= ((namepspaces (entities :entity1/entity2 db)) :entity2)))
      (is (= ((namepspaces (entities :entity1/self db)) :entity1)))
      (is (= ((namepspaces (entities :entity1/multi db)) [:entity1 :entity2])))
      (is (= ((namepspaces (entities :entity2/entity1 db)) :entity1))))))
