import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

public class Chatbot extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtRequest=null;
	public static TextArea txtArea=null;
	public static String request="";
	
	public Chatbot(Bot bot) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 62, 527, 247);
		getContentPane().add(scrollPane);
		
		txtArea = new TextArea();
		scrollPane.setViewportView(txtArea);
		txtArea.setEditable(false);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(448, 11, 89, 40);
		getContentPane().add(btnEnviar);
		
		txtRequest = new JTextField();
		txtRequest.setBounds(10, 11, 428, 40);
		getContentPane().add(txtRequest);
		txtRequest.setColumns(10);
		
		Chat chatSession = new Chat(bot);
			
		btnEnviar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String request="";
				String response="";
				request=txtRequest.getText();
				txtArea.append("Usuario: "+request+"\n");
				txtRequest.setText("");
				response=chatSession.multisentenceRespond(request);
				txtArea.append("Bot: "+response+"\n");
				if (response.contains("intent")){
					String[] parameters = response.split(":");
					for (int i=0;i<parameters.length;i++){
						txtArea.append("Parameters: "+parameters[i]+"\n");
					}
				}
			}
		});
		this.getRootPane().setDefaultButton(btnEnviar);
	}

}