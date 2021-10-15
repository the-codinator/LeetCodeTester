module org.codi.LeetCodeTester {
    /* Exported from module for usage */
    // Annotations
    exports org.codi.lct.annotation;
    exports org.codi.lct.annotation.settings;
    // Core Testing
    exports org.codi.lct.core;
    exports org.codi.lct.core.tester;
    // Utilities
    exports org.codi.lct.ds;
    exports org.codi.lct.ds.extra;

    /* Module Dependencies */
    // Lombok (Compile Only)
    requires static lombok;
    // Jackson
    requires com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;
    // JUnit
    requires transitive org.junit.jupiter.api;
    // Apache Commons
    requires org.apache.commons.lang3;
    // Logging
    requires org.slf4j;
    // Javadoc
    requires static org.apiguardian.api;

    /* Expose for reflection & access */
    exports org.codi.lct.impl.adapter.eq to org.apache.commons.lang3;
    exports org.codi.lct.impl.testcase to org.junit.platform.commons;
    opens org.codi.lct.core.tester to org.junit.platform.commons;
    opens org.codi.lct.impl.adapter.ser to com.fasterxml.jackson.databind;
    opens org.codi.lct.impl.adapter.deser to com.fasterxml.jackson.databind;
}
