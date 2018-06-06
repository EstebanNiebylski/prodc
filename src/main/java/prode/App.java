package prode;

import org.javalite.activejdbc.Base;
import prode.User;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class App
{
    public static void main( String[] args ) throws IOException
    {
        staticFileLocation("/public");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Cargar ganadores de los primeros 4 games");
        ArrayList<String> prediccion= new ArrayList<String>(); //cambiar String por game luego
        
        String winner; 

        for (int i=0; i<4; i++) {
            winner=br.readLine();
            prediccion.add(winner);
        }

        Prediction p1= new Prediction(1);
        p1.setPrediction(prediccion);

        Iterator<String> printPrediccion = prediccion.iterator();
        while(printPrediccion.hasNext()){
            String elemento = printPrediccion.next();
            System.out.print(elemento+" / ");
        }


        get("/", (request, response) -> {
            return new ModelAndView(new HashMap(), "templates/Dashboard/hello.vtl");
        }, new VelocityTemplateEngine());

        post("/algo", (request, response) -> {
            response.redirect("/carlos");
            return "moved to /bar path";
        });

        // Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");
        // User u = new User();
        // u.set("username", "Riquelme");
        // u.set("password", "password");
        // u.saveIt();
        // Base.close();
        // System.out.println( "Hello World!" );
    }
}
