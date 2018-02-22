package esgi.jwm.project.loyalty.data;

/**
 * Created by wmorgado on 14/02/2018.
 */

public class Person {
    private long personId;

    private String email;
    private String password;
    private String telephone;
    private String firstname;
    private String lastname;
    private String sexe;
    private String age;
    private String city;
    private String country;


    public Person(String email, String password, String telephone, String firstname, String lastname, String sexe, String age, String city, String country) {
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sexe = sexe;
        this.age = age;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Person{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", sexe='" + sexe + '\'' +
                ", age='" + age + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
}
