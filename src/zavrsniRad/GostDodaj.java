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
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import com.toedter.calendar.JDateChooser;

public class GostDodaj {

	private JFrame frame;
	private JTextField txtImeGosta, txtPrezimeGosta, txtAdresaGosta, txtBrojDokumenta, txtBrojTelefona;
	private JComboBox<String> comboDrzavljanstvo, comboVrstaDokumenta;
	private JDateChooser DatumRodenja;
	private GostPopis gostPopis;
	private GetterSetter korisnik;

	public GostDodaj(GetterSetter korisnik, GostPopis gostPopis) {
		this.gostPopis = gostPopis;
		this.korisnik = korisnik;
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Kreiranje gosta");
		frame.setBounds(100, 100, 553, 476);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Unos novog gosta");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(181, 21, 220, 31);
		frame.getContentPane().add(lblNaslov);

		JLabel lblImeGosta = new JLabel("Unesite ime gosta:");
		lblImeGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblImeGosta.setBounds(33, 77, 167, 22);
		frame.getContentPane().add(lblImeGosta);

		txtImeGosta = new JTextField();
		txtImeGosta.setBounds(210, 77, 292, 28);
		txtImeGosta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtImeGosta);

		JLabel lblPrezimeGosta = new JLabel("Unesite prezime gosta:");
		lblPrezimeGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrezimeGosta.setBounds(33, 115, 167, 22);
		frame.getContentPane().add(lblPrezimeGosta);

		txtPrezimeGosta = new JTextField();
		txtPrezimeGosta.setBounds(210, 115, 292, 28);
		txtPrezimeGosta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtPrezimeGosta);

		txtBrojDokumenta = new JTextField();
		txtBrojDokumenta.setBounds(210, 304, 292, 28);
		txtBrojDokumenta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtBrojDokumenta);

		JLabel lblDatumRodenja = new JLabel("Unesite datum rođenja:");
		lblDatumRodenja.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDatumRodenja.setBounds(33, 153, 167, 22);
		frame.getContentPane().add(lblDatumRodenja);

		DatumRodenja = new JDateChooser();
		DatumRodenja.setDateFormatString("dd-MM-yyyy");
		((com.toedter.calendar.JCalendar) DatumRodenja.getJCalendar()).setWeekOfYearVisible(false);
		DatumRodenja.setBounds(210, 153, 292, 28);
		DatumRodenja.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(DatumRodenja);

		JLabel lblAdresaGosta = new JLabel("Unesite adresu gosta:");
		lblAdresaGosta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAdresaGosta.setBounds(33, 191, 167, 22);
		frame.getContentPane().add(lblAdresaGosta);

		txtAdresaGosta = new JTextField();
		txtAdresaGosta.setBounds(210, 191, 292, 28);
		txtAdresaGosta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtAdresaGosta);

		JButton btnSpremi = new JButton("Spremi gosta");
		btnSpremi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dodajGosta();
			}
		});
		btnSpremi.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSpremi.setBounds(32, 385, 227, 40);
		btnSpremi.setBackground(new Color(0, 111, 128));
		btnSpremi.setForeground(Color.WHITE);
		frame.getContentPane().add(btnSpremi);

		JSeparator separator = new JSeparator();
		separator.setBounds(33, 62, 469, 16);
		frame.getContentPane().add(separator);

		String[] dokument = { "Osobna iskaznica", "Putovnica" };
		comboVrstaDokumenta = new JComboBox<>(dokument);
		comboVrstaDokumenta.setBounds(210, 267, 292, 28);
		comboVrstaDokumenta.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(comboVrstaDokumenta);

		String[] drzavljanstvo = { "ISO3166" };
		comboDrzavljanstvo = new JComboBox<>(drzavljanstvo);
		comboDrzavljanstvo.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		comboDrzavljanstvo.setBounds(210, 229, 292, 28);

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
		AutoCompleteDecorator.decorate(comboDrzavljanstvo);
		frame.getContentPane().add(comboDrzavljanstvo);

		JLabel lblBrojTelefona = new JLabel("Unesite broj telefona:");
		lblBrojTelefona.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBrojTelefona.setBounds(33, 342, 150, 22);
		frame.getContentPane().add(lblBrojTelefona);

		txtBrojTelefona = new JTextField();
		txtBrojTelefona.setBounds(210, 342, 292, 28);
		txtBrojTelefona.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		txtBrojTelefona.setToolTipText("Unesite broj telefona u formatu 0912345678");
		txtBrojTelefona.setColumns(10);
		frame.getContentPane().add(txtBrojTelefona);

		JLabel lblBrojDokumenta = new JLabel("Unesite broj dokumenta:");
		lblBrojDokumenta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBrojDokumenta.setBounds(33, 299, 140, 34);
		frame.getContentPane().add(lblBrojDokumenta);

		JLabel lblUnesiteVrstuDokumenta = new JLabel("Odaberite dokument:");
		lblUnesiteVrstuDokumenta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnesiteVrstuDokumenta.setBounds(33, 269, 167, 22);
		frame.getContentPane().add(lblUnesiteVrstuDokumenta);

		JLabel lblOdaberiteDravljanstvo = new JLabel("Odaberite državljanstvo:");
		lblOdaberiteDravljanstvo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOdaberiteDravljanstvo.setBounds(33, 231, 167, 22);
		frame.getContentPane().add(lblOdaberiteDravljanstvo);

		JButton btnOdustani = new JButton("Natrag");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnOdustani.setForeground(Color.WHITE);
		btnOdustani.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnOdustani.setBackground(new Color(0, 111, 128));
		btnOdustani.setBounds(274, 385, 227, 40);
		frame.getContentPane().add(btnOdustani);
	}

	private void dodajGosta() {
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

			if (ime.isEmpty() || prezime.isEmpty() || adresa.isEmpty() || telefon.isEmpty() || brojDokumenta.isEmpty() || drzavljanstvo == null || vrstaDokumenta == null) {
				JOptionPane.showMessageDialog(frame, "Sva polja moraju biti popunjena!");
				return;
			}

			if (ime.length() > 50 || prezime.length() > 50) {
				JOptionPane.showMessageDialog(frame, "Ime i prezime ne smiju imati više od 50 znakova!");
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
				JOptionPane.showMessageDialog(frame, "Broj telefona smije sadržavati samo znamenke!");
				return;
			}

			if (!brojDokumenta.matches("[a-zA-Z0-9]{5,15}")) {
				JOptionPane.showMessageDialog(frame, "Broj dokumenta mora imati 5-15 znakova(bez simbola)!");
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
				String checkSql = "SELECT COUNT(*) FROM Gost WHERE broj_dokumenta = ? AND id_iznajmljivaca = ?";
				try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
					checkStmt.setString(1, brojDokumenta);
					checkStmt.setInt(2, korisnik.getId_korisnika());

					try (ResultSet rs = checkStmt.executeQuery()) {
						if (rs.next() && rs.getInt(1) > 0) {
							JOptionPane.showMessageDialog(frame, "Gost s tim brojem dokumenta već postoji!");
							return;
						}
					}
				}

				String sql = "INSERT INTO Gost (ime_gosta, prezime_gosta, datum_rodenja, adresa_prebivalista, drzavljanstvo, vrsta_dokumenta, broj_dokumenta, broj_telefona, id_iznajmljivaca) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement psmt = con.prepareStatement(sql)) {
					psmt.setString(1, ime);
					psmt.setString(2, prezime);
					psmt.setDate(3, java.sql.Date.valueOf(datumRodenja));
					psmt.setString(4, adresa);
					psmt.setString(5, drzavljanstvo);
					psmt.setString(6, vrstaDokumenta);
					psmt.setString(7, brojDokumenta);
					psmt.setString(8, telefon);
					psmt.setInt(9, korisnik.getId_korisnika());

					int rows = psmt.executeUpdate();
					if (rows > 0) {
						JOptionPane.showMessageDialog(frame, "Gost uspješno dodan!");
						gostPopis.ucitajGoste();
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(frame, "Greška pri dodavanju gosta.");
					}
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška: " + e.getMessage());
		}
	}
}
