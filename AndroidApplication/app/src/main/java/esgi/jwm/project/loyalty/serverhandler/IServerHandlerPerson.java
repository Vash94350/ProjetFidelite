package esgi.jwm.project.loyalty.serverhandler;

import esgi.jwm.project.loyalty.data.Person;

/**
 * Created by wmorgado on 14/02/2018.
 */

public interface IServerHandlerPerson {
    public long personLogIn(String email, String password);
    public boolean personRegister();
}
