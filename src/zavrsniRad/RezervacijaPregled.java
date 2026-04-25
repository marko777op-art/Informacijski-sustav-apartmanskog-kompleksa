package zavrsniRad;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;


public class RezervacijaPregled {
	private JFrame frame;
	private JXTable table;
	private DefaultTableModel model;
	private GetterSetter korisnik;

	private JLabel lblNewLabel_5, lblNewLabel_3, lblNewLabel_5_1, lblNewLabel_5_1_1, lblNewLabel_5_1_2, lblNewLabel_5_1_3, lblNewLabel_5_1_3_1, lblUkupnaCijena_2;
	private JTextArea txtrImeIPrezime;
	private JScrollPane scrollPane;

	private List<ApartmanNaRezervaciji> dodaniApartmani = new ArrayList<>();
	public List<ApartmanNaRezervaciji> getDodaniApartmani() {
		return dodaniApartmani;
	}

	public RezervacijaPregled(GetterSetter korisnik, int selectedId) {
		this.korisnik = korisnik;

		initialize();
		ucitajRezervaciju(selectedId);
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame("Pregled rezervacije");
		frame.setTitle("APARTMANSKI SUSTAV - Pregled rezervacije");
		frame.setBounds(100, 100, 833, 519);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("PREGLED REZERVACIJE");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(283, 23, 308, 30);
		frame.getContentPane().add(lblNaslov);

		JLabel lblGost = new JLabel("Rezervirao/la:");
		lblGost.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblGost.setBounds(37, 124, 115, 28);
		frame.getContentPane().add(lblGost);

		JLabel lblBrojGostiju = new JLabel("Broj gostiju:");
		lblBrojGostiju.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblBrojGostiju.setBounds(37, 238, 150, 28);
		frame.getContentPane().add(lblBrojGostiju);

		model = new DefaultTableModel(
				new String[] { "ID apartmana", "Opis apartmana", "Kapacitet", "Lokacija", "Cijena noćenja (€)" }, 0) {
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
		scrollPane.setBounds(36, 320, 740, 88);
		scrollPane.setViewportView(table);

		frame.getContentPane().add(scrollPane);
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);

		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setWidth(0);

		JLabel lblUkupnaCijena = new JLabel("Ukupan iznos (€):");
		lblUkupnaCijena.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblUkupnaCijena.setBounds(37, 276, 180, 28);
		frame.getContentPane().add(lblUkupnaCijena);

		JButton btnSpremi = new JButton("Natrag");
		btnSpremi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnSpremi.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSpremi.setBounds(416, 426, 360, 40);
		btnSpremi.setBackground(new Color(0, 111, 128));
		btnSpremi.setForeground(Color.WHITE);

		frame.getContentPane().add(btnSpremi);

		JLabel lblNewLabel_4_1 = new JLabel("Iznajmljivač:");
		lblNewLabel_4_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_1.setBounds(409, 86, 129, 29);
		frame.getContentPane().add(lblNewLabel_4_1);

		JLabel lblNewLabel_4_2 = new JLabel("ID rezervacije:");
		lblNewLabel_4_2.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_2.setBounds(37, 86, 160, 28);
		frame.getContentPane().add(lblNewLabel_4_2);

		JLabel lblNewLabel = new JLabel("Datum dolaska:");
		lblNewLabel.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel.setBounds(37, 162, 115, 28);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Datum odlaska:");
		lblNewLabel_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_1.setBounds(37, 200, 115, 28);
		frame.getContentPane().add(lblNewLabel_1);

		JSeparator separator = new JSeparator();
		separator.setBounds(27, 63, 749, 18);
		frame.getContentPane().add(separator);

		JLabel lblNewLabel_2 = new JLabel("Ostali gosti:");
		lblNewLabel_2.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_2.setBounds(409, 162, 150, 28);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_4_2_1 = new JLabel("Datum rezervacije:");
		lblNewLabel_4_2_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_2_1.setBounds(409, 124, 150, 28);
		frame.getContentPane().add(lblNewLabel_4_2_1);

		txtrImeIPrezime = new JTextArea();

		txtrImeIPrezime.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		txtrImeIPrezime.setText("ime i prezime");
		txtrImeIPrezime.setBounds(536, 162, 240, 142);

		txtrImeIPrezime.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		txtrImeIPrezime.setBackground(frame.getContentPane().getBackground());
		txtrImeIPrezime.setOpaque(true);
		txtrImeIPrezime.setEditable(false);

		frame.getContentPane().add(txtrImeIPrezime);

		lblNewLabel_5 = new JLabel("id rezervacije");
		lblNewLabel_5.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(143, 86, 246, 28);

		lblNewLabel_5.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5);

		lblNewLabel_3 = new JLabel("broj gostiju");
		lblNewLabel_3.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_3.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		lblNewLabel_3.setBounds(143, 238, 246, 28);
		frame.getContentPane().add(lblNewLabel_3);

		lblNewLabel_5_1 = new JLabel("datum rezervacije");
		lblNewLabel_5_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1.setBounds(536, 124, 240, 28);
		lblNewLabel_5_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1);

		lblNewLabel_5_1_1 = new JLabel("korisnicko ime");
		lblNewLabel_5_1_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_1.setBounds(536, 86, 240, 28);
		lblNewLabel_5_1_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_1);

		lblNewLabel_5_1_2 = new JLabel("broj dok - ime prezime");
		lblNewLabel_5_1_2.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_2.setBounds(143, 124, 246, 28);
		lblNewLabel_5_1_2.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_2);

		lblNewLabel_5_1_3 = new JLabel("datum dolaska");
		lblNewLabel_5_1_3.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_3.setBounds(143, 162, 246, 28);
		lblNewLabel_5_1_3.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_3);

		lblNewLabel_5_1_3_1 = new JLabel("datum odlaska");
		lblNewLabel_5_1_3_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_3_1.setBounds(143, 200, 246, 28);
		lblNewLabel_5_1_3_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_3_1);

		lblUkupnaCijena_2 = new JLabel("ukupna cijena");
		lblUkupnaCijena_2.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblUkupnaCijena_2.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true),	new EmptyBorder(0, 3, 0, 0)));
		lblUkupnaCijena_2.setBounds(143, 277, 246, 28);
		frame.getContentPane().add(lblUkupnaCijena_2);

		JButton btnGenerirajRacun = new JButton("Generiraj račun");
		btnGenerirajRacun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generirajRacun();
			}
		});
		btnGenerirajRacun.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnGenerirajRacun.setBackground(new Color(0, 111, 128));
		btnGenerirajRacun.setForeground(Color.WHITE);
		btnGenerirajRacun.setBounds(37, 426, 360, 40);
		frame.getContentPane().add(btnGenerirajRacun);
	}

	private void ucitajRezervaciju(int id) {
		try (Connection con = Database.getConnection()) {

			String sql = """
					    SELECT *
					    FROM Stavka_rezervacije
					    WHERE id_rezervacije = ? AND id_iznajmljivaca = ?
					""";

			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, id);
				ps.setInt(2, korisnik.getId_korisnika());

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						lblNewLabel_5.setText(String.valueOf(rs.getInt("id_rezervacije")));
						lblNewLabel_5_1_1.setText(rs.getString("korisnicko_ime"));
						lblNewLabel_5_1_2.setText(rs.getString("broj_dokumenta") + " - " + rs.getString("ime_gosta")
								+ " " + rs.getString("prezime_gosta"));
						lblNewLabel_5_1.setText(rs.getDate("datum_rezervacije").toLocalDate()
								.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
						lblNewLabel_3.setText(String.valueOf(rs.getInt("broj_gostiju")));
						lblNewLabel_5_1_3.setText(rs.getDate("datum_dolaska").toLocalDate()
								.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
						lblNewLabel_5_1_3_1.setText(rs.getDate("datum_odlaska").toLocalDate()
								.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
						lblUkupnaCijena_2.setText(String.format(Locale.ENGLISH, "%.2f", rs.getDouble("ukupna_cijena")));

						String ostali = rs.getString("ime_i_prezime");
						if (ostali == null || ostali.isBlank()) {
							txtrImeIPrezime.setText("Nema dodatnih gostiju.");
						} else {

							String prikaz = ostali.replaceAll(",\\s*", "\n").trim();
							txtrImeIPrezime.setText(prikaz);

						}

						model.setRowCount(0);
						model.addRow(new Object[] { rs.getInt("id_stavke_rezervacije"), rs.getString("opis_apartmana"),
								rs.getInt("kapacitet"), rs.getString("lokacija"),
								String.format(Locale.ENGLISH, "%.2f", rs.getDouble("cijena_nocenja")) });
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Greška pri učitavanju: " + e.getMessage());
		}
	}

	private void generirajRacun() {
		int idRezervacije = Integer.parseInt(lblNewLabel_5.getText().trim());
		int idIznajmljivaca = korisnik.getId_korisnika();

		try (Connection con = Database.getConnection()) {

			String provjera = "SELECT COUNT(*) FROM Racun WHERE id_rezervacije = ? AND id_iznajmljivaca = ?";
			try (PreparedStatement ps = con.prepareStatement(provjera)) {
				ps.setInt(1, idRezervacije);
				ps.setInt(2, idIznajmljivaca);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						JOptionPane.showMessageDialog(frame, "Račun za ovu rezervaciju već postoji!");
						return;
					}
				}
			}

			String sql = """
					    INSERT INTO Racun (id_iznajmljivaca, id_rezervacije, datum_racuna)
					    VALUES (?, ?, ?)
					""";
			try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, idIznajmljivaca);
				ps.setInt(2, idRezervacije);
				ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
				ps.executeUpdate();

				int idRacuna = -1;
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						idRacuna = rs.getInt(1);
					}
				}

				if (idRacuna > 0) {
					if (idRacuna > 0) {
						
						JOptionPane.showMessageDialog(frame, "Račun uspješno generiran!");

						String sqlSnapshot = """
								    INSERT INTO Stavka_racuna (
								        id_iznajmljivaca, id_racuna, datum_racuna,
								        datum_rezervacije, broj_gostiju, datum_dolaska, datum_odlaska,
								        ukupna_cijena, broj_dokumenta, ime_gosta, prezime_gosta,
								        kapacitet, lokacija, cijena_nocenja, opis_apartmana,
								        ime_i_prezime, korisnicko_ime, id_rezervacije
								    )
								    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
								""";

						try (PreparedStatement psSnap = con.prepareStatement(sqlSnapshot)) {
							psSnap.setInt(1, idIznajmljivaca);
							psSnap.setInt(2, idRacuna);
							psSnap.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
							psSnap.setDate(4, java.sql.Date.valueOf(LocalDate.parse(lblNewLabel_5_1.getText(),
									DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
							psSnap.setInt(5, Integer.parseInt(lblNewLabel_3.getText().trim()));
							psSnap.setDate(6, java.sql.Date.valueOf(LocalDate.parse(lblNewLabel_5_1_3.getText(),
									DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
							psSnap.setDate(7, java.sql.Date.valueOf(LocalDate.parse(lblNewLabel_5_1_3_1.getText(),
									DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
							psSnap.setDouble(8, Double.parseDouble(lblUkupnaCijena_2.getText().replace(",", "."))); 
																													

							psSnap.setString(9, lblNewLabel_5_1_2.getText().split(" - ")[0]);
							String[] imePrezime = lblNewLabel_5_1_2.getText().split(" - ")[1].split(" ", 2);
							psSnap.setString(10, imePrezime[0]);
							psSnap.setString(11, imePrezime.length > 1 ? imePrezime[1] : "");

							psSnap.setInt(12, (Integer) model.getValueAt(0, 2));
							psSnap.setString(13, (String) model.getValueAt(0, 3));
							psSnap.setDouble(14, Double.parseDouble(((String) model.getValueAt(0, 4)).replace(",", ".")));
							psSnap.setString(15, (String) model.getValueAt(0, 1));
							psSnap.setString(16, txtrImeIPrezime.getText().replace("\n", ", ")); 
																									
							psSnap.setString(17, lblNewLabel_5_1_1.getText());
							psSnap.setInt(18, idRezervacije);

							psSnap.executeUpdate();
						}

						new RacunPregled(korisnik, idRezervacije);
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(frame, "Greška: račun nije generiran!");
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Greška pri generiranju računa: " + ex.getMessage());
		}
	}
}
