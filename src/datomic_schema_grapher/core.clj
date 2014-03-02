(ns datomic-schema-grapher.core
  (:require [datomic.api :as d]
            [datomic-schema-grapher.database :refer (schema references)]
            [datomic-schema-grapher.dot :refer (show)])
  (:import javax.swing.JFrame))

(defn graph-datomic
  "Render a nice graphviz of your Datomic schema"
  [uri & {:keys [exit-on-close] :as options}]
  (let [db (d/db (d/connect uri))
        schema (schema db)
        references (references db)]
    (let [jframe (show schema references)]
      (when exit-on-close
        (.setDefaultCloseOperation jframe JFrame/EXIT_ON_CLOSE)
        (while true (Thread/sleep 500))))))
