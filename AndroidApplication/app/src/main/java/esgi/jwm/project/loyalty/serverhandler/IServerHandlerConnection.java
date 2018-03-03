package esgi.jwm.project.loyalty.serverhandler;

/**
 * Created by wmorgado on 22/02/2018.
 */

public interface IServerHandlerConnection {
    public void login(String mail, String password, APICallback apiCallback);
    public boolean register();
}
