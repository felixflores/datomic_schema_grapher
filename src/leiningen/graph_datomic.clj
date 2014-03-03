(ns leiningen.graph-datomic
  (:require [leiningen.core.eval :refer (eval-in-project)]))

(defn graph-datomic
  "Render a nice graphviz of your Datomic schema"
  [project uri & opts]
  (eval-in-project project
                   `(datomic-schema-grapher.core/graph-datomic ~uri :exit-on-close true)
                   '(require 'datomic-schema-grapher.core)))
