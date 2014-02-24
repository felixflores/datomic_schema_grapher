(ns datomic-schema-grapher.dot
  (:require [dorothy.core :refer (subgraph node-attrs digraph dot show!)]))

(defn- parse-schema
  ([schema]
   (parse-schema 0 schema))
  ([cluster-number schema]
   (when (not-empty schema)
     (let [[entity-name attributes] (first schema)]
       (conj
         {entity-name [(keyword (str "cluster_" cluster-number))
                       (map #(select-keys % [:db/ident :db/cardinality]) attributes)]}
         (parse-schema (inc cluster-number) (rest schema)))))))

(defn- create-subgraph
  [[entity-name [cluster-name attributes]]]
  (subgraph (keyword cluster-name)
            [{:label entity-name :style :filled :color :lightgrey}
             (node-attrs {:style :filled :color :white})
             (vec (map :db/ident attributes))]))

(defn to-dot
  [schema]
  (->> (parse-schema schema)
       (map create-subgraph)
       digraph
       dot))

(defn show-graph
  [schema]
  (show! (to-dot schema)))



