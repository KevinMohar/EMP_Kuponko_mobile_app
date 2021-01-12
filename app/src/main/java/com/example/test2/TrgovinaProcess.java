package com.example.test2;

import java.util.ArrayList;
import java.util.List;

public class TrgovinaProcess {
    private String[] trgovine;
    private int check;

    public String ime;
    public String naslov;
    public List<Izdelek> izdelki;
    public float znesek;

    public boolean error;

    public TrgovinaProcess() {
        trgovine = new String[]{"Mercator", "SPAR", "INTERSPAR", "HOFER", "Lidl"};
        check = -1;
        ime = "";
        naslov = "";
        izdelki = new ArrayList<>();
        error = false;
    }

    public boolean isSet(StringBuilder shop) {
        String shopName;

        try {
            shopName = shop.substring(0, shop.indexOf("\n"));
        } catch (Exception e) {
            error = true;
            return false;
        }

        for (int i = 0; i < trgovine.length; i++) {
            if (shopName.contains(trgovine[i])) {
                check = i;
                ime = trgovine[i];
                naslov = getNaslov(shop);
                return true;
            }
        }
        return false;
    }

    private String getNaslov(StringBuilder racunString) {
        String tempNaslov = "";
        String[] lines = racunString.toString().split(System.getProperty("line.separator"));

        try {
            switch (check) {
                case 0:
                    tempNaslov = tempNaslov + lines[1].split(", ")[2] + ", " + lines[2];
                    break;
                case 1:
                case 2:
                    tempNaslov = tempNaslov + lines[1].split(", ")[1] + ", " + lines[1].split(", ")[2];
                    break;
                case 3:
                    tempNaslov = tempNaslov + lines[1] + ", " + lines[2];
                    break;
                case 4:
                    tempNaslov = tempNaslov + lines[1];
                    break;
                default:
                    error = true;
            }
        } catch (Exception e) {
            error = true;
        }

        return tempNaslov;
    }

    public void processIzdelki(StringBuilder shop) {
        String[] lines = shop.toString().split(System.getProperty("line.separator"));
        String start = "";

        switch (check) {
            case 0:
                start = "Kol. EM";
                break;
            case 1:
            case 2:
                start = "Znesek";
                break;
            case 3:
            case 4:
                start = "EUR";
                break;
            default:
                error = true;
        }

        boolean checker = false;
        float znk = 0.0f;
        List<Float> tempZneseki = new ArrayList<>();
        List<String> tempImena = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            if (checker) {
                try {
                    if (check == 0)
                        znk = Float.parseFloat(lines[i].replace(",","."));
                    else {
                        znk = Float.parseFloat(lines[i].replace(",",".").split(" ")[0]);
                        if (lines[i].split(" ").length < 2 || lines[i].split(" ")[1].length() > 1)
                            continue;
                    }
                    tempZneseki.add(znk);
                } catch (Exception e) {
                    if (lines[i].length() > 1)
                        tempImena.add(lines[i]);
                }
            }

            if (lines[i].contains(start))
                checker = true;
        }

        for (int x = 0; x < Math.min(tempImena.size(), tempZneseki.size()); x++) {
            znesek += tempZneseki.get(x);
            izdelki.add(new Izdelek(tempImena.get(x), 1, tempZneseki.get(x)));
        }
    }
}
