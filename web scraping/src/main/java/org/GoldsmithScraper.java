package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.util.*;

/**
 * This class represents a scraper for Goldsmiths website to extract watch information.
 * Extends Thread to allow asynchronous execution.
 */
public class GoldsmithScraper extends Thread {

    /**
     * Constructor
     */
    public GoldsmithScraper() {
        try {
            scrapeGoldsmith();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Scrapes watches from the Goldsmiths website.
     *
     * @throws Exception If an error occurs during the scraping process.
     */
    public void scrapeGoldsmith() throws Exception {
        // Number of pages from the website that will be scraped
        String url = "";
        String url_watch = "";
        String name = "";
        Set<String> uniqueNames = new HashSet<>();

        int pages = 13;

        for (int x = 0; x < pages; x++) {
            url = "https://www.goldsmiths.co.uk/c/Watches?q=&page=" + Integer.toString(x) + "&sort=";

            // Download HTML document from the website
            Document doc = Jsoup.connect(url).get();
            // Get all products on the page
            Elements prods = doc.select(".productTile");

            // Work through the products
            for (Element prod : prods) {

                Elements brand = prod.select(".productTileBrand"); // Brand
                // Get the product description
                Elements description = prod.select(".productTileName"); // Description

                String[] words = description.text().split("\\s+");
                name = String.join(" ", Arrays.copyOfRange(words, 0, 1));

                Elements price1 = prod.select(".productTilePrice"); // Price
                Elements price2 = prod.select(" .productTilePrice.salePrice");
                String price = "";

                if (price2.isEmpty()) {
                    price = price1.text();
                } else {
                    for (Element salePriceElement : price2) {
                        // Consider only direct text nodes to avoid including nested elements
                        price = salePriceElement.ownText().trim();
                    }
                }

                Elements anchorTags = prod.select("a");

                for (Element anchorTag : anchorTags) {
                    // Get the href attribute value
                    String hrefValue = anchorTag.attr("href");
                    url_watch = "https://www.goldsmiths.co.uk/" + hrefValue; // URL
                }

                // Corrected selector for the image tag
                Elements imageTags = prod.select("img.productListerPrimary");

                String imageUrl = "";
                for (Element imageTag : imageTags) {
                    // Get the src attribute value
                    imageUrl = imageTag.attr("data-src");
                }

                // Check if the name is unique before adding it to the ArrayList
                if (uniqueNames.add(name) && !name.isEmpty()) {
                    price = price.replaceAll("[^0-9.]+", "");
                    if (!Hibernate.searchWatch(name)) {
                        Hibernate.addWatch(brand.text(), name, description.text(), imageUrl); // Add data
                        Hibernate.addComparison(Hibernate.searchWatchID(name), url_watch, Float.parseFloat(price), "Goldsmith"); // Add data
                    } else {
                        Hibernate.addComparison(Hibernate.searchWatchID(name), url_watch, Float.parseFloat(price), "Goldsmith"); // Add data
                    }
                }
            }
        }
    }
}
