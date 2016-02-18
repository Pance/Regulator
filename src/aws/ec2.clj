(ns aws.ec2
  (:require [amazonica.aws.ec2]))

(defn describe-spotfleet-instances [spotfleet-request-id]
  (amazonica.aws.ec2/describe-spot-fleet-instances :spot-fleet-request-id spotfleet-request-id))
