package logic;

/**
 * Класс-сущность "строка в счете"
 */
public class LineInvoice implements Cloneable {
    private int id;
    private String description;
    private int amount;
    private int price;
    private int sum;

    public LineInvoice() {}

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public int getSum() {
        return sum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
