package pe.edu.upc.configurationservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.configurationservice.entities.Building;
import pe.edu.upc.configurationservice.entities.Department;
import pe.edu.upc.configurationservice.models.*;
import pe.edu.upc.configurationservice.services.BuildingService;
import pe.edu.upc.configurationservice.services.DepartmentService;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/configurations")
public class ConfigurationController {

    ConfigurationController() {
        response = new Response();
        responseAuth = new ResponseAuth();
    }

    private final static String URL_PROFILE = "http://localhost:8092/profiles";
    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");
    HttpStatus status;

    Response response = new Response();
    ResponseAuth responseAuth = new ResponseAuth();

    private ResponseAuth authToken(String token) {
        try {
            var values = new HashMap<String, String>() {{
            }};
            var objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(values);
            String url = URL_PROFILE + "/authToken";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .setHeader("Authorization", token)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseAPI = new JSONObject(response.body());
            var status = responseAPI.getInt("status");
            if (status != 200) {
                var message = responseAPI.getString("message");
                responseAuth.initError(false, message);
                return responseAuth;
            }
            JSONObject result = responseAPI.getJSONObject("result");
            responseAuth.init(result.getLong("id"), result.getString("userType"), result.getBoolean("authorized"), "");
            return responseAuth;
        } catch (Exception e) {
            responseAuth.initError(false, e.getMessage());
            return responseAuth;
        }
    }

    public void unauthorizedResponse() {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage("UNAUTHORIZED USER");
        response.setResult(null);
        status = HttpStatus.UNAUTHORIZED;
    }

    public void notFoundResponse() {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage("ENTITY NOT FOUND");
        response.setResult(null);
        status = HttpStatus.NOT_FOUND;
    }

    public void okResponse(Object result) {
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("SERVICE SUCCESS");
        response.setResult(result);
        status = HttpStatus.OK;
    }

    public void conflictResponse(String message) {
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setMessage(message);
        status = HttpStatus.CONFLICT;
    }

    public void internalServerErrorResponse(String message) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setResult(null);
        response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + " => " + message);
    }

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private DepartmentService departmentService;


    public String generateCode() {
        String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        Random rnd = new Random();
        for (int index = 0; index < 7; index++) {
            char letter = data.charAt(rnd.nextInt(data.length()));
            result.append(letter);
        }
        return result.toString();
    }

    //INIT DEPARTMENT
    @GetMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}/departments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getDepartmentsByBuilding(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @RequestHeader String Authorization) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<List<Department>> departments = departmentService.findAllByBuildingId(buildingId);
            if (departments.isEmpty()) {
                okResponse(new ArrayList<>());
            } else {
                okResponse(departments.get());
            }
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @GetMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}/departments/{deparmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getDepartment(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @PathVariable("deparmentId") Long deparmentId, @RequestHeader String Authorization) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<Department> department = departmentService.findById(deparmentId);
            if (department.isEmpty()) {
                okResponse(null);
            } else {
                okResponse(department.get());
            }
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @PostMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}/departments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> postDepartmentsByBuilding(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @RequestHeader String Authorization, @RequestBody RequestDepartment requestDepartment) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Department department = new Department();

            if (!requestDepartment.getName().isEmpty())
                department.setName(requestDepartment.getName());
            department.setBuildingId(buildingId);
            department.setLimiteRegister(requestDepartment.getLimitRegister());
            department.setCondominiumId(condominiumId);
            department.setSecretCode(generateCode());
            department.setDelete(false);

            Department departmentSaved = departmentService.save(department);

            Optional<List<Department>> departments = departmentService.findAllByBuildingId(buildingId);
            var countDepartments = 0;
            if (!departments.isEmpty())
                countDepartments = departments.get().size();
            Optional<Building> building = buildingService.findById(buildingId);
            if (!building.isEmpty()) {
                building.get().setNumberOfHomes(countDepartments);
                buildingService.save(building.get());
            }
            okResponse(departmentSaved);
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @PutMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}/departments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateDepartmentsByBuilding(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @RequestHeader String Authorization, @RequestBody RequestDepartment requestDepartment) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<Department> department = departmentService.findById(requestDepartment.getId());
            if (department.isEmpty()) {
                notFoundResponse();
                return new ResponseEntity<>(response, status);
            }
            if (!requestDepartment.getName().isEmpty())
                department.get().setName(requestDepartment.getName());
            department.get().setLimiteRegister(requestDepartment.getLimitRegister());

            Department departmentSaved = departmentService.save(department.get());
            okResponse(departmentSaved);
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @DeleteMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}/departments/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteDepartmentsByBuilding(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @PathVariable("departmentId") Long departmentId, @RequestHeader String Authorization) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<Department> department = departmentService.findById(departmentId);
            if (department.isEmpty()) {
                notFoundResponse();
                return new ResponseEntity<>(response, status);
            }
            department.get().setDelete(true);
            departmentService.save(department.get());
            okResponse(null);
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }
    //FINISH DEPARMENT

    //INIT BUILDING
    @GetMapping(path = "/condominiums/{condominiumId}/buildings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getBuildingsByCondominium(@PathVariable("condominiumId") Long condominiumId, @RequestHeader String Authorization) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<List<Building>> buildings = buildingService.findAllByCondominiumId(condominiumId);
            if (buildings.isEmpty()) {
                okResponse(new ArrayList<>());
            } else {
                okResponse(buildings.get());
            }
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @GetMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getBuilding(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @RequestHeader String Authorization) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<Building> building = buildingService.findById(buildingId);
            if (building.isEmpty()) {
                okResponse(null);
            } else {
                okResponse(building.get());
            }
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @PostMapping(path = "/condominiums/{condominiumId}/buildings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> postBuildinfByCondominium(@PathVariable("condominiumId") Long condominiumId, @RequestHeader String Authorization, @RequestBody RequestBuilding requestBuilding) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Building building = new Building();
            building.setName(requestBuilding.getName());
            building.setCondominiumId(condominiumId);
            building.setNumberOfHomes(0);
            building.setDelete(false);
            Building buildingSaved = buildingService.save(building);
            okResponse(buildingSaved);
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @PutMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateDepartmentsByBuilding(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @RequestHeader String Authorization, @RequestBody RequestBuilding requestBuilding) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<Building> building = buildingService.findById(buildingId);
            if (building.isEmpty()) {
                notFoundResponse();
                return new ResponseEntity<>(response, status);
            }
            building.get().setName(requestBuilding.getName());
            Building buildingSaved = buildingService.save(building.get());
            okResponse(buildingSaved);
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }

    @DeleteMapping(path = "/condominiums/{condominiumId}/buildings/{buildingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteDepartmentsByCondominium(@PathVariable("condominiumId") Long condominiumId, @PathVariable("buildingId") Long buildingId, @RequestHeader String Authorization) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<Building> building = buildingService.findById(buildingId);
            if (building.isEmpty()) {
                notFoundResponse();
                return new ResponseEntity<>(response, status);
            }
            building.get().setDelete(true);
            buildingService.save(building.get());
            okResponse(null);
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }
    //FINISH BUILDING

    @GetMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getBuilding(@PathParam("code") String code, @RequestHeader String Authorization) {
        try {
            ResponseAuth authToken = authToken(Authorization);
            if (!authToken.isAuthorized()) {
                unauthorizedResponse();
                return new ResponseEntity<>(response, status);
            }
            Optional<Department> department = departmentService.findByCode(code);
            if (department.isEmpty()) {
                notFoundResponse();
            } else {
                okResponse(department.get());
            }
            return new ResponseEntity<>(response, status);
        } catch
        (Exception e) {
            internalServerErrorResponse(e.getMessage());
            return new ResponseEntity<>(response, status);
        }
    }
}
