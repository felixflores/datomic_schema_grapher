(ns leiningen.graph-datomic
  (:require [datomic.api :as d]
            [datomic-schema-grapher.database :refer (schema references)]
            [datomic-schema-grapher.dot :refer (show)])
  (:import javax.swing.JFrame))

(defn ^:no-project-needed graph-datomic
  [project uri]
  (let [db (d/db (d/connect uri))
        jframe (show (schema db) (references db))]
    (.setDefaultCloseOperation jframe JFrame/EXIT_ON_CLOSE)
    (while true (Thread/sleep 1000))))
