package zavrsniRad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.BorderStyle;

public class GostPopis {

	private JFrame frame;
	private JTextField txtPretraziteGoste;
	private JXTable table;
	private DefaultTableModel model;
	private int selectedRow = -1;
	private int selectedId = -1;
	private GetterSetter korisnik;

	public GostPopis(GetterSetter korisnik) {
		this.korisnik = korisnik;
		initialize();
		ucitajGoste();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Upravljanje gostima");
		frame.setBounds(100, 100, 842, 594);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Upravljanje gostima");
		lblNaslov.setBackground(new Color(128, 255, 255));
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNaslov.setBounds(288, 94, 358, 67);
		frame.getContentPane().add(lblNaslov);

		JSeparator separator = new JSeparator();
		separator.setBounds(37, 16, 751, 17);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(37, 94, 751, 19);
		frame.getContentPane().add(separator_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 210, 751, 220);
		frame.getContentPane().add(scrollPane);

		model = new DefaultTableModel(new String[] { "ID gosta", "Ime", "Prezime", "Datum rođenja", "Adresa gosta", "Državljanstvo", "Dokument", "Broj dokumenta", "Broj telefona" }, 0) {
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
		btnDodaj.setBounds(227, 440, 180, 40);
		btnDodaj.setBackground(new Color(0, 111, 128));
		btnDodaj.setForeground(Color.WHITE);
		btnDodaj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnDodaj);

		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GostDodaj(korisnik, GostPopis.this);
			}
		});

		JButton btnAzuriraj = new JButton("Ažuriraj");
		btnAzuriraj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAzuriraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Niste odabrali gosta za ažuriranje!");
					return;
				}

				String ime = (String) model.getValueAt(selectedRow, 1);
				String prezime = (String) model.getValueAt(selectedRow, 2);
				String datum = (String) model.getValueAt(selectedRow, 3);
				String adresa = (String) model.getValueAt(selectedRow, 4);
				String drzavljanstvo = (String) model.getValueAt(selectedRow, 5);
				String dokument = (String) model.getValueAt(selectedRow, 6);
				String brojDokumenta = (String) model.getValueAt(selectedRow, 7);
				String telefon = (String) model.getValueAt(selectedRow, 8);

				new GostAzuriraj(korisnik, GostPopis.this, selectedId, ime, prezime, datum, adresa, drzavljanstvo, dokument, brojDokumenta, telefon);
			}
		});
		btnAzuriraj.setBounds(417, 440, 180, 40);
		btnAzuriraj.setBackground(new Color(0, 111, 128));
		btnAzuriraj.setForeground(Color.WHITE);
		btnAzuriraj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnAzuriraj);

		JButton btnObrisi = new JButton("Obriši");
		btnObrisi.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obrisiGosta();
			}
		});
		btnObrisi.setBounds(608, 440, 180, 40);
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

		JButton btnIzbornik = new JButton("Apartmani");
		btnIzbornik.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnIzbornik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ApartmanPopis(korisnik);
				frame.dispose();
			}
		});
		btnIzbornik.setBounds(227, 27, 180, 57);
		btnIzbornik.setBackground(new Color(102, 153, 153));
		btnIzbornik.setForeground(Color.WHITE);
		frame.getContentPane().add(btnIzbornik);

		JButton btnBoravci = new JButton("Rezervacije");
		btnBoravci.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnBoravci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RezervacijaPopis(korisnik);
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
					JOptionPane.showMessageDialog(null, "Niste odabrali gosta za pregled!");
					return;
				}
				String ime = (String) model.getValueAt(selectedRow, 1);
				String prezime = (String) model.getValueAt(selectedRow, 2);
				String datum = (String) model.getValueAt(selectedRow, 3);
				String adresa = (String) model.getValueAt(selectedRow, 4);
				String drzavljanstvo = (String) model.getValueAt(selectedRow, 5);
				String dokument = (String) model.getValueAt(selectedRow, 6);
				String brojDokumenta = (String) model.getValueAt(selectedRow, 7);
				String telefon = (String) model.getValueAt(selectedRow, 8);

				new GostPregled(korisnik, GostPopis.this, selectedId, ime, prezime, datum, adresa, drzavljanstvo, dokument, brojDokumenta, telefon);

			}
		});
		btnNewButton_1.setBounds(37, 441, 180, 40);
		btnNewButton_1.setBackground(new Color(0, 111, 128));
		btnNewButton_1.setForeground(Color.WHITE);

		btnNewButton_1.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnNewButton_1);

		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(37, 491, 751, 21);
		frame.getContentPane().add(separator_1_1);

		JButton btnNewButton_2 = new JButton("Generiraj izvještaj");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generirajIzvjestaj();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_2.setBounds(37, 502, 751, 40);
		btnNewButton_2.setBackground(new Color(102, 153, 153));
		btnNewButton_2.setForeground(Color.WHITE);
		frame.getContentPane().add(btnNewButton_2);

		txtPretraziteGoste = new JTextField();
		txtPretraziteGoste.setText("Pretražite goste");
		txtPretraziteGoste.setForeground(Color.GRAY);
		txtPretraziteGoste.setBorder(null);

		txtPretraziteGoste.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent e) {
				if (txtPretraziteGoste.getText().equals("Pretražite goste")) {
					txtPretraziteGoste.setText("");
					txtPretraziteGoste.setForeground(Color.BLACK);
				}
			}

			public void focusLost(java.awt.event.FocusEvent e) {
				if (txtPretraziteGoste.getText().isEmpty()) {
					txtPretraziteGoste.setForeground(Color.GRAY);
					txtPretraziteGoste.setText("Pretražite goste");
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
				txtPretraziteGoste.setText("");
				txtPretraziteGoste.requestFocus();
				sorter.setRowFilter(null);
			}
		});

		JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
		searchPanel.setBounds(137, 157, 541, 32);
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		searchPanel.add(searchLabel, BorderLayout.WEST);
		searchPanel.add(txtPretraziteGoste, BorderLayout.CENTER);
		searchPanel.add(clearLabel, BorderLayout.EAST);
		frame.getContentPane().add(searchPanel);

		txtPretraziteGoste.setColumns(10);

		txtPretraziteGoste.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				String searchText = txtPretraziteGoste.getText().trim().toLowerCase();
				if (searchText.length() == 0 || searchText.equals("pretražite goste")) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
				}
			}
		});
	}

	public void ucitajGoste() {
		model.setRowCount(0);

		String sql = "SELECT id_gosta, ime_gosta, prezime_gosta, datum_rodenja, adresa_prebivalista, drzavljanstvo, vrsta_dokumenta, broj_dokumenta, broj_telefona FROM Gost WHERE id_iznajmljivaca = ?";

		try (Connection con = Database.getConnection(); PreparedStatement psmt = con.prepareStatement(sql)) {

			psmt.setInt(1, korisnik.getId_korisnika());

			try (ResultSet rs = psmt.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("id_gosta");
					String ime = rs.getString("ime_gosta");
					String prezime = rs.getString("prezime_gosta");

					java.sql.Date sqlDate = rs.getDate("datum_rodenja");
					String datum = sqlDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

					String adresa = rs.getString("adresa_prebivalista");
					String drzavljanstvo = rs.getString("drzavljanstvo");
					String vrstaDokumenta = rs.getString("vrsta_dokumenta");
					String brojDokumenta = rs.getString("broj_dokumenta");
					String telefon = rs.getString("broj_telefona");

					model.addRow(new Object[] { id, ime, prezime, datum, adresa, drzavljanstvo, vrstaDokumenta, brojDokumenta, telefon });
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Greška pri učitavanju gostiju iz baze: " + e.getMessage());
		}
	}

	private void obrisiGosta() {
		if (selectedRow == -1 || selectedId == -1) {
			JOptionPane.showMessageDialog(null, "Niste odabrali gosta za brisanje!");
			return;
		}

		int potvrda = JOptionPane.showConfirmDialog(null, "Jeste li sigurni da želite obrisati gosta?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

		if (potvrda != JOptionPane.YES_OPTION)
			return;

		String sql = "DELETE FROM Gost WHERE id_gosta = ? AND id_iznajmljivaca = ?";

		try (Connection con = Database.getConnection(); PreparedStatement psmt = con.prepareStatement(sql)) {

			psmt.setInt(1, selectedId);
			psmt.setInt(2, korisnik.getId_korisnika());

			int affectedRows = psmt.executeUpdate();

			if (affectedRows > 0) {
				JOptionPane.showMessageDialog(null, "Gost uspješno obrisan.");
				ucitajGoste();
				selectedId = -1;
				selectedRow = -1;
			} else {
				JOptionPane.showMessageDialog(null, "Brisanje nije uspjelo. Gost možda nije pronađen.");
			}

		} catch (Exception e) {

			if (e.getMessage().toLowerCase().contains("foreign key") || e.getMessage().toLowerCase().contains("constraint fails")) {

				JOptionPane.showMessageDialog(null, "Gost se ne može obrisati jer je povezan s jednom ili više rezervacija.", "Brisanje nije moguće", JOptionPane.WARNING_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(null, "Greška pri brisanju gosta: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void generirajIzvjestaj() {
		String sql = "SELECT ime_gosta, prezime_gosta, datum_rodenja, adresa_prebivalista, drzavljanstvo, vrsta_dokumenta, broj_dokumenta, broj_telefona FROM Gost WHERE id_iznajmljivaca = ?";

		try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, korisnik.getId_korisnika());
			ResultSet rs = ps.executeQuery();

			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("Gosti");

			CellStyle headerStyle = wb.createCellStyle();
			org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
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
			org.apache.poi.ss.usermodel.Font dateFont = wb.createFont();
			dateFont.setBold(true);
			dateStyle.setFont(dateFont);

			Row dateRow = sheet.createRow(0);
			Cell dateCell = dateRow.createCell(0);
			DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
			dateCell.setCellValue("Izvještaj: " + dateTimeFormat.format(LocalDateTime.now()));
			dateCell.setCellStyle(dateStyle);

			Row header = sheet.createRow(1);
			String[] headers = { "Ime", "Prezime", "Datum rođenja", "Adresa", "Državljanstvo", "Dokument", "Broj Dokumenta", "Telefon" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = header.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			while (rs.next()) {
				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(rs.getString("ime_gosta"));
				row.createCell(1).setCellValue(rs.getString("prezime_gosta"));

				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				LocalDate datumRod = rs.getDate("datum_rodenja").toLocalDate();
				row.createCell(2).setCellValue(dateFormat.format(datumRod));

				row.createCell(3).setCellValue(rs.getString("adresa_prebivalista"));
				row.createCell(4).setCellValue(rs.getString("drzavljanstvo"));
				row.createCell(5).setCellValue(rs.getString("vrsta_dokumenta"));
				row.createCell(6).setCellValue(rs.getString("broj_dokumenta"));
				row.createCell(7).setCellValue(rs.getString("broj_telefona"));

				for (int i = 0; i < 8; i++) {
					row.getCell(i).setCellStyle(cellStyle);
				}
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			String fileName = "Izvjestaj_gosti_" + System.currentTimeMillis() + ".xlsx";
			FileOutputStream fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
			wb.close();

			JOptionPane.showMessageDialog(frame, "Izvještaj uspješno generiran: " + fileName);

			try {
				java.awt.Desktop.getDesktop().open(new java.io.File(fileName));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Izvještaj je spremljen, ali ga nije moguće automatski otvoriti: " + ex.getMessage());
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška pri generiranju izvještaja: " + e.getMessage());
		}
	}

}
