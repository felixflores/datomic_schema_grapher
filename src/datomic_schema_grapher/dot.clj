(ns datomic-schema-grapher.dot
  (:require [clojure.string :refer (split join)]
            [dorothy.core :refer (subgraph node-attrs digraph dot show!)]))

(defn parse
  ([schema]
   (parse 0 schema))
  ([cluster-number schema]
   (when (not-empty schema)
     (let [[entity-name attributes] (first schema)]
       (conj
         {entity-name [(keyword (str "cluster_" cluster-number))
                       (map #(select-keys % [:db/ident :db/cardinality :db/valueType]) attributes)]}
         (parse (inc cluster-number) (rest schema)))))))

(defn remove-namespace
  [keyword-val]
  (-> (str keyword-val)
      (split #"\/")
      last))

(defn create-node
  [info]
  [(info :db/ident) {:label (str "{"
                                 (->> (map info [:db/ident :db/valueType :db/cardinality])
                                      (map remove-namespace)
                                      (join "|"))
                                 "}")}])

(defn- create-subgraph
  [[entity-name [cluster-name attributes]]]
  (subgraph (keyword cluster-name)
            (concat [{:label entity-name :style :filled :color :lightgrey}
                     (node-attrs {:style :filled :color :white :shape :record})]
                    (vec (map create-node attributes)))))

(defn to-dot
  [mapping]
  (->> (map create-subgraph mapping)
       digraph
       dot))

(defn show
  [mapping]
  (show! (to-dot mapping)))



