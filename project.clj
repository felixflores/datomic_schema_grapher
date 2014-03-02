(defproject datomic-schema-grapher "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.datomic/datomic-pro "0.9.4572"]
                 [dorothy "0.0.5"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.namespace "0.2.4"]
                 [org.clojure/tools.trace "0.7.6"]]
  :jvm-opts ["-Xmx1g"]
  :plugins [[quickie "0.2.5"]]
  :eval-in-leiningen true)

