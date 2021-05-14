package umn.ac.id.rajoot;

//Every pata type on product
public class ItemModel {
    String position;
    String user;
    String title;
    String price;
    String phone;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    String condition;
    String description;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String path;

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public ItemModel (String position, String user, String title, String price, String phone, String condition, String path, String description) {
        this.position = position;
        this.user = user;
        this.title = title;
        this.price = price;
        this.phone = phone;
        this.condition = condition;
        this.path = path;
        this.description = description;
    }
}
