## JSALVOBR-HOMEWORK-RHOAR
repository with homework sources for advanced cloud native development (RHOAR) course.

DOWNLOAD SOURCES
```bash
# Clone Sources
git clone https://github.com/jsalvobr/jsalvobr-homework-rhoar.git

# Go to jsalvobr-homework-rhoar
cd jsalvobr-homework-rhoar
```

INSTALL ENVIROMENT
```bash
# Openshift client login:
oc login -u -p

# Generate variable for ocp project:
export HW_PRJ=jsalvobr-homework-rhoar-temporal

# Generate ocp project:
oc new-project $HW_PRJ
oc policy add-role-to-user view -z default -n $HW_PRJ
```

FRELANCER SERVICE (spring boot) INSTALL AND DEPLOY
```bash
# Go to Freelancer service path:
cd freelancer-service/

# Install postgress DB for freenalcer service:
oc process -f etc/freelancer-postgresql-persistent.yaml -p FREELANCER_DB_USERNAME=jboss -p FREELANCER_DB_PASSWORD=jboss | oc create -f - -n $HW_PRJ

# OCP run spring boot freelancer-service app:
mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$HW_PRJ -DskipTests

# Generate variable for route to Freenalncer Service:
export FREELANCER_URL=http://$(oc get route freelancer-service -n $HW_PRJ -o template --template='{{.spec.host}}')
```

PROJECT SERVICE (vertx) INSTALL AND DEPLOY
```bash
# Go to Project service path:
cd project-service/

# Install Mongo DB for project service:
oc process -f etc/project-service-mongodb-persistent.yaml -p PROJECT_DB_USERNAME=mongo -p PROJECT_DB_PASSWORD=mongo | oc create -f - -n $HW_PRJ

# Create Config Map with datasource conexion params for mongodb
oc create configmap project-service --from-file=etc/app-config.yml -n $HW_PRJ

# OCP run vertx app project-service:
mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$HW_PRJ -DskipTests

# Generate variable for route to Project Service:
export PROJECT_URL=http://$(oc get route project-service -n $HW_PRJ -o template --template='{{.spec.host}}')
```

API GATEWAY (thorntail) INSTALL AND DEPLOY
```bash
# Go to Api Gateway service path:
cd api-gateway/

# Configure endopoint URL for gateway service: 
# edit project-defaults.yml with the URL of the end services
vi src/main/resources/project-defaults.yml

# with the output
echo $FREELANCER_URL
echo $PROJECT_URL

# OCP run thorntail app api-gateway:
mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$HW_PRJ

#Generate variable for route to Api Gateway:
export GATEWAY_URL=http://$(oc get route api-gateway -n $HW_PRJ -o template --template='{{.spec.host}}')
```

CONSUME SERVICE THROUGT API GATEWAY
```bash
# get all Freelancers:
curl $GATEWAY_URL/gateway/freelancers

# get Freelancers per ID:
curl $GATEWAY_URL/gateway/freelancers/{freelancerId}

# get all Projects:
curl $GATEWAY_URL/gateway/projects

# get Project per ID:
curl $GATEWAY_URL/gateway/projects/{projectId}

# get Projects per Status:
curl $GATEWAY_URL/gateway/projects/status/{theStatus}
```

