(ns datomic-schema-grapher.database-adapter.associated
  (:require [datomic.api :as d]))

(defn- get-namespaces
  [entities]
  (let [attrs (flatten (map keys entities))]
    (if (every? nil? attrs)
      #{}
      (set (map namespace attrs)))))

(defn entities
  [attr-name database]
  (->> (d/q '[:find ?refs
              :in $ ?attr-name
              :where
              [_ ?attr-name ?refs]]
            database
            attr-name)
       (map #(d/entity database (first %)))))

(defn namespaces
  [attr-name database]
  (-> (entities attr-name database)
      (get-namespaces)))
