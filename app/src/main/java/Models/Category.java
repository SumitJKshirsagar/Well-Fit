package Models;

public class Category {
    private String name;
    private String id;
    private String imageUrl;

    public Category(String name, String imageUrl, String id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getId ( ) {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
