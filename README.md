# LeetCodeTester

A Java Testing Suite for [LeetCode][leetcode] syntax built over [JUnit 5][junit5] and [AssertJ][assertj].

Uses:

- [Lombok][lombok] for some boilerplate
- [Jackson][jackson] for Json parsing
- [Apache commons lang3][lang3] for reflection utilities
- [Slf4J][slf4j] as the logging adapter

## Usage

### Maven

Include the following dependency in your pom

```xml
<dependency>
  <groupId>org.codi</groupId>
  <artifactId>leetcode-tester</artifactId>
  <version>${lct.version}</version>
  <!-- checkout latest version under releases -->
</dependency>
```

With the above dependency, you should be able to run individual tests easily via your favorite IDEs like IntelliJ,
Eclipse, etc

Optionally include a test runner plugin like surefire in your pom to automatically run tests on project build

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <version>${surefire.version}</version>
  <!-- checkout latest version for the plugin -->
  <!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-surefire-plugin -->
</plugin>
```

### Examples

Many folks like me don't like to go through endless documentation. If you would rather look at some examples and make
your own way, checkout the [tests and examples](./src/test/java)

### Basic Usage

Create a class (`org.example.MyClass`) extending `LCTester` and define your implementation as a public non-static method.
If there are multiple candidate methods, add the `@LCSolution` annotation to the correct implementation.

Create test case file (`lct/org/example/MyClass.txt`) in your resources containing testcase(s).
The file must be divided into 2 parts: input & output.
The input section is the input for testcase(s) which follows the same format as LeetCode custom test cases input for problems.
The second part is the expected output, corresponding to each input test case.
The 2 sections MUST BE SEPARATED by AT LEAST 3 BLANK LINES.
This format is to allow easy copy-pasting between LeetCode's UI and the test file.

Run class as a JUnit 5 test from your IDE / command line.

### Advanced Usage

Refer [documentation](DOCS.md)

## Issue Reporting & Contribution

Create Git Issues and PRs

Please be as descriptive as necessary, and provide reproduction steps / explanations / code snippets wherever possible

## Disclaimer

I am in no way affiliated to LeetCode. I do not have anything to do with <leetcode.com>. For all means and purposes,
this is an independent project from LeetCode. This project is just an easy utility to write tests for code using input /
output syntax similar to that of LeetCode. Improvement ideas are welcome.

PS: If LeetCode ends up using this project, it'd be quite interesting :D


[leetcode]: https://leetcode.com/terms/
[junit5]: https://junit.org/junit5/
[assertj]: https://assertj.github.io/doc/
[lombok]: https://projectlombok.org/
[jackson]: https://github.com/FasterXML/jackson
[lang3]: https://commons.apache.org/proper/commons-lang/
[slf4j]: http://www.slf4j.org/
