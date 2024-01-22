package components;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import framework.Main;
import framework.SuperSocketMaster;

public class ChatPanel extends JPanel implements ActionListener {

    private SuperSocketMaster ssm;
    private JTextArea chatTextArea = new JTextArea(30, 30);
    private JTextField chatInput = new JTextField(20);
    private JScrollPane scrollPane = new JScrollPane(chatTextArea);
    private JButton sendButton = new JButton("Send");

    public ChatPanel(SuperSocketMaster ssm) {
        this.ssm = ssm;

        chatTextArea.setEditable(false);
        sendButton.addActionListener(this);

        add(scrollPane);
        add(chatInput);
        add(sendButton);
    }

    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() == sendButton){
            try{
                String strOutMessage = chatInput.getText();

                if(Main.intSessionId == 1){
                    ssm.sendText("h>a>cCHAT~"+strOutMessage+", "+(Main.intSessionId-1));
                    chatTextArea.append(strOutMessage+"\n");
                    chatInput.setText("");
                }else{
                    ssm.sendText("c"+Main.intSessionId+">h>cCHAT~"+strOutMessage+", "+(Main.intSessionId-1));
                    chatTextArea.append(strOutMessage+"\n");
                    chatInput.setText("");
                }
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        }
        if(evt.getSource() == Main.ssm){
            String strInMessage = Main.ssm.readText();
            System.out.println(strInMessage);
            chatTextArea.append(strInMessage+"\n");
        }
    }
}