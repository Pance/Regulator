(ns aws.cloudformation
  (:require [amazonica.aws.cloudformation]))

(defn create-stack!
  ([stack-name template-body]
   (amazonica.aws.cloudformation/create-stack :stack-name stack-name
                                              :template-body template-body
                                              :capabilities ["CAPABILITY_IAM"])) ;Had trouble creating a cfn stack with spotfleet and an instance profile without this line
  ([stack-name template-body params]
    (amazonica.aws.cloudformation/create-stack :stack-name stack-name
                  :template-body template-body
                  :parameters params)))

(defn describe-stack [stack-name]
  (get (:stacks (amazonica.aws.cloudformation/describe-stacks :stack-name stack-name)) 0))
