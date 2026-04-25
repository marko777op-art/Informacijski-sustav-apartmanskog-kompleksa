package zavrsniRad;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.Font;
import java.awt.Color;
import org.mindrot.jbcrypt.BCrypt;

public class Prijava {

	private JFrame frame;
	private JTextField unosIme;
	private JPasswordField unosLozinka;
	private GetterSetter korisnik;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Prijava window = new Prijava();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Prijava() {
		korisnik = new GetterSetter();
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Prijava");
		frame.setBounds(100, 100, 449, 304);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Prijava");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNaslov.setBounds(184, 21, 161, 25);
		frame.getContentPane().add(lblNaslov);

		JSeparator separator = new JSeparator();
		separator.setBounds(26, 199, 381, 9);
		frame.getContentPane().add(separator);

		JLabel lblKorisnickoIme = new JLabel("Unesi korisničko ime:");
		lblKorisnickoIme.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblKorisnickoIme.setBounds(26, 71, 148, 29);
		frame.getContentPane().add(lblKorisnickoIme);

		JLabel lblLozinka = new JLabel("Unesi lozinku:");
		lblLozinka.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLozinka.setBounds(26, 110, 142, 30);
		frame.getContentPane().add(lblLozinka);

		unosIme = new JTextField();
		unosIme.setBounds(175, 70, 232, 30);
		unosIme.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(unosIme);

		unosLozinka = new JPasswordField();
		unosLozinka.setBounds(175, 110, 232, 30);
		unosLozinka.setBorder(new LineBorder(new Color(0, 111, 128), 2, true));
		frame.getContentPane().add(unosLozinka);

		JButton btnPrijava = new JButton("Prijavi se");
		btnPrijava.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPrijava.setBounds(175, 150, 232, 35);
		btnPrijava.setBackground(new Color(0, 111, 128));
		btnPrijava.setForeground(Color.WHITE);
		btnPrijava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String imes = unosIme.getText().trim();
				String lozinkas = new String(unosLozinka.getPassword());

				if (imes.isEmpty() || lozinkas.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Molimo unesite korisničko ime i lozinku!");
					return;
				}

				try (Connection con = Database.getConnection();
						PreparedStatement psmt = con.prepareStatement("SELECT id_iznajmljivaca, lozinka FROM Iznajmljivac WHERE korisnicko_ime=?")) {

					psmt.setString(1, imes);

					try (ResultSet rs = psmt.executeQuery()) {
						if (rs.next()) {
							String storedHash = rs.getString("lozinka");

							if (BCrypt.checkpw(lozinkas, storedHash)) {
								korisnik.setId_korisnika(rs.getInt("id_iznajmljivaca"));
								korisnik.setKorisnicko_ime(imes);

								JOptionPane.showMessageDialog(null, "Prijava uspješna!");

								GlavniIzbornik window = new GlavniIzbornik(korisnik);
								window.frame.setVisible(true);
								frame.dispose();

							} else {
								JOptionPane.showMessageDialog(null, "Neispravno korisničko ime ili lozinka.");
							}

						} else {
							JOptionPane.showMessageDialog(null, "Korisničko ime ne postoji!");
						}
					}

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Greška prilikom prijave! " + e1.getMessage());
				}
			}

		});
		frame.getContentPane().add(btnPrijava);

		JButton btnRegistracija = new JButton("Registracija");
		btnRegistracija.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRegistracija.setBounds(175, 215, 232, 35);
		btnRegistracija.setBackground(new Color(102, 153, 153));
		btnRegistracija.setForeground(Color.WHITE);

		frame.getContentPane().add(btnRegistracija);

		btnRegistracija.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Registracija window = new Registracija();
				window.frame.setVisible(true);
				frame.dispose();
			}
		});

		JLabel lblNemaRacun = new JLabel("Kreiraj račun:");
		lblNemaRacun.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNemaRacun.setBounds(26, 222, 142, 25);
		frame.getContentPane().add(lblNemaRacun);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(26, 54, 381, 9);
		frame.getContentPane().add(separator_1);
	}
}
