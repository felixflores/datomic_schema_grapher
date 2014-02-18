(ns datomic-schema-grapher.database-adapter.associated
  (:require [datomic.api :as d]))

(defn- ref-entities
  [attr-name database]
  (->> (d/q '[:find ?refs
              :in $ ?attr-name
              :where
              [_ ?attr-name ?refs]]
            database
            attr-name)
       (map #(d/entity database (first %)))))

(defn- ref-namespaces
  [entities]
  (let [attrs (flatten (map keys entities))]
    (if (every? nil? attrs)
      #{}
      (set (map namespace attrs)))))

(defn namespaces
  [attr-name database]
  (-> (ref-entities attr-name database)
      (ref-namespaces)))
