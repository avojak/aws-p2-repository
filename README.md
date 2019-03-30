# AWS p2 Repository

[![Build Status](https://travis-ci.org/avojak/aws-p2-repository.svg?branch=master)](https://travis-ci.org/avojak/aws-p2-repository) 
[![Coverage Status](https://coveralls.io/repos/github/avojak/aws-p2-repository/badge.svg?branch=master)](https://coveralls.io/github/avojak/aws-p2-repository?branch=master)
[![Snyk Vulnerabilities for GitHub Repo](https://img.shields.io/snyk/vulnerabilities/github/avojak/aws-p2-repository.svg)](https://snyk.io/test/github/avojak/aws-p2-repository)
[![GitHub](https://img.shields.io/github/license/avojak/aws-p2-repository.svg)](https://github.com/avojak/aws-p2-repository/LICENSE)

Web application to host p2 repositories backed by AWS S3.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

* Maven 3.5.x
* Java 1.8+

### Installing

Package the self-contained JAR:

```console
$ mvn clean package
```

Run the JAR:

```console
$ java -jar aws-p2-repository-webapp/target/aws-p2-repository-webapp-<version>.jar
```

To view, open a web browser and navigate to `localhost:<port>/browse`.

## Running the tests

To execute all unit tests and perform code coverage analysis, run the following from the root of the project:

```console
$ mvn clean verify
```

## Deployment

Add additional notes about how to deploy this on a live system

### Required Environment Variables

* `AWS_ACCESS_KEY_ID` - See [Working with AWS Credentials](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html)
* `AWS_SECRET_KEY` - See [Working with AWS Credentials](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html)
* `AWS_S3_BUCKET_NAME` - The name of the AWS S3 bucket where the p2 repositories are stored
* `AWS_P2_REPO_WEBAPP_BRAND_NAME` - Custom website name branding
* `AWS_P2_REPO_WEBAPP_BRAND_ICON` - URL of the branding icon to use
* `AWS_P2_REPO_WEBAPP_BRAND_FAVICON` - URL of the branding favicon to use
* `AWS_P2_REPO_WEBAPP_CUSTOM_DOMAIN` - The welcome message to display on the dashboard
* `PORT` - The port number that the embedded server will use
* `P2_INSPECTOR_BASE_URL` - The base URL of the [p2 Inspector](https://github.com/avojak/p2-inspector) to retrieve p2 repository metadata

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The web application framework
* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/avojak/aws-p2-repository/tags).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
