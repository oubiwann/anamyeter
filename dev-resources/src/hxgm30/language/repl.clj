(ns hxgm30.language.repl
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]
    [clojure.tools.namespace.repl :as repl]
    [clojusc.system-manager.core :as system-manager :refer [reset shutdown startup system]]
    [clojusc.twig :as logger]
    [com.stuartsierra.component :as component]
    [hxgm30.dice.components.random :as random]
    [hxgm30.language.components.config :as config]
    [hxgm30.language.components.core]
    [hxgm30.language.syntagmata.core :as syntagmata]
    [hxgm30.language.syntagmata.corpus :as corpus]
    [hxgm30.language.syntagmata.lang.core :as lang]
    [hxgm30.language.syntagmata.lang.fictional.mythgarthur :as mythgarthur]
    [hxgm30.language.syntagmata.lang.fictional.rook :as rook]
    [hxgm30.language.syntagmata.lang.names :as names]
    [hxgm30.language.syntagmata.rand :as rand]
    [hxgm30.language.syntagmata.util :as util]
    [trifl.java :refer [show-methods]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Initial Setup & Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def setup-options {
  :init 'hxgm30.language.components.core/init
  :after-refresh 'hxgm30.language.repl/init-and-startup
  :throw-errors false})

(defn init
  "This is used to set the options and any other global data.

  This is defined in a function for re-use. For instance, when a REPL is
  reloaded, the options will be lost and need to be re-applied."
  []
  (logger/set-level! '[hxgm30] :debug)
  (system-manager/setup-manager setup-options))

(defn init-and-startup
  "This is used as the 'after-refresh' function by the REPL tools library.
  Not only do the options (and other global operations) need to be re-applied,
  the system also needs to be started up, once these options have be set up."
  []
  (init)
  (system-manager/startup))

; It is not always desired that a system be started up upon REPL loading.
; Thus, we set the options and perform any global operations with init,
; and let the user determine when then want to bring up (a potentially
; computationally intensive) system.
(init)

(defn banner
  []
  (println (slurp (io/resource "text/banner.txt")))
  :ok)

(comment

  )
