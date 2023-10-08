interface DiscountStrategy {
    double applyDiscount(double originalPrice);
}

class TenPercentDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice * 0.9;
    }
}

class TwentyPercentDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice * 0.8;
    }
}

interface Product {
    double getPrice();
    String getDescription();
}

class BasicProduct implements Product {
    private String name;
    private double price;

    public BasicProduct(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return name;
    }
}

interface ProductDecorator extends Product {
}

class GiftWrappedDecorator implements ProductDecorator {
    private final Product product;

    public GiftWrappedDecorator(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return product.getPrice() + 5.0;
    }

    public String getDescription() {
        return product.getDescription() + " (Gift Wrapped)";
    }
}

class DiscountDecorator implements ProductDecorator {
    private final Product product;
    private double discount;

    public DiscountDecorator(Product product, double discount) {
        this.product = product;
        this.discount = discount;
    }

    public double getPrice() {
        return product.getPrice() * (1 - discount);
    }

    public String getDescription() {
        return product.getDescription() + " (Discount: " + (discount * 100) + "%)";
    }
}

public class ProductStoreExample {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        Product product1 = new BasicProduct("Product 1", 100.0);
        Product product2 = new BasicProduct("Product 2", 50.0);
        cart.addProduct(product1);
        cart.addProduct(product2);

        DiscountStrategy discountStrategy = new TenPercentDiscount();
        cart.setDiscountStrategy(discountStrategy);

        product1 = new GiftWrappedDecorator(product1);
        product2 = new DiscountDecorator(product2, 0.2);

        double totalPrice = cart.calculateTotal();
        System.out.println("Total amount after applying the discount: $" + totalPrice);
    }
}

class ShoppingCart {
    private DiscountStrategy discountStrategy;
    private List<Product> products = new ArrayList<>();

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public double calculateTotal() {
        double totalPrice = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        if (discountStrategy != null) {
            totalPrice = discountStrategy.applyDiscount(totalPrice);
        }

        return totalPrice;
    }
}
