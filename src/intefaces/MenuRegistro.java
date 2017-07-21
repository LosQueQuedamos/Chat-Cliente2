package intefaces;

import java.awt.Color;

import javax.swing.JFrame;

import cliente.*;
import paqueteEnvios.Comando;

import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

/*import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;*/
import javax.swing.JButton;
import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class MenuRegistro extends JFrame {

	JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField pwPassword;

	public MenuRegistro(final Cliente cliente) {


		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized(cliente){
					cliente.setAccion(Comando.DESCONECTAR);
					cliente.notify();
				}
				dispose();
				System.exit(0);
			}
		});
		
		setTitle("Registrarse");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		setLayout(null);
		setBackground(Color.GRAY);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setBounds(0, 0, 444, 271);
		contentPane.setBackground(Color.GRAY);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBackground(Color.WHITE);
		lblUsuario.setBounds(113, 70, 57, 19);
		contentPane.add(lblUsuario, new Integer(1));
		lblUsuario.setForeground(Color.BLACK);
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBackground(Color.BLACK);
		lblPassword.setBounds(113, 121, 65, 17);
		contentPane.add(lblPassword, new Integer(1));
		lblPassword.setForeground(Color.BLACK);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setBounds(143, 182, 153, 23);
		contentPane.add(btnRegistrarse, new Integer(1));
		btnRegistrarse.setFocusable(false);

		pwPassword = new JPasswordField();
		pwPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(cliente){
					cliente.getPaqueteUsuario().setUsername(txtUsuario.getText());
					cliente.getPaqueteUsuario().setPassword(pwPassword.getText());
					cliente.setAccion(Comando.REGISTRO);
					cliente.notify();
				}
				dispose();
			}
		});
		pwPassword.setBounds(199, 120, 118, 20);
		contentPane.add(pwPassword, new Integer(1));

		txtUsuario = new JTextField();
		txtUsuario.setBounds(199, 69, 118, 20);
		contentPane.add(txtUsuario, new Integer(1));
		txtUsuario.setColumns(10);
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(cliente){
					cliente.getPaqueteUsuario().setUsername(txtUsuario.getText());
					cliente.getPaqueteUsuario().setPassword(pwPassword.getText());
					cliente.setAccion(Comando.REGISTRO);
					cliente.notify();
				}
				dispose();
			}
		});

	}

	public JTextField gettxtUsuario() {
		return txtUsuario;
	}

	public void settxtUsuario(JTextField txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public JPasswordField getPasswordField() {
		return pwPassword;
	}

	public void setPasswordField(JPasswordField pwPassword) {
		this.pwPassword = pwPassword;
	}
}