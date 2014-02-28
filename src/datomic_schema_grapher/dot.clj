(ns datomic-schema-grapher.dot
  (:require [clojure.string :refer (split join)]
            [dorothy.core :refer (subgraph node-attrs digraph dot show! graph-attrs)]))

(defn group-as-entities
  [schema]
  (group-by #(namespace (% :db/ident)) schema))

(defn cluster-names
  []
  (iterate
    (fn
      [cluster-id]
      (let [[cname cid] (clojure.string/split cluster-id #"\_")]
        (str cname "_" (inc (Integer/parseInt cid)))))
    "cluster_0"))

(defn schema-cluster-map
  [schema]
  (let [namespaces (keys (group-as-entities schema))]
    (->> (interleave namespaces (take (count namespaces) (cluster-names)))
         (apply hash-map))))

(defn node-name
  [attr]
  (subs (str (attr :db/ident)) 1))

(defn cardinality-one?
  [attr]
  (= (attr :db/cardinality) :db.cardinality.one))

(defn node-label
  [attr]
  (->> (map attr [:db/ident :db/valueType])
       (map name)
       (join "\n")))

(defn dot-attribute
  [attr]
  [(node-name attr) {:label (node-label attr)
                     :peripheries (if (cardinality-one? attr) 0 2)}])


;; '([:entity1/multi #{"entity1" "entity2"}]
;;   [:entity1/entity2 #{"entity2"}]
;;   [:entity2/entity1 #{"entity1"}]
;;   [:entity1/self #{"entity1"}])
;; [["entity1/multi" "quote/user" {:lhead :cluster_1}]]

;; (defn- dot-relationships
;;   [schema])
;;
(defn dot-subgraphs
  [schema]
  (for [entity (group-as-entities schema)
        :let [entity-name (first entity)
              entity-attrs (last entity)]]

    (do
      (println "==========\n"
               (concat [{:label entity-name :style "rounded, filled" :color :lightgrey}]
                       [(node-attrs {:style :filled :color :white})]
                       (vec (map dot-attribute entity-attrs))) 
               "\n========")
    (subgraph ((schema-cluster-map schema) entity-name)
              (concat [{:label entity-name :style "rounded, filled" :color :lightgrey}]
                      [(node-attrs {:style :filled :color :white})]
                      (vec (map dot-attribute entity-attrs)))))))
;;
;; (defn to-dot
;;   [schema relationships]
;;   (dot (digraph (concat [{:compound :true}]
;;                         (dot-subgraph schema)
;;                         (dot-relationships relationships schema)))))
;;
;; (defn show
;;   [schema relationships]
;;   (show! (to-dot schema relationships)))
