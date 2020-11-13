package controllers;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("patientexercises/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class PatientExercise {

    @GET
    @Path("get/{PatientLink}")
    public String getPatientLink(@PathParam("PatientLink") String PatientLink) {
        System.out.println("Invoked Food.getFood() with PatientLink " + PatientLink);
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT PatientLink, FirstName, LastName FROM Patients WHERE PatientLink =?");
            ps.setString(1, PatientLink);
            ResultSet results = ps.executeQuery();
            while (results.next() == true) {
                JSONObject row = new JSONObject();
                row.put("PatientLink", results.getString(1));
                row.put("FirstName", results.getString(2));
                row.put("LastName", results.getString(3));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }
}

