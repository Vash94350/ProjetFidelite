package esgi.jwm.project.loyalty.serverhandler;

/**
 * Created by wmorgado on 22/02/2018.
 */

public interface IServerHandlerConnection {
    public void login(String mail, String password, APICallback apiCallback);
    public void register(String mail, String password, String telephone,
                         String firstname, String lastname, String sex,
                         String birthDate, String streetNumber, String route,
                         String zipCode, String city, String Country,
                         APICallback callback);
    public void resendMail(String mail, APICallback apiCallback);
}