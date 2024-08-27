#!/bin/sh

CURRENT_DATETIME=$(date +%Y-%m-%dT%H:%M:%S)

java -Dspring.profiles.active=dev -jar app.jar --job.name=flight-statistics datetime="${CURRENT_DATETIME}"
