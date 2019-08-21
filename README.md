# RIEAU API

[![CircleCI](https://circleci.com/gh/MTES-MCT/rieau-api/tree/master.svg?style=svg)](https://circleci.com/gh/MTES-MCT/rieau-api/tree/master)

> API backend de RIEAU

## Développement

### Prérequis

* Java 11, par exemple AdoptOpenJDK 11 installé depuis [sdkman](https://sdkman.io).

* Adaptez les variables à chaque environnement:

```shell
cp src/main/resources/application-{env}.properties.sample src/main/resources/application-{env}.properties
```

env = test, dev, staging ou production

* Activez les environnements en ajoutant:

```shell
-Dspring.profiles.active=<env>
```

### Dev

* Maven 3.6+, par exemple installé depuis [sdkman](https://sdkman.io).
* Spring boot CLI 2.1+, par exemple installé depuis [sdkman](https://sdkman.io).

### Lancement

```shell
./mvnw spring-boot:run
```

### Tests unitaires

* Lancez tous les tests:

```shell
./mvnw test
```

* Lancez une seule classe de test:

```shell
./mvnw test -Dtest=<nomdelaclasse> 
```

* Lancez une seule méthode de test:

```shell
./mvnw test -Dtest=<nomdelaclasse>#<nomdelamethode>
```

### Tests d'intégration

* Nécessitent une base de données PostgreSQL:

```shell
docker-compose -f src/main/docker/stack.yml up --build -d
```

* Lancez tous les tests:

```shell
./mvnw test -Dspring.profiles.active=staging
```

### Vérification des vulnérabilités

```shell
./mvnw verify
```

### Construction

```shell
./mvnw clean package
```

### Docker

* Build:

```shell
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
docker build -t tristanrobert/rieau-api -f src/main/docker/Dockerfile .
```

* Run:

```shell
docker run -p 5000:5000 --name rieau-api -d -t tristanrobert/rieau-api
```

Il est possible de changer le port http du serveur à l'éxécution en ajoutant `-e SERVER_PORT=5000`. Par défaut, il est égal à `5000`.

Il est possible de changer à l'exécution les variables d'environnement (cf. [rieau-infra](https://github.com/MTES-MCT/rieau-infra)).

* Scan des vulnérabilités:

```shell
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
-v $HOME/.trivy/caches:/root/.cache/ knqyf263/trivy tristanrobert/rieau-api:latest
```
