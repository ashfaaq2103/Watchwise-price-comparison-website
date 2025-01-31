package org.example;

/**
 * This class represents a ComparisonXML entity for database operations.
 */
public class ComparisonXML {
    private int id;
    private int foreign_id;
    private String url;
    private float price;
    private String website;

    /**
     * Empty constructor
     */
    public ComparisonXML() {
    }

    /**
     * Gets id.
     *
     * @return the id
     */
// Getters and setters
    public int getId() {
        return id;
    }

    /**
     * Gets foreign id.
     *
     * @return the foreign id
     */
    public int getForeign_id() {
        return foreign_id;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * Gets website.
     *
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets foreign id.
     *
     * @param foreign_id the foreign id
     */
    public void setForeign_id(int foreign_id) {
        this.foreign_id = foreign_id;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Sets website.
     *
     * @param website the website
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Returns a String description of the class
     */
    public String toString() {
        return "Comparison ID: " + id + "; Watch ID : " + foreign_id + "; Url : " +
                url + "; Price : " + price + "; Website : " + website;
    }
}
