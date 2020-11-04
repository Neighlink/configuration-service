package pe.edu.upc.configurationservice.models;

import com.sun.istack.Nullable;
import lombok.Data;

@Data
public class RequestDepartment {
    private Long id;
    private String name;
    @Nullable
    private Integer limitRegister;
}
