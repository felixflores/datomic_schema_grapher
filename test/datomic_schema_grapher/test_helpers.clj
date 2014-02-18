(ns datomic-schema-grapher.test-helpers)

(defn setup-surround
  [before after]
  (fn [test-case]
    (do
      (before)
      (test-case)
      (after))))

