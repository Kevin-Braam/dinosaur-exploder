#!/bin/bash
set -e # Stop the script if a command fails

mvn clean package
java -jar target/analysis-1.0-SNAPSHOT.jar
