package controllers;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("patientexercise/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class PatientExercise {

    @GET
    @Path("list/{PatientLink}")
    public String getPatient(@PathParam("PatientLink") String PatientLink) {
        System.out.println("Invoked getPatient() with PatientLink " + PatientLink);

        //need to know what their PatientID is in order to get their exercies
        int patientID = -1;  //set originally to rogue value

        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT PatientID FROM Patients WHERE PatientLink =?");
            ps.setString(1, PatientLink);
            ResultSet results = ps.executeQuery();
            while (results.next() == true) {
                patientID = results.getInt(1);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }


        //now we have the patientID we need to return all their exercies
        JSONArray response = new JSONArray();
        try {

            //Henry, you need to change the SQL in the statement below so it returns the details of the exercies from
            //the exercises table.  The SQL will therefore need a JOIN stateement.  Have a go.

            PreparedStatement ps = Main.db.prepareStatement("SELECT ExerciseID, DateSet, Repetitions FROM PatientExercises WHERE PatientID = ?");
            ps.setInt(1, patientID);
            ResultSet results = ps.executeQuery();
            while (results.next() == true) {
                JSONObject row = new JSONObject();
                row.put("ExerciseID", results.getInt(1));
                row.put("DateSet", results.getString(2));
                row.put("Repetitions", results.getInt(3));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }


    }


}

