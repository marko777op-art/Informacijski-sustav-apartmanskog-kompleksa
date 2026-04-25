package zavrsniRad;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GostPregled {

	private JFrame frame;
	private JLabel txtImeGosta, txtPrezimeGosta, txtDatumRodenja, txtAdresaGosta, txtDrzavljanstvo, txtVrstaDokumenta, txtBrojDokumenta, txtBrojTelefona;
	private JLabel lblDravljanstvo;
	private JLabel lblVrstaDokumenta;
	private JLabel lblBrojDokumenta;

	public GostPregled(GetterSetter korisnik, GostPopis gostPopis, int selectedId, String ime, String prezime, String datum, String adresa, String drzavljanstvo, String dokument, String brojDokumenta, String telefon) {
		initialize(ime, prezime, datum, adresa, drzavljanstvo, dokument, brojDokumenta, telefon);
		frame.setVisible(true);
	}

	private void initialize(String ime, String prezime, String datum, String adresa, String drzavljanstvo, String dokument, String brojDokumenta, String telefon) {
		frame = new JFrame();
		frame.setTitle("APARTMANSKI SUSTAV - Pregled gosta");
		frame.setBounds(100, 100, 509, 476);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNaslov = new JLabel("Pregled gosta");
		lblNaslov.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNaslov.setBounds(180, 20, 190, 31);
		frame.getContentPane().add(lblNaslov);

		JLabel lblImeGosta = new JLabel("Ime gosta:");
		lblImeGosta.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblImeGosta.setBounds(33, 77, 167, 22);
		frame.getContentPane().add(lblImeGosta);

		txtImeGosta = new JLabel(ime);
		txtImeGosta.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		txtImeGosta.setBounds(160, 77, 297, 28);
		txtImeGosta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtImeGosta);

		JLabel lblPrezimeGosta = new JLabel("Prezime gosta:");
		lblPrezimeGosta.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblPrezimeGosta.setBounds(33, 115, 167, 22);
		frame.getContentPane().add(lblPrezimeGosta);

		txtPrezimeGosta = new JLabel(prezime);
		txtPrezimeGosta.setFont(new Font("Arial Nova", Font.PLAIN, 12));
		txtPrezimeGosta.setBounds(160, 115, 297, 28);
		txtPrezimeGosta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtPrezimeGosta);

		JLabel lblDatumRodenja = new JLabel("Datum rođenja:");
		lblDatumRodenja.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblDatumRodenja.setBounds(33, 153, 167, 22);
		frame.getContentPane().add(lblDatumRodenja);

		txtDatumRodenja = new JLabel(String.valueOf(datum));
		txtDatumRodenja.setFont(new Font("Arial Nova", Font.PLAIN, 12));

		txtDatumRodenja.setBounds(160, 153, 297, 28);
		txtDatumRodenja.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtDatumRodenja);

		JLabel lblAdresaGosta = new JLabel("Adresa gosta:");
		lblAdresaGosta.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblAdresaGosta.setBounds(33, 191, 167, 22);
		frame.getContentPane().add(lblAdresaGosta);

		txtAdresaGosta = new JLabel(adresa);
		txtAdresaGosta.setFont(new Font("Arial Nova", Font.PLAIN, 12));

		txtAdresaGosta.setBounds(160, 191, 297, 28);
		txtAdresaGosta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtAdresaGosta);

		JButton btnNatrag = new JButton("Natrag");
		btnNatrag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNatrag.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNatrag.setBounds(33, 385, 424, 40);
		btnNatrag.setBackground(new Color(0, 111, 128));
		btnNatrag.setForeground(Color.WHITE);
		frame.getContentPane().add(btnNatrag);

		JSeparator separator = new JSeparator();
		separator.setBounds(33, 55, 424, 16);
		frame.getContentPane().add(separator);

		txtDrzavljanstvo = new JLabel(drzavljanstvo);
		txtDrzavljanstvo.setFont(new Font("Arial Nova", Font.PLAIN, 12));

		txtDrzavljanstvo.setBounds(160, 229, 297, 28);
		txtDrzavljanstvo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtDrzavljanstvo);

		txtVrstaDokumenta = new JLabel(dokument);
		txtVrstaDokumenta.setFont(new Font("Arial Nova", Font.PLAIN, 12));

		txtVrstaDokumenta.setBounds(160, 267, 297, 28);
		txtVrstaDokumenta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtVrstaDokumenta);

		txtBrojDokumenta = new JLabel(brojDokumenta);
		txtBrojDokumenta.setFont(new Font("Arial Nova", Font.PLAIN, 12));

		txtBrojDokumenta.setBounds(160, 305, 297, 28);
		txtBrojDokumenta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtBrojDokumenta);

		JLabel lblBrojTelefona = new JLabel("Broj telefona:");
		lblBrojTelefona.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblBrojTelefona.setBounds(33, 344, 115, 22);
		frame.getContentPane().add(lblBrojTelefona);

		txtBrojTelefona = new JLabel(telefon);
		txtBrojTelefona.setFont(new Font("Arial Nova", Font.PLAIN, 12));

		txtBrojTelefona.setBounds(160, 343, 297, 28);
		txtBrojTelefona.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 111, 128), 2, true), new EmptyBorder(0, 3, 0, 0)));
		frame.getContentPane().add(txtBrojTelefona);

		lblDravljanstvo = new JLabel("Državljanstvo:");
		lblDravljanstvo.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblDravljanstvo.setBounds(33, 230, 167, 22);
		frame.getContentPane().add(lblDravljanstvo);

		lblVrstaDokumenta = new JLabel("Vrsta dokumenta:");
		lblVrstaDokumenta.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblVrstaDokumenta.setBounds(33, 268, 167, 22);
		frame.getContentPane().add(lblVrstaDokumenta);

		lblBrojDokumenta = new JLabel("Broj dokumenta:");
		lblBrojDokumenta.setFont(new Font("Arial Nova", Font.BOLD, 12));
		lblBrojDokumenta.setBounds(33, 306, 167, 22);
		frame.getContentPane().add(lblBrojDokumenta);

	}
}
