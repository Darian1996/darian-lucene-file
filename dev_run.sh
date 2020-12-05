#!/usr/bin/env bash

mvn clean package -Dmaven.test.skip=true
java  -jar target/darian-lucene-file-0.0.1-SNAPSHOT.jar
