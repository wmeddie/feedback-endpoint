# Crazy Custom Transform Process

This repo shows how to create a custom transform process for datavec or for hosting inside SKIL.

## How to

When using SKIL the easiest way to get started is to create a blank maven project using the quickstart archetype:

    $ mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.3

Name your artifact whatever you want, but be sure to use a unique class name so that you can specify it in your 
transform.

In the generated `pom.xml` add the following repository and dependencies:

```xml
    <repositories>
        <repository>
            <name>Skymind Nexus</name>
            <id>skymind-nexus</id>
            <url>https://nexus.skymind.io/repository/skil/</url>
        </repository>
    </repositories>
```

```xml
    <properties>
        <dl4j.version>0.9.2_skil-1.0.0</dl4j.version>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
```

```xml
    <dependencies>
        <dependency>
            <groupId>org.nd4j</groupId>
            <artifactId>jackson</artifactId>
            <version>${dl4j.version}</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.nd4j</groupId>
            <artifactId>nd4j-jackson</artifactId>
            <version>${dl4j.version}</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.datavec</groupId>
            <artifactId>datavec-api</artifactId>
            <version>${dl4j.version}</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>io.skymind</groupId>
            <artifactId>skil-core</artifactId>
            <version>1.0.4</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.21</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>
```

Since this plugin will be added to the SKIL class path, you should specify all the dependencies as provided.

Now import the project into your IDE and add a class that extends `BaseColumnTransform` or the type of transform you
 need.

The transform must be serializable as JSON so you must provide an empty constructor.

### Using the Transform Process

Once your transform is done, you can add it to your transform process code inside Zeppelin, or directly in your JSON or
Yaml file.

For example to use the crazy transform process defined here just make a json like so:

```json
{
    "initialSchema" : {
        "Schema" : {
        "columns" : [ {
            "String" : {
            "name" : "text"
            }
        } ]
        }
    },
    "actionList" : [ {
        "transform" : {
            "FeedbackEndpointProcess" : {
                "columnName" : "text"
            }
        }
    } ]
}
```

You will have to restart the Spark interpreter (or restart SKIL haven't checked) to use a plugin inside Zeppelin since
we can't modify an existing processe's class path.



### Building and Installing

To run just do `mvn clean package` and upload the jar file inside `target` to SKIL using the plugin UI.
