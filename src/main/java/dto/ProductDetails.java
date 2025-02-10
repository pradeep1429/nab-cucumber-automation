package dto;

public class ProductDetails {

    private String title;
    private String price;
    private String topSeller;

    public ProductDetails(String title, String price, String topSeller) {
        this.title = title;
        this.price = price;
        this.topSeller = topSeller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTopSeller() {
        return topSeller;
    }

    public void setTopSeller(String topSeller) {
        this.topSeller = topSeller;
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
                "title='" + title + '\'' +
                "| price='" + price + '\'' +
                "| topSeller='" + topSeller + '\'' +
                '}';
    }
}
