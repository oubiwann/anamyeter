(defn get-banner
  []
  (try
    (str
      (slurp "resources/text/banner.txt")
      (slurp "resources/text/loading.txt"))
    ;; If another project can't find the banner, just skip it;
    ;; this function is really only meant to be used by Dragon itself.
    (catch Exception _ "")))

(defn get-prompt
  [ns]
  (str "\u001B[35m[\u001B[34m"
       ns
       "\u001B[35m]\u001B[33m λ\u001B[m=> "))

(defproject hexagram30/language "4.2.0-SNAPSHOT"
  :description "A syntagmata and Markov chain language and word generator for use in hexagram30 narratives"
  :url "https://github.com/hexagram30/language"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :exclusions [
    ;; JDK version issues overrides
    [fipp]
    [io.aviso/pretty]
    [org.clojure/tools.reader]]
  :dependencies [
    ;; JDK version issues overrides
    [fipp "0.6.18"]
    [io.aviso/pretty "0.1.37"]
    [org.clojure/tools.reader "1.3.2"]
    ;; Regular dependencies
    [cheshire "5.9.0"]
    [clojure-opennlp "0.5.0"]
    [clojusc/system-manager "0.3.0"]
    [clojusc/twig "0.4.1"]
    [clojusc/wordnet "1.0.0"]
    [hexagram30/common "0.1.0-SNAPSHOT"]
    [hexagram30/db-plugin "0.1.0-SNAPSHOT"]
    [hexagram30/dice "0.1.0-SNAPSHOT"]
    [hexagram30/httpd "0.1.0-SNAPSHOT"]
    [org.clojure/clojure "1.10.1"]]
  :jar-exclusions [
    #"corpora"
    #"nlp"
    #"wordnet"]
  :profiles {
    :ubercompile {
      :aot :all}
    :dev {
      :dependencies [
        [clojusc/trifl "0.4.2"]
        [org.clojure/tools.namespace "0.3.1"]]
      :plugins [
        [lein-shell "0.5.0"]
        [venantius/ultra "0.6.0"]]
      :source-paths ["dev-resources/src"]
      :aot [clojure.tools.logging.impl]
      :repl-options {
        :init-ns hxgm30.language.repl
        :prompt ~get-prompt
        :init ~(println (get-banner))}}
    :lint {
      :source-paths ^:replace ["src"]
      :test-paths ^:replace []
      :plugins [
        [jonase/eastwood "0.3.6"]
        [lein-ancient "0.6.15"]
        [lein-kibit "0.1.7"]
        [lein-nvd "1.3.0"]]}
    :test {
      :plugins [
        [lein-ltest "0.3.0"]]
      :test-selectors {
        :unit #(not (or (:integration %) (:system %)))
        :integration :integration
        :system :system
        :default (complement :system)}}
    :redis-plugin {
      :jvm-opts [
        "-Ddb.backend=redis"
        "-Ddb.backend.subtype=db"]
      :dependencies [
        [hexagram30/redis-db-plugin "0.1.0-SNAPSHOT"]]
      :aliases {
        "read-db-cfg" ["run" "-m" "hxgm30.db.plugin.docker" "read" "compose-redis-db.yml"]
        "start-db" ["run" "-m" "hxgm30.db.plugin.docker" "up" "compose-redis-db.yml"]
        "stop-db" ["run" "-m" "hxgm30.db.plugin.docker" "down" "compose-redis-db.yml"]}}}
  :aliases {
    ;; Dependencies
    "wordnet"
      ["shell" "resources/scripts/setup-wordnet.sh"]
    ;; Dev Aliases
    "repl" ["do"
      ["clean"]
      ["with-profile" "+redis-plugin" "repl"]]
    "ubercompile" ["do"
      ["clean"]
      ["with-profile" "+ubercompile,+redis-plugin" "compile"]]
    "check-security" ["with-profile" "+lint,+redis-plugin" "nvd" "check"]
    "check-vers" ["with-profile" "+lint,+redis-plugin" "ancient" "check" ":all"]
    "check-jars" ["with-profile" "+lint,+redis-plugin" "do"
      ["deps" ":tree"]
      ["deps" ":plugin-tree"]]
    "check-deps" ["do"
      ["check-jars"]
      ["check-vers"]]
    "kibit" ["with-profile" "+lint" "kibit"]
    "eastwood" ["with-profile" "+lint" "eastwood" "{:namespaces [:source-paths]}"]
    ; "eastwood" ["with-profile" "+lint" "eastwood" "{:namespaces [hxgm30.language.gen.core hxgm30.language.syntagmata.corpus]}"]
    "lint" ["do"
      ["kibit"]
      ;["eastwood"]
      ]
    "ltest" ["with-profile" "+test,+redis-plugin" "ltest"]
    "ltest-clean" ["do"
      ["clean"]
      ["ltest"]]
    "build" ["do"
      ["clean"]
      ["check-vers"]
      ["lint"]
      ["wordnet"]
      ["ltest" ":all"]
      ["uberjar"]]
    ;; Lang-Gen Scripts
    "fictional" [
      "with-profile" "+redis-plugin" "run" "-m"
      "hxgm30.language.cli" "assembled"]
    "names" [
      "with-profile" "+redis-plugin" "run" "-m"
      "hxgm30.language.cli" "names"]
    "regen-markov" [
      "with-profile" "+redis-plugin" "run" "-m"
      "hxgm30.language.cli" "regen-markov"]
    "regen-syntagmata" [
      "with-profile" "+redis-plugin" "run" "-m"
      "hxgm30.language.cli" "regen-syntagmata"]
    ;; WordNet scripts
    "install-jwi" ["shell" "resources/scripts/install-jwi.sh"]})

