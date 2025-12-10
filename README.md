# email-insights-performance-tests

Performance test suite for `email-insights`, using [performance-test-runner](https://github.com/hmrc/performance-test-runner) under the hood.

NOTE: These test rely on specific data in `staging` environment.
If the data is missing then it can be reloaded using the data files in `src/test/resources/data/`.
Specifically

| file                |type of data|
|---------------------|------------|
| email_ipp.csv       | ipp relationship data|
| email_reject.csv    | watchlist data|
| email_addresses.csv |used to drive the watchlist simulation|

## Running the tests

Prior to executing the tests ensure you have:

* Docker - to start mongo & postgres container
* Installed/configured service manager 2 [sm2](https://github.com/hmrc/sm2)

If you don't have mongodb installed locally you can run it in docker using the following commands:

```bash
    docker run --restart unless-stopped --name mongodb -p 27017:27017 -d percona/percona-server-mongodb:7.0 --replSet rs0
    docker exec -it mongodb mongosh --eval "rs.initiate();"
```

If you don't have postgres installed locally you can run it in docker using the following command

```bash
    docker run -d --rm --name postgresql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:latest
```

Run the following script to start the dependent services locally `./start_services.sh`

#### Smoke test

Before raising a PR, ensure the smoke tests pass locally by running this script:
```bash
  ./run-local.sh
```

#### Run the performance test

To run a full performance test against staging environment, implement a job builder and run the test **only** from Jenkins.

### Scalafmt
This repository uses [Scalafmt](https://scalameta.org/scalafmt/), a code formatter for Scala. The formatting rules configured for this repository are defined within [.scalafmt.conf](.scalafmt.conf).

To apply formatting to this repository using the configured rules in [.scalafmt.conf](.scalafmt.conf) execute:

 ```bash
 sbt scalafmtAll
 ```

To check files have been formatted as expected execute:

 ```bash
 sbt scalafmtCheckAll scalafmtSbtCheck
 ```

[Visit the official Scalafmt documentation to view a complete list of tasks which can be run.](https://scalameta.org/scalafmt/docs/installation.html#task-keys)

### Logging

The default log level for all HTTP requests is set to `WARN`. Configure [logback.xml](src/test/resources/logback.xml) to update this if required.

### WARNING :warning:

Do **NOT** run a full performance test against staging from your local machine. Please [implement a new performance test job](https://docs.tax.service.gov.uk/mdtp-handbook/documentation/mdtp-test-approach/performance-testing/performance-test-a-microservice/index.html) and execute your job from the dashboard in [Performance Jenkins](https://performance.tools.staging.tax.service.gov.uk).

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

