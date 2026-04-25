Apartmanski sustav — Desktop aplikacija (Java Swing)  
Aplikacija za upravljanje apartmanima, gostima, rezervacijama i računima.
Izrađena u Java Swingu, uz korištenje MySQL baze i dodatnih biblioteka (SwingX, Apache POI, JCalendar).

FUNKCIONALNOSTI

Upravljanje apartmanima (pregled, dodavanje, uređivanje, brisanje, slike)
Upravljanje gostima
Upravljanje rezervacijama
Generiranje računa
Generiranje Excel izvještaja (gosti, apartmani, rezervacije, računi)
Pretraga i filtriranje tablica
Validacija unosa
Automatsko otvaranje generiranih izvještaja

STRUKTURA PROJEKTA

zavrsniRad/
│── src/                     # Izvorni kod
│── lib/                     # Svi potrebni JAR-ovi (obavezno)
│── img/                     # Ikone
│── slike_apartmana/         # Slike apartmana
│── dokumentacija/           # Opis sustava i modeliranje podataka i procesa
│── screenshots/             
│── app.jar/
│── .classpath
│── .project
│── .gitignore
│── README.md                # Ovaj file

BAZA PODATAKA
Aplikacija koristi MySQL.

VAŽNO
Datoteka Database.java nije uključena u repozitorij jer sadrži privatne podatke (lozinke).
Korisnik mora sam kreirati datoteku:
src/zavrsniRad/Database.java

PRIMJER SADRŽAJA
java
public class Database {
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/ime_baze",
            "korisnik",
            "lozinka"
        );
    }
}

POKRETANJE APLIKACIJE
1.Klonirati repozitorij
2.Importati projekt u Eclipse (Existing Java Project)
3.Provjeriti da su svi JAR-ovi dodani u Build Path
4.Kreirati Database.java
5.Pokrenuti Main.java (ili klasu koja otvara login/glavni izbornik)

SLIKE KORIŠTENJA APLIKACIJE
Slike aplikacije nalaze se u folderu `screenshots/`.

NAPOMENA
Ovaj projekt je izrađen kao završni rad i služi kao demonstracija rada s:
Swing GUI-em
SQL bazama
Excel generiranjem
Validacijama
OOP principima
