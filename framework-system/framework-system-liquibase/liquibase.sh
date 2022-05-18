#!/usr/bin/env bash

CONTEXT_NAME=framework
FRAMEWORK_VERSION=11.0.0-M11
EVENT_STORE_VERSION=11.0.0-M13

#fail script on error
set -e

function runSystemLiquibase {
    echo "Running system liquibase"
    mvn org.apache.maven.plugins:maven-dependency-plugin:3.0.1:copy -DoutputDirectory=target -Dartifact=uk.gov.justice.services:framework-system-liquibase:${FRAMEWORK_VERSION}:jar
    java -jar target/framework-system-liquibase-${FRAMEWORK_VERSION}.jar --url=jdbc:postgresql://localhost:5432/${CONTEXT_NAME}system --username=${CONTEXT_NAME} --password=${CONTEXT_NAME} --logLevel=info update
    echo "Finished executing system liquibase"
}


runSystemLiquibase