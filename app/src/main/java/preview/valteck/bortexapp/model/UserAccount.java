package preview.valteck.bortexapp.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by SterlingRyan on 7/27/2017.
 */

public class UserAccount implements Serializable {

    private String userId;
    private String email;
    private HashMap<String, String> itemFavourites;

    /**
     * Empty constructor for Firebase
     */
    public UserAccount() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, String> getItemFavourites() {
        return itemFavourites;
    }

    public void setItemFavourites(HashMap<String, String> itemFavourites) {
        this.itemFavourites = itemFavourites;
    }
}
