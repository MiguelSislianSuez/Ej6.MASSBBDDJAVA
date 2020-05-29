package formularios;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.Driver;

public class Login extends JDialog {

	private JTextField txtUser;
	private JPasswordField txtPwd;
	private DialogCliente dc;

	public Login() {
		super();
		setTitle("Introduzca usuario y contraseña");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// dipose para mantener la maquina virtual funcionando y

		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error de registros del driver", "Error", JOptionPane.ERROR_MESSAGE);
		} // optimizar la memoria

		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 10, 10));// filas, columnas, distancias h y v

		// Etiqueta y cmpos de user

		JLabel lbUser = new JLabel("Usuario");
		txtUser = new JTextField(5);

		JLabel lbPass = new JLabel("Contraseña");
		txtPwd = new JPasswordField(5);
		txtPwd.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);

				if (e.getKeyCode() == KeyEvent.VK_ENTER)// si e. es igual a tecla llama a aceptar
					aceptar();
			}

		});

		// Hacer botones
		// Funcionalidades
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				aceptar();
			}
		});

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cancelar();

			}
		});

		// Siempre en orden de ventana
		panel.add(lbUser);
		panel.add(txtUser);
		panel.add(lbPass);
		panel.add(txtPwd);
		panel.add(btnAceptar);
		panel.add(btnCancelar);

		panel.setBorder(new EmptyBorder(30, 30, 30, 30));// emptyBorder para evitar que se pegue a lo laterales
		add(panel, BorderLayout.CENTER);
	}

	protected void cancelar() {

		if (dc == null) {// si no es igual a null...entonces vamos al dialogo

			System.exit(DO_NOTHING_ON_CLOSE);

			dc.setVisible(true);
			setVisible(false);

		} else if (DialogCliente.limpiar = true) { // si se da la condicion limpiar datos del log
			txtUser.setText("");
			txtPwd.setText("");

		}

	}

	private void aceptar() {
		Connection conn = null;
		String usuario = txtUser.getText();
		String password = String.valueOf(txtPwd.getPassword());

		try {

			if ("".equals(txtUser.getText())) {// si lacadena es equals a el txtUser pide
				JOptionPane.showMessageDialog(this, "Introduzca usuario", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (txtPwd.getPassword().length == 0 || txtPwd.getPassword() == null) {
				JOptionPane.showMessageDialog(this, "Introduzca contraseña", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid", usuario,
					password);

			dc = new DialogCliente(txtUser.getText(), String.valueOf(txtPwd.getPassword()), this);
			dc.setVisible(true);
			this.setVisible(false);
//			conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
//					usuario, password);

//			if ("".equals(txtUser.getText())) {// si lacadena es equals a el txtUser pide
//				JOptionPane.showMessageDialog(this, "Introduzca usuario", "Error", JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			if (txtPwd.getPassword().length == 0) {
//				JOptionPane.showMessageDialog(this, "Introduzca contraseña", "Error", JOptionPane.ERROR_MESSAGE);
//
////			if (txtPwd.getPassword().length == 0 || txtPwd.getPassword().equals(false)) {
////				JOptionPane.showMessageDialog(this, "Introduzca contraseña", "Error", JOptionPane.ERROR_MESSAGE);
//
//				return;
//			}

		} catch (SQLException ex) {// SQLexcetion se da cunado no hay conewxion o algun error con la bbdd
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Contraseña erronea", JOptionPane.ERROR_MESSAGE);

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
}

//Connection conn = null;
//String usuario = txtUser.getText();
//String password = String.valueOf(txtPwd.getPassword());
//
//try {
//        if ("".equals(txtUser.getText())) {// si lacadena es equals a el txtUser pide
//                JOptionPane.showMessageDialog(this, "Introduzca usuario", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//        }
//        if (txtPwd.getPassword().length == 0) {
//                JOptionPane.showMessageDialog(this, "Introduzca contraseña", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//        }
//        
//        conn = DriverManager.getConnection("jdbc:mysql://localhost/Liceo?serverTimezone=Europe/Madrid",
//            usuario, password);
//        
//        dc = new DialogCliente(txtUser.getText(), String.valueOf(txtPwd.getPassword()), this);
//        dc.setVisible(true);
//        this.setVisible(false);
