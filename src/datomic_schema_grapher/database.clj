(ns datomic-schema-grapher.database
  (:require [datomic.api :as d]))

(defn datomic-attribute?
  [identifier]
  ((complement empty?) (re-matches #"(db|fressian).*" (namespace identifier))))

(defn schema
  "Returns all user defined datomic attribute as entities,
  grouped by their common namespace."
  [database]
  (let [entities (->> (d/q '[:find ?attr ?name
                             :where
                             [_ :db.install/attribute ?attr]
                             [?attr :db/ident ?name]]
                           database)
                      (remove #(datomic-attribute? (last %)))
                      (map #(d/entity database (first %))))]
    (fn
      [attr]
      (if (= attr :db/ident)
        (group-by #(namespace (attr %)) entities)
        (group-by attr entities)))))

(defn ref-attrs
  "Returns all entities the references a given datomic attribute."
  [attr-name database]
  (->>  (d/q '[:find ?ref-name
               :in $ ?attr-name
               :where
               [_ ?attr-name ?ref]
               [?ref ?ref-attr]
               [?ref-attr :db/ident ?ref-name]]
             database
             attr-name)
       (map first)
       (map namespace)
       (set)))

(defn all-refs
  [database]
  (->> ((schema database) :db/valueType)
       :db.type/ref
       (map :db/ident)
       (map (fn [r] [r (ref-attrs r database)]))))
