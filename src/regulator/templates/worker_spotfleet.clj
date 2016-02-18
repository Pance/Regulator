(ns regulator.templates.worker-spotfleet
  (:require [regulator.configs.worker-spotfleet :refer [config userdata]]
            [clojure.data.codec.base64]))

(defn stack-name []
  (config :stack-name))

(def worker-instance-launch-specs
  {:BlockDeviceMappings
    (vec
      (map (fn [[device virtual]] {:DeviceName device :VirtualName virtual})
        [["/dev/sdc" "ephemeral0"]
         ["/dev/sdd" "ephemeral1"]
         ["/dev/sde" "ephemeral2"]
         ["/dev/sdf" "ephemeral3"]
         ["/dev/sdg" "ephemeral4"]
         ["/dev/sdh" "ephemeral5"]]))
   :IamInstanceProfile {:Arn {"Fn::GetAtt" ["InstanceProfile" "Arn"]}} ;;hardcoded to the other resource name
   :ImageId (:ImageId config)
   :InstanceType (:InstanceType config)
   :KeyName (:KeyName config)
   :SecurityGroups (:SecurityGroups config)
   :SubnetId (:SubnetId config)
   :UserData (String. (clojure.data.codec.base64/encode (.getBytes (clojure.string/join (userdata config)) "UTF-8")) "UTF-8")
})

(def spot-fleet-request-config-data
  {:IamFleetRole (:spotfleet-iam-role-arn config)
   :LaunchSpecifications [worker-instance-launch-specs]
   :SpotPrice "0.40"
   :TargetCapacity 4
   })

(def spot-fleet-resource
  {:Type "AWS::EC2::SpotFleet"
   :Properties {:SpotFleetRequestConfigData spot-fleet-request-config-data}})

(def template
  {:AWSTemplateFormatVersion "2010-09-09"
   :Resources {"SpotFleet" spot-fleet-resource
               "InstanceProfile" {:Type "AWS::IAM::InstanceProfile"
                                  :Properties {:Path "/"
                                               :Roles [(:instance-role config)]}}}})
