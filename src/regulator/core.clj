(ns regulator.core
  (:require [regulator.templates.worker-spotfleet :as worker-spotfleet]
            [aws.cloudformation]
            [aws.ec2]
            [clojure.data.json]
            [clojure.core.async :as async]))

(defn launch-worker-spot-fleet! []
  (aws.cloudformation/create-stack! (worker-spotfleet/stack-name)
                                    (clojure.data.json/write-str worker-spotfleet/template)))

(defn is-stack-complete [stack-description]
  (let [done-statuses #{"CREATE_COMPLETE"
                        "CREATE_FAILED"
                        "DELETE_COMPLETE"
                        "DELETE_FAILED"
                        "ROLLBACK_COMPLETE"
                        "ROLLBACK_FAILED"
                        "UPDATE_COMPLETE"
                        "UPDATE_ROLLBACK_COMPLETE"
                        "UPDATE_ROLLBACK_FAILED"}]
    (contains? done-statuses (:stack-status stack-description))))

(defn wait-until-stack-done [stack-id]
  (while
    (not (is-stack-complete (aws.cloudformation/describe-stack stack-id)))
         (do (print (str stack-id " not done\n"))
             (Thread/sleep 5000))))

(defn go! []
  (let [stack-id (:stack-id (launch-worker-spot-fleet!))]
    (print (str stack-id "\n"))
    (wait-until-stack-done stack-id) ;badbadbad. we really just need to wait for the stack to create to get the spotfleetId. could use a future
    (print "Stack is complete!\n")))

(defn get-spotfleet-id-from-stack [stack-id]
  (:output-value (first (filter #(= (:output-key %) "SpotFleetId") (:outputs (aws.cloudformation/describe-stack stack-id))))))

(defn add-instance-to-cluster-machine []
  "Returns an input channel that gets read asynchronously by this 'machine'
   that will handle registration of the instance with a cluster on clouderaManager"
  (let [input-chan (async/chan)]
    (async/go
      (let [{:keys [instance-id cluster-name config]} (async/<! input-chan)]
        (print (str "add-instance-to-cluster! " instance-id)))
      )
    input-chan))
