package esgi.jwm.project.loyalty.serverhandler;

/**
 * Created by wmorgado on 22/02/2018.
 */

public interface IServerHandlerConnection {
    public int login(String mail, String password);
    public boolean register();
}
