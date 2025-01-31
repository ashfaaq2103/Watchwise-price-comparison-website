package JunitTest;

import org.example.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for Hibernate operations.
 */
@DisplayName("Hibernate Test")
public class HibernateTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void initAll() {
        // Initialize any global setup if needed
    }

    @BeforeEach
    void init() {
        // Initialize the Hibernate session factory and start the session
        initSessionFactory();
        Hibernate.init();
    }

    @Test
    @DisplayName("Test Add and Search Watch")
    void testAddAndSearchWatch() {
        // Test adding a watch to the database and searching for it
        String brand = "TestBrand";
        String name = "TestWatch";
        String description = "TestDescription";
        String url = "TestURL";

        Hibernate.addWatch(brand, name, description, url);

        assertTrue(Hibernate.searchWatch(name));
    }

    @Test
    @DisplayName("Test Add and Search Comparison")
    void testAddAndSearchComparison() {
        // Test adding a comparison to the database and searching for it
        int foreignId = 1; // Replace with a valid foreign ID
        String pageUrl = "TestPageURL";
        float price = 99.99f;
        String webName = "TestWeb";

        Hibernate.addComparison(foreignId, pageUrl, price, webName);

    }

    @Test
    @DisplayName("Test Delete All Rows")
    void testDeleteAllRows() {
        // Test deleting all rows from the database
        // Add some dummy data to the database for testing
        String brand = "DummyBrand";
        String name = "DummyWatch";
        String description = "DummyDescription";
        String url = "DummyURL";

        Hibernate.addWatch(brand, name, description, url);

        // Check if the dummy data is in the database before deletion
        assertTrue(Hibernate.searchWatch(name));

        // Delete all rows from the database
        Hibernate.deleteAllRows();

        // Check if the dummy data is no longer in the database after deletion
        assertFalse(Hibernate.searchWatch(name));
    }

    @AfterEach
    void tearDown() {
        // Shutdown Hibernate after each test
        Hibernate.shutDown();
    }

    @AfterAll
    static void tearDownAll() {
        // Close the session factory after all tests are finished
        sessionFactory.close();
    }

    private static void initSessionFactory() {
        // Initialize the Hibernate session factory
        if (sessionFactory == null || sessionFactory.isClosed()) {
            StandardServiceRegistry registry = null;
            try {
                registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                System.err.println("SessionFactory initialization failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
    }
}
