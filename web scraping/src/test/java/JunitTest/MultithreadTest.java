package JunitTest;

import org.example.Multithread;
import org.example.WatchoScraper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for Multithread operations.
 */
@DisplayName("Multithread Test")
public class MultithreadTest {

    @Test
    @DisplayName("Test Multithread Run")
    void testMultithreadRun() {
        // Test if the Multithread class runs without exceptions
        Multithread multithread = new Multithread();
        assertNotNull(multithread);
    }

    @Test
    @DisplayName("Test Scraper Thread (testing one example)")
    void testWatchoScraperThread() {
        // Test if the WatchoScraper thread starts without exceptions
        Thread watchoThread = new WatchoScraper();
        assertDoesNotThrow(watchoThread::start);
    }
}
