package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("user/")
public class User {

    @POST
    @Path("login")

    public String loginUser(@FormDataParam("username") String username, @FormDataParam("password") String password) {

        System.out.println("Invoked loginUser() on path user/login");
        try {
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ((PreparedStatement) ps1).setString(1, username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {
                String correctPassword = loginResults.getString(1);
                if (password.equals(correctPassword)) {
                    String token = UUID.randomUUID().toString();
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, username);
                    ps2.executeUpdate();

                    JSONObject userDetails = new JSONObject();
                    userDetails.put("username", username);
                    userDetails.put("token", token);
                    return userDetails.toString();
                } else {
                    return "{\"Error\": \"Incorrect password!\"}";
                }

            } else {
                return "{\"Error\": \"Unknown user!\"}";
            }

        } catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"Error\": \"Server side error!\"}";
        }
    }

    public static boolean isValidToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return false;
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {

        try {

            System.out.println("user/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";

            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }

    }}

