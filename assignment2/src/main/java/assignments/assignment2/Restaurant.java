package assignments.assignment2;

import java.util.ArrayList;

public class Restaurant {
    private String nama;
    private ArrayList<Menu> menu;

    public Restaurant(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }
}
