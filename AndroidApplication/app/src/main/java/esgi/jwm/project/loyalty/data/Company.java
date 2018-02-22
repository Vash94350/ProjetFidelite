package esgi.jwm.project.loyalty.data;

import java.util.Date;

/**
 * Created by wmorgado on 15/02/2018.
 */

public class Company {

    private long companyId;
    private long siret;
    private int companyType;

    private String description;
    private String companyName;
    private String password;
    private String mail;
    private String telephone;
    private String city;
    private String country;

    private Date creationDate;

    public Company(long siret, int companyType, String description, String companyName, String password, String mail, String telephone, String city, String country, Date creationDate) {
        this.siret = siret;
        this.companyType = companyType;
        this.description = description;
        this.companyName = companyName;
        this.password = password;
        this.mail = mail;
        this.telephone = telephone;
        this.city = city;
        this.country = country;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Company{" +
                "siret=" + siret +
                ", companyType=" + companyType +
                ", description='" + description + '\'' +
                ", companyName='" + companyName + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", telephone='" + telephone + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

}
