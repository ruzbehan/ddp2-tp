package assignments.assignment1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

    /* 
    Anda boleh membuat method baru sesuai kebutuhan Anda
    Namun, Anda tidak boleh menghapus ataupun memodifikasi return type method yang sudah ada.
    */

    /*
     * Method  ini untuk menampilkan menu
     */
    public static void showMenu(){
        System.out.println(">>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
        System.out.println();
        System.out.println("Pilih menu:");
        System.out.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
    }

    /*
     * Method ini digunakan untuk membuat ID
     * dari nama restoran, tanggal order, dan nomor telepon
     * 
     * @return String Order ID dengan format sesuai pada dokumen soal
     */
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        // Buat variabel untuk membentuk order id
        String orderId = "";

        // Mengambil nama restoran
        for (int i = 0; i < namaRestoran.length(); i++) {

            // Mengambil empat karakter pertama
            if (orderId.length() == 4) {
                break;
            }

            // Menambahkan karakter saat ini ke orderId
            if (namaRestoran.charAt(i) != ' ') {
                orderId += namaRestoran.charAt(i);
            }
        }

        // Mengubah jadi uppercase
        orderId = orderId.toUpperCase();

        // Masukkan data tanggal pada order id
        for (int i = 0; i < tanggalOrder.length(); i++) {

            // Membuang karakter '/'
            if (tanggalOrder.charAt(i) != '/') {
                orderId += tanggalOrder.charAt(i);;
            }
        }

        // Menyimpan jumlah no telepon
        int jumlah = 0;

        for (int i = 0; i < noTelepon.length(); i++) {

            // Menguubah digit jumlah sesuai encoding dari soal
            jumlah += noTelepon.charAt(i) - 48;

        }

        // Tambahkan kode nomor telepon yang sudah diformat
        orderId += String.format("%02d", jumlah);

        // Tambahkan checksum
        orderId += checksum(orderId);

        return orderId;
    }

    public static String checksum(String orderId) {
        int jumlah1 = 0;
        int jumlah2 = 0;

        for (int i = 0; i < orderId.length(); i++ ) {
            char karakter = orderId.charAt(i);

            // Jika karakter merupakan digit, akan dikurangi '0' agar sesuai kode
            // jika karakter merupakan huruf, akan dikurangi 'A' dan ditambah 10 (mulai kode) agar sesuai kode
            if (karakter <= '9') {
                karakter -= '0';
            } else {
                karakter -= ('A' - 10);
            }

            // Cek
            if (i % 2 != 0) {
                jumlah2 += karakter;
            } else {
                jumlah1 += karakter;
            }
        }

        jumlah1 %= 36;
        jumlah2 %= 36;

        if (jumlah1 < 10) {
            jumlah1 += '0';  // Menambahkan '0' untuk mengubahnya kembali ke bentuk digit dalam kode ascii
        } else {
            jumlah1 += 'A' - 10;  // Menambahkan 'A' + 10 untuk mengubahnya kembali ke bentuk huruf besar dalam kode ascii
        }

        if (jumlah2 < 10) {
            jumlah2 += '0';
        } else {
            jumlah2 += 'A' - 10;
        }

        // Mengembalikan checksum
        return Character.toString(jumlah1) + Character.toString(jumlah2);
    }

    public static boolean dateChecker(String tanggalOrder) {
        // Variabel untuk menentukan kebenaran tanggal
        Date date = null;

        // Mengecek format tanggal
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/YYYY");
            date = simpleDateFormat.parse(tanggalOrder);
        } catch (ParseException e) {}

        // Jika tanggal berhasil di parse maka date tidak akan null
        if (date == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Method ini digunakan untuk membuat bill
     * dari order id dan lokasi
     * 
     * @return String Bill dengan format sesuai di bawah:
     *          Bill:
     *          Order ID: [Order ID]
     *          Tanggal Pemesanan: [Tanggal Pemesanan]
     *          Lokasi Pengiriman: [Kode Lokasi]
     *          Biaya Ongkos Kirim: [Total Ongkos Kirim]
     */
    public static String generateBill(String OrderID, String lokasi){

        // Siapkan string biaya ongkir
        String ongkir;

        // Cek biaya ongkir
        switch (lokasi.toUpperCase()) {
            case "P":
                ongkir = "Rp 10.000";
                break;
            case "U":
                ongkir = "Rp 20.000";
                break;
            case "T":
                ongkir = "Rp 35.000";
                break;
            case "S":
                ongkir = "Rp 40.000";
                break;
            case "B":
                ongkir = "Rp 60.000";
                break;
            default:
                ongkir = "";
                break;
        }

        // Mengambil bagian tanggal
        String date = OrderID.substring(4, 12);

        // Mengubah tanggal
        String tanggal = date.substring(0,2) + "/" + date.substring(2, 4) + "/" + date.substring(4, 8);

        // Mengeluarkan output
        String output = "Bill:\nOrder ID: " + OrderID + "\nTanggal Pemesanan: " + tanggal + "\nLokasi Pengiriman: " + lokasi.toUpperCase() + "\nBiaya Ongkos Kirim: " + ongkir + "\n";

        return output;
    }

    public static void main(String[] args) {

        // Menampilkan menu
        showMenu();

        // Loop utama
        while (true) {
            System.out.println("--------------------------------");
            System.out.println("Pilihan menu: ");

            // Meminta input dari user
            int menu = input.nextInt();

            // Menghapus input sebelumnya
            input.nextLine();

            if (menu == 1) {
                String namaRestoran = "";
                String tanggalOrder = "";
                String noTelepon = "";

                // Menanyakan nama restoran
                while (true) {

                    // Meminta input user
                    System.out.print("Nama Restoran: ");
                    namaRestoran = input.nextLine().toUpperCase();

                    String tmp = "";

                    // Memotong whitespace pada nama restoran
                    for (int i = 0; i < namaRestoran.length(); i++) {
                        if (namaRestoran.charAt(i) != ' ') {
                            tmp += namaRestoran.charAt(i);
                        }
                    }

                    // Nama restoran telah tanpa spasi
                    namaRestoran = tmp;

                    // Cek panjang nama restoran
                    if (namaRestoran.length() < 4) {
                        System.out.println("Nama Restoran tidak valid\n");
                        continue;
                    }

                    // Menghentikan loop jika nama restoran valid
                    break;
                }

                // Menanyakan tanggal pemesanan
                while (true) {

                    // Meminta input user
                    System.out.print("Tanggal pemesanan: ");
                    tanggalOrder = input.nextLine();

                    // Cek tanggal order apakah valid
                    if (!dateChecker(tanggalOrder)) {
                        System.out.println("Tanggal Pemesanan dalam format DD/MM/YYYY!\n");
                        continue;
                    }

                    break;
                }

                // Menanyakan nomor hp
                while (true) {

                    // Meminta input user
                    System.out.print("No. telepon: ");
                    noTelepon = input.nextLine();

                    // Cek apakah nomor hp valid
                    try {
                        long notelp = Long.parseLong(noTelepon);

                        // Cek apakah nomor telepon bilangan bulat positif
                        if (notelp <= 0) {
                            System.out.println("Harap masukan nomor telepon dalam bentuk bilangan bulat positif.\n");
                            continue;
                        }
                        // Cek apakah nomor telepon angka
                    } catch (NumberFormatException e) {
                        System.out.println("Harap masukan nomor telepon dalam bentuk bilangan bulat positif.\n");
                        continue;
                    }

                    break;
                }

                // Memparse informasi dari user untuk dijadikan order ID
                String orderId = generateOrderID(namaRestoran, tanggalOrder, noTelepon);

                // Mencetak order id
                String output = "Order ID " + orderId + " diterima";

                System.out.println(output);

            } else if (menu == 2) {

                String orderId = "";
                String lokasiPengiriman = "";

                // Meminta order id
                while (true) {

                    // Meminta input user
                    System.out.print("Order ID: ");
                    orderId = input.nextLine();

                    String tanggal = orderId.substring(4,6) + "/" + orderId.substring(6, 8) + "/" + orderId.substring(8, 12);
                    String orderIdNoChecksum = orderId.substring(0, orderId.length() - 2);
                    String orderIdChecksum = orderId.substring(orderId.length() - 2);
                    String realChecksum = checksum(orderIdNoChecksum);

                    // Melakukan pengecekan order id, panjang, checksum, dan tanggal
                    if (orderId.length() < 16) {
                        System.out.println("Order ID minimal 16 karakter\n");
                        continue;
                    } else if (!orderIdChecksum.equals(realChecksum) || !dateChecker(tanggal)) {
                        System.out.println("Silahkan masukkan Order ID yang valid!\n");
                        continue;
                    }

                    break;
                }

                while (true) {
                    // Meminta lokasi pengiriman
                    System.out.print("Lokasi Pengiriman: ");
                    lokasiPengiriman = input.nextLine();

                    lokasiPengiriman = lokasiPengiriman.toUpperCase();

                    // Apabila lokasi pengiriman tidak terdaftar program akan mengulang pertanyaan
                    if (!(lokasiPengiriman.equals("P") || lokasiPengiriman.equals("U") || lokasiPengiriman.equals("T") || lokasiPengiriman.equals("S") || lokasiPengiriman.equals("B"))) {
                        System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!\n");
                        continue;
                    }

                    break;
                }

                System.out.println(generateBill(orderId, lokasiPengiriman));

                // Jika user memilih 3 program akan berhenti
            } else {
                break;
            }

            System.out.println();

            System.out.println("Pilih menu:");
            System.out.println("1. Generate Order ID");
            System.out.println("2. Generate Bill");
            System.out.println("3. Keluar");
        }
    }

    
}
