package esgi.jwm.project.loyalty.serverhandler;

import esgi.jwm.project.loyalty.data.Person;

/**
 * Created by wmorgado on 14/02/2018.
 */

public class ServerHandler {
    public IServerHandlerConnection serverHandler;

//    here polymorphism allows you to put in parameter any class implementing IServerHandlerPerson
    public ServerHandler(IServerHandlerConnection serverHandler){
        this.serverHandler = serverHandler;
    }

    public long logIn(String mail, String password){
        return serverHandler.login(mail, password);
    }

    public boolean register(){

        return serverHandler.register();
    }

}
