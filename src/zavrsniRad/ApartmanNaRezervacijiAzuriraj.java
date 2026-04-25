package zavrsniRad;

import java.time.LocalDate;

public class ApartmanNaRezervacijiAzuriraj {

    private int idApartmana;
    private String opisApartmana;
    private String lokacija;
    private int kapacitet;
    private double cijenaApartmana;
    private LocalDate dolazak;
    private LocalDate odlazak;

    public ApartmanNaRezervacijiAzuriraj(int id, String opis, String lokacija, int kapacitet, double cijena, LocalDate dolazak, LocalDate odlazak) {
        this.idApartmana = id;
        this.opisApartmana = opis;
        this.lokacija = lokacija;
        this.kapacitet = kapacitet;
        this.cijenaApartmana = cijena;
        this.dolazak = dolazak;
        this.odlazak = odlazak;
    }

    public int getIdApartmana() {
        return idApartmana;
    }

    public String getOpis() {
        return opisApartmana;
    }

    public String getLokacija() {
        return lokacija;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public double getCijenaNocenja() {
        return cijenaApartmana;
    }

    public LocalDate getDolazak() {
        return dolazak;
    }

    public LocalDate getOdlazak() {
        return odlazak;
    }
}