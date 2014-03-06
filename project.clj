(defproject datomic-schema-grapher "0.0.1"
  :description "A library and lein plugin for graphing the datomic schema."
  :url "https://github.com/felixflores/datomic_schema_grapher"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[dorothy "0.0.5"]
                 [hiccup "1.0.5"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.5.1"]
                                  ;; user must provide her own datomic
                                  [com.datomic/datomic-free "0.9.4572"]]}}
  :jvm-opts ["-Xmx1g"]
  :eval-in-leiningen true)
