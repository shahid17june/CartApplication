package my.cart.model;

/**
 * Created by shahid Akhtar on 10-02-2017.
 */

public class CartModel {
    private String name;
    private int imagepath;

    public CartModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImagepath() {
        return imagepath;
    }

    public void setImagepath(int imagepath) {
        this.imagepath = imagepath;
    }
}
