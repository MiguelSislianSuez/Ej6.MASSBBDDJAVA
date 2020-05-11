package formularios;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.Driver;

public class DialogCliente extends JFrame {
	
	private String usuario;
	private String contrasena;
	private Login login;
	private JPanel pnlCampos;
	private JTextField txtCodigo;
	private JTextField txtDescripcion;
	private JComboBox <String> txtTipo;
	private JTextField txtmarca;
	private JTextField txtmodelo;
	private JTextField txtNumSerie;
	private JTextField txtResponsable;
	private JTextField txtLocalAula;
	private JTextField txtFechaModificacion;
	private JTextField txtFechaAlta;
	private JTextField txtFechaBaja;
	private JTextField txtMotBaja;
	private JTextField txtObservaciones;
	
	public DialogCliente(String usuario, String contrasena, Login login) throws HeadlessException {

		super("Clientes");
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.login = login;

		setSize(700, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// ----------Menú superior------------

//		menuSup();
		crearCampos();
		crearBotonesDialog();

		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error de registros del driver", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void crearCampos() {
		pnlCampos = new JPanel();
		
		pnlCampos.setLayout(new GridLayout(14, 2, 0, 0));
		pnlCampos.setBorder(new EmptyBorder(30, 30, 0, 30));
		
		pnlCampos.add(new JLabel("Código"));
		txtCodigo = new JTextField();
		pnlCampos.add(txtCodigo);
		
		pnlCampos.add(new JLabel("Descripción"));
		txtDescripcion = new JTextField();
		pnlCampos.add(txtDescripcion);
		
		pnlCampos.add(new JLabel("Tipo"));
		txtTipo = new JComboBox<String>();
		pnlCampos.add(txtTipo);
		txtTipo.addItem("PC");
		txtTipo.addItem("Proyector");
		txtTipo.addItem("Pantalla");
		txtTipo.addItem("Pantalla Interactiva");
		txtTipo.addItem("Tablet");
		txtTipo.addItem("HIFI");
		txtTipo.addItem("TV");
		txtTipo.addItem("DVD");
		txtTipo.addItem("Combo");

		
		pnlCampos.add(new JLabel("Marca"));
		txtmarca = new JTextField();
		pnlCampos.add(txtmarca);
		
		pnlCampos.add(new JLabel("Modelo"));
		txtmodelo = new JTextField();
		pnlCampos.add(txtmodelo);
		
		pnlCampos.add(new JLabel("Número de serie"));
		txtNumSerie = new JTextField();
		pnlCampos.add(txtNumSerie);
		
		pnlCampos.add(new JLabel("Responsable"));
		txtResponsable = new JTextField();
		pnlCampos.add(txtResponsable);
		
		pnlCampos.add(new JLabel("Local/Aula"));
		txtLocalAula = new JTextField();
		pnlCampos.add(txtLocalAula);
		
		pnlCampos.add(new JLabel("Fecha de Alta"));
		txtFechaAlta = new JTextField();
		pnlCampos.add(txtFechaAlta);
		
		pnlCampos.add(new JLabel("Fecha de Modificación"));
		txtFechaModificacion = new JTextField();
		pnlCampos.add(txtFechaModificacion);
		
		pnlCampos.add(new JLabel("Fecha de baja"));
		txtFechaBaja = new JTextField();
		pnlCampos.add(txtFechaBaja);
		
		pnlCampos.add(new JLabel("Motivo de baja"));
		txtMotBaja = new JTextField();
		pnlCampos.add(txtMotBaja);
		
		pnlCampos.add(new JLabel("Observaciones"));
		txtObservaciones = new JTextField();
		pnlCampos.add(txtObservaciones);
		
		add(pnlCampos, BorderLayout.NORTH);// para que todos los campos que meta entren por arriba
		
	}

	private void crearBotonesDialog() {
		JPanel pnlBotons = new JPanel(new GridLayout(1,2,0,0));
		pnlBotons.setBorder(new EmptyBorder(0,10,10,10));
		
		JButton btnGrabar = new JButton("Grabar datos");
		btnGrabar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		pnlBotons.add(btnGrabar);
		
		JButton btnBaja = new JButton("Dar de baja");
		btnGrabar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		pnlBotons.add(btnBaja);
		add(pnlBotons, BorderLayout.SOUTH);
	}


}
