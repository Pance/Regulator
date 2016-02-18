(ns regulator.core
  (:require [regulator.templates.worker-spotfleet :as worker-spotfleet]
            [aws.cloudformation]
            [clojure.data.json]))

(defn launch-worker-spot-fleet! []
  (aws.cloudformation/create-stack! (worker-spotfleet/stack-name)
                                    (clojure.data.json/write-str worker-spotfleet/template)))
