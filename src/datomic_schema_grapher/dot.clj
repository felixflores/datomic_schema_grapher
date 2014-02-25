(ns datomic-schema-grapher.dot
  (:require [clojure.string :refer (split join)]
            [dorothy.core :refer (subgraph node-attrs digraph dot show! graph-attrs)]))

(defn parse
  ([entities]
   (parse 0 entities))
  ([cluster-number entities]
   (when (not-empty entities)
     (let [[entity-name attributes] (first entities)]
       (conj
         {entity-name [(keyword (str "cluster_" cluster-number))
                       (map #(select-keys % [:db/ident :db/cardinality :db/valueType]) attributes)]}
         (parse (inc cluster-number) (rest entities)))))))

(defn remove-namespace
  [keyword-val]
  (-> (str keyword-val)
      (split #"\/")
      last))

(defn create-node
  [info]
  [(subs (str (info :db/ident)) 1) {:label (str (->> (map info [:db/ident :db/valueType])
                                                     (map remove-namespace)
                                                     (join "\n")))
                                    :peripheries (if (= (info :db/cardinality) :db.cardinality/one) 0 2)}])

(defn- create-subgraph
  [[entity-name [cluster-name attributes]]]
  (subgraph (keyword cluster-name)
            (concat [{:label entity-name :style "rounded, filled" :color :lightgrey}
                     (node-attrs {:style :filled :color :white})]
                    (vec (map create-node attributes)))))


(defn to-dot
  [entities]
  (dot (digraph (concat [{:compound :true}]
                        (map create-subgraph (parse entities))
                        [["user/last-name" "quote/user" {:lhead :cluster_1}]]))))

(defn show
  [schema]
  (show! (to-dot (schema :db/ident))))
