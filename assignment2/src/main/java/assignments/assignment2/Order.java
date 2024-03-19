package assignments.assignment2;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private String tanggalPemesanan;
    private int biayaOngkosKirim;
    private Restaurant restaurant;
    private ArrayList<Menu> items;
    private boolean orderFinished;

    public Order(String orderId, String tanggalPemesanan, int biayaOngkosKirim, Restaurant restaurant, ArrayList<Menu> items) {
        this.orderId = orderId;
        this.tanggalPemesanan = tanggalPemesanan;
        this.biayaOngkosKirim = biayaOngkosKirim;
        this.restaurant = restaurant;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTanggalPemesanan() {
        return tanggalPemesanan;
    }

    public int getBiayaOngkosKirim() {
        return biayaOngkosKirim;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public ArrayList<Menu> getItems() {
        return items;
    }

    public boolean isOrderFinished() {
        return orderFinished;
    }

    public void setOrderFinished(boolean orderFinished) {
        this.orderFinished = orderFinished;
    }
}
