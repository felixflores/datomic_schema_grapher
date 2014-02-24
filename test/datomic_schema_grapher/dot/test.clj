(ns datomic-schema-grapher.dot.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.dot :refer :all]))

(def schema {"entity1" [{:db/ident :entity1/attr
                         :db/cardinality :db.cardinality.one
                         :someother/thing 21}]})

(deftest test-subgraph
  (testing "Create dot file form mapping"
    (is (= (to-dot schema)
           "digraph {\nsubgraph cluster_0 {\ngraph [label=entity1,style=filled,color=lightgrey];\nnode [color=white,style=filled];\nattr;\n} ;\n} "))))

;;     (let [namespaces (schema/namespaces (database uri))]
;;
;;       (is (= (create-subgraph [:cluster_0 ["entity1" (namespaces "entity1")]])
;;              '(subgraph :cluster_0 [{:label "entity1"}
;;                                     (node-attr {:style :filled})
;;                                     (:entity1/attr2 :entity1/attr3 :entity1/attr1)])))
;;
;;       (is (= (create-subgraph [:cluster_1 ["entity1" (namespaces "entity2")]])
;;              '(subgraph :cluster_1 [{:label "entity1"}
;;                                     (node-attr {:style :filled})
;;                                     (:entity2/attr1 :entity2/ref1 :entity2/attr2)]))))))
