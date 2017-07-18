package cliente;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

import intefaces.Chat;
import intefaces.VentanaPrincipal;
import paqueteEnvios.*;
/**La clase EscuchaMensajes tiene como función  
 * esuchar los mensajes que se enviaran
 * al servidor.
 */
public class EscuchaMensajes extends Thread {

	private Cliente cliente;
	private ObjectInputStream entrada;
	private final Gson gson = new Gson();
	private Chat chat;

	protected static ArrayList<String> usuariosConectados = new ArrayList<String>();
	/**Constructor de EsuchaMensaje
	 * @param juego juego del que se escucha el mensaje
	 */
	public EscuchaMensajes(final Cliente cliente) {
		this.cliente = cliente;
		entrada = cliente.getEntrada();
	}

	@Override
	public void run() {

		try {

			Paquete paquete;
			PaqueteUsuario paqueteUsuario;
			
			ArrayList<String> usuariosAntiguos = new ArrayList<String>();
			ArrayList<String> diferenciaContactos = new ArrayList<String>();

			while (true) {

				String objetoLeido = (String) entrada.readObject();

				paquete = gson.fromJson(objetoLeido , Paquete.class);

				switch (paquete.getComando()) {

				case Comando.CONEXION:
						usuariosConectados = (ArrayList<String>) gson.fromJson(objetoLeido, PaqueteDeUsuarios.class)
						.getPersonajes();
						for (String usuario : usuariosConectados) {
							if (!usuariosAntiguos.contains(usuario)) {
								usuariosAntiguos.add(usuario);
							}
						}
						diferenciaContactos = new ArrayList<String>(usuariosAntiguos);
						diferenciaContactos.removeAll(usuariosConectados);
						if (!diferenciaContactos.isEmpty()) {
							for (String usuario : diferenciaContactos) {
							if (cliente.getChatsActivos().containsKey(usuario)) {
							cliente.getChatsActivos().get(usuario).getChat()
							.append("El usuario: " + usuario + " se ha desconectado\n");
						}
						usuariosAntiguos.remove(usuario);
						}
					}
					cliente.getPaqueteUsuario().setListaDeConectados(usuariosConectados);
					actualizarLista(cliente);
					break;

				case Comando.INICIOSESION:
					cliente.getPaqueteUsuario().setMensaje(paquete.getMensaje());

					if (paquete.getMensaje().equals(Paquete.msjFracaso)) {
						JOptionPane.showMessageDialog(null, "Usuario existente, por favor logee con otro usuario.");
						this.stop();
					} else {
						usuariosConectados = (ArrayList<String>) gson.fromJson(objetoLeido, PaqueteDeUsuarios.class)
								.getPersonajes();
					}
					break;
					
				case Comando.TALK:

					cliente.setPaqueteMensaje((PaqueteMensaje) gson.fromJson(objetoLeido, PaqueteMensaje.class));

					if (!(cliente.getChatsActivos().containsKey(cliente.getPaqueteMensaje().getUserEmisor()))) {
						chat = new Chat(cliente);

						chat.setTitle(cliente.getPaqueteMensaje().getUserEmisor());
						chat.setVisible(true);

						cliente.getChatsActivos().put(cliente.getPaqueteMensaje().getUserEmisor(), chat);
					}
					cliente.getChatsActivos().get(cliente.getPaqueteMensaje().getUserEmisor()).getChat()
							.append(cliente.getPaqueteMensaje().getUserEmisor() + ": "
									+ cliente.getPaqueteMensaje().getMensaje() + "\n");
					cliente.getChatsActivos().get(cliente.getPaqueteMensaje().getUserEmisor()).getTexto().grabFocus();
					break;

				case Comando.CHATALL:

					cliente.setPaqueteMensaje((PaqueteMensaje) gson.fromJson(objetoLeido, PaqueteMensaje.class));
					if (!cliente.getChatsActivos().containsKey("Sala")) {

						VentanaPrincipal.setTextoChatGeneral(cliente.getPaqueteMensaje().getUserEmisor(),cliente.getPaqueteMensaje().getMensaje());
					}
					break;
					
				case Comando.MP:

					cliente.setPaqueteMensaje((PaqueteMensaje) gson.fromJson(objetoLeido, PaqueteMensaje.class));

					if (!(cliente.getChatsActivos().containsKey(cliente.getPaqueteMensaje().getUserEmisor()))) {
						chat = new Chat(cliente);

						chat.setTitle(cliente.getPaqueteMensaje().getUserEmisor());
						chat.setVisible(true);

						cliente.getChatsActivos().put(cliente.getPaqueteMensaje().getUserEmisor(), chat);
					}
					cliente.getChatsActivos().get(cliente.getPaqueteMensaje().getUserEmisor()).getChat()
							.append(cliente.getPaqueteMensaje().getUserEmisor() + ": "
									+ cliente.getPaqueteMensaje().getMensaje() + "\n");
					cliente.getChatsActivos().get(cliente.getPaqueteMensaje().getUserEmisor()).getTexto().grabFocus();
					break;	
					
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexión con el servidor.");
		}
	}

	private void actualizarLista(final Cliente cliente) {
		DefaultListModel<String> modelo = new DefaultListModel<String>();
		synchronized (cliente) {
			try {
				cliente.wait(300);
				VentanaPrincipal.getList().removeAll();
				if (cliente.getPaqueteUsuario().getListaDeConectados() != null) {
					cliente.getPaqueteUsuario().getListaDeConectados()
							.remove(cliente.getPaqueteUsuario().getUsername());
					for (String cad : cliente.getPaqueteUsuario().getListaDeConectados()) {
						modelo.addElement(cad);
					}
					VentanaPrincipal.setCantUsuariosCon(modelo.getSize());
					VentanaPrincipal.getList().setModel(modelo);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**Pide los personajes conectados
	 * @return devuelve los personajes conectados
	 */
	public static ArrayList<String> getUsuariosConectados() {
		return usuariosConectados;
	}
}