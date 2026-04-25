package zavrsniRad;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class GostAzuriraj {

	private JFrame frame;
	private JTextField txtImeGosta, txtPrezimeGosta, txtAdresaGosta, txtBrojDokumenta, txtBrojTelefona;
	private JComboBox<String> comboDrzavljanstvo, comboVrstaDokumenta;
	private JDateChooser DatumRodenja;
	private GostPopis gostPopis;
	private GetterSetter korisnik;
	private JLabel lblOdaberiteDravljanstvo;
	private JLabel lblUnesiteBrojDokumenta;
	private int selectedId;
	private JButton btnOdustani;

	public GostAzuriraj(GetterSetter korisnik, GostPopis gostPopis, int selectedId, String ime, String prezime, String datum, String adresa, String drzavljanstvo, String dokument, String brojDokumenta, String telefon) {
		this.gostPopis = gostPopis;
		this.selectedId = selectedId;
		this.korisnik = korisnik;
		initialize(ime, prezime, datum, adresa, drzavljanstvo, dokument, brojDokumenta, telefon);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String ime, String prezime, String datum, String adresa, String drzavljanstvo, String dokument, String brojDokumenta, String telefon) {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Ažuriranje gosta");
		frame.setBounds(100, 100, 553, 476);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Ažuriranje gosta");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(189, 21, 205, 31);
		frame.getContentPane().add(lblNaslov);

		JLabel lblImeGosta = new JLabel("Unesite ime gosta:");
		lblImeGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblImeGosta.setBounds(33, 77, 167, 22);
		frame.getContentPane().add(lblImeGosta);

		txtImeGosta = new JTextField(ime);
		txtImeGosta.setBounds(210, 77, 292, 28);
		txtImeGosta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtImeGosta);

		JLabel lblPrezimeGosta = new JLabel("Unesite prezime gosta:");
		lblPrezimeGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrezimeGosta.setBounds(33, 115, 167, 22);
		frame.getContentPane().add(lblPrezimeGosta);

		txtPrezimeGosta = new JTextField(prezime);
		txtPrezimeGosta.setBounds(210, 115, 292, 28);
		txtPrezimeGosta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtPrezimeGosta);

		JLabel lblDatumRodenja = new JLabel("Unesite datum rođenja:");
		lblDatumRodenja.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDatumRodenja.setBounds(33, 153, 167, 22);
		frame.getContentPane().add(lblDatumRodenja);

		DatumRodenja = new JDateChooser();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate localDate = LocalDate.parse(datum, formatter);
			DatumRodenja.setDate(java.sql.Date.valueOf(localDate));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Neispravan format datuma: " + datum);
			DatumRodenja.setDate(null);
		}
		DatumRodenja.setDateFormatString("dd-MM-yyyy");

		((com.toedter.calendar.JCalendar) DatumRodenja.getJCalendar()).setWeekOfYearVisible(false);
		DatumRodenja.setBounds(210, 153, 292, 28);
		DatumRodenja.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(DatumRodenja);

		String[] dokumentStr = { "Osobna iskaznica", "Putovnica" };
		comboVrstaDokumenta = new JComboBox<>(dokumentStr);
		comboVrstaDokumenta.setSelectedItem(dokument);
		comboVrstaDokumenta.setBounds(210, 267, 292, 28);
		comboVrstaDokumenta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(comboVrstaDokumenta);

		String[] drzavljanstvoStr = { "ISO3166" };
		comboDrzavljanstvo = new JComboBox<>(drzavljanstvoStr);

		comboDrzavljanstvo.setBounds(210, 229, 292, 28);
		comboDrzavljanstvo.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));

		java.util.Locale[] locales = java.util.Locale.getAvailableLocales();
		java.util.Set<String> drzaveSet = new java.util.TreeSet<>();

		for (java.util.Locale locale : locales) {
			String country = locale.getDisplayCountry();
			if (!country.isEmpty()) {
				drzaveSet.add(country);
			}
		}
		comboDrzavljanstvo.removeAllItems();
		for (String country : drzaveSet) {
			comboDrzavljanstvo.addItem(country);
		}
		comboDrzavljanstvo.setSelectedItem(drzavljanstvo);
		AutoCompleteDecorator.decorate(comboDrzavljanstvo);
		frame.getContentPane().add(comboDrzavljanstvo);

		txtBrojDokumenta = new JTextField(brojDokumenta);
		txtBrojDokumenta.setBounds(210, 304, 292, 28);
		txtBrojDokumenta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtBrojDokumenta);

		JLabel lblAdresaGosta = new JLabel("Unesite adresu gosta:");
		lblAdresaGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAdresaGosta.setBounds(33, 191, 167, 22);
		frame.getContentPane().add(lblAdresaGosta);

		txtAdresaGosta = new JTextField(adresa);
		txtAdresaGosta.setBounds(210, 191, 292, 28);
		txtAdresaGosta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtAdresaGosta);

		JButton btnNatrag = new JButton("Spremi gosta");
		btnNatrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				azurirajGosta();

			}
		});
		btnNatrag.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNatrag.setBounds(33, 386, 227, 40);
		btnNatrag.setBackground(new Color(0, 111, 128));
		btnNatrag.setForeground(Color.WHITE);
		frame.getContentPane().add(btnNatrag);

		JSeparator separator = new JSeparator();
		separator.setBounds(33, 61, 469, 16);
		frame.getContentPane().add(separator);

		JLabel lblBrojTelefona = new JLabel("Unesite broj telefona:");
		lblBrojTelefona.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBrojTelefona.setBounds(33, 343, 150, 22);
		frame.getContentPane().add(lblBrojTelefona);

		txtBrojTelefona = new JTextField(telefon);
		frame.getContentPane().add(txtBrojTelefona);
		txtBrojTelefona.setBounds(210, 342, 292, 28);
		txtBrojTelefona.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		txtBrojTelefona.setColumns(10);

		JLabel lblOibGosta = new JLabel("Odaberite dokument:");
		lblOibGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOibGosta.setBounds(33, 261, 139, 34);
		frame.getContentPane().add(lblOibGosta);

		lblOdaberiteDravljanstvo = new JLabel("Odaberite državljanstvo:");
		lblOdaberiteDravljanstvo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOdaberiteDravljanstvo.setBounds(33, 229, 150, 22);
		frame.getContentPane().add(lblOdaberiteDravljanstvo);

		lblUnesiteBrojDokumenta = new JLabel("Unesite broj dokumenta:");
		lblUnesiteBrojDokumenta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnesiteBrojDokumenta.setBounds(33, 301, 167, 31);
		frame.getContentPane().add(lblUnesiteBrojDokumenta);

		btnOdustani = new JButton("Natrag");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnOdustani.setForeground(Color.WHITE);
		btnOdustani.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnOdustani.setBackground(new Color(0, 111, 128));
		btnOdustani.setBounds(275, 386, 227, 40);
		frame.getContentPane().add(btnOdustani);
	}

	private void azurirajGosta() {
		try {
			String ime = txtImeGosta.getText().trim();
			String prezime = txtPrezimeGosta.getText().trim();
			String adresa = txtAdresaGosta.getText().trim();
			String telefon = txtBrojTelefona.getText().trim();
			String brojDokumenta = txtBrojDokumenta.getText().trim();
			String drzavljanstvo = (String) comboDrzavljanstvo.getSelectedItem();
			String vrstaDokumenta = (String) comboVrstaDokumenta.getSelectedItem();
			java.util.Date selectedDate = DatumRodenja.getDate();

			if (selectedDate == null) {
				JOptionPane.showMessageDialog(frame, "Odaberite datum rođenja.");
				return;
			}

			LocalDate datumRodenja = selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
			if (datumRodenja.isAfter(LocalDate.now())) {
				JOptionPane.showMessageDialog(frame, "Datum rođenja ne može biti u budućnosti!");
				return;
			}

			if (ime.isEmpty() || prezime.isEmpty() || adresa.isEmpty() || telefon.isEmpty() || brojDokumenta.isEmpty()
					|| drzavljanstvo == null || vrstaDokumenta == null) {
				JOptionPane.showMessageDialog(frame, "Sva polja moraju biti popunjena!");
				return;
			}

			if (ime.length() > 50 || prezime.length() > 50) {
				JOptionPane.showMessageDialog(frame, "Ime i prezime ne smiju imati više od 50 znakova!");
				return;
			}

			if (!ime.matches("[a-zA-ZčćžšđČĆŽŠĐ ]+") || !prezime.matches("[a-zA-ZčćžšđČĆŽŠĐ ]+")) {
				JOptionPane.showMessageDialog(frame, "Ime i prezime mogu sadržavati samo slova!");
				return;
			}

			if (telefon.length() < 6 || telefon.length() > 20) {
				JOptionPane.showMessageDialog(frame, "Broj telefona mora imati između 6 i 20 znamenki!");
				return;
			}

			if (!telefon.matches("[0-9+ ]+")) {
				JOptionPane.showMessageDialog(frame, "Broj telefona smije sadržavati samo znamenke i razmake!");
				return;

			}

			if (!brojDokumenta.matches("[a-zA-Z0-9]{5,15}")) {
				JOptionPane.showMessageDialog(frame, "Broj dokumenta mora imati 5-15 slova/brojeva(bez simbola)!");
				return;
			}

			if (datumRodenja.isBefore(LocalDate.of(1900, 1, 1))) {
				JOptionPane.showMessageDialog(frame, "Datum rođenja ne smije biti prije 1900. godine!");
				return;
			}

			if (adresa.length() < 5 || adresa.length() > 100) {
				JOptionPane.showMessageDialog(frame, "Adresa mora imati između 5 i 100 znakova!");
				return;
			}

			try (Connection con = Database.getConnection()) {
				String checkSql = "SELECT COUNT(*) FROM Gost WHERE broj_dokumenta = ? AND id_iznajmljivaca = ? AND id_gosta != ?";
				try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
					checkStmt.setString(1, brojDokumenta);
					checkStmt.setInt(2, korisnik.getId_korisnika());
					checkStmt.setInt(3, selectedId);

					try (ResultSet rs = checkStmt.executeQuery()) {
						if (rs.next() && rs.getInt(1) > 0) {
							JOptionPane.showMessageDialog(frame, "Gost s tim brojem dokumenta već postoji!");
							return;
						}
					}
				}

				String sql = "UPDATE Gost SET ime_gosta = ?, prezime_gosta = ?, datum_rodenja = ?, adresa_prebivalista = ?, drzavljanstvo=?, vrsta_dokumenta=?, broj_dokumenta=?, broj_telefona = ? WHERE id_gosta = ? AND id_iznajmljivaca = ?";
				try (PreparedStatement psmt = con.prepareStatement(sql)) {
					psmt.setString(1, ime);
					psmt.setString(2, prezime);
					psmt.setDate(3, java.sql.Date.valueOf(datumRodenja));
					psmt.setString(4, adresa);
					psmt.setString(5, drzavljanstvo);
					psmt.setString(6, vrstaDokumenta);
					psmt.setString(7, brojDokumenta);
					psmt.setString(8, telefon);
					psmt.setInt(9, selectedId);
					psmt.setInt(10, korisnik.getId_korisnika());

					int rows = psmt.executeUpdate();
					if (rows > 0) {
						JOptionPane.showMessageDialog(frame, "Gost uspješno ažuriran!");
						gostPopis.ucitajGoste();
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(frame, "Greška pri ažuriranju gosta.");
					}
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška: " + e.getMessage());
		}
	}

}
