package zavrsniRad;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.decorator.HighlighterFactory;


public class RezervacijaAzuriraj {
    private JFrame frame;
    private JComboBox<String> comboGosti;
    private JLabel lblUkupnaCijena1;
    private JXTable table;
    private DefaultTableModel model;
    private GetterSetter korisnik;
    private RezervacijaPopis rezervacijaPopis;
    private JButton btnDodajApartman, btnNewButton, btnSpremi, btnNewButton_1;
    private JScrollPane scrollPane;
    private JLabel lblUkupnaCijena;
    private JLabel lblBrojGostiju;
    private int selectedId;
    private JLabel lblPlus, lblMinus;
    private LocalDate stariDolazak;
    private LocalDate stariOdlazak;
    
    private List<ApartmanNaRezervaciji> dodaniApartmani = new ArrayList<>();
    private Map<String, Integer> gostMap = new HashMap<>();
    private JTextField textField_1, textField_3, textField_4, textField_5, textField_6, textField_7, textField_8, textField_9, textField_10;
    private JTextArea txtrUnesiImenaI;
    public List<ApartmanNaRezervaciji> getDodaniApartmani() {
        return dodaniApartmani;
    }

    public RezervacijaAzuriraj(GetterSetter korisnik, RezervacijaPopis rezervacijaPopis, int selectedId) {
        this.korisnik = korisnik;
        this.rezervacijaPopis = rezervacijaPopis;
        this.selectedId=selectedId;
        initialize();
        ucitajRezervaciju();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Ažuriranje rezervacije");
        frame.setTitle("APARTMANSKI SUSTAV - Ažuriranje rezervacije");
        frame.setBounds(100, 100, 943, 571);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel lblNaslov = new JLabel("Ažuriranje rezervacije");
        lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblNaslov.setBounds(344, 23, 285, 30);
        frame.getContentPane().add(lblNaslov);

        JLabel lblGost = new JLabel("Odaberi gosta:");
        lblGost.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblGost.setBounds(27, 89, 200, 25);
        frame.getContentPane().add(lblGost);

        comboGosti = new JComboBox<>();
        comboGosti.setEditable(false);
        comboGosti.setBounds(156, 86, 315, 28);
        comboGosti.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        frame.getContentPane().add(comboGosti);

        JLabel lblDodajGosta = new JLabel("Odaberi broj gostiju:");
        lblDodajGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblDodajGosta.setBounds(27, 129, 150, 25);
        frame.getContentPane().add(lblDodajGosta);
     
        lblBrojGostiju = new JLabel("1", SwingConstants.CENTER);
        lblBrojGostiju.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblBrojGostiju.setBounds(196, 124, 29, 35);
        frame.getContentPane().add(lblBrojGostiju);

        btnDodajApartman = new JButton("Dodaj apartman");
        btnDodajApartman.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int brojGostiju=Integer.parseInt(lblBrojGostiju.getText());
        		new RezervacijaAzurirajApartman(korisnik, RezervacijaAzuriraj.this, brojGostiju, selectedId, stariDolazak, stariOdlazak);
        	}
        });
        btnDodajApartman.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnDodajApartman.setBounds(27, 220, 435, 35);
        btnDodajApartman.setBackground(new Color(102, 153, 153));
		btnDodajApartman.setForeground(Color.WHITE);
        
        frame.getContentPane().add(btnDodajApartman);
     
        model = new DefaultTableModel(new String[] {"ID apartmana", "Opis apartmana", "Lokacija", "Kapacitet", "Cijena noćenja (€)", "Datum dolaska", "Datum odlaska"}, 0) {
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
        
		table.setBackground(new Color(255, 255, 255));
		table.getTableHeader().setBackground(new Color(0, 111, 128));
		table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setSelectionForeground(Color.BLACK);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.getViewport().setBackground(new Color(255, 255, 255));
		scrollPane.setBounds(28, 265, 878, 143);
		scrollPane.setViewportView(table);
		
		frame.getContentPane().add(scrollPane);
		
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        
        lblUkupnaCijena = new JLabel("Ukupna cijena (€):");
        lblUkupnaCijena.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblUkupnaCijena.setBounds(27, 426, 180, 25);
        frame.getContentPane().add(lblUkupnaCijena);

        lblUkupnaCijena1 = new JLabel();
        lblUkupnaCijena1.setBounds(156, 426, 306, 28);
        lblUkupnaCijena1.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        frame.getContentPane().add(lblUkupnaCijena1);

        btnSpremi = new JButton("Spremi rezervaciju");
        btnSpremi.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		azurirajRezervaciju();
        	}
        });
        btnSpremi.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnSpremi.setBounds(27, 480, 435, 40);
        btnSpremi.setBackground(new Color(0, 111, 128));
		btnSpremi.setForeground(Color.WHITE);
    
        frame.getContentPane().add(btnSpremi);
        
        btnNewButton = new JButton("Ukloni apartman");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ukloniApartmanSTabele();
        	}
        });
        btnNewButton.setBounds(479, 220, 427, 35);
        btnNewButton.setBackground(new Color(102, 153, 153));
		btnNewButton.setForeground(Color.WHITE);
    
        frame.getContentPane().add(btnNewButton);
        
        btnNewButton_1 = new JButton("Odustani");
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        btnNewButton_1.setBounds(479, 480, 427, 40);
        btnNewButton_1.setBackground(new Color(0, 111, 128));
		btnNewButton_1.setForeground(Color.WHITE);
        frame.getContentPane().add(btnNewButton_1);
        
        textField_1 = new JTextField();
        textField_1.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_1.setBounds(156, 169, 243, 28);
        frame.getContentPane().add(textField_1);
        
        textField_3 = new JTextField();
        textField_3.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_3.setBounds(409, 169, 243, 28);
        frame.getContentPane().add(textField_3);
        
        textField_4 = new JTextField();
        textField_4.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_4.setBounds(663, 169, 243, 28);
        frame.getContentPane().add(textField_4);
        
        textField_5 = new JTextField();
        textField_5.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_5.setBounds(156, 207, 243, 28);
        frame.getContentPane().add(textField_5);
        
        textField_6 = new JTextField();
        textField_6.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_6.setBounds(409, 207, 243, 28);
        frame.getContentPane().add(textField_6);
        
        textField_7 = new JTextField();
        textField_7.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_7.setBounds(662, 207, 243, 28);
        frame.getContentPane().add(textField_7);
        
        textField_8 = new JTextField();
        textField_8.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_8.setBounds(156, 245, 243, 28);
        frame.getContentPane().add(textField_8);
        
        textField_9 = new JTextField();
        textField_9.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_9.setBounds(409, 245, 243, 28);
        frame.getContentPane().add(textField_9);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(27, 63, 878, 18);
        frame.getContentPane().add(separator);
        
        textField_10 = new JTextField();
        textField_10.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        textField_10.setBounds(662, 245, 243, 28);
        frame.getContentPane().add(textField_10);

        lblPlus = new JLabel("+", SwingConstants.CENTER); 
        lblPlus.setBounds(226, 126, 30, 28); 
        lblPlus.setFont(new Font("SansSerif", Font.BOLD, 25)); 
        frame.getContentPane().add(lblPlus);

        lblMinus = new JLabel("-", SwingConstants.CENTER);
        lblMinus.setBounds(166, 124, 30, 28);
        lblMinus.setFont(new Font("SansSerif", Font.BOLD, 25));
        frame.getContentPane().add(lblMinus);
        
        txtrUnesiImenaI = new JTextArea();
        txtrUnesiImenaI.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtrUnesiImenaI.setEditable(false);
        txtrUnesiImenaI.setOpaque(false); 
        txtrUnesiImenaI.setWrapStyleWord(true); 
        txtrUnesiImenaI.setLineWrap(true);
        txtrUnesiImenaI.setBorder(null); 
        txtrUnesiImenaI.setText("Unesite ime i prezime ostalih gostiju");
        txtrUnesiImenaI.setBounds(27, 170, 119, 102);
 
        frame.getContentPane().add(txtrUnesiImenaI);
        
        dodajPlusMinusLogiku(lblBrojGostiju, lblPlus, lblMinus);
        ucitajGoste();
    }
    
    private void dodajPlusMinusLogiku(JLabel lblBrojGostiju, JLabel lblPlus, JLabel lblMinus) {
        List<JTextField> ostaliTextFields = List.of(
            textField_1, textField_3, textField_4, textField_5,
            textField_6, textField_7, textField_8, textField_9, textField_10
        );

        txtrUnesiImenaI.setVisible(false);
        for (JTextField tf : ostaliTextFields) tf.setVisible(false);

        lblPlus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int current = Integer.parseInt(lblBrojGostiju.getText());
                if (current < ostaliTextFields.size() + 1) {
                    if (current == 1) {
                        txtrUnesiImenaI.setVisible(true);
                    }
                    ostaliTextFields.get(current - 1).setVisible(true);
                    lblBrojGostiju.setText(String.valueOf(current + 1));
                    adjustLayout(current + 1);
                }
            }
        });

        lblMinus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int current = Integer.parseInt(lblBrojGostiju.getText());
                if (current > 1) {
                    int index = current - 2;
                    ostaliTextFields.get(index).setVisible(false);
                    ostaliTextFields.get(index).setText("");
                    lblBrojGostiju.setText(String.valueOf(current - 1));
                    if (current - 1 == 1) {
                        txtrUnesiImenaI.setVisible(false);
                    }
                    adjustLayout(current - 1);
                }
            }
        });

        adjustLayout(1);
    }
    
    private void ucitajGoste() {
        gostMap.clear();
        comboGosti.removeAllItems();

        try (Connection con = Database.getConnection()) {
            String sql = "SELECT id_gosta, broj_dokumenta, ime_gosta, prezime_gosta FROM Gost WHERE id_iznajmljivaca = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, korisnik.getId_korisnika());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id_gosta");
                        String broj = rs.getString("broj_dokumenta");
                        String ime = rs.getString("ime_gosta");
                        String prezime = rs.getString("prezime_gosta");

                        String prikaz = broj + " - " + ime + " " + prezime;

                        gostMap.put(prikaz, id);
                        comboGosti.addItem(prikaz);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Greška kod dohvata gostiju: " + e.getMessage());
        }

        AutoCompleteDecorator.decorate(comboGosti);
    }

    public void dodajApartmanNaTabelu(ApartmanNaRezervaciji apartman) {
    	model.setRowCount(0);
        model.addRow(new Object[] {
            apartman.getIdApartmana(),
            apartman.getOpis(),
            apartman.getLokacija(),
            apartman.getKapacitet(),
            apartman.getCijenaNocenja(),
            apartman.getDolazak().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
            apartman.getOdlazak().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        });
        
        long brojDana = java.time.temporal.ChronoUnit.DAYS.between(apartman.getDolazak(), apartman.getOdlazak());
        if(brojDana <=0) brojDana = 1;
        double ukupno = brojDana * apartman.getCijenaNocenja();
        lblUkupnaCijena1.setText(String.format(Locale.ENGLISH, "%.2f", ukupno));
    }
 
    private void ukloniApartmanSTabele() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Odaberite apartman koji želite ukloniti.");
            return;
        }
        int potvrda = JOptionPane.showConfirmDialog(frame, "Jeste li sigurni da želite ukloniti odabrani apartman?", "Potvrda", JOptionPane.YES_NO_OPTION);
        if (potvrda != JOptionPane.YES_OPTION) return;
        model.removeRow(selectedRow);
        dodaniApartmani.remove(selectedRow);
        lblUkupnaCijena1.setText("");
    }
 
    private void ucitajRezervaciju() {
        try (Connection con = Database.getConnection()) {
                String sql = """
                    SELECT r.id_gosta, r.broj_gostiju, r.ukupna_cijena,
                           g.broj_dokumenta, g.ime_gosta, g.prezime_gosta
                    FROM Rezervacija r
                    JOIN Gost g ON r.id_gosta = g.id_gosta
                    WHERE r.id_rezervacije = ?
                """;
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, selectedId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                           
                            int brojGostiju = rs.getInt("broj_gostiju");
                            double ukupnaCijena = rs.getDouble("ukupna_cijena");
                            String gostPrikaz = rs.getString("broj_dokumenta") + " - "
                                              + rs.getString("ime_gosta") + " "
                                              + rs.getString("prezime_gosta");

                            comboGosti.setSelectedItem(gostPrikaz);
                            lblUkupnaCijena1.setText(String.format(Locale.ENGLISH, "%.2f", ukupnaCijena));

                            List<JTextField> ostaliFields = List.of(
                                textField_1, textField_3, textField_4, textField_5,
                                textField_6, textField_7, textField_8, textField_9, textField_10
                            );
                            for (JTextField tf : ostaliFields) {
                                tf.setVisible(false);
                                tf.setText("");
                            }
                            txtrUnesiImenaI.setVisible(false);

                            if (brojGostiju > 1) {
                                txtrUnesiImenaI.setVisible(true);
                                for (int i = 1; i < brojGostiju; i++) {
                                    ostaliFields.get(i - 1).setVisible(true);
                                }
                            }
                            adjustLayout(brojGostiju);
                            lblBrojGostiju.setText(String.valueOf(brojGostiju));
                        }
                    }
                }

                String sqlOstali = "SELECT ime_i_prezime FROM Gost_ostali WHERE id_rezervacije = ?";
                try (PreparedStatement psOstali = con.prepareStatement(sqlOstali)) {
                    psOstali.setInt(1, selectedId);
                    try (ResultSet rsOstali = psOstali.executeQuery()) {
                        List<JTextField> ostaliFields = List.of(
                            textField_1, textField_3, textField_4, textField_5,
                            textField_6, textField_7, textField_8, textField_9, textField_10
                        );
                        int index = 0;
                        while (rsOstali.next() && index < ostaliFields.size()) {
                            JTextField tf = ostaliFields.get(index);
                            tf.setVisible(true);
                            tf.setText(rsOstali.getString("ime_i_prezime"));
                            index++;
                        }
                        if (index > 0) {
                            txtrUnesiImenaI.setVisible(true);
                        }
                    }
                }

                String sqlApartman = """
                    SELECT a.id_apartmana, a.opis_apartmana, a.lokacija, 
                           a.kapacitet, a.cijena_nocenja,
                           r.datum_dolaska, r.datum_odlaska
                    FROM Rezervacija r
                    JOIN Apartman a ON r.id_apartmana = a.id_apartmana
                    WHERE r.id_rezervacije = ?
                """;
                try (PreparedStatement ps = con.prepareStatement(sqlApartman)) {
                    ps.setInt(1, selectedId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            ApartmanNaRezervaciji apartman = new ApartmanNaRezervaciji(
                                rs.getInt("id_apartmana"),
                                rs.getString("opis_apartmana"),
                                rs.getString("lokacija"),
                                rs.getInt("kapacitet"),
                                rs.getDouble("cijena_nocenja"),
                                rs.getDate("datum_dolaska").toLocalDate(),
                                rs.getDate("datum_odlaska").toLocalDate()
                            );
                            stariDolazak=apartman.getDolazak();
                            stariOdlazak=apartman.getOdlazak();
                            dodaniApartmani.clear();
                            dodaniApartmani.add(apartman);
                            dodajApartmanNaTabelu(apartman);
                        }
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Greška pri učitavanju: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        private void azurirajRezervaciju() {
            if (dodaniApartmani.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Prvo dodajte apartman!");
                return;
            }

            String odabraniGost = (String) comboGosti.getSelectedItem();
            if (odabraniGost == null || !gostMap.containsKey(odabraniGost)) {
                JOptionPane.showMessageDialog(frame, "Odaberite gosta!");
                return;
            }

            int idGosta = gostMap.get(odabraniGost);
            ApartmanNaRezervaciji apartman = dodaniApartmani.get(0);

            int brojGostiju = 1;
            try {
                brojGostiju = Integer.parseInt(lblBrojGostiju.getText());
            } catch (Exception e) {
            }

            List<String> ostaliGosti = new ArrayList<>();
            List<JTextField> ostaliTextFields = List.of(
                textField_1, textField_3, textField_4, textField_5,
                textField_6, textField_7, textField_8, textField_9, textField_10
            );
            for (JTextField tf : ostaliTextFields) {
                if (tf.isVisible()) {
                    String text = tf.getText().trim();
                    if (text.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Unesite ime i prezime za sve dodatne goste!");
                        return;
                    }
                    if (!text.matches("[a-zA-ZčćšđžČĆŠĐŽ ]+")) {
                        JOptionPane.showMessageDialog(frame, "Ime i prezime dodatnih gostiju smije sadržavati samo slova!");
                        return;
                    }
                    if (text.length() > 100) {
                        JOptionPane.showMessageDialog(frame, "Ime i prezime gosta ne smije biti duže od 100 znakova!");
                        return;
                    }
                    ostaliGosti.add(text);
                }
            }

            long brojDana = java.time.temporal.ChronoUnit.DAYS.between(apartman.getDolazak(), apartman.getOdlazak());
            if (brojDana <= 0) brojDana = 1;
            double ukupnaCijena = brojDana * apartman.getCijenaNocenja();

            if (brojGostiju > apartman.getKapacitet()) {
                JOptionPane.showMessageDialog(frame,
                    "Broj gostiju ne smije biti veći od kapaciteta apartmana (" + apartman.getKapacitet() + ").");
                return;
            }

            if (ukupnaCijena <= 0) {
                JOptionPane.showMessageDialog(frame, "Ukupna cijena mora biti veća od 0.");
                return;
            }

          
                try (Connection con = Database.getConnection()) {

                con.setAutoCommit(false);

                String sql = """
                    UPDATE Rezervacija 
                    SET id_gosta = ?, id_apartmana = ?, datum_dolaska = ?, datum_odlaska = ?, 
                        broj_gostiju = ?, ukupna_cijena = ?
                    WHERE id_rezervacije = ?
                """;
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idGosta);
                    ps.setInt(2, apartman.getIdApartmana());
                    ps.setDate(3, java.sql.Date.valueOf(apartman.getDolazak()));
                    ps.setDate(4, java.sql.Date.valueOf(apartman.getOdlazak()));
                    ps.setInt(5, brojGostiju);
                    ps.setDouble(6, ukupnaCijena);
                    ps.setInt(7, selectedId);
                    ps.executeUpdate();
                }

                String sqlDeleteOstali = "DELETE FROM Gost_ostali WHERE id_rezervacije = ?";
                try (PreparedStatement psDel = con.prepareStatement(sqlDeleteOstali)) {
                    psDel.setInt(1, selectedId);
                    psDel.executeUpdate();
                }

                if (!ostaliGosti.isEmpty()) {
                    String sqlOstali = "INSERT INTO Gost_ostali (id_rezervacije, ime_i_prezime) VALUES (?, ?)";
                    try (PreparedStatement psOstali = con.prepareStatement(sqlOstali)) {
                        for (String imePrezime : ostaliGosti) {
                            psOstali.setInt(1, selectedId);
                            psOstali.setString(2, imePrezime);
                            psOstali.addBatch();
                        }
                        psOstali.executeBatch();
                    }
                }

                String sqlDeleteStavke = "DELETE FROM Stavka_rezervacije WHERE id_rezervacije = ?";
                try (PreparedStatement psStavkaDel = con.prepareStatement(sqlDeleteStavke)) {
                    psStavkaDel.setInt(1, selectedId);
                    psStavkaDel.executeUpdate();
                }

                String sqlInsertStavka = """
                    INSERT INTO Stavka_rezervacije 
                    (id_iznajmljivaca, id_rezervacije, datum_rezervacije, broj_gostiju, 
                     datum_dolaska, datum_odlaska, ukupna_cijena, broj_dokumenta,
                     ime_gosta, prezime_gosta, kapacitet, lokacija, cijena_nocenja,
                     opis_apartmana, ime_i_prezime, korisnicko_ime)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                try (PreparedStatement psStavkaIns = con.prepareStatement(sqlInsertStavka)) {
                    psStavkaIns.setInt(1, korisnik.getId_korisnika());
                    psStavkaIns.setInt(2, selectedId);
                    psStavkaIns.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    psStavkaIns.setInt(4, brojGostiju);
                    psStavkaIns.setDate(5, java.sql.Date.valueOf(apartman.getDolazak()));
                    psStavkaIns.setDate(6, java.sql.Date.valueOf(apartman.getOdlazak()));
                    psStavkaIns.setDouble(7, ukupnaCijena);

                    String[] parts = odabraniGost.split(" - ");
                    String brojDokumenta = parts[0].trim();
                    String[] imePrezimeArr = parts[1].split(" ");
                    String imeGosta = imePrezimeArr[0].trim();
                    String prezimeGosta = imePrezimeArr.length > 1 ? imePrezimeArr[1].trim() : "";

                    psStavkaIns.setString(8, brojDokumenta);
                    psStavkaIns.setString(9, imeGosta);
                    psStavkaIns.setString(10, prezimeGosta);

                    psStavkaIns.setInt(11, apartman.getKapacitet());
                    psStavkaIns.setString(12, apartman.getLokacija());
                    psStavkaIns.setDouble(13, apartman.getCijenaNocenja());
                    psStavkaIns.setString(14, apartman.getOpis());

                    String sviOstali = String.join(",", ostaliGosti);
                    psStavkaIns.setString(15, sviOstali);

                    psStavkaIns.setString(16, korisnik.getKorisnicko_ime());

                    psStavkaIns.executeUpdate();
                }

                con.commit();

                JOptionPane.showMessageDialog(frame, "Rezervacija uspješno ažurirana!");
                frame.dispose();
                new RezervacijaPregled(korisnik, selectedId);
                if (rezervacijaPopis != null) rezervacijaPopis.ucitajRezervacije();
               
                

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Greška prilikom ažuriranja: " + ex.getMessage());
            }
        }
        
        private void adjustLayout(int guestCount) {
            int baseY = 180;  
            int rowHeight = 35;

            int rows = (guestCount <= 1) ? 0 : ((guestCount - 2) / 3) + 1;
            int shift = rows * rowHeight;

            btnDodajApartman.setLocation(btnDodajApartman.getX(), baseY + shift);
            btnNewButton.setLocation(btnNewButton.getX(), baseY + shift);
            scrollPane.setLocation(scrollPane.getX(), baseY + 50 + shift);
            lblUkupnaCijena.setLocation(lblUkupnaCijena.getX(), baseY + 220 + shift);
            lblUkupnaCijena1.setLocation(lblUkupnaCijena1.getX(), baseY + 220 + shift);
            btnSpremi.setLocation(btnSpremi.getX(), baseY + 280 + shift);
            btnNewButton_1.setLocation(btnNewButton_1.getX(), baseY + 280 + shift);

            int minHeight = 550; 
            frame.setSize(frame.getWidth(), minHeight + shift);
        }
}