(ns utils.datomic-helpers
  (:require [clojure.edn :as edn]
            [datomic.api :as d]))

(defn- transact-edn
  [connection file]
  (let [project-dir (System/getProperty "user.dir")
        ; Requires readers to be manually loaded because
        ; the project is set to eval-in-leningen, which does
        ; no have access to the datomic readers.
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
  (d/delete-database uri))

(defn database [uri] (d/db (d/connect uri)))
