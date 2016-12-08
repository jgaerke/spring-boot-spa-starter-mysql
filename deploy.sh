#!/usr/bin/env bash

./gradlew bundleDeploymentFiles
eb create --envvars SPRING_PROFILES_ACTIVE=production

