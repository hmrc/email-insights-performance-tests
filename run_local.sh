#!/usr/bin/env bash

SMOKE=${1:-true}

sbt -DrunLocal=true -Dperftest.runSmokeTest=$SMOKE gatling:test \
  -DjourneysToRun.0=email-insights-simulation