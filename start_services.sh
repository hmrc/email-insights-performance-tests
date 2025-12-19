#!/usr/bin/env bash

sm2 --start EMAIL_INSIGHTS_PROXY EMAIL_GATEWAY INTERNAL_AUTH CIP_RISK --appendArgs '{
            "EMAIL_INSIGHTS_PROXY": [
                "-J-Dauditing.consumer.baseUri.port=6001",
                "-J-Dauditing.consumer.baseUri.host=localhost",
                "-J-Dauditing.enabled=false",
                "-J-Dmicroservice.services.access-control.enabled=true",
                "-J-Dmicroservice.services.access-control.allow-list.0=email-gateway",
                "-J-Dmicroservice.services.access-control.allow-list.1=email-insights-performance-tests"
            ],
            "EMAIL_INSIGHTS": [
                "-J-Dmicroservice.phone-number-insights.database.dbName=postgres",
                "-J-Dauditing.enabled=false"
            ],
            "CIP_RISK": [
                "-J-Dauditing.enabled=false"
            ]
        }'