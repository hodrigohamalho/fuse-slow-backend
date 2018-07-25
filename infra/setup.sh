oc login -u system:admin
oc adm policy add-cluster-role-to-user cluster-admin developer
oc create -f amq63-image-stream.json -n openshift
oc create -f amq63-basic.json -n openshift
oc login -u developer
oc new-app --template=amq63-basic --param=MQ_USERNAME=connect-inspire --param=MQ_PASSWORD=c0nn3ct-1nspir3 --param=IMAGE_STREAM_NAMESPACE=openshift

