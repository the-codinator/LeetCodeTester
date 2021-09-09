# LeetCodeTester

A Java Testing Suite for [LeetCode][leetcode] syntax built over [JUnit 5][junit5].

Status: Alpha release, In development.

Uses:

- [Lombok][lombok] for some boilerplate
- [Jackson][jackson] for parsing
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

I am currently releasing to GitHub packages, so you would also need to add the required repository to your `settings.xml`.
For quick setup, just copy the `.mvn` directory in this repo to the root of your project, and update the `settings.xml`
file inside it, by replacing the username & password with your own GitHub credentials.
Use a GitHub PAT as password via environment variables to avoid committing credentials to GitHub.

Additionally, include any [SLF4J supported logger implementation](http://www.slf4j.org/faq.html#where_is_binding) dependencies for output.
The tests here use [logback](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic)
with [this configuration](./src/test/resources/logback.xml)

With the above configuration, you should be able to run individual tests easily via your favorite IDEs like IntelliJ,
Eclipse, etc

### Examples

Many folks like me don't like to go through endless documentation. If you would rather look at some examples and make
your own way, checkout the [examples](./src/test/java/org/codi/lct/example)

### Basic Usage

Create a class (`org.example.MyClass`) extending `LCTester` and define your implementation as a public non-static method.
If there are multiple candidate methods, add the `@LCSolution` annotation to the correct implementation.

Create test case file (`lct/org/example/MyClass.txt`) in your resources containing testcase(s).
The file must be divided into 2 parts: input & output.
The first section is the input for testcase(s) which follows the same format as LeetCode custom test cases input for problems.
The second part is the expected output, corresponding to each input test case.
While it isn't required, keep a few blank lines between the input and output to maintain distinction.
This format is to allow easy copy-pasting between LeetCode's UI and the test file.

Run the class as a JUnit 5 test from your IDE / via command line.

### Advanced Usage

Refer [documentation](DOCS.md)

## Issue Reporting & Contribution

Create Git Issues and PRs

Please be as descriptive as necessary, and provide reproduction steps / explanations / code snippets wherever possible

### Local Project Setup

Well, it works like any other maven + java project...

How to compile ? Run `make build`

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
