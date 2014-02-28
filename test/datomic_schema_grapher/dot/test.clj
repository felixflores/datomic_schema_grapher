(ns datomic-schema-grapher.dot.test
  (:require [clojure.test :refer :all]
            [datomic-schema-grapher.dot :refer :all]))

(def schema1 [{:db/ident :entity1/attr1
              :db/cardinality :db.cardinality.one
              :db/valueType :db.type/string}
             {:db/ident :entity1/attr2
              :db/cardinality :db.cardinality.many
              :db/valueType :db.type/string}
             {:db/ident :entity2/attr1
              :db/cardinality :db.cardinality.many
              :db:valueType :db.type/string}
             {:db/ident :entity2/entity1
              :db/cardinality :db.cardinality.one
              :db:valueType :db.type/ref}])


(testing "group-as-entities"
  (let [entities (group-as-entities schema1)]
    (are [x y] (= (map :db/ident x) y)
         (entities "entity1") '(:entity1/attr1 :entity1/attr2)
         (entities "entity2") '(:entity2/attr1 :entity2/entity1))))

(testing "map-clusters"
  (testing "Entities are properly mapped to cluster names"
    (is (= (schema-cluster-map schema1) {"entity1" "cluster_0", "entity2" "cluster_1"}))))

(testing "cluster-names"
  (is (= (take 3 (cluster-names)) '("cluster_0" "cluster_1" "cluster_2"))))

(testing "dot-subgraphs")


(def attr-cardinality-one {:db/ident :entity1/attr1
                           :db/cardinality :db.cardinality.one
                           :db/valueType :db.type/string})

(def attr-cardinality-many {:db/ident :entity1/attr1
                            :db/cardinality :db.cardinality.many
                            :db/valueType :db.type/string})

(testing "node-name"
  (is (= (node-name attr-cardinality-one) "entity1/attr1")))

(testing "cardinality-one?"
  (are [x y] (= x y)
       (cardinality-one? attr-cardinality-one) true
       (cardinality-one? attr-cardinality-many) false))

(testing "node-label"
  (is (= (node-label attr-cardinality-one) "attr1\nstring")))

(testing "Converts attributes info to proper dorothy notation"
  (is (= (dot-attribute attr-cardinality-one)
         ["entity1/attr1" {:label "attr1\nstring", :peripheries 0}])))

(def relationships '([:entity1/multi #{"entity1" "entity2"}]
                     [:entity1/entity2 #{"entity2"}]
                     [:entity2/entity1 #{"entity1"}]
                     [:entity1/self #{"entity1"}]))

(def schema2 [{:db/ident :entity1/multi
               :db/cardinality :db.cardinality.many
               :db:valueType :db.type/ref}
              {:db/ident :entity1/entity2
               :db/cardinality :db.cardinality.one
               :db:valueType :db.type/ref}
              {:db/ident :entity2/entity1
               :db/cardinality :db.cardinality.one
               :db:valueType :db.type/ref}
              {:db/ident :entity1/self
               :db/cardinality :db.cardinality.one
               :db:valueType :db.type/ref}])

(testing "dot-subgraphs"
  (is (= (dot-subgraphs schema1) "")))

;; [["entity1/multi" "quote/user" {:lhead :cluster_1}]]
;; (deftest test-dot-subgraph
;;   (testing "Dorothy subgraph is properly constructed"
;;     (is (= (dot-subgraph (first (parse entities))) []))))

;; (deftest test-subgraph
;;   (testing "Create dot file form mapping"
;;     (is (= (to-dot (parse entities))
;;            "digraph {\nsubgraph cluster_0 {\ngraph [label=entity1,style=filled,color=lightgrey];\nnode [color=white,style=filled];\nattr;\n} ;\n} "))))
