(ns regulator.configs.worker-spotfleet)

(def config
  {:stack-name "someStackName"
   :aws-region "us-east-1"
   :hosted-zone-name "someHostedZoneName"
   :hosted-zone-name-reverse "someReverseHostedZoneName"
   :environment "dev"
   :label "someLabrl"
   :cloudera-manager-hostname "someHostname"
   :spotfleet-iam-role-arn "someIamArn"
   :instance-role "someIamInstanceProfile"
   :instance-role-arn "someIamInstanceProfileArn"
   :ImageId "someImageId"
   :InstanceType "someEc2InstanceType"
   :KeyName "someKeyName"
   :SecurityGroups [{:GroupId "sg-be2ab8d8"}]
   :SubnetId "subnet-fe2a2ed5"})

(defn userdata [{stack-name :stack-name
                 aws-region :aws-region
                 hosted-zone-name :hosted-zone-name
                 hosted-zone-name-reverse :hosted-zone-name-reverse
                 environment :environment
                 label :label
                 cloudera-manager-hostname :cloudera-manager-hostname}]
  ["#!/bin/bash\n"
   "echo "
   stack-name
   "\n"
   "echo "
   aws-region
   "\n"
   "echo "
   stack-name
   "\n"
   "echo "
   hosted-zone-name
   "\n"
   "echo "
   hosted-zone-name-reverse
   "\n"
   "echo "
   cloudera-manager-hostname
   "\n"
 ])
