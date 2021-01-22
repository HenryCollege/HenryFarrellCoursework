package controllers;


import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
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

            PreparedStatement ps = Main.db.prepareStatement("SELECT " +
                    "PatientExercises.PatientID , " +   //1
                    "Exercises.ExerciseID, " +          //2
                    "Name, " +                          //3
                    "BodyPart, " +                      //4
                    "Age, " +                           //5
                    "Difficulty, " +                    //6
                    "Condtion, " +                      //7
                    "Benefits, " +                      //8
                    "RecommendedReps, " +               //9
                    "Video, " +                         //10
                    "Image " +                          //11
                        "FROM PatientExercises " +
                        "JOIN Exercises ON main.PatientExercises.ExerciseID = main.Exercises.ExerciseID " +
                        "where patientID = ?;");

            ps.setInt(1, patientID);
            ResultSet results = ps.executeQuery();
            while (results.next() == true) {
                JSONObject row = new JSONObject();
                row.put("PatientID", results.getInt(1));
                row.put("ExerciseID", results.getInt(2));
                row.put("Name", results.getString(3));
                row.put("BodyPart", results.getString(4));
                row.put("Age", results.getString(5));
                row.put("Difficulty", results.getInt(6));
                row.put("Condition", results.getString(7));
                row.put("Benefits", results.getString(8));
                row.put("RecommendedReps", results.getInt(9));
                row.put("Video", results.getString(10));
                row.put("Image", results.getString(11));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }


    }

    @POST
    @Path("add")
    public String patientExerciseAdd(@CookieParam("token") Cookie token, @FormDataParam("PatientID") Integer PatientID, @FormDataParam("ExerciseID") Integer ExerciseID , @FormDataParam("DateSet") String DateSet , @FormDataParam("Repetitions") Integer Repetitions) {

        System.out.println("Invoked Patient.patientExerciseAdd()");

        if (token == null) {
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

        if (User.isValidToken(token.getValue()) == false) {
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO PatientExercises (PatientID, ExerciseID,DateSet,Repetitions) VALUES (?, ?,?,?)");
            ps.setInt(1, PatientID);
            ps.setInt(2, ExerciseID);
            ps.setString(3 , DateSet);
            ps.setInt(4, Repetitions);

            ps.execute();
            return "{\"OK\": \"Added patient.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }










    }

