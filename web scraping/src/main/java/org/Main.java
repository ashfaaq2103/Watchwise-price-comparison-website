package org.example;

/**
 * Main class to demonstrate and test database operations using Hibernate and multithreading.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Create a new instance of the Hibernate class

        // Set up the SessionFactory
        Hibernate.init();

        // Example operations
        Hibernate.deleteAllRows();

        new Multithread();

        // Shut down Hibernate
        Hibernate.shutDown();
    }
}
