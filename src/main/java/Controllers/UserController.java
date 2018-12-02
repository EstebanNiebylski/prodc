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

public class UserController {

	public UserController(final UserDao userDao) {

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
		Map map = new HashMap();
		req.session().removeAttribute("USER");
		req.session().removeAttribute("ID");
		req.session().removeAttribute("TYPE");
		map.put("message",true);
    return new ModelAndView(map, "./Dashboard/index.mustache");
  }, new MustacheTemplateEngine());


	//Buscar un usuario
  get("/users/:id", (req, res) -> {
			Map map = new HashMap();
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
			Map map = new HashMap();
			map.put("nombre",req.session().attribute("USER"));
			return new ModelAndView(map, "./Dashboard/profile.mustache");
		}, new MustacheTemplateEngine());

		//Controla y autentifica al usuario - inicia session variables
    post("/profile", (req, res) -> {
			Map map = new HashMap();
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
}
