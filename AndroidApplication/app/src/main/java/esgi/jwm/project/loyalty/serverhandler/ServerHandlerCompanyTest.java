package esgi.jwm.project.loyalty.serverhandler;

import java.util.Date;

import esgi.jwm.project.loyalty.data.Company;
import esgi.jwm.project.loyalty.data.Person;

/**
 * Created by wmorgado on 15/02/2018.
 */

public class ServerHandlerCompanyTest implements IServerHandlerConnection{


    @Override
    public int login(String mail, String password) {
        return 1;
    }

    @Override
    public boolean register() {
        return false;
    }
}
