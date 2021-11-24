package com.mycompany.apis;

import java.io.IOException;
import javax.swing.JOptionPane;

public class UIMenu {
   
    public static void Menu() throws IOException{
    int opcion_menu = -1;
    String [] botones ={ "1.Ver gatos","2.Ver favoritos", "3.Salir" };
    
    do {
        String opcion = (String)JOptionPane.showInputDialog(null, "Gatos Java", "Menu", JOptionPane.INFORMATION_MESSAGE,
                null,botones, botones[0]);
        //validar que opcion selecciona el usuario 
        for(int i= 0 ; i<botones.length;i++){
            if(opcion.equals(botones[i])){
                opcion_menu = i;
            }
                    
        }
        switch(opcion_menu){
            case 0:
                GatoService.verGatos();
                break;
            case 1:
                Gatos gatos = new Gatos();
                GatoService.verFavorito(gatos.getApykey());
            default: 
                break;
        }
    }while(opcion_menu != 1);
   }
}
