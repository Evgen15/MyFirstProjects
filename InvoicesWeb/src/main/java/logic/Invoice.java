package logic;

import java.util.Date;
import java.util.List;

/**
 * Класс-сущность "счет"
 */
public class Invoice {
    private int id;
    private int number;
    private Date date;
    private int sum;
    private Seller seller;
    private List<LineInvoice> listLines;

    public Invoice() {}

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }

    public Seller getSeller() {
        return seller;
    }

    public List<LineInvoice> getListLines() {
        return listLines;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setListLines(List<LineInvoice> listLines) {
        this.listLines = listLines;
    }
}
