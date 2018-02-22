package esgi.jwm.project.loyalty.serverhandler;

import esgi.jwm.project.loyalty.data.Person;

/**
 * Created by wmorgado on 14/02/2018.
 */

public class ServerHandlerPersonTest implements IServerHandlerConnection {
//    @Override
//    public long personLogIn() {
//      return a basic Person account
//        return new Person(1,"username","toto@gmail.com","toto", 1,19, "Chenneviers-sur-Marne", "France");
//    }


    @Override
    public int login(String mail, String password) {
//        the line below is on purpose for testing
        return 1;
    }

    @Override
    public boolean register() {
        return false;
    }
}
