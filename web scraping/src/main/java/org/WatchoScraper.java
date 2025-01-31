package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

/**
 * Scraper class for extracting watch information from the Watcho website.
 */
public class WatchoScraper extends Thread {

    /**
     * Instantiates a new Watcho scraper.
     */
    public WatchoScraper() {
        try {
            scrapeWatcho();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void scrapeWatcho() throws Exception {

        String newPrice = "";
        Set<String> uniqueNames = new HashSet<>();

        String url = "https://www.watcho.co.uk/Bestsellers.html";
        String name = "";

        // Number of pages from the website that will be scraped
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        WebDriver driver = new ChromeDriver(options);

        driver.get(url);

        // Use WebDriverWait to wait for specific elements to be present
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Wait for the product elements to be present
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".product")));

        // Get the HTML source after the page is fully loaded
        String htmlSource = driver.getPageSource();

        // Parse the HTML source using Jsoup
        Document doc = Jsoup.parse(htmlSource);
        Elements prods = doc.select(".product"); // Change the class to match the actual class in your HTML

        for (Element prod : prods) {
            Elements brand = prod.select(".card-text.underline[data-test-info-type=brandName]"); // Brand
            Elements description = prod.select(".card-title.product-custom-tag a"); // Description

            String long_name = description.text();
            String[] words = long_name.split("\\s+");
            if (words.length >= 3) {
                name = String.join(" ", Arrays.copyOfRange(words, 0, 3));
            } else {
                name = long_name;
            }

            Elements price = prod.select(".price.price--withTax"); // Price

            Elements anchorTags = prod.select("a");

            String watchPageUrl = "";
            for (Element anchorTag : anchorTags) {
                // Get the href attribute value
                String hrefValue = anchorTag.attr("href");
                watchPageUrl = hrefValue; // URL
            }

            // Corrected selector for the image tag
            Elements imageTags = prod.select("img.card-image");

            String imageUrl = "";
            for (Element imageTag : imageTags) {
                // Get the src attribute value
                imageUrl = imageTag.attr("data-src");
            }

            // Check if the name is unique before adding it to the ArrayList
            if (uniqueNames.add(name) && !name.isEmpty()) {
                newPrice = price.text();
                newPrice = newPrice.replaceAll("[^0-9.]+", "");
                if (!Hibernate.searchWatch(name)) {
                    Hibernate.addWatch(brand.text(), name, description.text(), imageUrl);// Add data
                    Hibernate.addComparison(Hibernate.searchWatchID(name), watchPageUrl, Float.parseFloat(newPrice), "Watcho");// Add data
                } else {
                    Hibernate.addComparison(Hibernate.searchWatchID(name), watchPageUrl, Float.parseFloat(newPrice), "Watcho");// Add data
                }
            }
        }
        driver.quit();

    }
}
