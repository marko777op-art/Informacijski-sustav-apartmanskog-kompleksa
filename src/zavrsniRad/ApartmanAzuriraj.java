package zavrsniRad;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class ApartmanAzuriraj {

	private JFrame frame;
	private JTextField txtOpisApartmana, txtCijenaNocenja, txtLokacija;
	private int selectedId;
	private ApartmanPopis apartmanPopis;
	private GetterSetter korisnik;
	private JComboBox<String> comboKapacitet;
	private static final int SIRINA_SLIKE = 518;
	private static final int VISINA_SLIKE = 280;
	private JLabel lblPrikazSlike, lblPutanjaSlike;
	private List<File> slike = new ArrayList<>();
	private List<String> uklonjenePutanje = new ArrayList<>();
	private int trenutnaSlika = -1;

	public ApartmanAzuriraj(GetterSetter korisnik, ApartmanPopis apartmanPopis, int selectedId, String opisApartmana, int kapacitet, double cijenaNocenja, String lokacija) {
		this.apartmanPopis = apartmanPopis;
		this.selectedId = selectedId;
		this.korisnik = korisnik;
		initialize(opisApartmana, kapacitet, cijenaNocenja, lokacija);
		ucitajPodatkeApartmana(selectedId);
		ucitajSlikeApartmana(selectedId);
		frame.setVisible(true);
	}

	private void initialize(String opisApartmana, int kapacitet, double cijenaNocenja, String lokacija) {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Ažuriranje apartmana");
		frame.setBounds(100, 100, 626, 666);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Ažuriranje apartmana");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(189, 23, 281, 31);
		frame.getContentPane().add(lblNaslov);

		JLabel lblOpisApartmana = new JLabel("Unesite opis:");
		lblOpisApartmana.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOpisApartmana.setBounds(55, 80, 154, 25);
		frame.getContentPane().add(lblOpisApartmana);

		txtOpisApartmana = new JTextField();
		txtOpisApartmana.setBounds(213, 80, 347, 28);
		txtOpisApartmana.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtOpisApartmana);

		JLabel lblKapacitet = new JLabel("Unesite kapacitet:");
		lblKapacitet.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblKapacitet.setBounds(55, 150, 154, 25);
		frame.getContentPane().add(lblKapacitet);

		String[] status = { "1 osoba", "2 osobe", "3 osobe", "4 osobe", "5 osoba", "6 osoba", "7 osoba", "8 osoba", "9 osoba", "10 osoba" };
		comboKapacitet = new JComboBox<>(status);
		comboKapacitet.setBounds(213, 149, 347, 28);
		comboKapacitet.setBorder(new LineBorder(new Color(0, 111, 128), 1, true));
		comboKapacitet.setMaximumRowCount(5);
		frame.getContentPane().add(comboKapacitet);

		JLabel lblCijenaNocenja = new JLabel("Unesite cijenu noćenja (€):");
		lblCijenaNocenja.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCijenaNocenja.setBounds(55, 185, 154, 25);
		frame.getContentPane().add(lblCijenaNocenja);

		txtCijenaNocenja = new JTextField();
		txtCijenaNocenja.setBounds(213, 185, 347, 28);
		txtCijenaNocenja.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtCijenaNocenja);

		JLabel lblLokacija = new JLabel("Unesite lokaciju:");
		lblLokacija.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLokacija.setBounds(55, 115, 154, 25);
		frame.getContentPane().add(lblLokacija);

		txtLokacija = new JTextField();
		txtLokacija.setBounds(213, 115, 347, 28);
		txtLokacija.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(txtLokacija);

		JButton btnAzuriraj = new JButton("Spremi apartman");
		btnAzuriraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				azurirajApartman();
			}
		});
		btnAzuriraj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAzuriraj.setBounds(55, 577, 248, 40);
		btnAzuriraj.setBackground(new Color(0, 111, 128));
		btnAzuriraj.setForeground(Color.WHITE);
		frame.getContentPane().add(btnAzuriraj);

		JButton btnDodajSliku = new JButton("Dodaj sliku");
		btnDodajSliku.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDodajSliku.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dodajSliku();
			}
		});
		btnDodajSliku.setBounds(55, 223, 248, 30);
		btnDodajSliku.setBackground(new Color(102, 153, 153));
		btnDodajSliku.setForeground(Color.WHITE);
		frame.getContentPane().add(btnDodajSliku);

		JButton btnUkloniSliku = new JButton("Ukloni sliku");
		btnUkloniSliku.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnUkloniSliku.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ukloniSliku();
			}
		});
		btnUkloniSliku.setBounds(312, 223, 248, 30);
		btnUkloniSliku.setBackground(new Color(102, 153, 153));
		btnUkloniSliku.setForeground(Color.WHITE);
		frame.getContentPane().add(btnUkloniSliku);

		lblPrikazSlike = new JLabel();
		lblPrikazSlike.setText("Slika");
		lblPrikazSlike.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblPrikazSlike.setBounds(55, 263, 505, 280);
		lblPrikazSlike.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		lblPrikazSlike.setOpaque(true);
		frame.getContentPane().add(lblPrikazSlike);

		lblPutanjaSlike = new JLabel("Putanja slike");
		lblPutanjaSlike.setBounds(55, 542, 505, 25);
		lblPutanjaSlike.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblPutanjaSlike);

		JLabel lblLeft = new JLabel();
		lblLeft.setBounds(0, 373, 45, 60);
		lblLeft.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblLeft.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				prikaziPrethodnuSliku();
			}
		});
		postaviIkonu(lblLeft, "/left.png");
		frame.getContentPane().add(lblLeft);

		JLabel lblRight = new JLabel();
		lblRight.setBounds(555, 373, 45, 60);
		lblRight.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblRight.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				prikaziSljedecuSliku();
			}
		});
		postaviIkonu(lblRight, "/right.png");
		frame.getContentPane().add(lblRight);

		JButton btnNatrag = new JButton("Natrag");
		btnNatrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNatrag.setForeground(Color.WHITE);
		btnNatrag.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNatrag.setBackground(new Color(0, 111, 128));
		btnNatrag.setBounds(311, 577, 249, 40);
		frame.getContentPane().add(btnNatrag);

		JSeparator separator = new JSeparator();
		separator.setBounds(55, 64, 505, 12);
		frame.getContentPane().add(separator);

	}

	private void postaviIkonu(JLabel label, String putanja) {
		URL iconURL = getClass().getResource(putanja);
		if (iconURL != null) {
			ImageIcon ikona = new ImageIcon(iconURL);
			label.setIcon(new NoScalingIcon(ikona));
		} else {
			System.err.println("Nije pronađena ikona: " + putanja);
		}
	}

	private void ucitajPodatkeApartmana(int apartmanId) {
		try (Connection con = Database.getConnection()) {

			PreparedStatement ps = con.prepareStatement("SELECT opis_apartmana, kapacitet, cijena_nocenja, lokacija FROM Apartman WHERE id_apartmana = ? AND id_iznajmljivaca=?");
			ps.setInt(1, apartmanId);
			ps.setInt(2, korisnik.getId_korisnika());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				txtOpisApartmana.setText(rs.getString("opis_apartmana"));
				txtLokacija.setText(rs.getString("lokacija"));
				txtCijenaNocenja.setText(String.format(java.util.Locale.ENGLISH, "%.2f", rs.getDouble("cijena_nocenja")));

				int kapacitet = rs.getInt("kapacitet");
				comboKapacitet.setSelectedIndex(kapacitet - 1);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, "Greška pri učitavanju podataka: " + e.getMessage());
		}
	}

	private void ucitajSlikeApartmana(int apartmanId) {
		slike.clear();

		String sql = "SELECT s.putanja_slike FROM Apartman_slika s JOIN Apartman a ON s.id_apartmana = a.id_apartmana WHERE s.id_apartmana = ? AND a.id_iznajmljivaca=?";

		try (Connection con = Database.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, apartmanId);
			ps.setInt(2, korisnik.getId_korisnika());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String putanja = rs.getString("putanja_slike");
				File slika = new File(putanja);

				if (slika.exists()) {
					slike.add(slika);
				} else {
					System.err.println("Slika ne postoji na disku: " + putanja);
				}
			}
			trenutnaSlika = slike.isEmpty() ? -1 : 0;
			prikaziSliku();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška pri učitavanju slika: " + e.getMessage());
		}
	}

	private void dodajSliku() {
		FileDialog dialog = new FileDialog(frame, "Odaberi sliku", FileDialog.LOAD);
		dialog.setVisible(true);

		if (dialog.getFile() != null) {
			File original = new File(dialog.getDirectory(), dialog.getFile());

			String ime = original.getName().toLowerCase();
			if (!(ime.endsWith(".png") || ime.endsWith(".jpg") || ime.endsWith(".jpeg"))) {
				JOptionPane.showMessageDialog(frame, "Dozvoljen je PNG, JPEG i JPG format.");
				return;
			}

			if (original.length() > 10 * 1024 * 1024) {
				JOptionPane.showMessageDialog(frame, "Slika je prevelika (max 10MB).");
				return;
			}

			if (slike.contains(original)) {
				JOptionPane.showMessageDialog(frame, "Ta slika je već dodana.");
				return;
			}

			if (slike.size() >= 10) {
				JOptionPane.showMessageDialog(frame, "Maksimalno 10 slika.");
				return;
			}

			slike.add(original);
			trenutnaSlika = slike.size() - 1;
			prikaziSliku();
		}
	}

	private void ukloniSliku() {
		if (!slike.isEmpty() && trenutnaSlika >= 0 && trenutnaSlika < slike.size()) {
			File uklonjena = slike.get(trenutnaSlika);
			uklonjenePutanje.add(uklonjena.getAbsolutePath());
			slike.remove(trenutnaSlika);

			if (slike.isEmpty()) {
				trenutnaSlika = -1;
			} else if (trenutnaSlika >= slike.size()) {
				trenutnaSlika = slike.size() - 1;
			}

			prikaziSliku();
		} else {
			JOptionPane.showMessageDialog(frame, "Nema slike za ukloniti.");
		}
	}

	private void prikaziPrethodnuSliku() {
		if (trenutnaSlika > 0) {
			trenutnaSlika--;
			prikaziSliku();
		}
	}

	private void prikaziSljedecuSliku() {
		if (trenutnaSlika < slike.size() - 1) {
			trenutnaSlika++;
			prikaziSliku();
		}
	}

	private void prikaziSliku() {
		if (trenutnaSlika >= 0 && trenutnaSlika < slike.size()) {
			File slika = slike.get(trenutnaSlika);
			lblPutanjaSlike.setText(slika.getAbsolutePath());

			ImageIcon original = new ImageIcon(slika.getAbsolutePath());
			Image scaled = original.getImage().getScaledInstance(SIRINA_SLIKE, VISINA_SLIKE, Image.SCALE_SMOOTH);
			lblPrikazSlike.setIcon(new ImageIcon(scaled));
		} else {
			lblPutanjaSlike.setText("Putanja slike");
			lblPrikazSlike.setIcon(null);
		}
	}

	private void azurirajApartman() {
		String opis = txtOpisApartmana.getText().trim();
		String kapacitetCombo = (String) comboKapacitet.getSelectedItem();
		String cijenaStr = txtCijenaNocenja.getText().trim();
		String lokacija = txtLokacija.getText().trim();

		if (opis.isEmpty() || kapacitetCombo == null || cijenaStr.isEmpty() || lokacija.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Popunite sva polja!");
			return;
		}

		if (opis.length() > 100 || lokacija.length() > 100) {
			JOptionPane.showMessageDialog(frame, "Opis apartmana i lokacija ne smiju imati više od 100 znakova!");
			return;
		}

		if (!cijenaStr.matches("[0-9]+(\\.[0-9]{1,2})?")) {
			JOptionPane.showMessageDialog(frame, "Cijena noćenja mora biti broj!");
			return;
		}

		try {
			int kapacitet = Integer.parseInt(kapacitetCombo.split(" ")[0]);
			double cijena = Double.parseDouble(cijenaStr);

			if (cijena < 1.00 || cijena > 10000) {
				JOptionPane.showMessageDialog(frame, "Unesite valjanu cijenu noćenja!");
				return;
			}

			try (Connection con = Database.getConnection()) {
				String sql = "UPDATE Apartman SET opis_apartmana = ?, kapacitet = ?, cijena_nocenja = ?, lokacija = ? WHERE id_apartmana = ? AND id_iznajmljivaca = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, opis);
				ps.setInt(2, kapacitet);
				ps.setDouble(3, cijena);
				ps.setString(4, lokacija);
				ps.setInt(5, selectedId);
				ps.setInt(6, korisnik.getId_korisnika());
				int rows = ps.executeUpdate();

				List<String> postojecePutanje = new ArrayList<>();
				PreparedStatement psStare = con.prepareStatement("SELECT putanja_slike FROM Apartman_slika WHERE id_apartmana = ?");
				psStare.setInt(1, selectedId);
				ResultSet rsStare = psStare.executeQuery();
				while (rsStare.next()) {
					postojecePutanje.add(rsStare.getString(1));
				}

				new File("slike_apartmana").mkdirs();

				for (File slika : slike) {
					String putanja;
					if (slika.getAbsolutePath().startsWith("slike_apartmana")) {
						putanja = slika.getAbsolutePath();
					} else {
						putanja = generirajJedinstvenoIme(slika);
						Files.copy(slika.toPath(), new File(putanja).toPath(), StandardCopyOption.REPLACE_EXISTING);
					}

					if (!postojecePutanje.contains(putanja)) {
						PreparedStatement psInsert = con.prepareStatement("INSERT INTO Apartman_slika (id_apartmana, putanja_slike) VALUES (?, ?)");
						psInsert.setInt(1, selectedId);
						psInsert.setString(2, putanja);
						psInsert.executeUpdate();
					}
				}

				for (String ukloni : uklonjenePutanje) {
					PreparedStatement psDel = con.prepareStatement("DELETE FROM Apartman_slika WHERE id_apartmana = ? AND putanja_slike = ?");
					psDel.setInt(1, selectedId);
					psDel.setString(2, ukloni);
					psDel.executeUpdate();

					try {
						Files.deleteIfExists(new File(ukloni).toPath());
					} catch (IOException ex) {
						System.err.println("Greška pri brisanju slike s diska: " + ukloni);
					}
				}

				if (rows > 0) {
					JOptionPane.showMessageDialog(frame, "Apartman uspješno ažuriran!");
					apartmanPopis.ucitajApartmane();
					frame.dispose();
				} else {
					JOptionPane.showMessageDialog(frame, "Ažuriranje nije uspjelo!");
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private String generirajJedinstvenoIme(File original) {
		String naziv = original.getName();
		String nazivBezEkstenzije = naziv.contains(".") ? naziv.substring(0, naziv.lastIndexOf('.')) : naziv;
		String ekstenzija = naziv.contains(".") ? naziv.substring(naziv.lastIndexOf('.')) : "";
		File folder = new File("slike_apartmana");

		int brojac = 1;
		File noviFile = new File(folder, naziv);
		while (noviFile.exists()) {
			noviFile = new File(folder, nazivBezEkstenzije + " (" + brojac + ")" + ekstenzija);
			brojac++;
		}
		return noviFile.getPath();
	}
}
