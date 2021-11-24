package com.mycompany.apis;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class GatoService {
    public static void verGatos() throws IOException{
        //1. vamos a traer los datos de la api 
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();
        Response response = client.newCall(request).execute();
        
        String elJson = response.body().string();
        
        //cortar los corechetes 
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length()-1); //corta la ultima posicion
        
        //crear un objeto de la clase gson 
        Gson sdon = new Gson();
        Gatos gatos = sdon.fromJson(elJson, Gatos.class);
        
        //redimensionar en caso necesarion
        Image image = null;
        try{
            URL url = new URL(gatos.getUrl());
            image=ImageIO.read(url);
            
            ImageIcon fondoGatio = new ImageIcon(image);
            
            if (fondoGatio.getIconWidth() > 800){
                //redimensionamos
                Image fondo = fondoGatio.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                
                fondoGatio = new ImageIcon(modificada);
            }
            String menu = "opciones: \n"
                     + "1. ver otra imagen\n"+
                     "2. Favorito\n"
                    + "3. Volver";
            
            String [] botones ={"Ver otra imagen", "favorito" , "volver"};
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null,  menu, id_gato,JOptionPane.INFORMATION_MESSAGE
            , fondoGatio,botones,botones[0]);
            
            int seleccion = -1;
            for(int i= 0 ; i<botones.length;i++){
            if(opcion.equals(botones[i])){
                seleccion= i;
            }
            switch(seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default: 
                    
            }
                    
        }
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    public static void favoritoGato (Gatos gato){
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\" : \""+gato.getId()+"\"\r\n}");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites")
              .post(body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gato.getApykey())
              .build();
            Response response = client.newCall(request).execute();
            
        }catch(IOException e){
            System.out.println(e);
        }
        
    }
    
    public static void verFavorito (String ApiKey) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/favourites")
          .get()
          .addHeader("x-api-key", ApiKey)
          .build();
        Response response = client.newCall(request).execute();
        
        //guardamos el string con la respuesta
        String elJson = response.body().string();
        
        //creamos el objeto json 
        Gson gson = new Gson ();
        
        
        GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class);
        
        if (gatosArray.length > 0){
            int min =1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max-min)+1)) + min;
            
            int indice = aleatorio -1;
            
            GatosFav gatosFav = gatosArray[indice];
            
             //redimensionar en caso necesarion
        Image image = null;
        try{
            URL url = new URL(gatosFav.image.getUrl());
            image=ImageIO.read(url);
            
            ImageIcon fondoGatio = new ImageIcon(image);
            
            if (fondoGatio.getIconWidth() > 800){
                //redimensionamos
                Image fondo = fondoGatio.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                
                fondoGatio = new ImageIcon(modificada);
            }
            String menu = "opciones: \n"
                     + "1. ver otra imagen\n"+
                     "2. Eleminar Favorito\n"
                    + "3. Volver";
            
            String [] botones ={"Ver otra imagen", "favorito" , "volver"};
            String id_gato = gatosFav.getId();
            String opcion = (String) JOptionPane.showInputDialog(null,  menu, id_gato,JOptionPane.INFORMATION_MESSAGE
            , fondoGatio,botones,botones[0]);
            
            int seleccion = -1;
            for(int i= 0 ; i<botones.length;i++){
            if(opcion.equals(botones[i])){
                seleccion= i;
            }
            switch(seleccion){
                case 0:
                    verFavorito(ApiKey);
                    break;
                case 1:
                    borrarFavorito(gatosFav);
                    break;
                default: 
                    
            }
                    
        }
        }catch(IOException e){
            System.out.println(e);
        }
            
        }
    }
    
    public static void borrarFavorito (GatosFav gatofav){
        try{
            OkHttpClient client = new OkHttpClient();
            
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites/"+gatofav.getId()+"")
              .post(null)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gatofav.getApikey())
              .build();
            Response response = client.newCall(request).execute();
            
            
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
