package Controllers;

import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.javalite.activejdbc.Base;

import Dao.*;
import Model.Team;
import spark.ModelAndView;

import java.io.*;
import java.nio.file.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class TeamController {

	public TeamController(File uploadDir, GameDao gameDao) {

		//Obtiene todos los equipos y los muestra
	get("/teams", (req, res) -> {
		Map<String, List<Team>> map = new HashMap<>();
		List<Team> lt = gameDao.listTeams();
		map.put("games",lt);
		map.put("nombre",req.session().attribute("USER"));
		String type = req.session().attribute("TYPE");
		if(type.equalsIgnoreCase("1")){
			map.put("admin",req.session().attribute("TYPE"));
		}
		return new ModelAndView(map, "./Dashboard/teams.mustache");
	}, new MustacheTemplateEngine());



	//formulario para crear un equipo
	get("/team/new", (req, res)->{
		Map map = new HashMap();
		map.put("nombre",req.session().attribute("USER"));
		return new ModelAndView (map, "./Dashboard/teamcreate.mustache");
	}, new MustacheTemplateEngine());

	//Creacion de equipo
	post("/team/save", "multipart/form-data", (req, res) -> {
			Map map = new HashMap();
			map.put("nombre",req.session().attribute("USER"));
			req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
			String teamname = req.queryParams("nombre");

			Path tempFile = Files.createTempFile(uploadDir.toPath(), teamname, ".png");
			final Part uploadedFile = req.raw().getPart("imagen");

			String mensaje ="Se creo Equipo exitosamente";

			 try (InputStream imagen = uploadedFile.getInputStream()) {
				Files.copy(imagen, tempFile, StandardCopyOption.REPLACE_EXISTING);
				String teamimage = tempFile.getFileName().toString();
				Team team = new Team(teamname,teamimage);
			 }catch (Exception e) {
				mensaje = "Error no se pudo cargar el equipo";
				System.out.println(mensaje);
				System.out.println(e);
			 }

			map.put("mensaje",mensaje);
			return new ModelAndView(map, "./Dashboard/teamcreate.mustache");
		}, new MustacheTemplateEngine());


  }
}
