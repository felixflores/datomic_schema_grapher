(ns datomic-schema-grapher.dot
  (:require [clojure.string :refer (split join)]
            [dorothy.core :refer (subgraph node-attrs digraph dot show! graph-attrs)]))

(defn group-as-entities
  [schema]
  (group-by #(namespace (:db/ident %)) schema))

(defn is-a-ref?
  [attr]
  (= (:db/valueType attr) :db.type/ref))

(defn attribute-label
  [attribute]
  (let [attr-name (name (:db/ident attribute))
        attr-type (name (:db/valueType attribute))
        attr-port (when (is-a-ref? attribute) (str "<" attr-name ">"))]
    (str attr-port attr-name " : " attr-type)))

(defn node-label
  [attributes]
  (let [entity-name (namespace (:db/ident (first attributes)))
        attr-labels (map attribute-label attributes)]
    {:label (str "{\n" entity-name "\n|" (apply str (interpose "|" attr-labels)) "}")
     :shape "Mrecord"}))

(defn dot-nodes
  [schema]
  (for [[entity-name attributes] (group-as-entities schema)]
    [entity-name (node-label attributes)]))

(defn dot-relationship
  [[root dest-label cardinality]]
  (let [root-label (str (namespace root) ":" (name root))
        dest-ref-label (str dest-label "_ref")
        cardinality-label {:arrowhead (if (= cardinality "one") "tee" "crown")}]
    (if (= (namespace root) dest-label)
      [[dest-ref-label {:label dest-label :style "dotted,rounded"}]
       [root-label dest-ref-label cardinality-label]]
      [[root-label dest-label cardinality-label]])))

(defn dot-relationships
  [relationships]
  (reduce #(concat %1 %2) (map dot-relationship relationships)))

(defn to-dot
  [schema relationships]
  (dot (digraph (concat [(node-attrs {:shape "record"})]
                        (dot-nodes schema)
                        (when (not-empty relationships)
                          (dot-relationships relationships))))))

(defn show
  [schema relationships]
  (show! (to-dot schema relationships)))
