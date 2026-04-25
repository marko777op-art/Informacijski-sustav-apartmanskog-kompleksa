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
import java.sql.SQLException;
import java.sql.Statement;
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


public class RezervacijaDodaj {
    private JFrame frame;
    private JComboBox<String> comboGosti;
    
    private JXTable table;
    private DefaultTableModel model;
    private GetterSetter korisnik;
    private RezervacijaPopis rezervacijaPopis;
    private JButton btnDodajApartman, btnNewButton, btnSpremi, btnNewButton_1;
    private JScrollPane scrollPane;
    private JLabel lblUkupnaCijena;
    private JLabel lblBrojGostiju;
 
    private List<ApartmanNaRezervaciji> dodaniApartmani = new ArrayList<>();
    private Map<String, Integer> gostMap = new HashMap<>();
    private JTextField textField_1, textField_3, textField_4, textField_5, textField_6, textField_7, textField_8, textField_9, textField_10;
    private JTextArea txtrUnesiImenaI;
    private JLabel lblUkupnaCijena1;
    public List<ApartmanNaRezervaciji> getDodaniApartmani() {
        return dodaniApartmani;
    }

    public RezervacijaDodaj(GetterSetter korisnik, RezervacijaPopis rezervacijaPopis) {
        this.korisnik = korisnik;
        this.rezervacijaPopis = rezervacijaPopis;
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Unos nove rezervacije");
        frame.setTitle("APARTMANSKI SUSTAV - Kreiranje rezervacije");
        frame.setBounds(100, 100, 943, 668);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNaslov = new JLabel("Unos nove rezervacije");
        lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblNaslov.setBounds(343, 23, 286, 30);
        frame.getContentPane().add(lblNaslov);

        JLabel lblGost = new JLabel("Odaberi gosta:");
        lblGost.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblGost.setBounds(27, 89, 200, 25);
        frame.getContentPane().add(lblGost);

        comboGosti = new JComboBox<>();
        comboGosti.setEditable(false);
        comboGosti.setBounds(156, 86, 300, 28);
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
        		new RezervacijaDodajApartman(korisnik, RezervacijaDodaj.this, brojGostiju);
        	}
        });
        btnDodajApartman.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnDodajApartman.setBounds(27, 207, 430, 35);
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
        lblUkupnaCijena.setBounds(27, 418, 180, 25);
        frame.getContentPane().add(lblUkupnaCijena);

        lblUkupnaCijena1 = new JLabel();
        lblUkupnaCijena1.setBounds(156, 418, 300, 28);
        lblUkupnaCijena1.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
        frame.getContentPane().add(lblUkupnaCijena1);

        btnSpremi = new JButton("Spremi rezervaciju");
        btnSpremi.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dodajRezervaciju();
        	}
        });
        btnSpremi.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnSpremi.setBounds(27, 465, 429, 40);
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
        btnNewButton.setBounds(475, 207, 429, 35);
    	btnNewButton.setBackground(new Color(102, 153, 153));
		btnNewButton.setForeground(Color.WHITE);
        frame.getContentPane().add(btnNewButton);
        
        btnNewButton_1 = new JButton("Natrag");
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();
        	}
        });
        btnNewButton_1.setBounds(475, 465, 429, 40);
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
     
        JLabel lblPlus = new JLabel("+", SwingConstants.CENTER); 
        lblPlus.setBounds(226, 126, 30, 28); 
        lblPlus.setFont(new Font("SansSerif", Font.BOLD, 25));
        frame.getContentPane().add(lblPlus);

        JLabel lblMinus = new JLabel("-", SwingConstants.CENTER);
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
    public void dodajRezervaciju() {
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
            // fallback
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

        double ukupnaCijena = 0.0;
        try {
            ukupnaCijena = Double.parseDouble(lblUkupnaCijena1.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Greška pri čitanju ukupne cijene.");
            return;
        }
        long brojDana = java.time.temporal.ChronoUnit.DAYS.between(apartman.getDolazak(), apartman.getOdlazak());
        if (brojDana <= 0) brojDana = 1;
        double ispravnaCijena = brojDana * apartman.getCijenaNocenja();
        ukupnaCijena = ispravnaCijena; 
        
        
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
                INSERT INTO Rezervacija 
                    (id_gosta, id_apartmana, datum_dolaska, datum_odlaska, broj_gostiju, ukupna_cijena, datum_rezervacije, id_iznajmljivaca)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

            int idRezervacije = -1;
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idGosta);
                ps.setInt(2, apartman.getIdApartmana());
                ps.setDate(3, java.sql.Date.valueOf(apartman.getDolazak()));
                ps.setDate(4, java.sql.Date.valueOf(apartman.getOdlazak()));
                ps.setInt(5, brojGostiju);
                ps.setDouble(6, ukupnaCijena);
                ps.setDate(7, java.sql.Date.valueOf(LocalDate.now())); 
                ps.setInt(8, korisnik.getId_korisnika()); 

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idRezervacije = rs.getInt(1);
                    }
                }
            }

            if (idRezervacije <= 0) {
                throw new SQLException("Nije moguće dohvatiti ID nove rezervacije.");
            }

            if (!ostaliGosti.isEmpty()) {
                String sqlOstali = """
                    INSERT INTO Gost_ostali (id_rezervacije, ime_i_prezime)
                    VALUES (?, ?)
                """;
                try (PreparedStatement psOstali = con.prepareStatement(sqlOstali)) {
                    for (String imePrezime : ostaliGosti) {
                        psOstali.setInt(1, idRezervacije);
                        psOstali.setString(2, imePrezime);
                        psOstali.addBatch();
                    }
                    psOstali.executeBatch();
                }
            }
            
            
            String sqlStavka = """
            	    INSERT INTO Stavka_rezervacije (
            	        id_iznajmljivaca, id_rezervacije, datum_rezervacije,
            	        broj_gostiju, datum_dolaska, datum_odlaska, ukupna_cijena,
            	        broj_dokumenta, ime_gosta, prezime_gosta,
            	        kapacitet, lokacija, cijena_nocenja, opis_apartmana,
            	        ime_i_prezime, korisnicko_ime
            	    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            	""";

            	try (PreparedStatement psStavka = con.prepareStatement(sqlStavka)) {
            	    psStavka.setInt(1, korisnik.getId_korisnika());
            	    psStavka.setInt(2, idRezervacije);
            	    psStavka.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            	    psStavka.setInt(4, brojGostiju);
            	    psStavka.setDate(5, java.sql.Date.valueOf(apartman.getDolazak()));
            	    psStavka.setDate(6, java.sql.Date.valueOf(apartman.getOdlazak()));
            	    psStavka.setDouble(7, ukupnaCijena);

            	    String[] parts = odabraniGost.split(" - ", 2);
            	    String brojDokumenta = parts.length > 0 ? parts[0].trim() : "";
            	    String imePrezime = parts.length > 1 ? parts[1].trim() : "";
            	    String[] imePrezimeSplit = imePrezime.split(" ", 2);
            	    String imeGosta = imePrezimeSplit.length > 0 ? imePrezimeSplit[0] : "";
            	    String prezimeGosta = imePrezimeSplit.length > 1 ? imePrezimeSplit[1] : "";

            	    psStavka.setString(8, brojDokumenta);
            	    psStavka.setString(9, imeGosta);
            	    psStavka.setString(10, prezimeGosta);

            	    psStavka.setInt(11, apartman.getKapacitet());
            	    psStavka.setString(12, apartman.getLokacija());
            	    psStavka.setDouble(13, apartman.getCijenaNocenja());
            	    psStavka.setString(14, apartman.getOpis());

            	    String ostali = String.join(", ", ostaliGosti);
            	    psStavka.setString(15, ostali.isEmpty() ? null : ostali);

            	    psStavka.setString(16, korisnik.getKorisnicko_ime());

            	    psStavka.executeUpdate();
            	}
            
            con.commit();

            JOptionPane.showMessageDialog(frame, "Rezervacija uspješno kreirana!");
            new RezervacijaPregled(korisnik, idRezervacije);
            frame.dispose();
            if (rezervacijaPopis != null) rezervacijaPopis.ucitajRezervacije();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Greška prilikom spremanja: " + ex.getMessage());
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