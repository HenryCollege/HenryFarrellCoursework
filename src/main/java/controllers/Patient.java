package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import server.Main;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;

@Path("patient/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Patient {

    @POST
    @Path("add")
    public String foodAdd(@FormDataParam("PatientID") Integer PatientID, @FormDataParam("firstName") String firstName, @FormDataParam("lastName") String lastName , @FormDataParam("DOB") String DOB){
        System.out.println("Invoked Patient.patientAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Patients (PatientID, FirstName, LastName , DOB) VALUES (?, ?,?,?)");
            ps.setInt(1, PatientID);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4,DOB);

            ps.execute();
            return "{\"OK\": \"Added patient.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }
}