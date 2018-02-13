package esgi.jwm.project.loyalty.settings;

public enum Error {

    DATABASE(0, "A database error has occured."),
    IMPORTANT_DATA_MISSING(1, "Some important data are missing");


    private final int code;
    private final String description;

    private Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}