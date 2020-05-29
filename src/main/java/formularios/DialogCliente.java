package formularios;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.Driver;

public class DialogCliente extends JFrame {

	private String usuario;
	private String contrasena;
	private Login login;
	private JPanel pnlCampos;
	private JTextField txtCodigo;
	private JTextField txtDescripcion;
	private JComboBox<String> txtTipo;
	private JTextField txtmarca;
	private JTextField txtmodelo;
	private JTextField txtNumSerie;
	private JTextField txtResponsable;
	private JTextField txtLocalAula;
	private JTextField txtFechaModificacion;
	private JTextField txtFechaAlta;
	private JTextField txtFechaBaja;
	private JTextField txtMotBaja;
	// private JTextField txtObservaciones;
	private JTextArea txtObservaciones;
	protected static boolean limpiar = false;
	private static boolean isCrear = false;

	public DialogCliente(String usuario, String contrasena, Login login) throws HeadlessException {

		super("Inventario");
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.login = login;

		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(true);

		// ----------Menú superior------------

		menuSup();
		crearCampos();
		crearBotonesDialog();

		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error de registros del driver", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void menuSup() {
		// +------**Opciones**-------+
		JMenuBar menuBar = new JMenuBar();
		JMenu dOpciones = new JMenu("Opciones");
		dOpciones.setMnemonic(KeyEvent.VK_O);

		// --*Cambiar Usuario*--
		JMenuItem itmCambioUsr = new JMenuItem("Cambiar usuario");
		itmCambioUsr.setMnemonic(KeyEvent.VK_U);
		itmCambioUsr.setAccelerator(KeyStroke.getKeyStroke("ctrl-U"));

		itmCambioUsr.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				cambiarUsuario();

			}
		});

		// --*Cargar datos*--
		JMenuItem cDatos = new JMenuItem("Cargar datos");
		cDatos.setMnemonic(KeyEvent.VK_D);
		cDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl-D"));

		cDatos.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				cargarDatos();

			}
		});

		// --*Limpiar datos*--
		JMenuItem lDatos = new JMenuItem("Limpiar datos");
		lDatos.setMnemonic(KeyEvent.VK_L);
		lDatos.setAccelerator(KeyStroke.getKeyStroke("ctrl-L"));

		lDatos.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				limpiarDatos();

			}
		});

		// +------**Informes**-------+
		JMenu dInfo = new JMenu("Informes");
		dInfo.setMnemonic(KeyEvent.VK_I);

		// --*Actuales*--
		JMenuItem itmActuales = new JMenuItem("Actuales");
		itmActuales.setMnemonic(KeyEvent.VK_T);
		itmActuales.setAccelerator(KeyStroke.getKeyStroke("ctrl-T"));

		itmActuales.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mostrarActual();
			}
		});

		// --*Actuales + Bajas*--
		JMenuItem itmActBajas = new JMenuItem("Actuales + Bajas");
		itmActBajas.setMnemonic(KeyEvent.VK_B);
		itmActBajas.setAccelerator(KeyStroke.getKeyStroke("ctrl-B"));

		itmActBajas.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mostrarActualesBajas();

			}
		});

		// --*Histórico*--
		JMenuItem itmHistoria = new JMenuItem("Histórico");
		itmHistoria.setMnemonic(KeyEvent.VK_I);
		itmHistoria.setAccelerator(KeyStroke.getKeyStroke("ctrl-I"));

		itmHistoria.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mostrarHistorico();
			}
		});

		// --*Responsable/Aula*--
		JMenuItem itmResponsable = new JMenuItem("Responsable/Aula");
		itmResponsable.setMnemonic(KeyEvent.VK_R);
		itmResponsable.setAccelerator(KeyStroke.getKeyStroke("ctrl-R"));

		itmResponsable.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mostrarResponsableAula();
			}
		});

		dOpciones.add(itmCambioUsr);
		dOpciones.add(cDatos);
		dOpciones.add(lDatos);

		dInfo.add(itmActuales);
		dInfo.add(itmActBajas);
		dInfo.add(itmHistoria);
		dInfo.add(itmResponsable);

		menuBar.add(dOpciones);
		menuBar.add(dInfo);
		setJMenuBar(menuBar);
	}

	protected void mostrarResponsableAula() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);

			String cod = JOptionPane.showInputDialog(this, "Introduzca Responsable o aula", "Cargar datos",
					JOptionPane.QUESTION_MESSAGE);
			if (cod == null || cod.equals("")) {// si dni es diferente a null y a campo vacio sale del dialogo

				// if (cod == null) {
				JOptionPane.showMessageDialog(this, "No existe ningún cliente con el código " + cod, "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			PreparedStatement ps;
			String consulta = "SELECT * FROM Actual WHERE Resp LIKE ? OR local LIKE ? ";

			ps = conn.prepareStatement(consulta);
			ps.setString(1, cod);
			ps.setString(2, cod);

			ResultSet rs = ps.executeQuery();// almacenamos consultaa

			new VistaListados(rs);

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}

	}

	protected void mostrarHistorico() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);

			PreparedStatement ps;
			String consulta = "SELECT * FROM Historico";

			ps = conn.prepareStatement(consulta);

			ResultSet rs = ps.executeQuery();// almacenamos consultaa

			new VistaListados(rs);

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}

	}

	protected void mostrarActualesBajas() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);

			PreparedStatement ps;
			String consulta = "SELECT * FROM actual";

			ps = conn.prepareStatement(consulta);

			ResultSet rs = ps.executeQuery();// almacenamos consultaa

			new VistaListados(rs);

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}

	}

	protected void mostrarActual() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);

			PreparedStatement ps;
			String consulta = "SELECT * FROM liceo.actual WHERE cod NOT IN (SELECT cod FROM liceo.actual WHERE length(fecbaja)>1)";

			ps = conn.prepareStatement(consulta);

			ResultSet rs = ps.executeQuery();// almacenamos consultaa

			new VistaListados(rs);

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}

	}

	protected void cargarDatos() {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);

			String cod = JOptionPane.showInputDialog(this, "Introduzca código", "Cargar datos",
					JOptionPane.QUESTION_MESSAGE);
			if (cod == null || cod.equals("")) {// si dni es diferente a null y a campo vacio sale del dialogo

				// if (cod == null) {
				JOptionPane.showMessageDialog(this, "No existe ningún cliente con el código " + cod, "Error",
						JOptionPane.ERROR_MESSAGE);

//			}else {
//				JOptionPane.showMessageDialog(this, "No existe ningún cliente " + cod, "Error",
//						JOptionPane.ERROR_MESSAGE);

				return;

			}
			
		mostrarDatos(conn, cod);
			isCrear = true;

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}

	}

	protected void limpiarDatos() {

		Component[] componentes = pnlCampos.getComponents();

		for (Component componente : componentes) {
			if (componente instanceof JTextField) {
				JTextField jtf = (JTextField) componente;
				jtf.setText("");
				txtObservaciones.setText("");
				isCrear = false;
			}
		}
	}

	private void crearCampos() {
		pnlCampos = new JPanel();

		pnlCampos.setLayout(new GridLayout(14, 2, 0, 0));
		pnlCampos.setBorder(new EmptyBorder(30, 30, 0, 30));

		pnlCampos.add(new JLabel("Código"));
		txtCodigo = new JTextField();
		txtCodigo.setEnabled(false);
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
		txtFechaAlta.setEnabled(false);
		pnlCampos.add(txtFechaAlta);

		pnlCampos.add(new JLabel("Fecha de Modificación"));
		txtFechaModificacion = new JTextField();
		txtFechaModificacion.setEnabled(false);
		pnlCampos.add(txtFechaModificacion);

		pnlCampos.add(new JLabel("Fecha de baja"));
		txtFechaBaja = new JTextField();
		txtFechaBaja.setEnabled(false);
		pnlCampos.add(txtFechaBaja);

		pnlCampos.add(new JLabel("Motivo de baja"));
		txtMotBaja = new JTextField();
		txtMotBaja.setEnabled(false);
		pnlCampos.add(txtMotBaja);

		pnlCampos.add(new JLabel("Observaciones"));
//		txtObservaciones = new JTextField();
//		pnlCampos.add(txtObservaciones);
		txtObservaciones = new JTextArea(1, 100);
//		txtObservaciones.setWrapStyleWord(true);
//		txtObservaciones.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(txtObservaciones);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		scroll.getViewport().add(txtObservaciones);
//	    scroll.getViewport().setPreferredSize(txtObservaciones.getPreferredSize());
//	  
		pnlCampos.add(scroll);

		add(pnlCampos, BorderLayout.NORTH);// para que todos los campos que meta entren por arriba
	}

	private void crearBotonesDialog() {
		JPanel pnlBotons = new JPanel(new GridLayout(1, 2, 0, 0));
		pnlBotons.setBorder(new EmptyBorder(0, 10, 10, 10));

		JButton btnGrabar = new JButton("Grabar datos");
		btnGrabar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				grabarDatos();

			}
		});
		pnlBotons.add(btnGrabar);

		JButton btnBaja = new JButton("Dar de baja");
		btnBaja.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				darDeBaja();
			}
		});
		pnlBotons.add(btnBaja);
		add(pnlBotons, BorderLayout.SOUTH);
	}

	protected void darDeBaja() {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);

			String motivo = JOptionPane.showInputDialog(this, "Introduzca un motivo", "Dar de baja",
					JOptionPane.QUESTION_MESSAGE);

			if (motivo.equals("")) {// si dni es diferente a null y a campo vacio sale del dialogo
				JOptionPane.showMessageDialog(this, "Debe introducir un motivo", "Error", JOptionPane.ERROR_MESSAGE);
				return;

			}

			String consulta = "UPDATE actual SET FecBaja = current_timeStamp, Motivo = ? WHERE Cod = ?";
			PreparedStatement ps = conn.prepareStatement(consulta);

			ps.setString(1, motivo);
			ps.setString(2, txtCodigo.getText());

			ps.executeUpdate();

			String consultaH = "INSERT INTO Historico (Cod, Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, FecMod, FecBaja, Motivo, Obs) "
					+ "SELECT Cod, Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, FecMod, FecBaja, Motivo, Obs FROM Actual WHERE Cod = ?";

			PreparedStatement ps2 = conn.prepareStatement(consultaH);
			ps2.setString(1, txtCodigo.getText());

			ps2.executeUpdate();
			
			mostrarDatos(conn, txtCodigo.getText());

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}

	}

	protected void grabarDatos() {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
					this.usuario, this.contrasena);
			String consulta;
			String consulta2;
			int codId = 0;

			if (isCrear == false) {
				consulta = "INSERT INTO Actual (Des, Tipo, Marca, Modelo, NumSerie, Resp, Local,FecAlta, Obs) VALUES (?,?,?,?,?,?,?,current_timestamp,?)";
			} else {
				consulta = "UPDATE actual SET Des = ?, Tipo = ?, Marca = ?, Modelo = ?, NumSerie = ?, Resp = ?, Local = ?, FecAlta = current_timestamp, Motivo = ?, Obs = ? WHERE Cod = ?";
			}
			consulta2 = "INSERT INTO Historico (Cod, Des, Tipo, Marca, Modelo, NumSerie, Resp, Local,FecAlta, Obs) VALUES (?,?,?,?,?,?,?,?,current_timestamp,?)";

			PreparedStatement ps = conn.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
			// pasamos valores que queremos y hacemos las comprobaciones
			if (txtDescripcion.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca un valor para descripción", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// int code = 0;
			// ps.setString(1, txtCodigo.getText());

			ps.setString(1, txtDescripcion.getText());

//			if (txtTipo.getText().equals("")) {
//				JOptionPane.showMessageDialog(this, "Introduzca un valor para primer apellido", "Error",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//						}
			ps.setString(2, txtTipo.getSelectedItem() + "");

			if (txtmarca.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca un valor para marca", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			ps.setString(3, txtmarca.getText());

			if (txtmodelo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca un valor para primer modelo", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			ps.setString(4, txtmodelo.getText());

			ps.setString(5, !txtNumSerie.getText().equals("") ? txtNumSerie.getText() : null);

			ps.setString(6, !txtResponsable.getText().equals("") ? txtResponsable.getText() : null);

			ps.setString(7, !txtLocalAula.getText().equals("") ? txtLocalAula.getText() : null);

			ps.setString(8, !txtMotBaja.getText().equals("") ? txtMotBaja.getText() : null);

			int rowAffected = ps.executeUpdate();
			if (rowAffected == 1) {
				ResultSet rs = ps.getGeneratedKeys();// trae todas las clave primaria del resultado
				if (rs.next()) {
					// recordar llamar al next siempre que hagamos resultSet sino el cursor no
					// apuntará a la siguiente fila
					codId = rs.getInt(1); //
				}

			}

			PreparedStatement ps2 = conn.prepareStatement(consulta2);
			ps2.setInt(1, codId);
			ps2.setString(2, txtDescripcion.getText());

//			if (txtTipo.getText().equals("")) {
//				JOptionPane.showMessageDialog(this, "Introduzca un valor para primer apellido", "Error",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//						}
			ps2.setString(3, txtTipo.getSelectedItem() + "");

			if (txtmarca.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca un valor para marca", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			ps2.setString(4, txtmarca.getText());

			if (txtmodelo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Introduzca un valor para primer modelo", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			ps2.setString(5, txtmodelo.getText());

			ps2.setString(6, !txtNumSerie.getText().equals("") ? txtNumSerie.getText() : null);

			ps2.setString(7, !txtResponsable.getText().equals("") ? txtResponsable.getText() : null);

			ps2.setString(8, !txtLocalAula.getText().equals("") ? txtLocalAula.getText() : null);

			ps2.setString(9, !txtMotBaja.getText().equals("") ? txtMotBaja.getText() : null);

			ps2.executeUpdate();

			String msResult;

			if (isCrear == false) {
				msResult = "Se han creado datos";
			} else {
				msResult = "Se han creado datos en el histórico";
			}

			JOptionPane.showMessageDialog(this, msResult, "Info", JOptionPane.INFORMATION_MESSAGE);

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al cargar datos", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex1) {
					System.out.println("Excepción. Cerrando conexión");
				}
			}
		}
	}

	protected void cambiarUsuario() {
		// limpiar = true;
		// login = new Login(); //nos borra los datos del log al cambiar de usuario
		login.setVisible(true);
		setVisible(false);

	}
	
	private void mostrarDatos(Connection conn, String cod) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(
				"SELECT Cod, Des, Tipo, Marca, Modelo, NumSerie, Resp, Local, FecAlta, FecMod, FecBaja, Motivo, Obs FROM actual WHERE Cod = ?");
		ps.setString(1, cod);// valor un que se gurda en resultst
		ResultSet rs = ps.executeQuery(); //
		if (rs.first()) {

			txtCodigo.setText(cod);
			// txtCodigo.setEditable(false);
			txtDescripcion.setText(rs.getString("Des"));
			txtTipo.setSelectedItem(rs.getString("Tipo"));
			txtmarca.setText(rs.getString("Marca"));
			txtmodelo.setText(rs.getString("Modelo"));
			txtNumSerie.setText(rs.getString("NumSerie"));
			txtResponsable.setText(rs.getString("Resp"));
			txtLocalAula.setText(rs.getString("Local"));
			txtFechaModificacion.setText(rs.getTimestamp("FecMod") + "");
			txtFechaAlta.setText(rs.getTimestamp("FecAlta") + "");
			txtFechaBaja.setText(rs.getTimestamp("FecBaja") + "");
			txtMotBaja.setText(rs.getString("Motivo"));
			txtObservaciones.setText(rs.getString("Obs"));
		}
		
	}

}
