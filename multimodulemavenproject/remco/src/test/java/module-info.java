module com.triodos.remco.test {
    exports com.triodos.remco.test;
    requires org.junit.jupiter.api;
    requires com.triodos.remco;
    opens com.triodos.remco.test to org.junit.platform.commons;
}
