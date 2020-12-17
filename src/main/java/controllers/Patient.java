package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.sql.PreparedStatement;

@Path("patient/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Patient {

    @POST
    @Path("add")
    public String patientAdd(@CookieParam("token") Cookie token , @FormDataParam("firstName") String firstName, @FormDataParam("lastName") String lastName , @FormDataParam("DOB") String DOB){

        System.out.println("Invoked Patient.patientAdd()");

        if (token == null) {
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

       if(User.isValidToken(token.getValue()) == false){
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
       }

        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Patients (FirstName, LastName , DOB) VALUES (?, ?,?)");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3,DOB);

            ps.execute();
            return "{\"OK\": \"Added patient.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }
}

