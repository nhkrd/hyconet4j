#!/bin/bash

# clean class
rm -rf *.class
rm -rf ./test/*.class
rm -rf ./sample/*.class

# libs build and copy
./gradlew

# run test
./test.sh

