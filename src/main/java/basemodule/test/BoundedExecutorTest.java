package basemodule.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class BoundedExecutorTest {
    @BeforeAll
    static void beforeAll() {
        System.out.println(">>> Starting BoundedExecutorTest.");
    }

    @AfterAll
    static void afterAll() {
        System.out.println(">>> Finished BoundedExecutorTest.");
    }
}
