## Compiling and Running From Source

The project is a maven project called lwjgl-sudoku, which means you need to first:
* Navigate to the `lwjgl-sudoku` directory. Do this via `cd lwjgl-sudoku`

**To compile and run directly:**
On Windows:
```bash
mvn install
mvn clean compile exec:java
```

On MacOS:
```bash
mvn install
mvn clean compile exec:exec
```