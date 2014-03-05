(ns datomic-schema-grapher.dot
  (:require [clojure.string :refer (split join)]
            [hiccup.core :refer (html)]
            [dorothy.core :refer (subgraph node-attrs digraph dot show! save! graph-attrs)]))

(def light-grey "#808080")
(def edge-colors ["#441C14" "#15484C" "#257580" "#6E7D2C" "#CED796"])

(defn group-as-entities
  [schema]
  (group-by #(namespace (:db/ident %)) schema))

(defn is-a-ref?
  [attr]
  (= (:db/valueType attr) :db.type/ref))

(defn is-unique?
  [attr]
  (= (:db/unique attr) :db.unique/identity))

(defn attr-row-label
  [attribute]
  (let [label (str (name (:db/ident attribute)) " : " (name (:db/valueType attribute)))]
    (if (is-unique? attribute)
      (html [:font {:color "red"} label])
      label)))

(defn node-label
  [attributes]
  (let [entity-name (namespace (:db/ident (first attributes)))]
    (html [:table {:port entity-name :border 0 :color light-grey :cellborder 1 :cellspacing 0}
           [:tr [:td {:bgcolor "gray"} entity-name]]
           (for [attribute attributes
                 :let [attr-name (name (:db/ident attribute))]]
             [:tr [:td (when (is-a-ref? attribute) {:port attr-name}) (attr-row-label attribute)]])])))

(defn dot-nodes
  [schema]
  (for [[entity-name attributes] (group-as-entities schema)]
    [entity-name {:label (node-label (sort #(compare (first %1) (first %2)) attributes))}]))

(defn circular-relationship?
  [[root dest-label _]]
  (= (namespace root) dest-label))


(defn ref-node
  [[root dest-label _]]
  [(str dest-label "_ref") {:label dest-label :shape "rectangle" :style "dotted,rounded" :color light-grey}])

(defn add-ref-colors
  [ref-nodes]
  (loop [nodes []
         colors (cycle edge-colors)
         ref-nodes ref-nodes]
    (let [[root-label dest-ref-label edge-attrs] (first ref-nodes)]
      (if (not-empty ref-nodes)
        (recur (conj nodes [root-label dest-ref-label (merge edge-attrs {:color (first colors)})])
               (rest colors)
               (rest ref-nodes))
        nodes))))

(defn dot-relationship
  [[root dest-label cardinality :as relationship]]
  (let [root-label (str (namespace root) ":" (name root))
        dest-ref-label (str dest-label "_ref")
        edge-attrs {:arrowhead (if (= cardinality "one") "tee" "crow")}]
    (if (circular-relationship? relationship)
      [root-label dest-ref-label edge-attrs]
      [root-label (str dest-label ":" dest-label) edge-attrs])))

(defn dot-relationships
  [relationships]
  (concat (add-ref-colors (map dot-relationship relationships))
          (map ref-node (filter circular-relationship? relationships))))

(defn to-dot
  [schema relationships]
  (dot (digraph (concat [(node-attrs {:shape "plaintext"})]
                        (dot-nodes schema)
                        (when (not-empty relationships)
                          (dot-relationships relationships))))))

(defn show
  [schema relationships]
  (show! (to-dot schema relationships)))
