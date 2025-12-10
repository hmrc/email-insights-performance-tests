#!/usr/bin/env bash

sm2 --start EMAIL_INSIGHTS_PROXY EMAIL_INSIGHTS EMAIL_GATEWAY INTERNAL_AUTH --appendArgs '{
        "EMAIL_INSIGHTS_PROXY": [
            "-J-Dauditing.consumer.baseUri.port=6001",
            "-J-Dauditing.consumer.baseUri.host=localhost",
            "-J-Dmicroservice.services.access-control.enabled=true",
            "-J-Dmicroservice.services.access-control.allow-list.0=email-gateway",
            "-J-Dmicroservice.services.access-control.allow-list.1=email-insights-performance-tests"
        ],
        "EMAIL_INSIGHTS": [
            "-J-Dmicroservice.email-insights.database.dbName=postgres",
            "-J-Dmicroservice.email-insights.database.use-canned-data=true",
            "-J-Dauditing.enabled=true"
        ]
    }'