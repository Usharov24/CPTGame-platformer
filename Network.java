public class Network {


    public Network(){
    }


        public void readdata(SuperSocketMaster ssm){

            
                String strInput = ssm.readText();
                System.out.println(strInput);
                
                if(strInput.substring(0,1).equals("c")){
                    String[] strMessage = strInput.split(",");
                    System.out.println(strMessage[1]);
                    //chatbox.append(strmessage[1] + "\n");
                }
                else if(strInput.substring(0,1).equals("m")){
                    System.out.println("msg recieved");
                    String[] strSelection = strInput.split(",");
                    if(strSelection[1].equals("start")){
                        Main.theFrame.setContentPane(Main.characterPanel);
                        Main.theFrame.pack();
                    }
                    if(strSelection[1].equals("ready")){
                        Main.theFrame.setContentPane(Main.mainPanel);
                        Main.mainPanel.setFocusable(true);
                        Main.mainPanel.requestFocus();
                        Main.theFrame.pack();
                    }
                    if(strSelection[1].equals("join")){
                        System.out.println("smth good happened");
                        if(Main.intjoinid == 0){
                            System.out.println("smth good happened");
                            Main.intjoinid = Integer.parseInt(strSelection[2]);
                            
                        }

                    }
                    
                    /*if(strSelection[1].equals("char1")){
                        Main.intcharbutton2 = 1;
                        Main.buttonchar1.setEnabled(false);
                    }
                    if(strSelection[1].equals("char2")){
                        Main.intcharbutton2 = 2;
                        Main.buttonchar2.setEnabled(false);
                    }
                    if(strSelection[1].equals("char3")){
                        Main.intcharbutton2 = 3;
                        Main.buttonchar3.setEnabled(false);
                    }
                    if(strSelection[1].equals("char4")){
                        Main.intcharbutton2 = 4;
                        Main.buttonchar4.setEnabled(false);
                    }
                    */
                    

                    

                }
                else if(strInput.substring(0,1).equals("o")){
                    String[] strSelection = strInput.split(",");
                    if (strSelection[5].equals("BULLET")){

                    }
                    if (strSelection[5].equals("PLAYER")){
                        
                    }
                    //
                    

                }

            }
           
        

        public void senddata(SuperSocketMaster ssm){
            ssm.sendText("m,join," + Main.intjoinhostid);
            System.out.println("sent msg");
        }
    
}
