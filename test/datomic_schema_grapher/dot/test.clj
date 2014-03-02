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
               :db/valueType :db.type/string}
              {:db/ident :entity2/entity1
               :db/cardinality :db.cardinality.one
               :db/valueType :db.type/ref}])

(deftest test-group-as-entities
  (testing "group-as-entities"
    (let [entities (group-as-entities schema1)]
      (are [x y] (= (map :db/ident x) y)
           (entities "entity1") '(:entity1/attr1 :entity1/attr2)
           (entities "entity2") '(:entity2/attr1 :entity2/entity1)))))

(def attr-ref {:db/ident :entity1/reference
               :db/cardinality :db.cardinality.one
               :db/valueType :db.type/ref})

(def attr-cardinality-one {:db/ident :entity1/attr1
                           :db/cardinality :db.cardinality.one
                           :db/valueType :db.type/string})

(def attr-cardinality-many {:db/ident :entity1/attr2
                            :db/cardinality :db.cardinality.many
                            :db/valueType :db.type/string})

(deftest test-is-a-ref?
  (testing "is-a-ref?"
    (are [x y] (= (is-a-ref? x) y)
         attr-ref true
         attr-cardinality-one false)))

(deftest test-attribute-label
  (testing "node-attribute-label"
    (are [x y] (= (attribute-label x) y)
         attr-ref "<reference>reference : ref"
         attr-cardinality-one "attr1 : string"
         attr-cardinality-many "attr2 : string")))

(deftest test-node-label
  (testing "node-label"
    (is (= (node-label [attr-ref attr-cardinality-one attr-cardinality-many])
           {:label "{\nentity1\n|<reference>reference : ref|attr1 : string|attr2 : string}" :shape "Mrecord"}))))

(deftest test-dot-relationship
  (testing "Relationships are properly mapped"
    (are [x y] (= (dot-relationship x) y)
         '(:entity1/multi "entity2" "many") [["entity1:multi" "entity2" {:arrowhead "crown"}]]
         '(:entity1/self "entity1" "one") [["entity1_ref" {:label "entity1" :style "dotted,rounded"}]
                                           ["entity1:self" "entity1_ref" {:arrowhead "tee"}]])))

