
# ThreadCheckArray — Multithreaded Subset Sum

## Overview

ThreadCheckArray is a Java project that searches for a subset of integers whose sum equals a specified target value.

The project demonstrates:
- Recursive Subset Sum algorithms.
- Multithreading and thread synchronization.
- Refactoring from `int[]` to `ArrayList<Integer>`.
- Professional code documentation using Javadoc.
- Git collaboration and version control.

## 1. Recursive Multithreaded Subset Sum Algorithm

### Problem Description

Given an array of integers and a target sum `b`, the algorithm determines whether a subset of the elements adds up to the target.

For example:

```text
Array: [3, 5, 8]
Target: 11
Solution: 3 + 8 = 11
```

The selected elements are represented using a boolean array:

```java
boolean[] winArray;
```

Each position indicates whether the corresponding element is included in the solution.

### Recursive Algorithm

The recursive method explores two possibilities for each element:

1. Include the current element in the subset.
2. Exclude the current element from the subset.

The recursive calls follow this structure:

```java
rec(n - 1, b - array.get(n - 1));
rec(n - 1, b);
```

The algorithm uses a recursive include/exclude search, which has exponential worst-case time complexity:

```text
Time Complexity: O(2^n)
Space Complexity: O(n) per worker
```

### Multithreading

The program creates two threads to divide the search space.

**Thread 1:**
- Searches for solutions that include the last element.
- Recursively checks the remaining elements.

**Thread 2:**
- Searches for solutions that exclude the last element.
- Recursively checks the remaining elements.

Both threads operate on the same `SharedData` object.

```java
Thread thread1 = new Thread(
    new ThreadCheckArray(sd), "thread1"
);

Thread thread2 = new Thread(
    new ThreadCheckArray(sd), "thread2"
);

thread1.start();
thread2.start();

thread1.join();
thread2.join();
```

The recursive searches run independently, allowing the two branches to execute concurrently.

### Thread Synchronization

The project uses Java's `synchronized` mechanism to coordinate access to shared information.

```java
synchronized (sd) {
    if (sd.getFlag()) {
        return;
    }
}
```

The shared object contains:
- The integer collection.
- The target sum.
- A flag indicating whether a solution was found.
- A boolean array representing the solution.

Each worker also maintains its own local search state.

Synchronization helps prevent conflicting access to shared variables and allows threads to detect when another thread has found a solution.

The `join()` method ensures that the main thread waits for both worker threads to finish before displaying the result, provided the wait is not interrupted.

**Concurrency limitation:** The completion flag and winning array are published in separate synchronized blocks. Therefore, publication of the complete result is not atomic.

## 2. Refactoring to ArrayList<Integer>

### Original Implementation

The original implementation stored integers using a primitive array:

```java
int[] array;
```

### Refactored Implementation

The integer collection was changed to:

```java
ArrayList<Integer> array;
```

This allows the program to use Java's collection framework.

### Main Changes

| Original Code | Refactored Code |
|---|---|
| `int[] array` | `ArrayList<Integer> array` |
| `array.length` | `array.size()` |
| `array[index]` | `array.get(index)` |
| `array[index] = value` | `array.add(value)` |
| `new boolean[array.length]` | `new boolean[array.size()]` |

### ArrayList Initialization

```java
ArrayList<Integer> array = new ArrayList<>();
```

Elements are inserted using:

```java
array.add(value);
```

Elements are accessed using:

```java
array.get(index);
```

The collection size is obtained using:

```java
array.size();
```

### Boolean Arrays

All boolean arrays remain primitive arrays:

```java
boolean[] winArray;
```

They are initialized dynamically:

```java
winArray = new boolean[array.size()];
```

This preserves the original representation of selected elements.

### Benefits of Refactoring

- Dynamic collection sizing.
- Improved flexibility when adding elements.
- Integration with Java's Collections Framework.
- Clearer collection operations.
- Easier future maintenance.

The refactoring is intended to preserve the original algorithm, thread synchronization, and program behavior.

**Integration note:** In the supplied project, `TestThreadCheckArray` still uses `int[]` while `SharedData` expects `ArrayList<Integer>`. The entry point must also be updated for successful compilation.

## 3. Javadoc Documentation

The project uses Javadoc to describe classes, fields, constructors, and methods.

Javadoc provides structured documentation that can be generated automatically into HTML pages.

### Class Documentation

Class-level documentation describes:
- The purpose of the class.
- Its responsibilities.
- Its relationship with other classes.
- Its role in multithreading.

### Field Documentation

Fields should include explanations of their purpose and how they are used.

For example:

```java
/**
 * Stores the integer values used by the
 * recursive subset sum algorithm.
 */
private ArrayList<Integer> array;
```

### Constructor Documentation

Constructors should describe their parameters and initialization behavior.

```java
/**
 * Constructs a SharedData object.
 *
 * @param array the input integer collection
 * @param b the target sum
 */
```

### Method Documentation

Methods should document:
- Their purpose.
- Parameters.
- Return values.
- Side effects.
- Relevant synchronization behavior.

Example:

```java
/**
 * Recursively searches for a subset whose
 * sum matches the remaining target.
 *
 * @param n the number of elements to examine
 * @param b the remaining target sum
 */
```

### Generic Types in Javadoc

Generic types should be formatted using `{@code ...}` to prevent documentation parsing errors.

For example:

```java
/**
 * Returns the integer collection.
 *
 * @return the {@code ArrayList<Integer>}
 */
```

### Generating Documentation

Javadoc HTML documentation can be generated using:

```bash
javadoc -d docs src/*.java
```

The generated documentation will be stored in the `docs` directory.

The supplied project includes Javadoc in `ThreadCheckArray.java`. Additional documentation may be needed in the remaining classes.

## 4. Project Structure

```text
ThreadCheckArray_Base/
|
|-- src/
|   |-- SharedData.java
|   |-- ThreadCheckArray.java
|   |-- TestThreadCheckArray.java
|
|-- README.md
```

### SharedData.java

Responsible for storing information shared between the worker threads.

Contains:
- The input collection.
- The target sum.
- The shared completion flag.
- The winning subset representation.

### ThreadCheckArray.java

Implements the recursive Subset Sum algorithm.

Responsible for:
- Recursive searching.
- Managing thread-local search state.
- Checking shared completion status.
- Reporting successful results.

### TestThreadCheckArray.java

Contains the main entry point.

Responsible for:
- Reading user input.
- Creating the shared data object.
- Creating and starting threads.
- Waiting for thread completion.
- Displaying the result.

## 5. Compilation and Execution

### Requirements

- Java Development Kit (JDK).
- Java compiler (`javac`).
- Java runtime environment.
- IntelliJ IDEA or another Java-compatible IDE.

No external libraries are required.

### Compile

After updating the entry point to use `ArrayList<Integer>`:

```bash
mkdir -p out
javac -d out src/*.java
```

### Run

```bash
java -cp out TestThreadCheckArray
```

### Expected Program Behavior

The program requests:
1. The number of elements.
2. The integer values.
3. The target sum.

The two threads search for a valid subset.

If a solution is found, the program displays the selected elements.

Otherwise, it reports that no solution was found.

## 6. Authors and Git Collaboration

### Authors

| Author | GitHub Username | Responsibilities |
|---|---|---|
| [Author 1] | [nofar1352] | [Actual contributions] |
| [Author 2] | [ofir23] | [Actual contributions] |
| [Author 3] | [GitHub Username] | [Actual contributions] |

Replace these placeholders with the actual team members and their verified contributions.

### Git Collaboration

Git was used for version control and collaborative development.

The recommended workflow includes:
- Creating separate branches for individual tasks.
- Committing changes with descriptive messages.
- Pushing changes to GitHub.
- Reviewing code before merging.
- Resolving merge conflicts.
- Testing the integrated project.

### Example Git Commands

Create a branch:

```bash
git checkout -b feature/arraylist-refactor
```

Stage changes:

```bash
git add .
```

Commit changes:

```bash
git commit -m "Refactor integer array to ArrayList"
```

Push changes:

```bash
git push -u origin feature/arraylist-refactor
```

### Collaboration Roles

Team responsibilities may include:

**Algorithm Development**
- Implementing the recursive Subset Sum search.
- Maintaining the original program logic.

**Data Structure Refactoring**
- Converting integer arrays to `ArrayList<Integer>`.
- Updating collection access methods.
- Preserving primitive boolean arrays.

**Documentation and Testing**
- Writing Javadoc comments.
- Creating and maintaining the README.
- Testing compilation and functionality.
- Reviewing the integrated project.

Actual contributions should be recorded according to the team's Git history and completed work.

## 7. Testing and Limitations

Recommended test cases include:

- A valid subset including the last element.
- A valid subset excluding the last element.
- No valid subset.
- Duplicate integer values.
- Negative integer values.
- Target sum equal to zero.
- A single-element input.
- Multiple valid subsets.

The current implementation has several limitations:

- Empty input is not supported by the recursive search.
- The one-element target-zero case is not handled consistently.
- Result publication is not fully atomic.
- Worker behavior depends on thread names.
- Interrupted `join()` calls require more robust handling.
- The supplied entry point requires an `ArrayList<Integer>` compatibility fix.

## 8. License

### Braude College Academic License

This project is designated as academic coursework under the:

**Braude College Academic License**

The project was developed for educational and academic purposes.

All contributors must follow applicable academic integrity, attribution, and institutional requirements.

The official legal terms of the Braude College Academic License were not included in the supplied project.

This README does not independently grant permission for redistribution, modification, or commercial use.

Before public distribution, the official licensing terms should be confirmed with the college or course staff.

If provided, the approved license text should be added to a separate `LICENSE` file.

---

**Project:** ThreadCheckArray

**Language:** Java

**Algorithm:** Recursive Multithreaded Subset Sum

**Data Structure:** ArrayList<Integer>

**Synchronization:** Java synchronized blocks

**Documentation:** Javadoc

**Version Control:** Git and GitHub

**License:** Braude College Academic License
