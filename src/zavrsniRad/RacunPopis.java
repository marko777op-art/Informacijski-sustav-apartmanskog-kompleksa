package zavrsniRad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;



public class RacunPopis {

	private JFrame frame;
	private JXTable table;
	private DefaultTableModel model;
	private int selectedRow = -1;
	private int selectedId = -1;
	private GetterSetter korisnik;
	private JTextField txtPretraziteApartmane;

	public RacunPopis(GetterSetter korisnik) {
		this.korisnik=korisnik;
		initialize();
		ucitajRacune();
		frame.setVisible(true);
	}
		
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Upravljanje računima");
		frame.setBounds(100, 100, 840, 585);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Upravljanje računima");
		lblNaslov.setBackground(new Color(128, 255, 255));
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNaslov.setBounds(275, 92, 334, 67);
		frame.getContentPane().add(lblNaslov);

		JSeparator separator = new JSeparator();
		separator.setBounds(37, 17, 751, 17);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(37, 94, 751, 21);
		frame.getContentPane().add(separator_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 203, 751, 220);
		frame.getContentPane().add(scrollPane);

		model = new DefaultTableModel(new String[] { "ID računa", "Datum računa", "ID rezervacije", "Gost", "Ukupna cijena (€)" }, 0) {
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

		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.getViewport().setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(table);
		
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setWidth(0);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					selectedId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
				}
			}
		});

		JButton btnObrisi = new JButton("Obriši");
		btnObrisi.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obrisiRacun();
			}
		});
		btnObrisi.setBounds(416, 433, 371, 40);
		btnObrisi.setBackground(new Color(0, 111, 128));
		btnObrisi.setForeground(Color.WHITE);

		btnObrisi.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnObrisi);

		JButton btnGosti = new JButton("Izbornik");
		btnGosti.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnGosti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GlavniIzbornik(korisnik);
				frame.dispose();
			}
		});
		btnGosti.setBounds(37, 27, 180, 57);
		btnGosti.setBackground(new Color(102, 153, 153));
		btnGosti.setForeground(Color.WHITE);

		frame.getContentPane().add(btnGosti);

		JButton btnIzbornik = new JButton("Gosti");
		btnIzbornik.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnIzbornik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GostPopis(korisnik);
				frame.dispose();			}
		});
		btnIzbornik.setBounds(417, 27, 180, 57);
		btnIzbornik.setBackground(new Color(102, 153, 153));
		btnIzbornik.setForeground(Color.WHITE);
		frame.getContentPane().add(btnIzbornik);

		JButton btnRezervacije = new JButton("Rezervacije");
		btnRezervacije.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnRezervacije.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RezervacijaPopis(korisnik);
				frame.dispose();
			}
		});
		btnRezervacije.setBounds(607, 27, 180, 57);
		btnRezervacije.setBackground(new Color(102, 153, 153));
		btnRezervacije.setForeground(Color.WHITE);
		frame.getContentPane().add(btnRezervacije);
		
		JButton btnRacuni = new JButton("Apartmani");
		btnRacuni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ApartmanPopis(korisnik);
				frame.dispose();
			}
		});
		btnRacuni.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnRacuni.setBounds(227, 27, 180, 57);
		btnRacuni.setBackground(new Color(102, 153, 153));
		btnRacuni.setForeground(Color.WHITE);
		frame.getContentPane().add(btnRacuni);
		
		JButton btnPregledaj = new JButton("Pregledaj");
		btnPregledaj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnPregledaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRow == -1) {
				    JOptionPane.showMessageDialog(null, "Niste odabrali račun za pregled!");
				    return;
				}
				new RacunPregled(korisnik, selectedId, true);

			}
		});
		btnPregledaj.setBounds(35, 433, 371, 40);
		btnPregledaj.setBackground(new Color(0, 111, 128));
		btnPregledaj.setForeground(Color.WHITE);
		btnPregledaj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnPregledaj);
		
		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(37, 483, 753, 21);
		frame.getContentPane().add(separator_1_1);
		
		JButton btnIzvjestaj = new JButton("Generiraj izvještaj");
		btnIzvjestaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generirajIzvjestaj();
			}
		});
		btnIzvjestaj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnIzvjestaj.setBounds(37, 495, 751, 40);
		btnIzvjestaj.setBackground(new Color(102, 153, 153));
		btnIzvjestaj.setForeground(Color.WHITE);
		frame.getContentPane().add(btnIzvjestaj);
	
		txtPretraziteApartmane = new JTextField();
		txtPretraziteApartmane.setText("Pretražite račune");
		txtPretraziteApartmane.setForeground(Color.GRAY);
		txtPretraziteApartmane.setBorder(null);

		txtPretraziteApartmane.addFocusListener(new java.awt.event.FocusAdapter() {
		    public void focusGained(java.awt.event.FocusEvent e) {
		        if (txtPretraziteApartmane.getText().equals("Pretražite račune")) {
		            txtPretraziteApartmane.setText("");
		            txtPretraziteApartmane.setForeground(Color.BLACK);
		        }
		    }

		    public void focusLost(java.awt.event.FocusEvent e) {
		        if (txtPretraziteApartmane.getText().isEmpty()) {
		            txtPretraziteApartmane.setForeground(Color.GRAY);
		            txtPretraziteApartmane.setText("Pretražite račune");
		        }
		    }
		});

		JLabel searchLabel = new JLabel();
		JLabel clearLabel = new JLabel();

		URL searchIconURL = getClass().getResource("/search.png");
		if (searchIconURL != null) {
		    ImageIcon lupaIcon = new ImageIcon(searchIconURL);
		    NoScalingIcon searchIcon = new NoScalingIcon(lupaIcon);
		    searchLabel.setIcon(searchIcon);
		    searchLabel.setBorder(null);
		    searchLabel.setOpaque(false);
		} else {
		    System.err.println("Nije pronađena ikona: search.png");
		}

		URL clearIconURL = getClass().getResource("/clear.png");
		if (clearIconURL != null) {
		    ImageIcon clearImg = new ImageIcon(clearIconURL);
		    NoScalingIcon clearXIcon = new NoScalingIcon(clearImg);
		    clearLabel.setIcon(clearXIcon);
		    clearLabel.setBorder(null);
		    clearLabel.setOpaque(false);
		    clearLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
		    System.err.println("Nije pronađena ikona: clear.png");
		}

		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		clearLabel.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        txtPretraziteApartmane.setText("");
		        txtPretraziteApartmane.requestFocus();
		        sorter.setRowFilter(null);
		    }
		});

		JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
		searchPanel.setBounds(142, 156, 541, 32);
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		searchPanel.add(searchLabel, BorderLayout.WEST);
		searchPanel.add(txtPretraziteApartmane, BorderLayout.CENTER);
		searchPanel.add(clearLabel, BorderLayout.EAST);

		frame.getContentPane().add(searchPanel);
		txtPretraziteApartmane.setColumns(10);

		txtPretraziteApartmane.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyReleased(java.awt.event.KeyEvent evt) {
		        String searchText = txtPretraziteApartmane.getText().trim().toLowerCase();
		        if (searchText.length() == 0 || searchText.equals("pretražite račune")) {
		            sorter.setRowFilter(null);
		        } else {
		            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
		        }
		    }
		});
			}
	public void ucitajRacune() {
	    model.setRowCount(0);

	    String sql = """
	        SELECT r.id_racuna, r.datum_racuna, r.id_rezervacije, 
	               CONCAT(g.broj_dokumenta, ' - ', g.ime_gosta, ' ', g.prezime_gosta) AS gost_podaci, 
	               rez.ukupna_cijena
	        FROM Racun r
	        JOIN Rezervacija rez ON r.id_rezervacije = rez.id_rezervacije
	        JOIN Gost g ON rez.id_gosta = g.id_gosta
	        WHERE r.id_iznajmljivaca = ?
	        ORDER BY r.datum_racuna DESC
	    """;

	    try (Connection con = Database.getConnection();
	         PreparedStatement psmt = con.prepareStatement(sql)) {

	        psmt.setInt(1, korisnik.getId_korisnika());

	        try (ResultSet rs = psmt.executeQuery()) {
	            while (rs.next()) {
	                int idRacuna = rs.getInt("id_racuna");
	                String datumRacuna = rs.getDate("datum_racuna").toLocalDate()
	                        .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	                int idRezervacije = rs.getInt("id_rezervacije");
	                String gostPodaci = rs.getString("gost_podaci");
	                double ukupnaCijena = rs.getDouble("ukupna_cijena");

	                String formatiranaCijena = String.format(java.util.Locale.ENGLISH, "%.2f", ukupnaCijena);

	                model.addRow(new Object[]{idRacuna, datumRacuna, idRezervacije, gostPodaci, formatiranaCijena
	                });
	            }
	        }

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Greška pri učitavanju računa iz baze: " + e.getMessage());
	    }
	}

	private void obrisiRacun() {
		if (selectedRow == -1 || selectedId == -1) {
		    JOptionPane.showMessageDialog(null, "Niste odabrali račun za brisanje!");
		    return;
		}

		int potvrda = JOptionPane.showConfirmDialog(null,
		        "Jeste li sigurni da želite obrisati odabrani račun?",
		        "Potvrda brisanja",
		        JOptionPane.YES_NO_OPTION);
		if (potvrda != JOptionPane.YES_OPTION) return;

		try (Connection con = Database.getConnection()) {

		    con.setAutoCommit(false); 

		    String sqlDeleteStavke = "DELETE FROM Stavka_racuna WHERE id_racuna = ?";
		    try (PreparedStatement ps = con.prepareStatement(sqlDeleteStavke)) {
		        ps.setInt(1, selectedId);
		        ps.executeUpdate();
		    }

		    String sqlDeleteRacun = "DELETE FROM Racun WHERE id_racuna = ? AND id_iznajmljivaca = ?";
		    try (PreparedStatement ps = con.prepareStatement(sqlDeleteRacun)) {
		        ps.setInt(1, selectedId);
		        ps.setInt(2, korisnik.getId_korisnika());
		        ps.executeUpdate();
		    }

		    con.commit();

		    JOptionPane.showMessageDialog(null, "Račun uspješno obrisan.");
		    ucitajRacune();
		    selectedRow = -1;
		    selectedId = -1;

		} catch (Exception e) {
		    e.printStackTrace();
		    JOptionPane.showMessageDialog(null, "Greška pri brisanju računa: " + e.getMessage());
		}
	}

	private void generirajIzvjestaj() {
	    String sql = """
	        SELECT *
	        FROM Stavka_racuna
	        WHERE id_iznajmljivaca = ?
	        ORDER BY id_racuna
	    """;

	    try (Connection con = Database.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, korisnik.getId_korisnika());
	        ResultSet rs = ps.executeQuery();

	        Workbook wb = new XSSFWorkbook();
	        Sheet sheet = wb.createSheet("Računi");

	        CellStyle headerStyle = wb.createCellStyle();
	        var headerFont = wb.createFont();
	        headerFont.setBold(true);
	        headerStyle.setFont(headerFont);
	        headerStyle.setBorderBottom(BorderStyle.THIN);
	        headerStyle.setBorderTop(BorderStyle.THIN);
	        headerStyle.setBorderLeft(BorderStyle.THIN);
	        headerStyle.setBorderRight(BorderStyle.THIN);

	        CellStyle cellStyle = wb.createCellStyle();
	        cellStyle.setBorderBottom(BorderStyle.THIN);
	        cellStyle.setBorderTop(BorderStyle.THIN);
	        cellStyle.setBorderLeft(BorderStyle.THIN);
	        cellStyle.setBorderRight(BorderStyle.THIN);

	        CellStyle dateStyle = wb.createCellStyle();
	        var dateFont = wb.createFont();
	        dateFont.setBold(true);
	        dateStyle.setFont(dateFont);

	        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
	        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	        Row dateRow = sheet.createRow(0);
	        Cell dateCell = dateRow.createCell(0);
	        dateCell.setCellValue("Izvještaj: " + dateTimeFormat.format(LocalDateTime.now()));
	        dateCell.setCellStyle(dateStyle);

	        String[] headers = {
	            "ID računa", "ID rezervacije", "Datum računa", "Datum rezervacije", "Gost",
	            "Ostali gosti", "Broj gostiju", "Ukupna cijena (€)", "Opis apartmana",
	            "Kapacitet", "Cijena noćenja (€)", "Lokacija",
	            "Datum dolaska", "Datum odlaska", "Iznajmljivač"
	        };

	        Row header = sheet.createRow(1);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = header.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	        }

	        int rowIndex = 2;

	        while (rs.next()) {
	            Row row = sheet.createRow(rowIndex++);

	            row.createCell(0).setCellValue(rs.getInt("id_racuna"));
	            row.createCell(1).setCellValue(rs.getInt("id_rezervacije"));
	            row.createCell(2).setCellValue(dateFormat.format(rs.getDate("datum_racuna").toLocalDate()));
	            row.createCell(3).setCellValue(dateFormat.format(rs.getDate("datum_rezervacije").toLocalDate()));

	            String gost = rs.getString("broj_dokumenta") + " - " + rs.getString("ime_gosta") + " " + rs.getString("prezime_gosta");
	            row.createCell(4).setCellValue(gost);
	            row.createCell(5).setCellValue(rs.getString("ime_i_prezime") == null || rs.getString("ime_i_prezime").isBlank() ? "-" : rs.getString("ime_i_prezime")
	            );

	            row.createCell(6).setCellValue(rs.getInt("broj_gostiju"));
	            row.createCell(7).setCellValue(rs.getDouble("ukupna_cijena"));
	            row.createCell(8).setCellValue(rs.getString("opis_apartmana"));
	            row.createCell(9).setCellValue(rs.getInt("kapacitet"));
	            row.createCell(10).setCellValue(rs.getDouble("cijena_nocenja"));
	            row.createCell(11).setCellValue(rs.getString("lokacija"));
	            row.createCell(12).setCellValue(dateFormat.format(rs.getDate("datum_dolaska").toLocalDate()));
	            row.createCell(13).setCellValue(dateFormat.format(rs.getDate("datum_odlaska").toLocalDate()));
	            row.createCell(14).setCellValue(rs.getString("korisnicko_ime"));

	            for (int i = 0; i < headers.length; i++) {
	                if (row.getCell(i) != null) {
	                    row.getCell(i).setCellStyle(cellStyle);
	                }
	            }
	        }

	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        String fileName = "Izvjestaj_racuni_" + System.currentTimeMillis() + ".xlsx";
	        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
	            wb.write(fileOut);
	        }
	        wb.close();
	        JOptionPane.showMessageDialog(frame, "Izvještaj uspješno generiran: " + fileName);

	        try {
	            Desktop.getDesktop().open(new File(fileName));
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(frame, "Datoteka je spremljena, ali nije moguće automatski otvoriti: " + ex.getMessage());
	        }

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(frame, "Greška pri generiranju izvještaja: " + e.getMessage());
	    }
	}
	
}
