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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class ApartmanDodaj {

	public JFrame frame;
	private JTextField txtOpisApartmana, txtCijenaNocenja, txtLokacija;
	private GetterSetter korisnik;
	private ApartmanPopis apartmanPopis;
	private JLabel lblPrikazSlike, lblPutanjaSlike;
	private static final int SIRINA_SLIKE = 518;
	private static final int VISINA_SLIKE = 280;
	private List<File> slike = new ArrayList<>();
	private int trenutnaSlika = -1;
	private JComboBox<String> comboKapacitet;

	public ApartmanDodaj(GetterSetter korisnik, ApartmanPopis apartmanPopis) {
		this.korisnik = korisnik;
		this.apartmanPopis = apartmanPopis;
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Kreiranje Apartmana");
		frame.setBounds(100, 100, 630, 667);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Unos novog apartmana");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(190, 25, 326, 25);
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

		String[] status = { "1 osoba", "2 osobe", "3 osobe", "4 osobe", "5 osoba", "6 osoba", "7 osoba", "8 osoba","9 osoba", "10 osoba" };
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

		JButton btnDodaj = new JButton("Kreiraj apartman");
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dodajApartman();
			}
		});
		btnDodaj.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnDodaj.setBounds(55, 577, 249, 40);
		btnDodaj.setBackground(new Color(0, 111, 128));
		btnDodaj.setForeground(Color.WHITE);
		frame.getContentPane().add(btnDodaj);

		JSeparator separator = new JSeparator();
		separator.setBounds(55, 64, 503, 12);
		frame.getContentPane().add(separator);

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
		lblPrikazSlike.setBounds(55, 263, 505, 280);
		lblPrikazSlike.setHorizontalAlignment(SwingConstants.CENTER);
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

	}

	private void dodajSliku() {
		FileDialog dialog = new FileDialog(frame, "Odaberi PNG/JPEG/JPG sliku", FileDialog.LOAD);
		dialog.setVisible(true);

		if (dialog.getFile() != null) {
			File original = new File(dialog.getDirectory(), dialog.getFile());

			if (!(original.getName().toLowerCase().endsWith(".png") || original.getName().toLowerCase().endsWith(".jpg") || original.getName().toLowerCase().endsWith(".jpeg"))) {
				JOptionPane.showMessageDialog(frame, "Dozvoljen je PNG, JPEG i JPG format slika.");
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
				JOptionPane.showMessageDialog(frame, "Maksimalno 10 slika po apartmanu.");
				return;
			}

			slike.add(original);
			trenutnaSlika = slike.size() - 1;

			lblPutanjaSlike.setText(original.getAbsolutePath());
			ImageIcon originalIcon = new ImageIcon(original.getAbsolutePath());
			Image scaled = originalIcon.getImage().getScaledInstance(SIRINA_SLIKE, VISINA_SLIKE, Image.SCALE_SMOOTH);
			lblPrikazSlike.setIcon(new ImageIcon(scaled));
		}
	}

	private void ukloniSliku() {
		if (!slike.isEmpty() && trenutnaSlika >= 0 && trenutnaSlika < slike.size()) {
			slike.remove(trenutnaSlika);

			if (!slike.isEmpty()) {
				if (trenutnaSlika >= slike.size()) {
					trenutnaSlika = slike.size() - 1;
				}
			} else {
				trenutnaSlika = -1;
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

	private void postaviIkonu(JLabel label, String putanja) {
		URL iconURL = getClass().getResource(putanja);
		if (iconURL != null) {
			ImageIcon ikona = new ImageIcon(iconURL);
			label.setIcon(new NoScalingIcon(ikona));
		} else {
			System.err.println("Nije pronađena ikona: " + putanja);
		}
	}

	private void dodajApartman() {
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

				String sql = "INSERT INTO Apartman (opis_apartmana, kapacitet, cijena_nocenja, lokacija, id_iznajmljivaca) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, opis);
				ps.setInt(2, kapacitet);
				ps.setDouble(3, cijena);
				ps.setString(4, lokacija);
				ps.setInt(5, korisnik.getId_korisnika());
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();
				int apartmanId = rs.next() ? rs.getInt(1) : -1;

				if (apartmanId != -1) {
					new File("slike_apartmana").mkdirs();

					for (File originalSlika : slike) {
						String novaPutanja;

						if (originalSlika.getAbsolutePath().startsWith("slike_apartmana")) {
							novaPutanja = originalSlika.getAbsolutePath();
						} else {
							novaPutanja = generirajJedinstvenoIme(originalSlika);
							Files.copy(originalSlika.toPath(), new File(novaPutanja).toPath(),
									StandardCopyOption.REPLACE_EXISTING);
						}

						PreparedStatement psSlika = con.prepareStatement("INSERT INTO Apartman_slika (id_apartmana, putanja_slike) VALUES (?, ?)");
						psSlika.setInt(1, apartmanId);
						psSlika.setString(2, novaPutanja);
						psSlika.executeUpdate();
					}
				}

				JOptionPane.showMessageDialog(frame, "Apartman uspješno dodan.");
				apartmanPopis.ucitajApartmane();
				frame.dispose();
			}

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Koristite ispravne numeričke vrijednosti za cijenu noćenja.");
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(frame, "Greška pri spremanju: " + e.getMessage());
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