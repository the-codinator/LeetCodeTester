# Detailed Usage Documentation

Default settings configuration
---

```properties
# lc-tester.properties
trackExecutionTime=false
allowMissingExpectedValues=false
executionTimeLimit=0
customSerializationThreshold=10000
```

The properties file `lc-tester.properties` allows configuration of global settings.

- `trackExecutionTime` - Whether the tester should output timing related info (default: `false`)
- `allowMissingExpectedValues` - Whether we want to allow certain test case outputs to be missing (default: `false`)
- `executionTimeLimit` - TLE threshold (default: `0` which means "do not track") (Feature currently not supported)
- `customSerializationThreshold` - max size threshold for structures like `ListNode` & `TreeNode`
  (to fail-fast in case of loops) (default: `10000`, 0 or negative means infinity)

Testing Annotations
---

### [`@LCSolution`](./src/main/java/org/codi/lct/annotation/LCSolution.java)

Add this to any (`public non-static`) method(s) to mark them as solution methods to run.
This annotation is optional if the test class contains a single `public non-static` method.
Note: multiple solution methods may exist, but they MUST have similar signatures.

### [`@LCTestCaseGenerator`](./src/main/java/org/codi/lct/annotation/LCTestCaseGenerator.java)

Add this to any method(s) to invoke them before the test to obtain Custom Test Cases.
Note: Must have signature `@LCTestCaseGenerator public static List<LCTestCase> anyMethodName`

### [`@LCDataStructureTest`](./src/main/java/org/codi/lct/annotation/LCDataStructureTest.java)

Marker annotation on a nested class or the test class itself to indicate that the test is not algorithm based,
but rather data structure implementation based.

Feature not yet supported!

### [`@LCInputConstraint`](./src/main/java/org/codi/lct/annotation/LCInputConstraint.java)

Interceptor method called before the test runs to perform constraint checks on the input.

Feature not yet supported!

### [`@LCOutputTransformation`](./src/main/java/org/codi/lct/annotation/LCOutputTransformation.java)

Wrapper function over the return type of the solution methods to transform the output before checking equality.

Feature not yet supported!

Configuration Annotations
---

### [`@LCInputFiles`](./src/main/java/org/codi/lct/annotation/settings/LCInputFiles.java)

Marker annotation on class to indicate a different set of input file paths rather than the default.

### [`@LCExecutionTimeLimit`](./src/main/java/org/codi/lct/annotation/settings/LCExecutionTimeLimit.java)

Marker annotation on class / solution method to modify the TLE threshold config.

### [`@LCTrackExecutionTime`](./src/main/java/org/codi/lct/annotation/settings/LCExecutionTimeLimit.java)

Marker annotation on class / solution method to modify the config value for tracking method execution time.

### [`@LCAllowMissingExpectedValues`](./src/main/java/org/codi/lct/annotation/settings/LCAllowMissingExpectedValues.java)

Marker annotation on class / solution method to modify the config value for allowing missing expected values in test cases.

Extension & Base Class
---

[`LCTester`](./src/main/java/org/codi/lct/core/LCTester.java) is the base class you should extend to write your solution implementation.
This is the recommended and simplest approach for testing your solution.

[`LCTestCase`](./src/main/java/org/codi/lct/core/LCTestCase.java) is the data object for defining custom test cases (input & expected result).

[`@LCExtension`](./src/main/java/org/codi/lct/core/LCExtension.java) is the top level JUnit 5 extension you can use to manually implement test cases.

[`LCExecutor`](./src/main/java/org/codi/lct/core/LCExecutor.java) defines the executor interface, an instance of which is injected for running manual test cases. 

Utility
---

Standard data structures used by LeetCode are available for direct use int the [ds package](./src/main/java/org/codi/lct/ds).
They are fully integrated with this framework for parsing input/output & checking equality.
Additional (non-standard) data structures are also provided in the [ds.extra package](./src/main/java/org/codi/lct/ds/extra).
These are *strictly* for utility purposes only. They are *not* integrated with this framework.

[`LCUtil`](./src/main/java/org/codi/lct/core/LCUtil.java) provides some utility functions for debugging and test boilerplate.

References
---

Checkout the [examples](./src/test/java/org/codi/lct/example) & [unit tests](./src/test/java/org/codi/lct/test) for live references.

Extending Data Structures & Equality Checkers
---

You can define your own data structures and use with this framework. The following items would be necessary for proper functioning:

- `@JsonDeserialize(using = MyDataStructureDeserializer.class)` annotation on the class for parsing input from data files.
  This entirely follows the rules of Jackson Databind for reading data from simpler types to the custom data structure (complex) type.
- `@JsonSerialize(using = MyDataStructureSerializer.class)` annotation on the class for converting the value from one type to another.
  This entirely follows the rules of Jackson Databind for converting values across different data types so that the equality matchers can do their thing.
- `@EqualityChecker(MyEqualityChecker.class)` annotation on the class to define a custom equality checked between the expected and actual test case results.
  TODO: feature not yet supported!

Note: if the data structure is a standard one, I would recommend you to contribute it back to this repo via pull requests.
