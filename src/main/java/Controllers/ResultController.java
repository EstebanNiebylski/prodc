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

public class ResultController {

	public ResultController(final GameDao gameDao) {

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
  };
}
