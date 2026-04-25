package zavrsniRad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import org.mindrot.jbcrypt.BCrypt;

public class Registracija {

	public JFrame frame;
	private JTextField unosIme;
	private JPasswordField unosLozinka;
	private JPasswordField potvrdaLozinke;

	public Registracija() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();

		frame.setTitle("APARTMANSKI SUSTAV - Registracija");

		frame.setBounds(100, 100, 479, 342);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Registracija");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNaslov.setBounds(179, 15, 180, 41);
		frame.getContentPane().add(lblNaslov);

		JLabel lblKorisnickoIme = new JLabel("Kreiraj korisničko ime:");
		lblKorisnickoIme.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblKorisnickoIme.setBounds(28, 74, 141, 30);
		frame.getContentPane().add(lblKorisnickoIme);

		unosIme = new JTextField();
		unosIme.setBounds(179, 74, 256, 30);
		unosIme.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(unosIme);

		JLabel lblLozinka = new JLabel("Kreiraj lozinku:");
		lblLozinka.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLozinka.setBounds(28, 114, 141, 28);
		frame.getContentPane().add(lblLozinka);

		unosLozinka = new JPasswordField();
		unosLozinka.setBounds(179, 114, 256, 30);
		unosLozinka.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(unosLozinka);

		JLabel lblPotvrdaLozinke = new JLabel("Potvrdi lozinku:");
		lblPotvrdaLozinke.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPotvrdaLozinke.setBounds(28, 154, 141, 30);
		frame.getContentPane().add(lblPotvrdaLozinke);

		potvrdaLozinke = new JPasswordField();
		potvrdaLozinke.setBounds(179, 154, 256, 30);
		potvrdaLozinke.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(potvrdaLozinke);

		JButton btnRegistracija = new JButton("Registriraj se");
		btnRegistracija.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRegistracija.setBounds(179, 194, 256, 35);
		btnRegistracija.setBackground(new Color(0, 111, 128));
		btnRegistracija.setForeground(Color.WHITE);
		frame.getContentPane().add(btnRegistracija);

		JSeparator separator = new JSeparator();
		separator.setBounds(28, 55, 407, 17);
		frame.getContentPane().add(separator);

		JLabel lblNewLabel = new JLabel("Ako već postoji račun:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(28, 256, 152, 35);
		frame.getContentPane().add(lblNewLabel);

		JButton btnPrijava = new JButton("Prijava");
		btnPrijava.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPrijava.setBounds(179, 256, 256, 35);
		btnPrijava.setBackground(new Color(102, 153, 153));
		btnPrijava.setForeground(Color.WHITE);
		btnPrijava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Prijava();
				frame.dispose();
			}
		});

		frame.getContentPane().add(btnPrijava);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(28, 242, 407, 9);
		frame.getContentPane().add(separator_1);

		btnRegistracija.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String imes = unosIme.getText().trim();
				String lozinkas = new String(unosLozinka.getPassword());
				String potvrdas = new String(potvrdaLozinke.getPassword());

				if (lozinkas.length() < 8) {
					JOptionPane.showMessageDialog(null, "Lozinka mora imati barem 8 znakova!");
					return;
				}

				if (imes.isEmpty() || lozinkas.isEmpty() || potvrdas.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Popunite sva polja!");
					return;
				}
				if (!lozinkas.equals(potvrdas)) {
					JOptionPane.showMessageDialog(null, "Lozinke se ne podudaraju!");
					return;
				}

				try (Connection con = Database.getConnection();
						PreparedStatement psmt = con.prepareStatement("INSERT INTO Iznajmljivac (korisnicko_ime, lozinka) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

					String hashedPassword = BCrypt.hashpw(lozinkas, BCrypt.gensalt());

					psmt.setString(1, imes);
					psmt.setString(2, hashedPassword);
					psmt.executeUpdate();

					try (ResultSet rs = psmt.getGeneratedKeys()) {
						if (rs.next()) {
							int idKorisnika = rs.getInt(1);

							GetterSetter korisnik = new GetterSetter();
							korisnik.setId_korisnika(idKorisnika);
							korisnik.setKorisnicko_ime(imes);

							JOptionPane.showMessageDialog(null, "Registracija uspješna!");

							GlavniIzbornik window = new GlavniIzbornik(korisnik);
							window.frame.setVisible(true);
							frame.dispose();
						}
					}

				} catch (Exception e1) {
					if (e1.getMessage().contains("Duplicate entry")) {
						JOptionPane.showMessageDialog(null, "Korisničko ime već postoji! Odaberite drugo.");
					} else {
						JOptionPane.showMessageDialog(null, "Greška: " + e1.getMessage());
					}
				}
			}
		});
	}
}
