# Enigma Quest - Payara Qube Quarkus Demo

**Quarkus Demo Application** showcasing deployment on Payara Qube, the zero-maintenance Java app deployment platform.

This interactive riddle game demonstrates a production-ready Quarkus application configured for Payara Qube deployment.
The application features:

- **RESTful API** endpoints for riddles, user submissions, and statistics
- **OpenAPI/Swagger** documentation
- **Micrometer metrics** for monitoring and observability
- **MicroProfile** Fault Tolerance (retry, timeout, fallback)
- **Bean Validation** for request validation
- **Static web UI** with interactive JavaScript game

## About Payara Qube

Payara Qube is a zero-maintenance, unified platform for deploying Jakarta EE, Spring, or Quarkus applications on your
own infrastructure with complete data sovereignty. It eliminates the complexity of Kubernetes setup and ongoing
maintenance while providing:

- Framework-agnostic support (Jakarta EE, Spring, Quarkus)
- Zero Kubernetes setup required
- Complete data sovereignty
- Integrated monitoring and observability
- Rapid deployment (hours to minutes)

This project uses Quarkus, the Supersonic Subatomic Java Framework. Learn more at <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/riddles-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Payara Qube Support ([guide](https://docs.quarkiverse.io/quarkus-payara-qube/dev/)): Runtime configuration for
  deploying to Payara Qube
