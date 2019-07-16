# jsalvobr-homework-rhoar
repository with homework sources for advanced cloud native development (RHOAR) course.

-------------------CREATE OCP PROJECT----------------------------
Generate variable for ocp project
export HW_PRJ=jsalvobr-homework-rhoar-temporal

oc new-project $HW_PRJ


--------------------FRELANCER deploy and consumer-------------------
install postgress DB for freenalcer service:
oc process -f etc/freelancer-postgresql-persistent.yaml -p FREELANCER_DB_USERNAME=jboss -p FREELANCER_DB_PASSWORD=jboss | oc create -f - -n $HW_PRJ

Local run spring boot app freelancer-service:
mvn clean install spring-boot:run -Dspring.profiles.active=local -DskipTests

Local get all freelancer command:
curl -H "Content-Type: application/json" -X GET http://localhost:8080/freelancers


OCP run spring boot app freelancer-service:
mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$HW_PRJ -DskipTests

Generate variable for route to Freenalncer Service:
export FREELANCER_URL=http://$(oc get route freelancer-service -n $HW_PRJ -o template --template='{{.spec.host}}')

OCP get all freelancer command:
curl -H "Content-Type: application/json" -X GET $FREELANCER_URL/freelancers




--------------------PROJECT deploy and consumer-------------------
install Mongo DB for project service:
oc process -f etc/project-service-mongodb-persistent.yaml -p PROJECT_DB_USERNAME=mongo -p PROJECT_DB_PASSWORD=mongo | oc create -f - -n $HW_PRJ

Create Config Map with datasource conexion params for mongodb
oc create configmap project-service --from-file=etc/app-config.yml -n $HW_PRJ

OCP run vertx app project-service:
mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$HW_PRJ -DskipTests

Generate variable for route to Project Service:
export PROJECT_URL=http://$(oc get route project-service -n $HW_PRJ -o template --template='{{.spec.host}}')

OCP get all projects command:
curl $PROJECT_URL/projects


--------------------API GATEWAY-----------------------------------
Create ConfigMap with final service endpoint

edit src/main/resources/project-defaults.yml with the URL of the end services:
echo $FREELANCER_URL
echo $PROJECT_URL


OCP run thorntail app api-gateway:
mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$HW_PRJ

Generate variable for route to Api Gateway:
export GATEWAY_URL=http://$(oc get route api-gateway -n $HW_PRJ -o template --template='{{.spec.host}}')

curl $GATEWAY_URL/gateway/freelancers


local run api gateway app:
mvn clean install thorntail:run
