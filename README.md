Openfaas Java SDK
==========
[![License (LGPL version 3)](https://img.shields.io/badge/license-GNU%20LGPL%20version%203.0-blue.svg)](https://github.com/zodo-dev/openfaas-java-sdk/blob/develop/LICENCE)
![Auto build CI](https://github.com/zodo-dev/openfaas-java-sdk/workflows/Auto%20build%20CI/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=zodo-dev%3Aopenfaas-java-sdk&metric=alert_status)](https://sonarcloud.io/dashboard?id=zodo-dev%3Aopenfaas-java-sdk)
[![Coverage Status](https://coveralls.io/repos/github/zodo-dev/openfaas-java-sdk/badge.svg?branch=develop)](https://coveralls.io/github/zodo-dev/openfaas-java-sdk?branch=develop)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.zodo/openfaas-java-sdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.zodo/openfaas-java-sdk/)
[![Javadoc](http://www.javadoc.io/badge/dev.zodo/openfaas-java-sdk.svg)](http://www.javadoc.io/doc/dev.zodo/openfaas-java-sdk)

---
Java implementation to call Openfaas functions

Flow
---
![Flow](https://zodo-dev.github.io/openfaas-java-sdk/images/openfaas-java-sdk-flow.png)

Openfaas Links?
---
Website: https://www.openfaas.com/

Docs: https://docs.openfaas.com/

Github: https://github.com/openfaas


Wants to contribute to openfaas-java-sdk?
---
Before working on the code, if you plan to contribute changes, please read the following [CONTRIBUTING](CONTRIBUTING.md) document.

Using openfaas-java-sdk
---

Maven:

``` xml
<dependency>
  <groupId>dev.zodo</groupId>
  <artifactId>openfaas-java-sdk</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Gradle:

```groovy
compile "dev.zodo:openfaas-java-sdk:1.0.0-SNAPSHOT"
```

If you want to use snapshots first config OSS Sonatype Snapshots repository:

Maven:

``` xml
<repositories>
    <repository>
        <id>oss-snapshots</id>
        <name>OSS Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

Gradle:

```groovy
repositories {
    maven {
        url 'https://oss.sonatype.org/content/repositories/snapshots'
    }
}
```

And then the dependency:

``` xml
<dependency>
  <groupId>dev.zodo</groupId>
  <artifactId>openfaas-java-sdk</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Gradle:

```groovy
compile 'dev.zodo:openfaas-java-sdk:1.0.0-SNAPSHOT'
```

Documentation and samples
---

For documentation and samples check out our [wiki](https://github.com/zodo-dev/openfaas-java-sdk/wiki)

Getting Started
---
Go to [Getting Started](https://zodo-dev.github.io/openfaas-java-sdk/getting-started.html)  to usage example.

Need help or found an issue?
---

When reporting an issue through the [issue tracker](https://github.com/zodo-dev/openfaas-java-sdk/issues?state=open)
on GitHub, please use the following guidelines:

* Check existing issues to see if it has been addressed already
* The version of openfaas-java-sdk you are using
* A short description of the issue you are experiencing and the expected outcome
* Description of how someone else can reproduce the problem
* Paste error output or logs in your issue or in a Gist. If pasting them in the GitHub issue, wrap 
it in three backticks: ```  so that it renders nicely
* Write a unit test to show the issue!

License
---

This project and its documentation are licensed under the LGPL license. Refer to [LICENSE](LICENSE) for more information.
