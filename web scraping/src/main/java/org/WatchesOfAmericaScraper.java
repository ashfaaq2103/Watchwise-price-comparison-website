package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Scraper class for extracting watch information from the Watches of America website.
 */
public class WatchesOfAmericaScraper extends Thread {

    /**
     * Instantiates a new Watches of america scraper.
     */
    WatchesOfAmericaScraper() {
        try {
            scrapeWatchersofamerica();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void scrapeWatchersofamerica() throws Exception {
        // Number of pages from the website that will be scraped
        int pages = 14;
        String url = "";
        String url_watch = "";
        String newPrice = "";
        String name = "";
        Set<String> uniqueNames = new HashSet<>();

        for (int x = 1; x < (pages + 1); x++) {
            url = "https://watchesofamerica.com/collections/mens-watches?page=" + Integer.toString(x);

            // Download HTML document from website
            Document doc = Jsoup.connect(url).get();
            // Get all of the products on the page
            Elements prods = doc.select(".ProductItem");

            // Work through the products
            for (Element prod : prods) {
                Elements brand = prod.select(".ProductItem__Vendor");
                // Get the product description
                Elements description = prod.select(".ProductItem__Title");
                String[] words = description.text().split("\\s+");
                name = String.join(" ", Arrays.copyOfRange(words, 0, 2));

                Elements price = prod.select(".Price--highlight");

                Elements anchorTags = prod.select("a");
                for (Element anchorTag : anchorTags) {
                    // Get the href attribute value
                    String hrefValue = anchorTag.attr("href");
                    url_watch = "https://watchesofamerica.com/" + hrefValue;
                }

                // Corrected selector for the image tag
                Elements imageTags = prod.select("img.ProductItem__Image--alternate");

                String imageUrl = "";
                for (Element imageTag : imageTags) {
                    // Get the data-src attribute value
                    imageUrl = "https:" + imageTag.attr("src");
                }

                // Check if the name is unique before adding it to the ArrayList
                if (uniqueNames.add(name) && !name.isEmpty()) {
                    newPrice = price.text();
                    newPrice = newPrice.replaceAll("[^0-9.]+", "");
                    if (!Hibernate.searchWatch(name)) {
                        Hibernate.addWatch(brand.text(), name, description.text(), imageUrl);// Add data
                        Hibernate.addComparison(Hibernate.searchWatchID(name), url_watch, Float.parseFloat(newPrice), "WatchesOfAmerica");// Add data
                    } else {
                        Hibernate.addComparison(Hibernate.searchWatchID(name), url_watch, Float.parseFloat(newPrice), "WatchesOfAmerica");// Add data
                    }
                }
            }
        }
    }
}
