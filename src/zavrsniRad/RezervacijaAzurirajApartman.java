package zavrsniRad;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;


public class RezervacijaAzurirajApartman {

    private JFrame frame;
    private JXDatePicker datumDolaskaPicker, datumOdlaskaPicker;
    private JXTable table;
    private DefaultTableModel model;
    private GetterSetter korisnik;
    private RezervacijaAzuriraj roditeljAzuriraj;
    private int brojGostiju;
    private int idRezervacije;
    private LocalDate stariDolazak;
    private LocalDate stariOdlazak;
    private LocalDate zadnjiPretrazeniDolazak = null;
    private LocalDate zadnjiPretrazeniOdlazak = null;
    
    public RezervacijaAzurirajApartman(GetterSetter korisnik, RezervacijaAzuriraj roditeljAzuriraj, int brojGostiju, int idRezervacije, LocalDate stariDolazak, LocalDate stariOdlazak) {
        this.korisnik = korisnik;
        this.roditeljAzuriraj = roditeljAzuriraj;
        this.brojGostiju = brojGostiju;
        this.idRezervacije= idRezervacije;
        this.stariDolazak=stariDolazak;
        this.stariOdlazak=stariOdlazak;
        initialize();
        frame.setVisible(true);
    }

    public JFrame getFrame() {
    	return frame;
    }

    private void initialize() {
        frame = new JFrame();
       
        frame.setTitle("APARTMANSKI SUSTAV - Azuriranje apartmana na rezervaciji");
       
        frame.setBounds(100, 100, 692, 443);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblDatumDolaska = new JLabel("Odaberite datum dolaska (dd-MM-yyyy):");
        lblDatumDolaska.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblDatumDolaska.setBounds(49, 35, 239, 20);
        frame.getContentPane().add(lblDatumDolaska);

        datumDolaskaPicker = new JXDatePicker();
        datumDolaskaPicker.setFormats("dd-MM-yyyy");
        datumDolaskaPicker.setBounds(309, 30, 322, 28);
        datumDolaskaPicker.setBorder(new LineBorder(new Color(0, 111, 128), 2));
        frame.getContentPane().add(datumDolaskaPicker);

        JLabel lblDatumOdlaska = new JLabel("Odaberite datum odlaska (dd-MM-yyyy):");
        lblDatumOdlaska.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblDatumOdlaska.setBounds(49, 75, 239, 20);
        frame.getContentPane().add(lblDatumOdlaska);

        datumOdlaskaPicker = new JXDatePicker();
        datumOdlaskaPicker.setFormats("dd-MM-yyyy");
        datumOdlaskaPicker.setBounds(309, 70, 322, 28);
        datumOdlaskaPicker.setBorder(new LineBorder(new Color(0, 111, 128), 2));
        frame.getContentPane().add(datumOdlaskaPicker);

        JButton btnPretrazi = new JButton("Pretraži slobodne apartmane");
        btnPretrazi.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnPretrazi.setBounds(49, 115, 582, 35);
        btnPretrazi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pretraziApartmane();
            }
        });
        btnPretrazi.setBackground(new Color(102, 153, 153));
        btnPretrazi.setForeground(Color.WHITE);
        frame.getContentPane().add(btnPretrazi);

  

        model = new DefaultTableModel(new String[] { "ID apartmana", "Opis", "Kapacitet", "Cijena noćenja (€)", "Lokacija" }, 0) {
			private static final long serialVersionUID = 1L;
			@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
		};
        table = new JXTable(model);
        table.setColumnControlVisible(true);  
        table.setHorizontalScrollEnabled(true);
        table.setShowGrid(true); 

        table.setHighlighters(HighlighterFactory.createAlternateStriping());
        
        JScrollPane scrollPane = new JScrollPane();
		scrollPane = new JScrollPane(table);
		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.getViewport().setBackground(new Color(255, 255, 255));
		scrollPane.setBounds(30, 166, 620, 172);
		scrollPane.setViewportView(table);
		
		frame.getContentPane().add(scrollPane);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
       
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JButton btnDodaj = new JButton("Dodaj");
        btnDodaj.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnDodaj.setBounds(240, 350, 200, 40);
        btnDodaj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dodajApartmanNaRezervaciju();
            }
        });
        btnDodaj.setBackground(new Color(0, 111, 128));
        btnDodaj.setForeground(Color.WHITE);
        frame.getContentPane().add(btnDodaj);
        
        JButton btnPregledajApartman = new JButton("Pregledaj");
        btnPregledajApartman.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnPregledajApartman.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		pregledajApartman();
        	}
        });
        btnPregledajApartman.setBounds(30, 350, 200, 40);
        btnPregledajApartman.setBackground(new Color(0, 111, 128));
        btnPregledajApartman.setForeground(Color.WHITE);
        frame.getContentPane().add(btnPregledajApartman);
        
        JButton btnOdustani = new JButton("Natrag");
        btnOdustani.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnOdustani.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        btnOdustani.setBounds(450, 350, 200, 40);
        btnOdustani.setBackground(new Color(0, 111, 128));
        btnOdustani.setForeground(Color.WHITE);
        frame.getContentPane().add(btnOdustani);
    }

    private void pretraziApartmane() {
        model.setRowCount(0);

        java.util.Date dolazakDate = datumDolaskaPicker.getDate();
        java.util.Date odlazakDate = datumOdlaskaPicker.getDate();

        if (dolazakDate == null || odlazakDate == null) {
            JOptionPane.showMessageDialog(frame, "Odaberite oba datuma.");
            return;
        }

        LocalDate dolazak = dolazakDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate odlazak = odlazakDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        if (dolazak == null || odlazak == null) {
            JOptionPane.showMessageDialog(frame, "Odaberite ispravan datum dolaska i odlaska.");
            return;
        }
        if (dolazak.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(frame, "Datum dolaska ne može biti u prošlosti.");
            return;
        }
        if (!dolazak.isBefore(odlazak)) {
            JOptionPane.showMessageDialog(frame, "Datum dolaska mora biti prije datuma odlaska.");
            return;
        }

        LocalDate danas = LocalDate.now();
        if (dolazak.isAfter(danas.plusYears(1))) {
            JOptionPane.showMessageDialog(frame, "Datum dolaska ne smije biti više od godinu dana unaprijed!");
            return;
        }

        long brojDana = java.time.temporal.ChronoUnit.DAYS.between(dolazak, odlazak);
        if (brojDana > 365) {
            JOptionPane.showMessageDialog(frame, "Period boravka ne smije biti dulji od godinu dana.");
            return;
        }
        
        zadnjiPretrazeniDolazak = dolazak;
        zadnjiPretrazeniOdlazak = odlazak;

        try (Connection con = Database.getConnection()) {


            String sql = """
                SELECT a.id_apartmana, 
                       a.opis_apartmana, 
                       a.kapacitet,
                       a.cijena_nocenja,
                       a.lokacija
                FROM Apartman a
                WHERE a.id_iznajmljivaca = ? 
                  AND a.kapacitet >= ?
                  AND NOT EXISTS (
    SELECT 1 FROM Rezervacija r
    WHERE r.id_apartmana = a.id_apartmana
      AND (
        (? <= r.datum_odlaska AND ? >= r.datum_dolaska)
        AND r.id_rezervacije != ?
        AND NOT (
            r.datum_dolaska = ? AND r.datum_odlaska = ?
        )
      )
)
            """;

            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1, korisnik.getId_korisnika());
            psmt.setInt(2, brojGostiju);
            
            psmt.setDate(3, java.sql.Date.valueOf(dolazak));
            psmt.setDate(4, java.sql.Date.valueOf(odlazak));
            psmt.setInt(5, idRezervacije);
            psmt.setDate(6, java.sql.Date.valueOf(stariDolazak));
            psmt.setDate(7, java.sql.Date.valueOf(stariOdlazak));   

            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_apartmana"),
                    rs.getString("opis_apartmana"),
                    rs.getInt("kapacitet"),
                    String.format(Locale.ENGLISH, "%.2f", rs.getDouble("cijena_nocenja")),
                    rs.getString("lokacija"),
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "Nema slobodnih apartmana za odabrani period i broj gostiju!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Greška: " + ex.getMessage());
        }}
    
    private void pregledajApartman() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Odaberite apartman koji želite pregledati.");
            return;
        }
        
        java.util.Date dolazakDate = datumDolaskaPicker.getDate();
        java.util.Date odlazakDate = datumOdlaskaPicker.getDate();
        
        if (dolazakDate == null || odlazakDate == null) {
            JOptionPane.showMessageDialog(frame, "Odaberite datume dolaska i odlaska prije pregleda apartmana.");
            return;
        }
     

        LocalDate dolazak = dolazakDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate odlazak = odlazakDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        if(!dolazak.equals(zadnjiPretrazeniDolazak) || !odlazak.equals(zadnjiPretrazeniOdlazak)) {
        	JOptionPane.showMessageDialog(frame, "Promjenili ste datume! Molimo ponovno kliknite gumb 'Pretraži slobodne apartmane' prije pregleda.");
        	return;
        }
        	
        	
        int id = (int) model.getValueAt(selectedRow, 0);
        String opis = (String) model.getValueAt(selectedRow, 1);
        int kapacitet = (int) model.getValueAt(selectedRow, 2);
        double cijena = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());
        String lokacija = (String) model.getValueAt(selectedRow, 4);

        new ApartmanPregled(korisnik, roditeljAzuriraj, this, id, opis, kapacitet, cijena, lokacija, dolazak, odlazak);
    }

    private void dodajApartmanNaRezervaciju() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Odaberite apartman koji želite dodati.");
            return;
        }

        java.util.Date dolazakDate = datumDolaskaPicker.getDate();
        java.util.Date odlazakDate = datumOdlaskaPicker.getDate();

        if (dolazakDate == null || odlazakDate == null) {
            JOptionPane.showMessageDialog(frame, "Odaberite datume dolaska i odlaska.");
            return;
        }

        LocalDate dolazak = dolazakDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate odlazak = odlazakDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        if (!dolazak.equals(zadnjiPretrazeniDolazak) || !odlazak.equals(zadnjiPretrazeniOdlazak)) {
            JOptionPane.showMessageDialog(frame, "Promijenili ste datume! Molimo ponovno kliknite 'Pretraži slobodne apartmane'.");
            return;
        }
        
        if (dolazak.isAfter(odlazak) || dolazak.isEqual(odlazak)) {
            JOptionPane.showMessageDialog(frame, "Datum dolaska mora biti prije datuma odlaska.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String opis = (String) model.getValueAt(selectedRow, 1);
        int kapacitet = (int) model.getValueAt(selectedRow, 2);
        double cijena = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());
        String lokacija = (String) model.getValueAt(selectedRow, 4);
       
        ApartmanNaRezervaciji apartman = new ApartmanNaRezervaciji(id, opis, lokacija, kapacitet, cijena, dolazak, odlazak);
    
        roditeljAzuriraj.getDodaniApartmani().clear();
        roditeljAzuriraj.getDodaniApartmani().add(apartman);
        roditeljAzuriraj.dodajApartmanNaTabelu(apartman);
        frame.dispose();
        
    }
}
