package zavrsniRad;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class GlavniIzbornik {
	public JFrame frame;
	private GetterSetter korisnik;
	private JLabel lblBrojGostiju;
	private JLabel lblBrojZauzetih;
	private JLabel lblBrojSlobodnih;
	private JLabel lblBrojBoravaka;

	public GlavniIzbornik(GetterSetter korisnik) {
		this.korisnik = korisnik;

		if (korisnik == null || korisnik.getId_korisnika() == 0) {
			JOptionPane.showMessageDialog(null, "Greška: Korisnik nije prijavljen!");
			return;
		}

		initialize();
		prikaziStatistiku();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 15));
		frame.setTitle("APARTMANSKI SUSTAV");
		frame.setBounds(100, 100, 707, 510);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		lblBrojGostiju = new JLabel("Broj gostiju: ");
		lblBrojZauzetih = new JLabel("Zauzeti apartmani: ");
		lblBrojSlobodnih = new JLabel("Slobodni apartmani: ");
		lblBrojBoravaka = new JLabel("Evidentirani boravci: ");

		JLabel lblNaslov = new JLabel("GLAVNI IZBORNIK");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNaslov.setBounds(231, 10, 276, 60);
		frame.getContentPane().add(lblNaslov);

		lblBrojGostiju.setOpaque(true);
		lblBrojGostiju.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBrojGostiju.setBounds(363, 200, 310, 54);
		lblBrojGostiju.setBorder(new LineBorder(new Color(0, 111, 128), 5, true));
		frame.getContentPane().add(lblBrojGostiju);

		lblBrojZauzetih.setOpaque(true);
		lblBrojZauzetih.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBrojZauzetih.setBounds(363, 264, 310, 54);
		lblBrojZauzetih.setBorder(new LineBorder(new Color(0, 111, 128), 5, true));
		frame.getContentPane().add(lblBrojZauzetih);

		lblBrojSlobodnih.setOpaque(true);
		lblBrojSlobodnih.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBrojSlobodnih.setBounds(363, 136, 310, 54);
		lblBrojSlobodnih.setBorder(new LineBorder(new Color(0, 111, 128), 5, true));
		frame.getContentPane().add(lblBrojSlobodnih);

		lblBrojBoravaka.setOpaque(true);
		lblBrojBoravaka.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblBrojBoravaka.setBounds(363, 328, 310, 54);
		lblBrojBoravaka.setBorder(new LineBorder(new Color(0, 111, 128), 5, true));
		frame.getContentPane().add(lblBrojBoravaka);

		lblBrojGostiju.setHorizontalAlignment(SwingConstants.CENTER);
		lblBrojZauzetih.setHorizontalAlignment(SwingConstants.CENTER);
		lblBrojSlobodnih.setHorizontalAlignment(SwingConstants.CENTER);
		lblBrojBoravaka.setHorizontalAlignment(SwingConstants.CENTER);

		JSeparator separator = new JSeparator();
		separator.setBounds(21, 67, 652, 11);
		frame.getContentPane().add(separator);

		JLabel lblNewLabel = new JLabel("Statistika");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel.setBounds(460, 72, 154, 67);
		frame.getContentPane().add(lblNewLabel);

		JButton btnApartmani = new JButton("Apartmani");
		btnApartmani.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnApartmani.setBackground(new Color(102, 153, 153));
		btnApartmani.setForeground(Color.WHITE);
		btnApartmani.setBounds(21, 137, 310, 53);
		frame.getContentPane().add(btnApartmani);

		btnApartmani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ApartmanPopis(korisnik);
				frame.dispose();
			}
		});

		JButton btnGosti = new JButton("Gosti");
		btnGosti.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnGosti.setBackground(new Color(102, 153, 153));
		btnGosti.setForeground(Color.WHITE);
		btnGosti.setBounds(21, 201, 310, 53);
		frame.getContentPane().add(btnGosti);

		btnGosti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GostPopis(korisnik);
				frame.dispose();
			}
		});

		JButton btnRezervacije = new JButton("Rezervacije");
		btnRezervacije.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnRezervacije.setBackground(new Color(102, 153, 153));
		btnRezervacije.setForeground(Color.WHITE);
		btnRezervacije.setBounds(21, 265, 310, 53);
		frame.getContentPane().add(btnRezervacije);

		btnRezervacije.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RezervacijaPopis(korisnik);
				frame.dispose();
			}
		});

		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(347, 80, 2, 302);
		frame.getContentPane().add(separator_2);

		JButton btnRacuni = new JButton("Računi");
		btnRacuni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RacunPopis(korisnik);
				frame.dispose();
			}
		});
		btnRacuni.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnRacuni.setBackground(new Color(102, 153, 153));
		btnRacuni.setForeground(Color.WHITE);
		btnRacuni.setBounds(21, 329, 310, 53);
		frame.getContentPane().add(btnRacuni);

		JLabel lblNewLabel_1 = new JLabel("Popisi");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel_1.setBounds(140, 82, 192, 46);
		frame.getContentPane().add(lblNewLabel_1);

		JButton btnNewButton = new JButton("Izlaz");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(21, 406, 652, 50);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBackground(new Color(0, 111, 128));
		btnNewButton.setForeground(Color.WHITE);
		frame.getContentPane().add(btnNewButton);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(21, 393, 652, 11);
		frame.getContentPane().add(separator_1);
	}

	private void prikaziStatistiku() {
		try (Connection con = Database.getConnection()) {

			int id = korisnik.getId_korisnika();

			int brojGostiju = prebroji("SELECT COUNT(*) FROM Gost WHERE id_iznajmljivaca = ?", con, id);

			int brojRezervacija = prebroji("SELECT COUNT(*) FROM Rezervacija WHERE id_iznajmljivaca = ?", con, id);

			int brojRacuna = prebroji("SELECT COUNT(*) FROM Racun WHERE id_iznajmljivaca = ?", con, id);

			int brojApartmana = prebroji("SELECT COUNT(*) FROM Apartman WHERE id_iznajmljivaca = ?", con, id);

			lblBrojGostiju.setText("Evidentirani gosti: " + brojGostiju);
			lblBrojZauzetih.setText("Evidentirane rezervacije: " + brojRezervacija);
			lblBrojBoravaka.setText("Evidentirani računi: " + brojRacuna);
			lblBrojSlobodnih.setText("Evidentirani apartmani: " + brojApartmana);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Greška u dohvaćanju statistike: " + e.getMessage());
		}
	}

	private int prebroji(String query, Connection con, int id) throws Exception {
		try (PreparedStatement psmt = con.prepareStatement(query)) {
			psmt.setInt(1, id);
			try (ResultSet rs = psmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}
}
