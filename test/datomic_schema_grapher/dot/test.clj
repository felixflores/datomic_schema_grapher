(ns datomic-schema-grapher.dot.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.dot :refer :all]))

(def entities {"entity1" [{:db/ident :entity1/attr
                           :db/cardinality :db.cardinality.one
                           :db/valueType :db.type/ref}]})

(deftest test-parse-schema
  (testing "Parsing of schema for dorothy"
    (is (= (parse 0 entities)
           {"entity1" [:cluster_0 [{:db/cardinality :db.cardinality.one
                                    :db/ident :entity1/attr
                                    :db/valueType :db.type/ref}]]}))))


;; (deftest test-subgraph
;;   (testing "Create dot file form mapping"
;;     (is (= (to-dot (parse entities))
;;            "digraph {\nsubgraph cluster_0 {\ngraph [label=entity1,style=filled,color=lightgrey];\nnode [color=white,style=filled];\nattr;\n} ;\n} "))))
