package zavrsniRad;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ApartmanPregled {

	private JFrame frame;
	private JLabel lblPrikazSlike;
	private JTextArea textAreaOpis, textAreaLokacija;
	private JButton btnDodajNaRezervaciju;

	private int trenutnaSlika = 0;
	private List<File> slike = new ArrayList<>();

	private RezervacijaDodaj roditelj = null;
	private RezervacijaAzuriraj roditeljAzuriraj = null;
	private LocalDate dolazak, odlazak;

	private int selectedId;
	private String opisApartmana, lokacija;
	private int kapacitet;
	private double cijenaNocenja;
	private RezervacijaDodajApartman dodajApartmanProzor;
	private RezervacijaAzurirajApartman azurirajApartmanProzor;

	/**
	 * @wbp.parser.constructor
	 */
	public ApartmanPregled(GetterSetter korisnik, ApartmanPopis apartmanPopis, int selectedId, String opisApartmana, int kapacitet, double cijenaNocenja, String lokacija) {
		this.selectedId = selectedId;
		this.opisApartmana = opisApartmana;
		this.kapacitet = kapacitet;
		this.cijenaNocenja = cijenaNocenja;
		this.lokacija = lokacija;

		initialize();
		btnDodajNaRezervaciju.setVisible(false);
		frame.setVisible(true);
	}

	public ApartmanPregled(GetterSetter korisnik, RezervacijaDodaj roditelj, RezervacijaDodajApartman dodajApartmanProzor, int selectedId, String opisApartmana, int kapacitet, double cijenaNocenja, String lokacija, LocalDate dolazak, LocalDate odlazak) {
		this.selectedId = selectedId;
		this.roditelj = roditelj;
		this.dodajApartmanProzor = dodajApartmanProzor;
		this.opisApartmana = opisApartmana;
		this.kapacitet = kapacitet;
		this.cijenaNocenja = cijenaNocenja;
		this.lokacija = lokacija;
		this.dolazak = dolazak;
		this.odlazak = odlazak;

		initialize();
		btnDodajNaRezervaciju.setVisible(true);
		btnDodajNaRezervaciju.addActionListener(e -> dodajApartmanNaRezervaciju());
		frame.setVisible(true);
	}

	public ApartmanPregled(GetterSetter korisnik, RezervacijaAzuriraj roditeljAzuriraj, RezervacijaAzurirajApartman azurirajApartmanProzor, int selectedId, String opisApartmana, int kapacitet, double cijenaNocenja, String lokacija, LocalDate dolazak, LocalDate odlazak) {
		this.selectedId = selectedId;
		this.roditeljAzuriraj = roditeljAzuriraj;
		this.azurirajApartmanProzor = azurirajApartmanProzor;
		this.opisApartmana = opisApartmana;
		this.kapacitet = kapacitet;
		this.cijenaNocenja = cijenaNocenja;
		this.lokacija = lokacija;
		this.dolazak = dolazak;
		this.odlazak = odlazak;

		initialize();
		btnDodajNaRezervaciju.setVisible(true);
		btnDodajNaRezervaciju.addActionListener(e -> dodajApartmanNaRezervaciju());
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();

		frame.setTitle("APARTMANSKI SUSTAV - Pregled apartmana");
		frame.setBounds(100, 100, 943, 577);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Pregled apartmana");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(359, 27, 275, 31);
		frame.getContentPane().add(lblNaslov);

		JLabel lblOpisApartmana = new JLabel("Opis apartmana:");
		lblOpisApartmana.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblOpisApartmana.setBounds(33, 94, 167, 22);
		frame.getContentPane().add(lblOpisApartmana);

		textAreaOpis = new JTextArea(opisApartmana);
		textAreaOpis.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		textAreaOpis.setBounds(33, 125, 198, 88);
		textAreaOpis.setWrapStyleWord(true);
		textAreaOpis.setLineWrap(true);
		textAreaOpis.setEditable(false);
		textAreaOpis.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		textAreaOpis.setBackground(frame.getContentPane().getBackground());
		frame.getContentPane().add(textAreaOpis);

		JLabel lblLokacija = new JLabel("Lokacija:");
		lblLokacija.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblLokacija.setBounds(33, 222, 167, 22);
		frame.getContentPane().add(lblLokacija);

		textAreaLokacija = new JTextArea(lokacija);
		textAreaLokacija.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		textAreaLokacija.setBounds(33, 250, 198, 88);
		textAreaLokacija.setWrapStyleWord(true);
		textAreaLokacija.setLineWrap(true);
		textAreaLokacija.setEditable(false);
		textAreaLokacija.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		textAreaLokacija.setBackground(frame.getContentPane().getBackground());
		frame.getContentPane().add(textAreaLokacija);

		JLabel lblKapacitet = new JLabel("Kapacitet:");
		lblKapacitet.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblKapacitet.setBounds(33, 344, 177, 22);
		frame.getContentPane().add(lblKapacitet);

		String kapacitetTekst = (kapacitet == 1 || kapacitet >= 5) ? kapacitet + " osoba" : kapacitet + " osobe";

		JLabel lblKapacitetValue = new JLabel(kapacitetTekst);
		lblKapacitetValue.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblKapacitetValue.setBounds(33, 370, 198, 28);
		lblKapacitetValue.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblKapacitetValue);

		JLabel lblCijenaNocenja = new JLabel("Cijena noćenja (€):");
		lblCijenaNocenja.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblCijenaNocenja.setBounds(33, 408, 167, 22);
		frame.getContentPane().add(lblCijenaNocenja);

		JLabel lblCijenaNocenjaValue = new JLabel(String.format(java.util.Locale.ENGLISH, "%.2f", cijenaNocenja));
		lblCijenaNocenjaValue.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		lblCijenaNocenjaValue.setBounds(33, 435, 198, 28);
		lblCijenaNocenjaValue.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(lblCijenaNocenjaValue);

		lblPrikazSlike = new JLabel("Slika");
		lblPrikazSlike.setBounds(275, 88, 605, 375);
		lblPrikazSlike.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrikazSlike.setBorder(new LineBorder(Color.GRAY, 2));
		lblPrikazSlike.setOpaque(true);
		frame.getContentPane().add(lblPrikazSlike);

		JLabel lblLeft = new JLabel();
		lblLeft.setBounds(220, 253, 45, 60);
		lblLeft.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblLeft.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				prikaziPrethodnuSliku();
			}
		});
		postaviIkonu(lblLeft, "/left.png");
		frame.getContentPane().add(lblLeft);

		JLabel lblRight = new JLabel();
		lblRight.setBounds(875, 253, 45, 60);
		lblRight.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblRight.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				prikaziSljedecuSliku();
			}
		});
		postaviIkonu(lblRight, "/right.png");
		frame.getContentPane().add(lblRight);

		JButton btnNatrag = new JButton("Natrag");
		btnNatrag.addActionListener(e -> frame.dispose());
		btnNatrag.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNatrag.setBounds(466, 484, 414, 40);
		btnNatrag.setBackground(new Color(0, 111, 128));
		btnNatrag.setForeground(Color.WHITE);
		frame.getContentPane().add(btnNatrag);

		btnDodajNaRezervaciju = new JButton("Dodaj na rezervaciju");
		btnDodajNaRezervaciju.setForeground(Color.WHITE);
		btnDodajNaRezervaciju.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnDodajNaRezervaciju.setBackground(new Color(0, 111, 128));
		btnDodajNaRezervaciju.setBounds(33, 484, 414, 40);
		btnDodajNaRezervaciju.setVisible(false);
		frame.getContentPane().add(btnDodajNaRezervaciju);

		JSeparator separator = new JSeparator();
		separator.setBounds(31, 68, 849, 10);
		frame.getContentPane().add(separator);

		ucitajSlikeApartmana(selectedId);
	}

	private void dodajApartmanNaRezervaciju() {
		ApartmanNaRezervaciji apartman = new ApartmanNaRezervaciji(selectedId, opisApartmana, lokacija, kapacitet, cijenaNocenja, dolazak, odlazak);

		if (roditelj != null) {
			roditelj.getDodaniApartmani().clear();
			roditelj.getDodaniApartmani().add(apartman);
			roditelj.dodajApartmanNaTabelu(apartman);
			if (dodajApartmanProzor != null) {
				dodajApartmanProzor.getFrame().dispose();
			}
		} else if (roditeljAzuriraj != null) {
			roditeljAzuriraj.getDodaniApartmani().clear();
			roditeljAzuriraj.getDodaniApartmani().add(apartman);
			roditeljAzuriraj.dodajApartmanNaTabelu(apartman);
			if (azurirajApartmanProzor != null) {
				azurirajApartmanProzor.getFrame().dispose();
			}
		}

		frame.dispose();
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

	private void ucitajSlikeApartmana(int apartmanId) {
		try (Connection con = Database.getConnection()) {
			String sql = "SELECT putanja_slike FROM Apartman_slika WHERE id_apartmana = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, apartmanId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				slike.add(new File(rs.getString("putanja_slike")));
			}

			prikaziSliku();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Greška pri učitavanju slika: " + e.getMessage());
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
			ImageIcon original = new ImageIcon(slika.getAbsolutePath());
			Image scaled = original.getImage().getScaledInstance(618, 375, Image.SCALE_SMOOTH);
			lblPrikazSlike.setIcon(new ImageIcon(scaled));
		} else {
			lblPrikazSlike.setIcon(null);
		}
	}
}