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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.BorderStyle;

public class RezervacijaPopis {

	private JFrame frame;
	private JXTable table;
	private DefaultTableModel model;
	private int selectedRow = -1;
	private int selectedId = -1;
	private GetterSetter korisnik;
	private JTextField txtPretraziteRezervacije;
	
	public RezervacijaPopis(GetterSetter korisnik) {
		this.korisnik=korisnik;
		initialize();
		ucitajRezervacije();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Upravljanje rezervacijama");
		frame.setBounds(100, 100, 840, 594);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Upravljanje rezervacijama");
		lblNaslov.setBackground(new Color(128, 255, 255));
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNaslov.setBounds(251, 92, 358, 67);
		frame.getContentPane().add(lblNaslov);

		JSeparator separator = new JSeparator();
		separator.setBounds(37, 17, 751, 17);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(37, 94, 751, 21);
		frame.getContentPane().add(separator_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 203, 751, 220);
		frame.getContentPane().add(scrollPane);

		model = new DefaultTableModel(new String[] { "ID rezervacije", "Datum rezervacije", "Gost", "Datum dolaska", "Datum odlaska", "Broj gostiju", "Ukupna cijena (€)"}, 0) {
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

		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnDodaj.setBounds(227, 435, 180, 40);
		btnDodaj.setBackground(new Color(0, 111, 128));
		btnDodaj.setForeground(Color.WHITE);
		btnDodaj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnDodaj);

		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RezervacijaDodaj(korisnik, RezervacijaPopis.this);
				
			}
		});
		JButton btnAzuriraj = new JButton("Ažuriraj");
		btnAzuriraj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAzuriraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 int selectedRow = table.getSelectedRow();
			        if (selectedRow == -1) {
			            JOptionPane.showMessageDialog(frame, "Molimo odaberite rezervaciju za ažuriranje.");
			            return;
			        }

			        int selectedId = (int) table.getValueAt(selectedRow, 0); 
			        new RezervacijaAzuriraj(korisnik, RezervacijaPopis.this, selectedId);
			      
			    }
		});
		btnAzuriraj.setBounds(417, 435, 180, 40);
		btnAzuriraj.setBackground(new Color(0, 111, 128));
		btnAzuriraj.setForeground(Color.WHITE);
		btnAzuriraj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnAzuriraj);

		JButton btnObrisi = new JButton("Obriši");
		btnObrisi.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obrisiRezervaciju();
				
			}
		});
		btnObrisi.setBounds(608, 435, 180, 40);
		btnObrisi.setBackground(new Color(0, 111, 128));
		btnObrisi.setForeground(Color.WHITE);
		btnObrisi.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnObrisi);

		JButton btnGosti = new JButton("Apartmani");
		btnGosti.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnGosti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ApartmanPopis(korisnik);
				frame.dispose();
			}
		});
		btnGosti.setBounds(227, 27, 180, 57);
		btnGosti.setBackground(new Color(102, 153, 153));
		btnGosti.setForeground(Color.WHITE);

		frame.getContentPane().add(btnGosti);

		JButton btnIzbornik = new JButton("Izbornik");
		btnIzbornik.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnIzbornik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GlavniIzbornik(korisnik);
				frame.dispose();			}
		});
		btnIzbornik.setBounds(37, 27, 180, 57);
		btnIzbornik.setBackground(new Color(102, 153, 153));
		btnIzbornik.setForeground(Color.WHITE);
		frame.getContentPane().add(btnIzbornik);

		JButton btnBoravci = new JButton("Gosti");
		btnBoravci.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnBoravci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GostPopis(korisnik);
				frame.dispose();
			}
		});
		btnBoravci.setBounds(417, 27, 180, 57);
		btnBoravci.setBackground(new Color(102, 153, 153));
		btnBoravci.setForeground(Color.WHITE);
		frame.getContentPane().add(btnBoravci);
		
		JButton btnNewButton = new JButton("Računi");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RacunPopis(korisnik);
				frame.dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setBounds(608, 27, 180, 57);
		btnNewButton.setBackground(new Color(102, 153, 153));
		btnNewButton.setForeground(Color.WHITE);
		frame.getContentPane().add(btnNewButton);
		
			JButton btnNewButton_1 = new JButton("Pregledaj");
			btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (selectedRow == -1) {
					    JOptionPane.showMessageDialog(null, "Niste odabrali rezervaciju za pregled!");
					    return;
					}
					
					new RezervacijaPregled(korisnik, selectedId);
				}
			});
		btnNewButton_1.setBounds(37, 435, 180, 40);
		btnNewButton_1.setBackground(new Color(0, 111, 128));
		btnNewButton_1.setForeground(Color.WHITE);

		btnNewButton_1.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnNewButton_1);
		
		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(37, 488, 751, 21);
		frame.getContentPane().add(separator_1_1);
		
		JButton btnNewButton_2 = new JButton("Generiraj izvještaj");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generirajIzvjestaj();
			
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_2.setBounds(37, 501, 751, 40);
		btnNewButton_2.setBackground(new Color(102, 153, 153));
		btnNewButton_2.setForeground(Color.WHITE);
		frame.getContentPane().add(btnNewButton_2);
		txtPretraziteRezervacije = new JTextField();
		txtPretraziteRezervacije.setText("Pretražite rezervacije");
		txtPretraziteRezervacije.setForeground(Color.GRAY);
		txtPretraziteRezervacije.setBorder(null);

		txtPretraziteRezervacije.addFocusListener(new java.awt.event.FocusAdapter() {
		    public void focusGained(java.awt.event.FocusEvent e) {
		        if (txtPretraziteRezervacije.getText().equals("Pretražite rezervacije")) {
		        	txtPretraziteRezervacije.setText("");
		        	txtPretraziteRezervacije.setForeground(Color.BLACK);
		        }
		    }

		    public void focusLost(java.awt.event.FocusEvent e) {
		        if (txtPretraziteRezervacije.getText().isEmpty()) {
		        	txtPretraziteRezervacije.setForeground(Color.GRAY);
		        	txtPretraziteRezervacije.setText("Pretražite rezervacije");
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
		    ImageIcon clearIcon = new ImageIcon(clearIconURL);
		    NoScalingIcon clearXIcon = new NoScalingIcon(clearIcon);
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
		    	txtPretraziteRezervacije.setText("");
		    	txtPretraziteRezervacije.requestFocus();
		        sorter.setRowFilter(null);
		    }
		});

		JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
		searchPanel.setBounds(143, 156, 541, 32);
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		searchPanel.add(searchLabel, BorderLayout.WEST);
		searchPanel.add(txtPretraziteRezervacije, BorderLayout.CENTER);
		searchPanel.add(clearLabel, BorderLayout.EAST);
		frame.getContentPane().add(searchPanel);
		txtPretraziteRezervacije.setColumns(10);

		txtPretraziteRezervacije.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyReleased(java.awt.event.KeyEvent evt) {
		        String searchText = txtPretraziteRezervacije.getText().trim().toLowerCase();
		        if (searchText.length() == 0 || searchText.equals("pretražite rezervacije")) {
		            sorter.setRowFilter(null);
		        } else {
		            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
		        }
		    }
		});
				}
	public void ucitajRezervacije() {
	    model.setRowCount(0); 
	    String sql = """
	        SELECT r.id_rezervacije, r.datum_rezervacije, 
	               g.broj_dokumenta, g.ime_gosta, g.prezime_gosta, 
	               r.datum_dolaska, r.datum_odlaska, 
	               r.broj_gostiju, r.ukupna_cijena 
	        FROM Rezervacija r 
	        JOIN Gost g ON r.id_gosta = g.id_gosta 
	        WHERE r.id_iznajmljivaca = ?
	    """;

	    try (Connection con = Database.getConnection();
	         PreparedStatement psmt = con.prepareStatement(sql)) {

	        psmt.setInt(1, korisnik.getId_korisnika());

	        try (ResultSet rs = psmt.executeQuery()) {
	            while (rs.next()) {
	                int id = rs.getInt("id_rezervacije");

	                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	                String datum = rs.getDate("datum_rezervacije")
	                                 .toLocalDate()
	                                 .format(dateFormat);

	                String gost = rs.getString("broj_dokumenta") + " - " +
	                              rs.getString("ime_gosta") + " " +
	                              rs.getString("prezime_gosta");

	                String datumDolaska = rs.getDate("datum_dolaska")
	                                        .toLocalDate()
	                                        .format(dateFormat);

	                String datumOdlaska = rs.getDate("datum_odlaska")
	                                        .toLocalDate()
	                                        .format(dateFormat);

	                int brojGostiju = rs.getInt("broj_gostiju");
	                double cijena = rs.getDouble("ukupna_cijena");

	                String cijenaFormatted = String.format(java.util.Locale.ENGLISH, "%.2f", cijena);

	                model.addRow(new Object[]{
	                    id, datum, gost, datumDolaska, datumOdlaska, brojGostiju, cijenaFormatted
	                });
	            }
	        }

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null,
	            "Greška pri učitavanju rezervacija iz baze: " + e.getMessage());
	    }
	}

	
	private void obrisiRezervaciju() {
	    if (selectedRow == -1 || selectedId == -1) {
	        JOptionPane.showMessageDialog(frame, "Molimo odaberite rezervaciju koju želite obrisati.");
	        return;
	    }

	    int potvrda = JOptionPane.showConfirmDialog(
	        frame,
	        "Jeste li sigurni da želite obrisati ovu rezervaciju?",
	        "Potvrda brisanja",
	        JOptionPane.YES_NO_OPTION
	    );
	    if (potvrda != JOptionPane.YES_OPTION) return;

	    try (Connection con = Database.getConnection()) {


	        con.setAutoCommit(false);

	        String provjeriRacun = "SELECT id_racuna FROM Racun WHERE id_rezervacije = ?";
	        try (PreparedStatement ps = con.prepareStatement(provjeriRacun)) {
	            ps.setInt(1, selectedId);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    JOptionPane.showMessageDialog(frame,
	                        "Rezervacija se ne može obrisati jer postoji račun koji je vezan za nju.\n" +
	                        "Prvo obrišite račun.");
	                    con.rollback();
	                    return;
	                }
	            }
	        }

	        String deleteOstali = "DELETE FROM Gost_ostali WHERE id_rezervacije = ?";
	        try (PreparedStatement ps = con.prepareStatement(deleteOstali)) {
	            ps.setInt(1, selectedId);
	            ps.executeUpdate();
	        }

	        String deleteStavkeRez = "DELETE FROM Stavka_rezervacije WHERE id_rezervacije = ?";
	        try (PreparedStatement ps = con.prepareStatement(deleteStavkeRez)) {
	            ps.setInt(1, selectedId);
	            ps.executeUpdate();
	        }
	  
	        String deleteRez = "DELETE FROM Rezervacija WHERE id_rezervacije = ? AND id_iznajmljivaca = ?";
	        try (PreparedStatement ps = con.prepareStatement(deleteRez)) {
	            ps.setInt(1, selectedId);
	            ps.setInt(2, korisnik.getId_korisnika());
	            ps.executeUpdate();
	        }

	        con.commit();

	        JOptionPane.showMessageDialog(frame, "Rezervacija uspješno obrisana.");
	        ucitajRezervacije();

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(frame, "Greška prilikom brisanja rezervacije: " + e.getMessage());
	    }
	}

	private void generirajIzvjestaj() {
	    String sql = """
	        SELECT *
	        FROM Stavka_rezervacije
	        WHERE id_iznajmljivaca = ?
	        ORDER BY id_rezervacije
	    """;

	    try (Connection con = Database.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, korisnik.getId_korisnika());
	        ResultSet rs = ps.executeQuery();


	        Workbook wb = new XSSFWorkbook();
	        Sheet sheet = wb.createSheet("Rezervacije");

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

	        Row dateRow = sheet.createRow(0);
	        Cell dateCell = dateRow.createCell(0);
	        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
	        String formattedDate = LocalDateTime.now().format(dateTimeFormat);
	        
	        dateCell.setCellValue("Izvještaj: " + formattedDate);
	        dateCell.setCellStyle(dateStyle);

	        String[] headers = {
	            "ID rezervacije",
	            "Datum rezervacije",
	            "Iznajmljivač",
	            "Gost (dok. - ime prezime)",
	            "Ostali gosti",
	            "Broj gostiju",
	            "Ukupna cijena (€)",
	            "Opis apartmana",
	            "Kapacitet",
	            "Cijena noćenja (€)",
	            "Lokacija",
	            "Datum dolaska",
	            "Datum odlaska"
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

	            String gost = rs.getString("broj_dokumenta") + " - " +
	                          rs.getString("ime_gosta") + " " + rs.getString("prezime_gosta");

	            row.createCell(0).setCellValue(rs.getInt("id_rezervacije"));
	            row.createCell(1).setCellValue(rs.getDate("datum_rezervacije").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            row.createCell(2).setCellValue(rs.getString("korisnicko_ime"));
	            row.createCell(3).setCellValue(gost);
	            row.createCell(4).setCellValue(
	                rs.getString("ime_i_prezime") == null || rs.getString("ime_i_prezime").isBlank()
	                ? "-"
	                : rs.getString("ime_i_prezime")
	            );
	            row.createCell(5).setCellValue(rs.getInt("broj_gostiju"));
	            row.createCell(6).setCellValue(rs.getDouble("ukupna_cijena"));
	            row.createCell(7).setCellValue(rs.getString("opis_apartmana"));
	            row.createCell(8).setCellValue(rs.getInt("kapacitet"));
	            row.createCell(9).setCellValue(rs.getDouble("cijena_nocenja"));
	            row.createCell(10).setCellValue(rs.getString("lokacija"));
	            row.createCell(11).setCellValue(rs.getDate("datum_dolaska").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            row.createCell(12).setCellValue(rs.getDate("datum_odlaska").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

	            for (int i = 0; i < headers.length; i++) {
	                row.getCell(i).setCellStyle(cellStyle);
	            }
	        }

	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        String fileName = "Izvjestaj_rezervacije_" + System.currentTimeMillis() + ".xlsx";
	        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
	            wb.write(fileOut);
	        }
	        wb.close();

	        JOptionPane.showMessageDialog(frame, "Izvještaj uspješno generiran: " + fileName);

	        try {
	            Desktop.getDesktop().open(new File(fileName));
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(frame, "Izvještaj je spremljen, ali ga nije moguće automatski otvoriti: " + ex.getMessage());
	        }

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(frame, "Greška pri generiranju izvještaja: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

}
