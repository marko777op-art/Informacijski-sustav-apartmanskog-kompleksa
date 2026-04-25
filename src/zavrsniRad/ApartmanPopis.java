package zavrsniRad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import java.util.ArrayList;
import java.util.List;
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

public class ApartmanPopis {

	private JFrame frame;
	private JXTable table;
	private DefaultTableModel model;
	private int selectedRow = -1;
	private int selectedId = -1;
	private GetterSetter korisnik;
	private JTextField txtPretraziteApartmane;

	public ApartmanPopis(GetterSetter korisnik) {
		this.korisnik = korisnik;
		initialize();
		ucitajApartmane();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();

		frame.setTitle("APARTMANSKI SUSTAV - Upravljanje apartmanima");

		frame.setBounds(100, 100, 840, 591);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Upravljanje apartmanima");
		lblNaslov.setBackground(new Color(128, 255, 255));
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNaslov.setBounds(251, 92, 358, 67);
		frame.getContentPane().add(lblNaslov);

		JSeparator separator = new JSeparator();
		separator.setBounds(37, 15, 751, 2);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(37, 94, 751, 17);
		frame.getContentPane().add(separator_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 205, 751, 220);
		frame.getContentPane().add(scrollPane);

		model = new DefaultTableModel(
				new String[] { "ID apartmana", "Opis apartmana", "Kapacitet", "Cijena noćenja (€)", "Lokacija" }, 0) {
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
		btnDodaj.setBounds(227, 436, 180, 40);
		btnDodaj.setBackground(new Color(0, 111, 128));
		btnDodaj.setForeground(Color.WHITE);

		btnDodaj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnDodaj);

		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ApartmanDodaj(korisnik, ApartmanPopis.this);

			}
		});

		JButton btnAzuriraj = new JButton("Ažuriraj");
		btnAzuriraj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAzuriraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Niste odabrali apartman za ažuriranje!");
					return;
				}

				String opisApartmana = (String) model.getValueAt(selectedRow, 1);
				int kapacitet = (int) model.getValueAt(selectedRow, 2);
				double cijenaNocenja = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());
				String lokacija = (String) model.getValueAt(selectedRow, 4);

				new ApartmanAzuriraj(korisnik, ApartmanPopis.this, selectedId, opisApartmana, kapacitet, cijenaNocenja, lokacija);
			}
		});
		btnAzuriraj.setBounds(417, 436, 180, 40);
		btnAzuriraj.setBackground(new Color(0, 111, 128));
		btnAzuriraj.setForeground(Color.WHITE);

		btnAzuriraj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnAzuriraj);

		JButton btnObrisi = new JButton("Obriši");
		btnObrisi.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obrisiApartman();
			}
		});
		btnObrisi.setBounds(608, 436, 180, 40);
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
				frame.dispose();
			}
		});
		btnIzbornik.setBounds(227, 27, 180, 57);
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
		btnRezervacije.setBounds(417, 27, 180, 57);
		btnRezervacije.setBackground(new Color(102, 153, 153));
		btnRezervacije.setForeground(Color.WHITE);
		frame.getContentPane().add(btnRezervacije);

		JButton btnRacuni = new JButton("Računi");
		btnRacuni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RacunPopis(korisnik);
				frame.dispose();
			}
		});
		btnRacuni.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnRacuni.setBounds(608, 27, 180, 57);
		btnRacuni.setBackground(new Color(102, 153, 153));
		btnRacuni.setForeground(Color.WHITE);
		frame.getContentPane().add(btnRacuni);

		JButton btnPregledaj = new JButton("Pregledaj");
		btnPregledaj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnPregledaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Niste odabrali apartman za pregled!");
					return;
				}
				String opisApartmana = (String) model.getValueAt(selectedRow, 1);
				int kapacitet = (int) model.getValueAt(selectedRow, 2);
				double cijenaNocenja = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());
				String lokacija = (String) model.getValueAt(selectedRow, 4);

				new ApartmanPregled(korisnik, ApartmanPopis.this, selectedId, opisApartmana, kapacitet, cijenaNocenja, lokacija);

			}
		});
		btnPregledaj.setBounds(37, 436, 180, 40);

		btnPregledaj.setBackground(new Color(0, 111, 128));
		btnPregledaj.setForeground(Color.WHITE);
		btnPregledaj.setHorizontalTextPosition(SwingConstants.LEFT);
		frame.getContentPane().add(btnPregledaj);

		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(37, 486, 751, 21);
		frame.getContentPane().add(separator_1_1);

		JButton btnIzvjestaj = new JButton("Generiraj izvještaj");
		btnIzvjestaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generirajIzvjestaj();
			}
		});
		btnIzvjestaj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnIzvjestaj.setBounds(37, 497, 751, 40);
		btnIzvjestaj.setBackground(new Color(102, 153, 153));
		btnIzvjestaj.setForeground(Color.WHITE);
		frame.getContentPane().add(btnIzvjestaj);

		txtPretraziteApartmane = new JTextField();
		txtPretraziteApartmane.setText("Pretražite apartmane");
		txtPretraziteApartmane.setForeground(Color.GRAY);
		txtPretraziteApartmane.setBorder(null);

		txtPretraziteApartmane.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent e) {
				if (txtPretraziteApartmane.getText().equals("Pretražite apartmane")) {
					txtPretraziteApartmane.setText("");
					txtPretraziteApartmane.setForeground(Color.BLACK);
				}
			}

			public void focusLost(java.awt.event.FocusEvent e) {
				if (txtPretraziteApartmane.getText().isEmpty()) {
					txtPretraziteApartmane.setForeground(Color.GRAY);
					txtPretraziteApartmane.setText("Pretražite apartmane");
				}
			}
		});

		JLabel clearLabel = new JLabel();
		JLabel searchLabel = new JLabel();

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
				txtPretraziteApartmane.setText("");
				txtPretraziteApartmane.requestFocus();
				sorter.setRowFilter(null);
			}
		});

		JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
		searchPanel.setBounds(139, 156, 541, 32);
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
				if (searchText.length() == 0 || searchText.equals("pretražite apartmane")) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
				}
			}
		});
	}

	public void ucitajApartmane() {
		model.setRowCount(0);

		String sql = "SELECT id_apartmana, opis_apartmana, kapacitet, cijena_nocenja, lokacija FROM Apartman WHERE id_iznajmljivaca = ?";

		try (Connection con = Database.getConnection(); PreparedStatement psmt = con.prepareStatement(sql)) {

			psmt.setInt(1, korisnik.getId_korisnika());

			try (ResultSet rs = psmt.executeQuery()) {
				while (rs.next()) {
					int idApartmana = rs.getInt("id_apartmana");
					String opisApartmana = rs.getString("opis_apartmana");
					int kapacitet = rs.getInt("kapacitet");
					double cijenaNocenja = rs.getDouble("cijena_nocenja");
					String cijenaFormatted = String.format(java.util.Locale.ENGLISH, "%.2f", cijenaNocenja);
					String lokacija = rs.getString("lokacija");

					model.addRow(new Object[] { idApartmana, opisApartmana, kapacitet, cijenaFormatted, lokacija });
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Greška pri učitavanju apartmana iz baze: " + e.getMessage());
		}
	}

	private void obrisiApartman() {
		if (selectedRow == -1 || selectedId == -1) {
			JOptionPane.showMessageDialog(null, "Niste odabrali apartman za brisanje!");
			return;
		}

		int potvrda = JOptionPane.showConfirmDialog(null, "Jeste li sigurni da želite obrisati apartman?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);

		if (potvrda != JOptionPane.YES_OPTION)
			return;

		try (Connection con = Database.getConnection()) {

			List<String> slikeZaBrisanje = new ArrayList<>();
			PreparedStatement psSlike = con
					.prepareStatement("SELECT putanja_slike FROM Apartman_slika WHERE id_apartmana = ?");
			psSlike.setInt(1, selectedId);
			ResultSet rs = psSlike.executeQuery();

			while (rs.next()) {
				String putanja = rs.getString("putanja_slike");
				PreparedStatement psProvjera = con.prepareStatement("SELECT COUNT(*) FROM Apartman_slika WHERE putanja_slike = ?");
				psProvjera.setString(1, putanja);
				ResultSet rsProvjera = psProvjera.executeQuery();

				if (rsProvjera.next() && rsProvjera.getInt(1) == 1) {
					slikeZaBrisanje.add(putanja);
				}

				rsProvjera.close();
				psProvjera.close();
			}

			rs.close();
			psSlike.close();

			PreparedStatement psBrisiSlikeZapise = con.prepareStatement("DELETE FROM Apartman_slika WHERE id_apartmana = ?");
			psBrisiSlikeZapise.setInt(1, selectedId);
			psBrisiSlikeZapise.executeUpdate();
			psBrisiSlikeZapise.close();

			PreparedStatement psBrisiApartman = con.prepareStatement("DELETE FROM Apartman WHERE id_apartmana = ? AND id_iznajmljivaca = ?");
			psBrisiApartman.setInt(1, selectedId);
			psBrisiApartman.setInt(2, korisnik.getId_korisnika());

			int affectedRows = psBrisiApartman.executeUpdate();
			psBrisiApartman.close();

			for (String putanja : slikeZaBrisanje) {
				File slikaFile = new File(putanja);
				if (slikaFile.exists()) {
					slikaFile.delete();
				}
			}

			if (affectedRows > 0) {
				JOptionPane.showMessageDialog(null, "Apartman uspješno obrisan.");
				ucitajApartmane();
				selectedId = -1;
				selectedRow = -1;
			} else {
				JOptionPane.showMessageDialog(null, "Brisanje nije uspjelo. Možda apartman nije pronađen.");
			}

		} catch (Exception e) {
			if (e.getMessage().contains("foreign key constraint fails")) {
				JOptionPane.showMessageDialog(null,
						"Apartman se ne može obrisati jer je povezan s jednom ili više rezervacija.", "Brisanje nije moguće", JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Greška pri brisanju apartmana: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void generirajIzvjestaj() {
		String sql = "SELECT opis_apartmana, kapacitet, cijena_nocenja, lokacija FROM Apartman WHERE id_iznajmljivaca = ?";

		try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, korisnik.getId_korisnika());
			ResultSet rs = ps.executeQuery();

			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("Apartmani");

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
			String[] headers = { "Opis apartmana", "Kapacitet", "Cijena noćenja", "Lokacija" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = header.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			while (rs.next()) {
				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(rs.getString("opis_apartmana"));
				row.createCell(1).setCellValue(rs.getInt("kapacitet"));
				row.createCell(2).setCellValue(rs.getDouble("cijena_nocenja"));
				row.createCell(3).setCellValue(rs.getString("lokacija"));

				for (int i = 0; i < 4; i++) {
					row.getCell(i).setCellStyle(cellStyle);
				}
			}
			
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}
			String fileName = "Izvjestaj_apartmani_" + System.currentTimeMillis() + ".xlsx";
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
