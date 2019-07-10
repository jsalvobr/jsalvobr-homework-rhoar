# jsalvobr-homework-rhoar
repository with homework sources for advanced cloud native development (RHOAR) course.


-------------Pre-Requisites OCP-----------------
install postgress DB for freenalcer service:
oc process -f etc/freelancer-postgresql-persistent.yaml -p FREELANCER_DB_USERNAME=jboss -p FREELANCER_DB_PASSWORD=jboss | oc create -f - -n jsalvobr-homework-rhoar-temporal

install Mongo DB for project service:




--------------------FRELANCER deploy and consumer-------------------
Local run spring boot app freelancer-service:
mvn clean install spring-boot:run -Dspring.profiles.active=local -DskipTests

Local get all freelancer command:
curl -H "Content-Type: application/json" -X GET http://localhost:8080/freelancers


OCP run spring boot app freelancer-service:
mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=jsalvobr-homework-rhoar-temporal

OCP get all freelancer command:



--------------------PROJECT deploy and consumer-------------------



--------------------API GATEWAY-----------------------------------
local run api gateway app:
mvn clean install thorntail:run
