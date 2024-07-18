# <u>HIV C&T Content Package</u>

This is the OpenMRS HIV care and treatment content package.

The contents of this package are:

```bash
configs/
├── frontend_assembly
```
Required ESMs that make up the HIV care and treatment content package, defined in a `spa-assemble-config.json` file


```bash
configs/
├── frontend_config
```

(WIP) Any configuration of the O3 frontend.

```bash
configs/
└── initializer_config
```

Initializer compatible configuration metadata that makes up the HIV care and treatment content package. There are forms, OCL concepts, programs, encounter types, workflows, identifiers and other metadata. 



## How to use the HIV Content Package in an OpenMRS Distro HIS-based distribution

To integrate the `openmrs-content-hiv` package into your OpenMRS distribution, follow these steps to modify your `pom.xml` file and manage the package resouces appropriately.

### 1. Declare Maven Dependency

Add the `openmrs-content-hiv` package to the `<dependencies>` section of your `pom.xml` file:

```xml
<!-- Add the HIV package dependency -->
<dependency>
  <groupId>org.openmrs</groupId>
  <artifactId>openmrs-content-hiv</artifactId>
  <version>1.0-0-SNAPSHOT</version>
  <type>zip</type>
</dependency>
```

### 2. Unpack the HIV Content Package

Include a build step to unpack the contents of the `openmrs-content-hiv` package:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-dependency-plugin</artifactId>
  <executions>
    <execution>
      <!-- Unpack the OpenMRS HIV content package -->
      <id>unpack-openmrs-hiv-content-package</id>
      <phase>generate-resources</phase>
      <goals>
        <goal>unpack-dependencies</goal>
      </goals>
      <configuration>
        <excludeTransitive>true</excludeTransitive>
        <outputDirectory>${project.build.directory}/content-package-hiv</outputDirectory>
        <includeArtifactIds>openmrs-content-hiv</includeArtifactIds>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### 3. Copy the Resource Files

Add another Maven step to then copy the files in their final destination:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-resources-plugin</artifactId>
  <executions>
    <execution>
      <!-- Copy the OpenMRS HIV content package resources -->
      <id>copy-openmrs-hiv-content-package</id>
      <phase>process-resources</phase>
      <goals>
        <goal>copy-resources</goal>
      </goals>
      <configuration>
        <outputDirectory>${project.build.directory}/${project.artifactId}-${project.version}/distro/configs/openmrs/</outputDirectory>
        <overwrite>true</overwrite>
        <resources>
          <resource>
            <directory>${project.build.directory}/content-package-hiv/configs/</directory>
          </resource>
        </resources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### 4. Rename Configuration File

Finally, add a step to rename the default `spa-assemble-config.json` to make it unique:

```xml
<plugin>
  <groupId>com.coderplus.maven.plugins</groupId>
  <artifactId>copy-rename-maven-plugin</artifactId>
  <version>1.0.1</version>
  <executions>
    <!-- Rename the spa-assemble-config.json to spa-assemble-config-hiv.json -->
    <execution>
      <id>rename-spa-assemble-config</id>
      <phase>process-resources</phase>
      <goals>
        <goal>rename</goal>
      </goals>
      <configuration>
        <sourceFile>${project.build.directory}/content-package-hiv/configs/frontend_assembly/spa-assemble-config.json</sourceFile>
        <destinationFile>${project.build.directory}/openmrs_frontend/spa-assemble-config-hiv.json</destinationFile>
      </configuration>
    </execution>
  </executions>
</plugin>
```
