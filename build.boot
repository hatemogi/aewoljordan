(set-env!
 :source-paths #{"src"}
 :resource-paths #{"res"}
 :dependencies '[[org.clojure/clojure "1.8.0"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.89"]])

(require '[aewoljordan.www.boot :refer :all])
