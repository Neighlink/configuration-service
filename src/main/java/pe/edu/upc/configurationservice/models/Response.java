package pe.edu.upc.configurationservice.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response implements Serializable {
    private int status;
    private String message = "";
    private Object result;
}
