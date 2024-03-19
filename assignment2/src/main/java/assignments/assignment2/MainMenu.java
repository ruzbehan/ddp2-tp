package assignments.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import assignments.assignment1.*;

public class MainMenu {
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList = new ArrayList<>();
    private static ArrayList<User> userList = new ArrayList<>();
    private static ArrayList<Order> orderList = new ArrayList<>();
    private static User userLoggedIn = null;

    public static void main(String[] args) {
        initUser();
        boolean programRunning = true;
        while(programRunning){
            printHeader();
            startMenu();
            int command = input.nextInt();
            input.nextLine();

            if(command == 1){
                System.out.println("\nSilakan Login:");
                System.out.print("Nama: ");
                String nama = input.nextLine();
                System.out.print("Nomor Telepon: ");
                String noTelp = input.nextLine();

                // Mengambil objek user sesuai dengan data yang diberikan
                userLoggedIn = getUser(nama, noTelp);

                // Cek apakah user ada, jika tidak ada maka program akan menanyakan lagi
                if (userLoggedIn == null) {
                    continue;
                }

                boolean isLoggedIn = true;
                System.out.printf("Selamat Datang %s!\n", userLoggedIn.getNama());

                if(Objects.equals(userLoggedIn.getRole(), "Customer")){
                    while (isLoggedIn){
                        menuCustomer();
                        int commandCust = input.nextInt();
                        input.nextLine();

                        switch(commandCust){
                            case 1 -> handleBuatPesanan();
                            case 2 -> handleCetakBill();
                            case 3 -> handleLihatMenu();
                            case 4 -> handleUpdateStatusPesanan();
                            case 5 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }else{
                    while (isLoggedIn){
                        menuAdmin();
                        int commandAdmin = input.nextInt();
                        input.nextLine();

                        switch(commandAdmin){
                            case 1 -> handleTambahRestoran();
                            case 2 -> handleHapusRestoran();
                            case 3 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }
            }else if(command == 2){
                programRunning = false;
            }else{
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
    }

    public static User getUser(String nama, String nomorTelepon){

        // Melakukan looping pada userList untuk mengecek user satu per satu
        for (int i = 0; i < userList.size(); i++) {

            // Mengambil user yang dicek saat ini
            User currentUser = userList.get(i);

            // Cek apakah nama uesr dan nomor telepon user sesuai dengan yang diberikan
            if (currentUser.getNama().equals(nama) && currentUser.getNomorTelepon().equals(nomorTelepon)) {

                // Mengembalikan user saat ini jika data yang diberikan sesuai
                return currentUser;
            }
        }

        // Jika looping telah selesai dilakukan dan user tidak ditemukan, fungsi akan mereturn null
        return null;
    }

    public static void handleBuatPesanan(){
        System.out.println("--------------Buat Pesanan--------------");
        String namaRestoran = "";

        // Membuat objek resto saat ini yang ingin dihapus nanti
        Restaurant currentResto = null;

        String tanggalPemesanan = "";

        int jumlahOrder = 0;

        // Menginisiasi array menuStrings untuk menyimpan nama makanan (menu) dalam bentuk string
        String[] menuStrings = null;
        ArrayList<Menu> menuList = new ArrayList<>();
        boolean isMenuValid = false;


        while (!isMenuValid) {

            System.out.print("Nama Restoran: ");
            namaRestoran = input.nextLine();

            // Cek apakah nama restoran terdaftar pada sistem
            for (Restaurant restaurant: restoList) {

                // Jika ada nama restoran yang cocok, akan diolah
                if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                    currentResto = restaurant;
                    System.out.println("Halo");
                    break;
                }

            }

            // Jika currentResto masih null berarti restoran tidak ditemukan, maka program akan
            // berhenti dan mengembalikan pesan
            if (currentResto == null) {
                System.out.println("Restoran tidak terdaftar pada sistem.");
                continue;
            }

            System.out.print("Tanggal Pemesanan (DD/MM/YYYY) : ");
            tanggalPemesanan = input.nextLine();

            // Melakukan validasi tanggal menggunakan fungsi pada class OrderGenerator TP1
            if (!OrderGenerator.validateDate(tanggalPemesanan)) {
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY) !");
                continue;
            }

            System.out.print("Jumlah Pesanan: ");

            // VALIDASI JUMLAH PESANAN

            // Mengecek input jumlah pesanan apakah valid
            try {
                jumlahOrder = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                System.out.println("Jumlah pesanan tidak valid!");
                continue;
            }

            isMenuValid = true;

            // Meminta input makanan
            menuStrings = new String[jumlahOrder];
            for (int i = 0; i < jumlahOrder; i++) {

                // Mengisi makanan dari input
                menuStrings[i] = input.nextLine();
            }

            // Melakukan looping untuk memparsing menu dalam bentuk string ke objek menu
            for (String stringMenu: menuStrings) {

                Menu currentMenu = null;

                // Mengecek nama menu pada restoran apakah valid
                for (Menu menu: currentResto.getMenu()) {
                    System.out.println(currentResto.getMenu().size());

                    System.out.println(menu.getNamaMakanan());
                    if (menu.getNamaMakanan().equalsIgnoreCase(stringMenu.strip())) {
                        currentMenu = menu;
                    }
                }

                if (currentMenu == null) {
                    System.out.println("Mohon memesan menu yang tersedia di Restoran!");
                    isMenuValid = false;
                    break;
                }

                menuList.add(currentMenu);
            }
        }

        // Buat order ID dari pesanan
        String orderId = OrderGenerator.generateOrderID(namaRestoran, tanggalPemesanan, userLoggedIn.getNomorTelepon());

        // Mengambil biaya ongkir
        int biayaOngkir;

        // Mengecek biaya ongkir
        switch (userLoggedIn.getLokasi()) {
            case "P":
                biayaOngkir = 10000;
                break;
            case "U":
                biayaOngkir = 20000;
                break;
            case "T":
                biayaOngkir = 35000;
                break;
            case "S":
                biayaOngkir = 40000;
                break;
            case "B":
                biayaOngkir = 60000;
                break;
            default:
                biayaOngkir = 0;
                break;
        }

        // Buat objek order
        Order newOrder = new Order(orderId, tanggalPemesanan, biayaOngkir, currentResto, menuList);

        // Tambahkan order baru ke orderList
        orderList.add(newOrder);

        System.out.printf("Pesanan dengan ID %s diterima!\n", orderId);
    }

    public static void handleCetakBill(){
        System.out.println("--------------Cetak Bill--------------");
        String orderId;
        Order currentOrder = null;

        while (true) {
            System.out.print("Masukkan Order ID: ");
            orderId = input.nextLine();

            // Cari order Id dari tiap order pada orderList
            for (Order order: orderList) {
                if (order.getOrderId().equals(orderId)) {
                    currentOrder = order;
                }
            }

            // Jika order Id tidak ditemukan, maka program akan mengirimkan pesan error
            if (currentOrder == null) {
                System.out.println("Order ID tidak dapat ditemukan.");
                continue;
            }

            break;
        }

        // Cetak bill
        String isFinished = "";

        if (currentOrder.isOrderFinished()) {
            isFinished = "Finished";
        } else {
            isFinished = "Not Finished";
        }

        String lokasiPengiriman = "";

        switch (currentOrder.getBiayaOngkosKirim()) {
            case 10000:
                lokasiPengiriman = "P";
                break;
            case 20000:
                lokasiPengiriman = "U";
                break;
            case 35000:
                lokasiPengiriman = "T";
                break;
            case 40000:
                lokasiPengiriman = "S";
                break;
            case 60000:
                lokasiPengiriman = "B";
                break;
            default:
                lokasiPengiriman = "";
                break;
        }

        // Siapkan variabel harga total untuk dicetak nanti
        double hargaTotal = 0;

        // Tambahkan harga ongkir
        hargaTotal += currentOrder.getBiayaOngkosKirim();

        // Siapkan variabel pesanan untuk mendaftarkan nama-nama pesanan yang akan dipesan
        String pesanan = "";

        // Melakukan looping untuk mendaftarkan nama tiap makanan sekaligus menambahkan
        // harga tiap makanan ke harga total
        for (Menu menu: currentOrder.getItems()) {
            pesanan += "\n- " + menu.getNamaMakanan() + " " + menu.getHarga();
            hargaTotal += menu.getHarga();
        }

        // Cetak bill sesuai data dari order
        String output = "Bill:\nOrder ID: " + orderId
                + "Tanggal Pemesanan: " + currentOrder.getTanggalPemesanan() + "\n"
                + "Restaurant: " + currentOrder.getRestaurant().getNama() + "\n"
                + "Lokasi Pengiriman: " + lokasiPengiriman + "\n"
                + "Status Pengiriman: " + isFinished + "\n"
                + "Pesanan: " + pesanan + "\n"
                + "Biaya Ongkos Kirim: Rp " + currentOrder.getBiayaOngkosKirim() + "\n"
                + "Total Biaya: Rp " + hargaTotal;

        System.out.println(output);
    }

    public static void handleLihatMenu(){
        System.out.println("--------------Lihat Menu--------------");
        String namaRestoran = "";

        // Membuat objek resto saat ini yang ingin dihapus nanti
        Restaurant currentResto = null;

        while (true) {
            System.out.print("Nama Restoran: ");
            namaRestoran = input.nextLine();

            // Cek apakah nama restoran terdaftar pada sistem
            for (Restaurant restaurant: restoList) {

                // Jika ada nama restoran yang cocok, akan diolah
                if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                    currentResto = restaurant;
                    break;
                }
            }

            // Jika currentResto masih null berarti restoran tidak ditemukan, maka program akan
            // berhenti dan mengembalikan pesan
            if (currentResto == null) {
                System.out.println("Restoran tidak terdaftar pada sistem.");
                continue;
            }

            break;
        }

        String output = "Menu:";

        // Lakukan looping untuk menambahkan menu yang tersedia pada output
        for (int i = 0; i < currentResto.getMenu().size(); i++) {
            Menu currentMenu = currentResto.getMenu().get(i);

            // Tambahkan nama makanan dan harga pada menu
            output += "\n" + i + ". " + currentMenu.getNamaMakanan() + " " + currentMenu.getHarga();
        }

        System.out.println(output);
    }

    public static void handleUpdateStatusPesanan(){
        System.out.println("--------------Update Status Pesanan--------------");
        String orderId;
        Order currentOrder = null;

        while (true) {
            System.out.print("Masukkan Order ID: ");
            orderId = input.nextLine();

            // Cari order Id dari tiap order pada orderList
            for (Order order: orderList) {
                if (order.getOrderId().equals(orderId)) {
                    currentOrder = order;
                }
            }

            // Jika order Id tidak ditemukan, maka program akan mengirimkan pesan error
            if (currentOrder == null) {
                System.out.println("Order ID tidak dapat ditemukan.");
                continue;
            }

            break;
        }

        // Meminta input status
        System.out.print("Status: ");
        String status = input.nextLine();

        // Jika status pesanan masih belum selesai, maka status pesanan tidak berhasil diubah
        if (currentOrder.isOrderFinished() && status.equalsIgnoreCase("Selesai")) {
            System.out.printf("Status pesanan dengan ID %s tidak berhasil diupdate!\n", orderId);
            return;
        } else if (!currentOrder.isOrderFinished() && status.equalsIgnoreCase("Belum Selesai")) {
            System.out.printf("Status pesanan dengan ID %s tidak berhasil diupdate!\n", orderId);
            return;
        }

        // Ubah status pesanan
        if (status.equalsIgnoreCase("Selesai")) {
            currentOrder.setOrderFinished(true);
        } else {
            currentOrder.setOrderFinished(false);
        }

        System.out.printf("Status pesanan dengan ID %s berhasil diupdate!\n", orderId);
    }

    public static void handleTambahRestoran(){
        System.out.println("--------------Tambah Restoran--------------");
        String namaRestoran = "";
        boolean isNamaRestoranValid = false;

        // Setelah dilakukan validasi, inisiasi objek Restaurant
        Restaurant newRestaurant = null;

        int jumlahMakanan;

        // MEMINTA INPUT MENU

        // Menginisiasi array menuStrings untuk menyimpan nama makanan dan harga dalam bentuk string
        String[] menuStrings;

        // Membuat array menu untuk nanti dibuat dan disimpan pada resto
        ArrayList<Menu> menuList = new ArrayList<>();

        boolean isMenuValid = false;

        while (!isNamaRestoranValid || !isMenuValid) {
            isNamaRestoranValid = true;
            System.out.print("Nama: ");
            namaRestoran = input.nextLine();

            // VALIDASI NAMA RESTORAN

            // Mengecek jumlah karakter nama restoran

            // Menyiapkan nama restoran tanpa whitespace
            String tmp = "";

            // Memfilter nama restoran tanpa whitespace (spasi)
            for (int i = 0; i < namaRestoran.length(); i++) {

                // Menambahkan karakter nama restoran tanpa spasi
                if (namaRestoran.charAt(i) != ' ') {
                    tmp += namaRestoran.charAt(i);
                }
            }

            // Cek apakah nama restoran memiliki karakter lebih dari empat
            if (tmp.length() < 4) {
                System.out.println("Nama Restoran tidak valid!\n");
                isNamaRestoranValid = false;
                continue;
            }

            // Mengecek apakah nama restoran sudah ada pada restoList
            for (Restaurant restaurant: restoList) {

                // Jika terdapat nama yang sama,maka program akan mengeluarkan pesan error
                // dan akan menghentikan fungsi dengan return
                if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                    System.out.printf("Restoran dengan nama %s sudah pernah terdaftar. Mohon masukkan nama yang berbeda!\n\n", namaRestoran);
                    isNamaRestoranValid = false;
                    continue;
                }
            }
            if (!isNamaRestoranValid) { continue;}
            newRestaurant = new Restaurant(namaRestoran);

            System.out.print("Jumlah Makanan: ");

            // VALIDASI JUMLAH MAKANAN

            // Mengecek input jumlah makanan apakah valid
            try {
                jumlahMakanan = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                System.out.println("Jumlah makanan tidak valid!\n");
                continue;
            }

            menuStrings = new String[jumlahMakanan];

            isMenuValid = true;

            // Meminta input makanan
            for (int i = 0; i < jumlahMakanan; i++) {

                // Mengisi makanan dari input
                menuStrings[i] = input.nextLine();
            }

            // Melakukan looping untuk memparsing menu dalam bentuk string ke objek menu
            // sekaligus melakukan validasi string tsb
            for (String stringMenu: menuStrings) {

                // Ambil data dari string menu
                String[] splittedString = stringMenu.split(" ");
                String namaMakanan = String.join(" ", Arrays.copyOfRange(splittedString, 0, splittedString.length - 1));
                int hargaMakanan;

                // VALIDASI HARGA MENU
                try {
                    hargaMakanan = Integer.parseInt(splittedString[splittedString.length - 1]);
                } catch (Exception e) {
                    System.out.println("Harga menu harus bilangan bulat!");
                    isMenuValid = false;
                    break;
                }

                // Jika valid maka akan membuat objek menu dan menambahkannya ke menuList
                Menu newMenu = new Menu(namaMakanan, hargaMakanan);

                // Masukkan menu baru sesuai dengan urutan harga dan nama
                boolean isAdded = false;

                for (int i = 0; i < menuList.size(); i++) {
                    if (isAdded) { break; }

                    // Bandingkan harga
                    if (menuList.get(i).getHarga() > newMenu.getHarga()) {
                        menuList.add(i, newMenu);
                    }

                    // Bandingkan nama
                    int stringSizeMin = 0;

                    if (newMenu.getNamaMakanan().length() < menuList.get(i).getNamaMakanan().length()) {
                        stringSizeMin = newMenu.getNamaMakanan().length();
                    } else {
                        stringSizeMin = menuList.get(i).getNamaMakanan().length();
                    }

                    for (int j = 0; j < stringSizeMin; j++) {
                        if (newMenu.getNamaMakanan().charAt(j) < menuList.get(i).getNamaMakanan().charAt(j)) {
                            menuList.add(i, newMenu);
                            isAdded = true;
                            break;
                        }
                    }
                }

                // Jika menu list masih kosong, akan menambahkan data pertama
                if (menuList.size() == 0) {
                    menuList.add(newMenu);
                }
            }
        }

        // Menambahkan menuList ke restoran
        newRestaurant.setMenu(menuList);

        // Menambahkan restoran baru ke restoList
        restoList.add(newRestaurant);

        System.out.printf("Restoran %s berhasil terdaftar.\n", namaRestoran);
    }

    public static void handleHapusRestoran(){
        System.out.println("--------------Hapus Restoran--------------");
        String namaRestoran = "";

        // Membuat objek resto saat ini yang ingin dihapus nanti
        Restaurant currentResto = null;

        while (true) {
            System.out.print("Nama: ");
            namaRestoran = input.nextLine();

            // Cek apakah nama restoran terdaftar pada sistem
            for (Restaurant restaurant: restoList) {

                // Jika ada nama restoran yang cocok, akan diolah
                if (restaurant.getNama().equalsIgnoreCase(namaRestoran)) {
                    currentResto = restaurant;
                    break;
                }
            }

            // Jika currentResto masih null berarti restoran tidak ditemukan, maka program akan
            // berhenti dan mengembalikan pesan
            if (currentResto == null) {
                System.out.println("Restoran tidak terdaftar pada sistem.");
                continue;
            }

            break;
        }

        // Jika ditemukan program akan menghapus restoran pada restoList
        restoList.remove(currentResto);

        System.out.println("Restoran berhasil dihapus.");
    }

    public static void initUser(){
        userList = new ArrayList<User>();
        userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
        userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
        userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
        userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
        userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));

        userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
        userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }

    public static void printHeader(){
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu(){
        System.out.println("Selamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuAdmin(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuCustomer(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }
}
