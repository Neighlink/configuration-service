package pe.edu.upc.configurationservice.models;

import lombok.Data;

@Data
public class ResponseAuth {
    private Long id;
    private String userType = "";
    private boolean isAuthorized = false;
    private String message = "";

    public void init(Long id, String userType, boolean isAuthorized, String message) {
        this.id = id;
        this.userType = userType;
        this.isAuthorized = isAuthorized;
        this.message = message;
    }

    public void initError(boolean isAuthorized, String message) {
        this.isAuthorized = isAuthorized;
        this.message = message;
    }
}
