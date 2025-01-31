package org.example;

/**
 * Class representing a Watch with attributes such as ID, name, brand, description, and image URL.
 */
public class WatchXML {

    private int id;
    private String name;
    private String brand;
    private String description;
    private String imageUrl;

    /**
     * Empty constructor
     */
    public WatchXML() {
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets brand.
     *
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets image url.
     *
     * @return the image url
     */
    public String getImageUrl() {
        return imageUrl;
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
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets brand.
     *
     * @param brand the brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets image url.
     *
     * @param urlId the url id
     */
    public void setImageUrl(String urlId) {
        this.imageUrl = urlId;
    }

    /** Returns a String description of the class */
    public String toString() {
        String str = "Watch ID: " + id + "; Name: " + name + "; Brand: " +
                brand + "; Description: " + description + "; Image URL: " + imageUrl;
        return str;
    }
}
