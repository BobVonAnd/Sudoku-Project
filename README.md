# Sudoku Project 2026
## Contributors
Project contributors:
* Nikolaj s245909
* Simon s214528
* Frederik s215062
* Victor s224230

Advisor \& Professor:
* Karl Magnus Heuer

## Installation
This project was built for Java 17+ and requires Maven 3.8+. The installation
varies depending on the operating system, so here are some basic instructions.

### Windows

We have built this on a Zulu distribution of Java. So for the sake of simplicity, we recommend a distribution of Azul Zulu, which can be found
[here](https://www.azul.com/downloads/?package=jdk#zulu). Scroll down and
select a LTS Java version of at least 17, we recommend `Java 21 (LTS)`, then click on the operating system that you are using, and install. 

To check if java is installed correctly, you can run the command:
```bash
java -version
```

Now you need to install Maven. You can so by following this guide:

https://maven.apache.org/

Install any version that is 3.8+.

## Running the packaged release
First. Follow the installation instructions.


Install the .jar from the [latest release](https://github.com/BobVonAnd/Sudoku-Project/releases) and run it via:
```bash
java -jar lwjgl-sudoku-1.0-SNAPSHOT.jar
```

## Compiling and Running From Source

The project is a maven project called lwjgl-sudoku, which means you need to first:
* Navigate to the `lwjgl-sudoku` directory. Do this via `cd lwjgl-sudoku`

**To compile and run directly:**

On Windows:
```bash
mvn install
mvn clean compile exec:java
```

## Packaging and running the project

The project is a maven project called lwjgl-sudoku, which means you need to first:
* Navigate to the `lwjgl-sudoku` directory. Do this via `cd lwjgl-sudoku`

**To package the project into a .jar:**

On Windows:
```bash
mvn install
mvn clean package
```

**To run the package:**

On Windows:
```bash
java -jar target/lwjgl-sudoku-1.0-SNAPSHOT.jar
```
