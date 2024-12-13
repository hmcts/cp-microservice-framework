#!/usr/bin/env bash

CONTEXT_NAME=framework
FRAMEWORK_VERSION=17.101.4-SNAPSHOT

LIQUIBASE_COMMAND=update
#LIQUIBASE_COMMAND=dropAll

#fail script on error
set -e


function runEventTrackingLiquibase() {
    echo "Running event repository Liquibase"
    java -jar target/event-error-liquibase-${FRAMEWORK_VERSION}.jar --url=jdbc:postgresql://localhost:5432/${CONTEXT_NAME}viewstore --username=${CONTEXT_NAME} --password=${CONTEXT_NAME} --logLevel=info ${LIQUIBASE_COMMAND}
    echo "Finished running event repository liquibase"
}


runEventTrackingLiquibase