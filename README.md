# readers_writers_problem
Classic concurrency problem solved in Java

#### Details:
- Maven based project
- Tested with JUnit - 90% of Code Coverage reported by SonarQube
- Fully documented with JavaDoc


### Starting the program using the command:

```shell
java -jar ./main/target/main-1.0-jar-with-dependencies.jar <n_readers> <n_writers>
```

Running the program without any parameters results in default settings of 10 readers and 3 writers.

### The program demonstrates a solution to the readers and writers problem using three semaphores.
