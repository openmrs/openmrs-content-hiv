# <u>HIV C&T Content Package</u>

This is the OpenMRS HIV care and treatment content package.

### ESMs

```bash
configs/
├── frontend_assembly/
    └── spa-assemble-config.json
```
Required ESMs that make up the HIV care and treatment content package, defined in a `spa-assemble-config.json` file

### Frontend configuration
```bash
configs/
├── frontend_config/
    └── config.json
```

(WIP) Any configuration of the O3 frontend.

### Backend configuration
```bash
configs/
├── initializer_config/
    ├── ampathforms
    └── ...
```

Initializer compatible configuration metadata that makes up the HIV care and treatment content package. There are forms, OCL concepts, programs, encounter types, workflows, identifiers and other metadata. 

### Backend modules

To be set as dependencies in the pom.xml.
Eg:
```xml
    <dependency>
      <groupId>org.openmrs.module</groupId>
      <artifactId>webservices.rest-omod</artifactId>
      <version>2.40.0-SNAPSHOT</version>
    </dependency>
```
Will be packaged in
```bash
binaries/
└── modules/
```
---

## How to use the HIV Content Package in an OpenMRS HIS-based distribution

To integrate the `openmrs-content-hiv` package into an OpenMRS HIS-based distribution, follow these steps to modify the `pom.xml` file and manage the package resouces appropriately.

### 1. Declare the package as a Maven Dependency

Add the `openmrs-content-hiv` package to the `<dependencies>` section of your `pom.xml` file:

```xml
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

### 3. Copy the Configs & Binaries Files

Add another Maven step to then copy the config files and binaries in their final destination:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-resources-plugin</artifactId>
  <executions>
    <execution>
      <!-- Copy the OpenMRS HIV package configs -->
      <id>Copy OpenMRS HIV package configs</id>
      <phase>process-resources</phase>
      <goals>
        <goal>copy-resources</goal>
      </goals>
      <configuration>
        <outputDirectory>
          ${project.build.directory}/${project.artifactId}-${project.version}/distro/configs/openmrs/</outputDirectory>
        <overwrite>true</overwrite>
        <resources>
          <resource>
            <directory>${project.build.directory}/content-package-hiv/configs/</directory>
          </resource>
        </resources>
      </configuration>
    </execution>
    <execution>
      <!-- Copy the OpenMRS HIV package binaires -->
      <id>Copy OpenMRS HIV package binaries</id>
      <phase>process-resources</phase>
      <goals>
        <goal>copy-resources</goal>
      </goals>
      <configuration>
        <outputDirectory>
          ${project.build.directory}/${project.artifactId}-${project.version}/distro/binaries/openmrs/</outputDirectory>
        <overwrite>true</overwrite>
        <resources>
          <resource>
            <directory>${project.build.directory}/content-package-hiv/binaries/</directory>
          </resource>
        </resources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### 4. Rename Frontend Assembly File

Finally, add a step to rename the default `spa-assemble-config.json` to make it unique:

```xml
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-antrun-plugin</artifactId>
        <version>3.0.0</version>
        <executions>
          <execution>
            <id>Rename spa-assemble-config.json into hiv-spa-assemble-config.json</id>
            <phase>process-resources</phase>
            <goals>
              <goal>run</goal>
            </goals>
            <configuration>
              <target>
                <copy
                  file="${project.build.directory}/content-package-hiv/configs/frontend_assembly/spa-assemble-config.json"
                  tofile="${project.build.directory}/openmrs_frontend/hiv-spa-assemble-config.json"
                  failonerror="false" />
              </target>
            </configuration>
          </execution>
        </executions>
      </plugin>
```

### 5. Build and Validate the Distribution

Run `mvn clean install -Pvalidator`
