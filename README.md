# datomic-schema-grapher

Visually see the datomic schema. Uses graphviz.

## Installation

Add to your `:dependencies`

```clojure
[datomic-schema-grapher "0.0.1"]
```

If you want to use the lein plugin you must also add the dependency to your project's :plugins vector.

```bash
brew install graphviz
lein deps
```

## REPL Usage

```clojure
(require '[datomic-schema-grapher.core :refer (graph-datomic)])
(graph-datomic "datomic:mem://example")
;; pops up a swing UI
```

```clojure
(graph-datomic "datomic:mem://example" :save-as "the-schema.dot")
;; writes graphviz to file and pops up swing UI
```

```clojure
(graph-datomic "datomic:mem://example" :save-as "the-schema.dot" :no-display true)
;; does not pop up a display
```

## Plugin

In order to use it as a lein plugin you must list it as a dependency *and* as a plugin.

```clojure
; project.clj
(defproject your-project "x.x.x"
  :dependencies [[datomic-schema-grapher "0.0.1"]]
  :plugins [[datomic-schema-grapher "0.0.1"]])
```

then

```bash
lein graph-datomic <datomic:protocol://example>
```

Note that the lein plugin will not work for in memory databases.
Consider using the repl with in memory databases.

This project is still in the early stages of development.
The API is likely to change.

## License

Copyright © 2014 MIT

