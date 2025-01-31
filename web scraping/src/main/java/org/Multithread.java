package org.example;

/**
 * The type Multithread.
 */
public class Multithread extends Thread {

    /**
     * Constructor that runs the scraper threads.
     */
    public Multithread() {
        try {
            runScrapers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initiates and runs scraper threads for different websites.
     * @throws InterruptedException If an error occurs during thread interruption.
     */
    private void runScrapers() throws InterruptedException {
        // Create threads for each scraper class
        Thread watchoThread = new WatchoScraper();
        Thread goldsmithThread = new GoldsmithScraper();
        Thread watchesofamericaThread = new WatchesOfAmericaScraper();
        Thread watchmaxxThread = new WatchmaxxScraper();
        Thread secretThread = new SecretsalesScraper();

        // Start all threads
        watchoThread.start();
        goldsmithThread.start();
        watchesofamericaThread.start();
        watchmaxxThread.start();
        secretThread.start();

        // Join all threads, waiting for their completion
        try {
            watchoThread.join();
            goldsmithThread.join();
            watchesofamericaThread.join();
            watchmaxxThread.join();
            secretThread.join();
        } catch (InterruptedException e) {
            System.out.println("One of the threads was interrupted");
        }
    }
}
