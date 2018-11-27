package Controllers;

import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.javalite.activejdbc.Base;

import Dao.*;
import Model.*;
import spark.ModelAndView;

public class Controller {

	static Map map = new HashMap();

	public Controller(final PredictionDao predictionDao, final GameDao gameDao, final UserDao userDao) {

		//Direcciona a la pagina correspondiente
		post("/prediction", (req, res) -> {
			Map<String, List<Team>> map = new HashMap<>();
			List<Team> lt = gameDao.listTeams();
			map.put("teams",lt);
			String option = req.queryParams("option");
			map.put("nombre",req.session().attribute("USER"));
			switch(option){
			case "Octavos":
				return new ModelAndView(map, "./Dashboard/octavos.mustache");
			case "Cuartos":
				return new ModelAndView(map, "./Dashboard/cuartos.mustache");
			case "Semifinales":
				return new ModelAndView(map, "./Dashboard/semifinales.mustache");
			case "Finales":
				return new ModelAndView(map, "./Dashboard/finales.mustache");
			case "Ganador":
				return new ModelAndView(map, "./Dashboard/ganador.mustache");
			}
			return new ModelAndView(map, "./Dashboard/index.mustache");
		}, new MustacheTemplateEngine());

		//Crea una nueva prediccion para un usuario
		post("/prediction/new", (req, res) -> {
			String[] games = req.queryParamsValues("partidos[]");
			String  fecha = (req.queryParams("fecha")).toString();
			int option = Integer.parseInt(fecha);
			//si los juegos fueron cargados incorrectamente
			if (gameArrayError(option, games)){
				return new ModelAndView(null, "./405.mustache");
			}
			else{
				int id_user = req.session().attribute("ID");
				String type = req.session().attribute("TYPE");
				map.put("nombre",req.session().attribute("USER"));
				if(type.equalsIgnoreCase("1")){
					gameDao.updateGames(option, games);
				}
				else {
					predictionDao.createPrediction(id_user, option, games);
				}
				return new ModelAndView(map, "./Dashboard/profile.mustache");
			}
		}, new MustacheTemplateEngine());

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

		//Devuelve tabla de puntos renderiza en results
		get("/results", (req, res) -> {
			Map<String,List<User>> map = new HashMap<>();
			List<User> lu = gameDao.listPoints();
			map.put("users",lu);
			map.put("nombre",req.session().attribute("USER"));
			return new ModelAndView(map, "./Dashboard/results.mustache");
		}, new MustacheTemplateEngine());

		//Devuelve tabla de juegos creados por el administrador
		get("/results/games", (req, res) -> {
			Map<String,List<Game>> map = new HashMap<>();
			List<Game> lg = gameDao.listGames();
			map.put("games",lg);
			map.put("nombre",req.session().attribute("USER"));
			return new ModelAndView(map, "./Dashboard/games.mustache");
		}, new MustacheTemplateEngine());

		//Validar que el usuario este logueado
		before("/profile/*",(request, response) -> {
			boolean authenticated = request.session().attribute("USER")!= null;
			if (!authenticated) {
				halt(401, "You are not welcome here");
			}
		});

		//Validar que el usuario este logueado
		before("/users/*",(request, response) -> {
			boolean authenticated = request.session().attribute("USER")!= null;
			if (!authenticated) {
				halt(401, "You are not welcome here");
			}
		});

		//Utilizado para cerrar sesion - borrar sesion
		get("/index", (req, res) -> {
			req.session().removeAttribute("USER");
			req.session().removeAttribute("ID");
			req.session().removeAttribute("TYPE");
			map.put("message",true);
			return new ModelAndView(map, "./Dashboard/index.mustache");
		}, new MustacheTemplateEngine());


		//Buscar un usuario
		get("/users/:id", (req, res) -> {
			String id = req.params(":id");
			User user = userDao.getUser(id);
			if(user == null){
				return new ModelAndView(null, "./404.mustache");
			}
			map.put("email",user.getEmail());
			return new ModelAndView(map, "./Dashboard/index.mustache");
		}, new MustacheTemplateEngine());

		//Utilizado para redireccionar desde elementos a
		get("/profile", (req, res) -> {
			map.put("nombre",req.session().attribute("USER"));
			return new ModelAndView(map, "./Dashboard/profile.mustache");
		}, new MustacheTemplateEngine());

		//Controla y autentifica al usuario - inicia session variables
		post("/profile", (req, res) -> {
			String username = req.queryParams("username");
			String pass = req.queryParams("password");
			User user = userDao.isUser(username,pass);
			if(user != null){
				req.session(true);
				req.session().attribute("USER",username);
				req.session().attribute("ID",user.getId());
				req.session().attribute("TYPE",user.getType());
				map.put("nombre",req.session().attribute("USER"));
			}
			else{
				map.put("error",true);
				return new ModelAndView(map, "./Dashboard/index.mustache");
			}
			return new ModelAndView(map, "./Dashboard/profile.mustache");
		}, new MustacheTemplateEngine());


		//Creacion de usuarios
		post("/new/user", (req, res) -> {
			String username = req.queryParams("username");
			String pass = req.queryParams("password");
			String email = req.queryParams("email");
			User user = userDao.createUser(username,pass,email);
			if(user == null){
				return new ModelAndView(null, "./404.mustache");
			}
			return new ModelAndView(null, "./Dashboard/index.mustache");
		}, new MustacheTemplateEngine());
	}

	public boolean gameArrayError(int caso, String [] listaEquipos){
		boolean result = false;
		List<String> lt = Team.findAll().collect("name");
		for(int i=0; i<listaEquipos.length; i++){
			if (!lt.contains(listaEquipos[i])) result = true;
		}
		return result;
	}
}
