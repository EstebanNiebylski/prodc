package prode;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import Controllers.*;
import Dao.*;

import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

import Model.*;

import com.codahale.metrics.*;
import Utils.*;


import java.io.*;
import java.nio.file.*;
import javax.servlet.*;
import javax.servlet.http.*;

//Clase principal
public class App
{

	public static void main( String[] args )
	{
		//metrica
		Meter requests = Metricas.getRegistry().meter("requests");
		Metricas.startReport();

		//Directorio upload donde se cargan las imagenes de los Equipos
		File uploadDir = new File("src/main/resources/public/images");
		uploadDir.mkdir(); // create the upload directory if it doesn't exist

		String projectDir = System.getProperty("user.dir");
		String staticDir = "/src/main/resources/public";
		//Directorio de recursos /imagenes/estilos/scripts
		staticFiles.externalLocation(projectDir + staticDir);


		//Puerto de la aplicacion
		port(1112);

		//Abre conexion antes de cada solicitud
		before((request, response) -> {
			requests.mark();
			Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
		});

		//Cierra conexion
		after((request, response) -> {
			Base.close();
		});

		//Pagina por defecto index
		get("/", (req, res) -> {
			Map map = new HashMap();
			map.put("error",false);
			return new ModelAndView(map, "./Dashboard/index.mustache");
		}, new MustacheTemplateEngine()
				);

		//Inicializa controladores
		new Controller(new PredictionDao(), new GameDao(), new UserDao());
		new TeamController(uploadDir);
		new PhaseController();
	}

}
