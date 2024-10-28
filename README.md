# SootShow

SootShow is a tool for visualizing the results of static analysis of Java bytecode and apk files using the Soot framework. 

It is designed to help developers understand the results of static analysis and to help researchers evaluate the effectiveness of their static analysis tools.

SootShow is implemented as a plugin for the Soot framework, which is a popular open-source framework for analyzing and transforming Java bytecode.

## Features

SootShow provides the following features:

- choose to use the default Soot analysis or a custom analysis
- select apk or java class files to analyze
- print jimple representation of the analyzed apk file code
- print extend relationships of the analyzed java class file

## Installation

### dependencies

use pom.xml to install the dependencies

```bash
mvn clean install
```

or use the soot jar file in the libs folder

### android platform file

get the android platform file from this repo:

https://github.com/Sable/android-platforms

## Structure

- sootshow: the main project
- sootshow.libs: the soot jar file
- sootshow.apks: the apk files for testing
- sootshow.javaclass: the java class files for testing

## Usage

### Run the SootShow tool

click run button in the IDE or run the following command in the terminal:

```bash
java -cp sootshow-1.0-SNAPSHOT.jar:soot-trunk.jar sootshow.SootShow
```