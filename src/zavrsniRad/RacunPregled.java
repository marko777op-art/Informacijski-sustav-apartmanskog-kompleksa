package zavrsniRad;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class RacunPregled {

	private JFrame frame;
	private JXTable table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel_5, lblNewLabel_3, lblNewLabel_5_1, lblNewLabel_5_1_1, lblNewLabel_5_1_2, lblNewLabel_5_1_3, lblNewLabel_5_1_3_1, lblNewLabel_5_1_1_1, lblNewLabel_5_1_1_1_1, lblUkupnaCijena_2;
	private JTextArea txtrImeIPrezime;

	private List<ApartmanNaRezervaciji> dodaniApartmani = new ArrayList<>();

	public List<ApartmanNaRezervaciji> getDodaniApartmani() {
		return dodaniApartmani;
	}

	/**
	 * @wbp.parser.constructor
	 */

	public RacunPregled(GetterSetter korisnik, int idRezervacije) {
		initialize();
		ucitajRezervaciju(idRezervacije, korisnik.getId_korisnika());
		frame.setVisible(true);
	}

	public RacunPregled(GetterSetter korisnik, int idRacuna, boolean poRacunu) {
		initialize();
		ucitajRacunPrekoRacuna(idRacuna, korisnik.getId_korisnika());
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame("Pregled računa");
		frame.setTitle("APARTMANSKI SUSTAV - Pregled računa");
		frame.setBounds(100, 100, 830, 542);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("PREGLED RAČUNA");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(309, 23, 257, 30);
		frame.getContentPane().add(lblNaslov);

		JLabel lblGost = new JLabel("Rezervirao/la:");
		lblGost.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblGost.setBounds(37, 158, 115, 28);
		frame.getContentPane().add(lblGost);

		JLabel lblBrojGostiju = new JLabel("Broj gostiju:");
		lblBrojGostiju.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblBrojGostiju.setBounds(37, 272, 150, 28);
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
		scrollPane.setBounds(37, 357, 735, 79);
		scrollPane.setViewportView(table);
		frame.getContentPane().add(scrollPane);

		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);

		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setWidth(0);

		JLabel lblUkupnaCijena = new JLabel("Ukupna iznos (€):");
		lblUkupnaCijena.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblUkupnaCijena.setBounds(37, 310, 180, 28);
		frame.getContentPane().add(lblUkupnaCijena);

		JButton btnSpremi = new JButton("Natrag");
		btnSpremi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnSpremi.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSpremi.setBounds(37, 450, 735, 40);
		btnSpremi.setBackground(new Color(0, 111, 128));
		btnSpremi.setForeground(Color.WHITE);

		frame.getContentPane().add(btnSpremi);

		JLabel lblNewLabel_4_1 = new JLabel("Iznajmljivač:");
		lblNewLabel_4_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_1.setBounds(37, 120, 115, 28);
		frame.getContentPane().add(lblNewLabel_4_1);

		JLabel lblNewLabel_4_2 = new JLabel("ID rezervacije:");
		lblNewLabel_4_2.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_2.setBounds(411, 120, 176, 28);
		frame.getContentPane().add(lblNewLabel_4_2);

		JLabel lblNewLabel = new JLabel("Datum dolaska:");
		lblNewLabel.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel.setBounds(37, 196, 115, 28);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Datum odlaska:");
		lblNewLabel_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_1.setBounds(37, 234, 115, 28);
		frame.getContentPane().add(lblNewLabel_1);

		JSeparator separator = new JSeparator();
		separator.setBounds(27, 63, 745, 18);
		frame.getContentPane().add(separator);

		JLabel lblNewLabel_2 = new JLabel("Ostali gosti:");
		lblNewLabel_2.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_2.setBounds(412, 191, 175, 28);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_4_2_1 = new JLabel("Datum rezervacije:");
		lblNewLabel_4_2_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_2_1.setBounds(412, 158, 175, 28);
		frame.getContentPane().add(lblNewLabel_4_2_1);

		JLabel lblNewLabel_4_1_1_1 = new JLabel("ID računa:");
		lblNewLabel_4_1_1_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_1_1_1.setBounds(412, 82, 175, 28);
		frame.getContentPane().add(lblNewLabel_4_1_1_1);

		JLabel lblNewLabel_4_1_1 = new JLabel("Datum računa:");
		lblNewLabel_4_1_1.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblNewLabel_4_1_1.setBounds(37, 82, 115, 28);
		frame.getContentPane().add(lblNewLabel_4_1_1);

		txtrImeIPrezime = new JTextArea();

		txtrImeIPrezime.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		txtrImeIPrezime.setText("ime i prezime");
		txtrImeIPrezime.setBounds(532, 196, 240, 146);

		txtrImeIPrezime.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		txtrImeIPrezime.setBackground(frame.getContentPane().getBackground());
		txtrImeIPrezime.setOpaque(true);

		txtrImeIPrezime.setEditable(false);

		frame.getContentPane().add(txtrImeIPrezime);

		lblNewLabel_5 = new JLabel("id rezervacije");
		lblNewLabel_5.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(531, 120, 241, 28);
		lblNewLabel_5.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5);

		lblNewLabel_3 = new JLabel("broj gostiju");
		lblNewLabel_3.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_3.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		lblNewLabel_3.setBounds(151, 272, 243, 28);
		frame.getContentPane().add(lblNewLabel_3);

		lblNewLabel_5_1 = new JLabel("datum rezervacije");
		lblNewLabel_5_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1.setBounds(532, 158, 240, 28);
		lblNewLabel_5_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1);

		lblNewLabel_5_1_1 = new JLabel("korisnicko ime");
		lblNewLabel_5_1_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_1.setBounds(151, 120, 243, 28);
		lblNewLabel_5_1_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_1);

		lblNewLabel_5_1_2 = new JLabel("broj dok - ime prezime");
		lblNewLabel_5_1_2.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_2.setBounds(151, 158, 243, 28);
		lblNewLabel_5_1_2.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_2);

		lblNewLabel_5_1_3 = new JLabel("datum dolaska");
		lblNewLabel_5_1_3.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_3.setBounds(151, 196, 243, 28);
		lblNewLabel_5_1_3.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_3);

		lblNewLabel_5_1_3_1 = new JLabel("datum odlaska");
		lblNewLabel_5_1_3_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_3_1.setBounds(151, 234, 243, 28);
		lblNewLabel_5_1_3_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_3_1);

		lblUkupnaCijena_2 = new JLabel("ukupna cijena");
		lblUkupnaCijena_2.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblUkupnaCijena_2.setBounds(151, 310, 243, 28);
		lblUkupnaCijena_2.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblUkupnaCijena_2);

		lblNewLabel_5_1_1_1 = new JLabel("datum računa");
		lblNewLabel_5_1_1_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_1_1.setBounds(151, 82, 243, 28);
		lblNewLabel_5_1_1_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_1_1);

		lblNewLabel_5_1_1_1_1 = new JLabel("id racuna");
		lblNewLabel_5_1_1_1_1.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblNewLabel_5_1_1_1_1.setBounds(532, 82, 240, 28);
		lblNewLabel_5_1_1_1_1.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblNewLabel_5_1_1_1_1);
	}

	private void ucitajRezervaciju(int idRezervacije, int idIznajmljivaca) {
		try (Connection con = Database.getConnection()) {

			String sql = """
					    SELECT *
					    FROM Stavka_racuna
					    WHERE id_rezervacije = ? AND id_iznajmljivaca = ?
					    ORDER BY id_stavke_racuna LIMIT 1
					""";

			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, idRezervacije);
				ps.setInt(2, idIznajmljivaca);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						lblNewLabel_5_1_1.setText(rs.getString("korisnicko_ime"));
						lblNewLabel_5_1_2.setText(rs.getString("broj_dokumenta") + " - " + rs.getString("ime_gosta") + " " + rs.getString("prezime_gosta"));

						lblNewLabel_5_1.setText(rs.getDate("datum_rezervacije").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_5.setText(String.valueOf(idRezervacije));

						lblNewLabel_5_1_1_1.setText(rs.getDate("datum_racuna").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_3.setText(String.valueOf(rs.getInt("broj_gostiju")));

						lblUkupnaCijena_2.setText(String.format(Locale.ENGLISH, "%.2f", rs.getDouble("ukupna_cijena")));

						lblNewLabel_5_1_3.setText(rs.getDate("datum_dolaska").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_5_1_3_1.setText(rs.getDate("datum_odlaska").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_5_1_1_1_1.setText(String.valueOf(rs.getInt("id_racuna")));

						String ostali = rs.getString("ime_i_prezime");
						if (ostali == null || ostali.isBlank()) {
							txtrImeIPrezime.setText("Nema dodatnih gostiju.");
						} else {
							String prikaz = ostali.replaceAll(",\\s*", "\n").trim();
							txtrImeIPrezime.setText(prikaz);
						}

						model.setRowCount(0);
						model.addRow(new Object[] { rs.getInt("id_stavke_racuna"), rs.getString("opis_apartmana"), rs.getInt("kapacitet"), rs.getString("lokacija"), String.format(Locale.ENGLISH, "%.2f", rs.getDouble("cijena_nocenja")) });
					}
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška pri učitavanju: " + e.getMessage());
		}
	}

	private void ucitajRacunPrekoRacuna(int idRacuna, int idIznajmljivaca) {
		try (Connection con = Database.getConnection()) {

			String sql = """
					    SELECT *
					    FROM Stavka_racuna
					    WHERE id_racuna = ? AND id_iznajmljivaca = ?
					""";

			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setInt(1, idRacuna);
				ps.setInt(2, idIznajmljivaca);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {

						lblNewLabel_5_1_1.setText(rs.getString("korisnicko_ime"));
						lblNewLabel_5_1_2.setText(rs.getString("broj_dokumenta") + " - " + rs.getString("ime_gosta") + " " + rs.getString("prezime_gosta"));

						lblNewLabel_5_1.setText(rs.getDate("datum_rezervacije").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_5.setText(String.valueOf(rs.getInt("id_rezervacije")));

						lblNewLabel_5_1_1_1.setText(rs.getDate("datum_racuna").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_3.setText(String.valueOf(rs.getInt("broj_gostiju")));

						lblUkupnaCijena_2.setText(String.format(Locale.ENGLISH, "%.2f", rs.getDouble("ukupna_cijena")));

						lblNewLabel_5_1_3.setText(rs.getDate("datum_dolaska").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_5_1_3_1.setText(rs.getDate("datum_odlaska").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

						lblNewLabel_5_1_1_1_1.setText(String.valueOf(rs.getInt("id_racuna")));

						String ostali = rs.getString("ime_i_prezime");
						if (ostali == null || ostali.isBlank()) {
							txtrImeIPrezime.setText("Nema dodatnih gostiju.");
						} else {
							String prikaz = ostali.replaceAll(",\\s*", "\n").trim();
							txtrImeIPrezime.setText(prikaz);
						}

						model.setRowCount(0);
						model.addRow(new Object[] { rs.getInt("id_stavke_racuna"), rs.getString("opis_apartmana"), rs.getInt("kapacitet"), rs.getString("lokacija"), String.format(Locale.ENGLISH, "%.2f", rs.getDouble("cijena_nocenja")) });
					}
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška pri učitavanju: " + e.getMessage());
		}
	}

}