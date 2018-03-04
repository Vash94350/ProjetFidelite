package esgi.jwm.project.loyalty.serverhandler;

import org.json.JSONObject;

import java.util.HashMap;

import com.android.volley.VolleyError;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by wmorgado on 14/02/2018.
 */

public class ServerHandler {
    public IServerHandlerConnection serverHandler;

//    here polymorphism allows you to put in parameter any class implementing IServerHandlerPerson
    public ServerHandler(IServerHandlerConnection serverHandler) throws UnirestException {
        this.serverHandler = serverHandler;
    }



    public void logIn(String mail, String password, APICallback callback){
        serverHandler.login(mail, password, callback);
    }

    public boolean register(String mail, String password, String telephone,
                            String firstname, String lastname, String sex,
                            String birthDate, String streetNumber, String route,
                            String zipCode, String city, String Country, APICallback callback){

        return serverHandler.register(mail,  password,  telephone,
                 firstname,  lastname,  sex,
                 birthDate,  streetNumber,  route,
                 zipCode,  city,  Country,
                 callback);
    }


    protected static HashMap<String,Object> prepareHashMap(Object... args){
        if((args.length%2) == 0){
            HashMap<String,Object> listParams = new HashMap<>();
            for(int i = 0; i < args.length; i = i+2){

                if(args[i+1].equals(0)||args[i+1].equals(false)){
                    args[i+1] = "";
                }
                if(args[i+1].equals(true)){
                    args[i+1] = 1;
                }
//                if(args[i+1].equals(false)){
//                    args[i+1] = 0;
//                }
                if(!(args[i+1].equals(""))){
                    listParams.put((String)args[i], args[i+1]);
                }

                System.out.println("args :" + (String)args[i] + " args + 1: " + args[i+1]);

            }

            return listParams;

        } else if(args.length == 0){

            HashMap<String,Object> listParams = new HashMap<>();

            return listParams;

        } else {
            // one value is missing
            return null;
        }
    }

}
