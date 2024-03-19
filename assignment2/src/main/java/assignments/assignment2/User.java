package assignments.assignment2;

import java.util.ArrayList;

public class User {
    private String nama;
    private String nomorTelepon;
    private String email;
    private String lokasi;
    private String role;
    private ArrayList<Order> orderHistory;

    public User(String nama, String nomorTelepon, String email, String lokasi, String role) {
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getRole() {
        return role;
    }

}
