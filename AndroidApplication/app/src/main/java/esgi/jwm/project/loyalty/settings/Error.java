package esgi.jwm.project.loyalty.settings;

public enum Error {

    PARAMETERS_MISSING(400, "Some important data are missing"),
    CHECK_FAIL(500, "An error has occure during the verification treatment");




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