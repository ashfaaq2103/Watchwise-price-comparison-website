package org.example;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;

import java.util.List;

/**
 * This class handles interactions with the Hibernate framework for database operations.
 */
public class Hibernate {

    // Use this class to create new Sessions to interact with the database
    private static SessionFactory sessionFactory;

    /**
     * Empty constructor
     */
    Hibernate() {
    }

    /**
     * Sets up the session factory.
     * Call this method first.
     */
    public static void init() {
        try {
            // Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            // Load configuration from hibernate configuration file
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            // Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

            try {
                // Create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                /*
                 * The registry would be destroyed by the SessionFactory,
                 * but we had trouble building the SessionFactory, so destroy it manually
                 */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }

            // Output result
            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }

    /**
     * Closes Hibernate down and stops its threads from running.
     */
    public static void shutDown() {
        sessionFactory.close();
    }

    /**
     * Adds a new watch to the database.
     *
     * @param brand       the brand
     * @param name        the name
     * @param description the description
     * @param url         the url
     */
    public static void addWatch(String brand, String name, String description, String url) {
        // Get a new Session instance from the SessionFactory
        Session session = sessionFactory.getCurrentSession();

        // Create an instance of a WatchXML class
        WatchXML watch = new WatchXML();

        // Set values of WatchXML class that we want to add
        watch.setBrand(brand);
        watch.setName(name);
        watch.setDescription(description);
        watch.setImageUrl(url);

        // Start transaction
        session.beginTransaction();

        // Add WatchXML to the database - will not be stored until we commit the transaction
        session.save(watch);

        // Commit transaction to save it to the database
        session.getTransaction().commit();

        // Close the session and release the database connection
        session.close();
        System.out.println("Watch added to database with ID: " + watch.getId());
    }

    /**
     * Adds a new comparison data to the database.
     *
     * @param foreign the foreign
     * @param pageUrl the page url
     * @param price   the price
     * @param WebName the web name
     */
    public static void addComparison(int foreign, String pageUrl, float price, String WebName) {
        // Get a new Session instance from the SessionFactory
        Session session = sessionFactory.getCurrentSession();

        // Create an instance of a ComparisonXML class
        ComparisonXML compare = new ComparisonXML();

        // Set values of ComparisonXML class that we want to add
        compare.setForeign_id(foreign);
        compare.setUrl(pageUrl);
        compare.setPrice(price);
        compare.setWebsite(WebName);

        // Start transaction
        session.beginTransaction();

        // Add ComparisonXML to the database - will not be stored until we commit the transaction
        session.save(compare);

        // Commit transaction to save it to the database
        session.getTransaction().commit();

        // Close the session and release the database connection
        session.close();
        System.out.println("Comparison added to the database with ID: " + compare.getId());
    }

    /**
     * Searches for Watches whose name is already present in the table watch.
     *
     * @param givenName The name to search for.
     * @return True if the watch is found, false otherwise.
     */
    public static Boolean searchWatch(String givenName) {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        List<WatchXML> watchList = session.createQuery("from WatchXML where name=:givenName", WatchXML.class)
                .setParameter("givenName", givenName)
                .getResultList();

        // Close the session and release the database connection
        session.close();

        // Return true if the list is not empty, otherwise false
        return !watchList.isEmpty();
    }

    /**
     * Searches for Watches and returns the ID if found.
     *
     * @param givenName The name to search for.
     * @return The ID of the watch if found, otherwise -1.
     */
    public static int searchWatchID(String givenName) {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        List<WatchXML> watchList = session.createQuery("from WatchXML where name=:givenName", WatchXML.class)
                .setParameter("givenName", givenName)
                .getResultList();

        // Close the session and release the database connection
        session.close();

        // Return the ID if the list is not empty, otherwise return a default value (-1)
        return !watchList.isEmpty() ? watchList.get(0).getId() : -1;
    }

    /**
     * Delete all rows.
     */
//    /** Deletes a watch in a way that will work with tables with foreign keys */
    public static void deleteAllRows() {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Delete all rows from the ComparisonXML table
        Query query1 = session.createQuery("DELETE FROM ComparisonXML");
        Query query2 = session.createQuery("DELETE FROM WatchXML");

        query1.executeUpdate();
        query2.executeUpdate();

        // Update the database
        session.getTransaction().commit();

        // Close the session and release the database connection
        session.close();
        System.out.println("Rows deleted");
    }
}
