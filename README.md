JSON-SS
=======

See the [JSON-SS Wiki](https://github.com/metadatacenter/json-ss/wiki) for documentation.

#### Building Prerequisites

To build this library you must have the following items installed:

+ A tool for checking out a [Git](http://git-scm.com/) repository.
+ Apache's [Maven](http://maven.apache.org/index.html).
+ [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

#### Building

Get a copy of the latest code:

    git clone https://github.com/metadatacenter/json-ss.git 

Change into the json-ss directory:

    cd json-ss

Then build it with Maven:

    mvn clean install

On build completion your local Maven repository will contain the generated json-ss-${version}.jar file.

A [Build Project](https://github.com/metadatacenter/json-ss-project) is provided to build core JSON-SS-related components.

