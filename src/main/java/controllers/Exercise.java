package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;

@Path("exercise/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Exercise {

    @POST
    @Path("add")
    public String ExerciseAdd(@CookieParam("token") Cookie token,@FormDataParam("name") String name, @FormDataParam("bodyPart") String bodyPart, @FormDataParam("age") String age, @FormDataParam("difficulty") Integer difficulty, @FormDataParam("condition") String condition , @FormDataParam("benefits") String benefits , @FormDataParam("recommendedReps") Integer recommendedReps , @FormDataParam("video") String video , @FormDataParam("image") String image  ) {

        System.out.println("Invoked Patient.patientAdd()");

        if (token == null) {
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

        if (User.isValidToken(token.getValue()) == false) {
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Exercises (Name, BodyPart, Age, Difficulty, Condition, Benefits, RecommendedReps, Video, Image) VALUES (?, ?,?,?,?,?,?,?,?)");
            ps.setString(1,name);
            ps.setString(2, bodyPart);
            ps.setString(3, age);
            ps.setInt(4, difficulty);
            ps.setString(5,condition);
            ps.setString(6,benefits);
            ps.setInt(7,recommendedReps);
            ps.setString(8,video);
            ps.setString(9,image);

            ps.execute();
            return "{\"OK\": \"Added patient.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }


}
