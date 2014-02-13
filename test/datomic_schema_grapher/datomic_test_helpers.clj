(ns datomic-schema-grapher.datomic-test-helpers
  (:require [datomic.api :as d]))

(defn- transact-edn
  [connection file]
  (let [project-dir (System/getProperty "user.dir")
        read-edn #(read-string (slurp (str project-dir %)))]
    (do
      (println "Transacting edn: " file)
      (println @(d/transact connection (read-edn file)) "\n -----------"))))

(defn prepare-database!
  [uri schema fixtures]
  (when (d/create-database uri)
    (let [connection (d/connect uri)]
      (do
        (transact-edn connection schema)
        (transact-edn connection fixtures)))))

(defn delete-database!
  [uri]
  (d/delete-database uri))

(defn database [uri] (d/db (d/connect uri)))
