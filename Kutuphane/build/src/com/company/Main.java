package com.company;

import java.sql.*;
import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Scanner al = new Scanner(System.in);

        //Gecici Degiskenler
        Uye uye = null;
        Kitap kitap = null;
        AlinanKitaplar ak = null;
        String KullaniciAdi;
        String Adi;
        String Soyadi;
        String _sifre;
        int uyeno;
        String email;
        String cevap = null;

        int secim;
        int devamMi=1;

        Random rand = new Random();

        UyeRepositoryPostgreSQL uyerepo = new UyeRepositoryPostgreSQL();
        KitaplarRepositoryPostgreSQL kitaprepo = new KitaplarRepositoryPostgreSQL();
        AKRepositoryPostgreSQL akrepo = new AKRepositoryPostgreSQL();

        System.out.println("Kütüphane Otomasyonuna Hoşgeldiniz");
        System.out.println("1- Giriş Yap \n2- Üyelik Aç");
        secim = al.nextInt();
        if (secim == 1) {
            System.out.println("Giriş Yapmak İçin Üye Numaranızı ve Şifrenizi Giriniz.");
            System.out.print("Üye Numaranız:");
            uyeno = al.nextInt();
            al.nextLine();
            System.out.print("Şifreniz:");
            String sifre = al.next();

            uye = uyerepo.ara(uyeno);

            if (Integer.parseInt(uye.getSifre()) == Integer.parseInt(sifre)) {
                System.out.println("Hoşgeldiniz " + uye.getAdi() + " " + uye.getSoyadi());
            } else {
                System.out.println("Böyle Bir üye yok!");
            }
        }
        else if (secim == 2) {
            System.out.print("Adınızı Giriniz:");
            Adi = al.next();
            System.out.print("\nSoyadinizi Giriniz:");
            Soyadi = al.next();
            System.out.print("\nSadece Sayilardan olusan sifre giriniz:");
            _sifre = al.next();
            System.out.print("\nKullanici Adinizi giriniz:");
            KullaniciAdi = al.next();
            System.out.print("\nEmail giriniz:");
            email = al.next();
            System.out.print("\nUyeNo Giriniz:");
            uyeno = al.nextInt();
            uye = new Uye(Adi, Soyadi, KullaniciAdi, _sifre, email, uyeno);

            uyerepo.kaydet(uye);
        }
        while(devamMi == 1){
            System.out.println("1- Kitapları Listele");
            System.out.println("2- Kitap bağışla");
            System.out.println("3- Kitap Ödünç Al");
            System.out.println("4- Yazarları Listele");
            System.out.println("5- Üyelik Bilgilerimi Güncelle");
            System.out.println("6- Çıkış Yap");

            secim = al.nextInt();
            if (secim == 1) {
                kitaprepo.tumUrunler();

            }
            else if (secim == 2) {
                System.out.print("Kitap no giriniz:");
                int kitapno = al.nextInt();
                System.out.print("\nISBN Giriniz:");
                int isbn = al.nextInt();
                System.out.print("\nKitap Adi Giriniz:");
                String adi = al.next();
                System.out.print("\nYayin Evi Giriniz:");
                int YarenEli = al.nextInt();
                System.out.print("\nKategori No giriniz:");
                int kategorino = al.nextInt();
                System.out.print("\nSayfa Sayisi Giriniz:");
                int sayfasayisi = al.nextInt();
                System.out.print("\nDil No giriniz");
                int dilNo = al.nextInt();

                kitap = new Kitap(kitapno, isbn, adi, YarenEli, kategorino, sayfasayisi, dilNo);
                kitaprepo.kaydet(kitap);
                System.out.println("Kütüphanemiz bağınız için size teşekkür ediyor!");
            }
            else if (secim == 3) {
                System.out.println("Silmek istediğininz Kitabın numarasını giriniz");
                secim = al.nextInt();

                ak = new AlinanKitaplar(rand.nextInt(1000), secim, uye.getno());
                akrepo.kaydet(ak);


                kitaprepo.sil(secim);

            }
            else if (secim == 4) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Kutuphane",
                            "postgres", "Galatasaray123!");
                    if (conn != null)
                        System.out.println("Veritabanına bağlandı!");
                    else
                        System.out.println("Bağlantı girişimi başarısız!");


                    String sql = "SELECT \"YazarAdi\", \"Email\"  FROM \"Yazarlar\"";


                    // Sorgu çalıştırma //
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);

                    // Bağlantı sonlandırma //
                    conn.close();

                    while (true) {
                        while (rs.next()) {
                            // Kayda ait alan değerlerini değişkene ata //
                            String yazarAdi = rs.getString("YazarAdi");
                            String Email = rs.getString("Email");

                            // Ekrana yazdır //
                            System.out.print("Yazar Adi: " + yazarAdi);
                            System.out.println(", Email: " + Email);
                        }
                        rs.close();
                        stmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(secim == 5){
                System.out.print("Adınızı Giriniz:");
                Adi = al.next();
                System.out.print("\nSoyadinizi Giriniz:");
                Soyadi = al.next();
                System.out.print("\nSadece Sayilardan olusan sifre giriniz:");
                _sifre = al.next();
                System.out.print("\nKullanici Adinizi giriniz:");
                KullaniciAdi = al.next();
                System.out.print("\nEmail giriniz:");
                email = al.next();
                uye = new Uye(Adi, Soyadi, KullaniciAdi, _sifre, email,uye.getno());
                uyerepo.degistir(uye);
                System.out.println("Bilgileriniz başarı ile değiştirilmiştir!");
            }
            else if(secim == 6){
                devamMi = 0;
            }
            else{
                System.out.print("Hatalı tuşladınız. Tekrar Deneyiniz");
            }
        }
    }
}

