package framework;

import objects.Bullet;

public class Network {

    private SuperSocketMaster ssm = null;

    public Network(SuperSocketMaster ssm) {
        this.ssm = ssm;
    }

    public void readMessage() { 
        String strInput = ssm.readText();
                
        if(strInput.substring(0,1).equals("c")) {
            String[] strMessage = strInput.split(",");
            System.out.println(strMessage[1]);
            //chatbox.append(strmessage[1] + "\n");
        } else if(strInput.substring(0, 1).equals("m")) {
            String[] strSelection = strInput.split(",");
            if(strSelection[1].equals("start")) {
                Main.theFrame.setContentPane(Main.characterPanel);
                Main.theFrame.pack();
            } else if(strSelection[1].equals("ready")) {
                Main.theFrame.setContentPane(Main.gamePanel);
                Main.gamePanel.setFocusable(true);
                Main.gamePanel.requestFocus();
                Main.theFrame.pack();
            } else if(strSelection[1].equals("join")) {
                if(Main.intjoinid == 0){    
                    Main.intjoinid = Integer.parseInt(strSelection[2]);      
                }
            } else if(strSelection[1].equals("charbutton")) {
                Main.intcharbutton[Integer.parseInt(strSelection[2])] = Integer.parseInt(strSelection[3]);
                Main.characterButtons[Integer.parseInt(strSelection[3])].setEnabled(false);
            } else if(strSelection[1].equals("oldbutton")) {
                Main.characterButtons[Integer.parseInt(strSelection[2])].setEnabled(true);
            }
        } else if(strInput.substring(0,1).equals("o")) {
            String[] strSelection = strInput.split(",");
            if (strSelection[1].equals("b")){
                Main.handler.addObject(new Bullet(Float.parseFloat(strSelection[2]), Float.parseFloat(strSelection[3]), Float.parseFloat(strSelection[4]), Float.parseFloat(strSelection[5]), Float.parseFloat(strSelection[6]), Float.parseFloat(strSelection[7]), ObjectId.BULLET, Main.handler));
            }
            if (strSelection[5].equals("PLAYER")){
                
            }
        }
    }
           
    public void sendMessage(String strMessage) {
        ssm.sendText(strMessage);
    }
}
