package preview.valteck.bortexapp.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by SterlingRyan on 8/16/2017.
 */

public class CartItem implements Serializable {

    private String itemId;
    private String name;
    private String imageURL;
    private String colour;
    private String size;
    private String description;
    private String price;
    private String itemType;

    /**
     * Empty constructor for Firebase
     */
    public CartItem() {
    }

    public CartItem(String itemId, String name, String imageURL, String colour, String size, String description, String price, String itemType) {
        this.itemId = itemId;
        this.name = name;
        this.imageURL = imageURL;
        this.colour = colour;
        this.size = size;
        this.description = description;
        this.price = price;
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
