package org.codi.lct.core.tester;

/**
 * An executor, which when provided a test case, figures out everything it needs to, and then runs the test for each
 * solution method. The test instance is shared across all solution methods, but not across test cases. A new executor
 * is created for each test case. The implementation is internal and may change over time.
 */
public interface LCExecutor {

    void executeTestCase(LCTestCase testCase);
}
