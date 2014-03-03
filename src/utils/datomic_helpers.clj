(ns utils.datomic-helpers
  (:require [clojure.edn :as edn]
    [datomic.api :as d]))

(defn- transact-edn
  [connection file]
  (let [project-dir (System/getProperty "user.dir")
        readers {'base64 #'datomic.codec/base-64-literal
                 'db/fn #'datomic.function/construct
                 'db/id #'datomic.db/id-literal}
        read-edn #(edn/read-string {:readers readers} (slurp (str project-dir %)))]
    @(d/transact connection (read-edn file))))

(defn prepare-database!
  ([uri schema]
   (when (d/create-database uri)
     (transact-edn (d/connect uri) schema)))
  ([uri schema fixtures]
   (prepare-database! uri schema)
   (transact-edn (d/connect uri) fixtures)))

(defn delete-database!
  [uri]
  (do
    (d/delete-database uri)))

(defn database [uri] (d/db (d/connect uri)))
