(ns datomic-schema-grapher.core
  (:require [clojure.string :refer (join)])
  (:require [dorothy.core :refer (digraph subgraph)]))

(defn read-schema
  [schema-files]
  (merge (read-string (slurp schema-files))))

(defn to-dot
  [schema datoms])

(defn map-entities
  [schema])

(defn graph-schema
  [files datoms]
  (let [schema (read-schema files)]
    (do
      (to-dot schema datoms))))
