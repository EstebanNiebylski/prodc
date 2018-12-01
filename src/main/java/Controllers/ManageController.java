package Controllers;

import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.javalite.activejdbc.Base;

import Dao.UserDao;
import Model.User;
import spark.ModelAndView;

public class ManageController{

    static Map map = new HashMap();

    public ManageController(UserDao userDao){
        //Ruta para ver las opciones de administrador
        get("/manage", (req,res)->{
          String nombreUsuario =  req.session().attribute("USER");
          String tipoUsuario =  req.session().attribute("TYPE");
          map.put("nombre", nombreUsuario);
          //verifica si el tipo de usuario es administrador
      		if (tipoUsuario.equalsIgnoreCase("1")){
      		    return new ModelAndView (map, "./Dashboard/manage.mustache");
          }
          else{
            return new ModelAndView (null, "./401.mustache");
          }
      	}, new MustacheTemplateEngine());

        //Ruta para ver todos los usuarios registrados
        get("/userList", (req,res)->{
          Map<String, List<User>> map = new HashMap<>();
          List<User> listaUsuarios = userDao.getAllUsers();
          String tipoUsuario =  req.session().attribute("TYPE");
          if (tipoUsuario.equalsIgnoreCase("1")){
              map.put("admin", req.session().attribute("TYPE"));
          }
          map.put("usuarios", listaUsuarios);
          return new ModelAndView (map, "./Dashboard/userList.mustache");

        }, new MustacheTemplateEngine());

        get("/newAdmin", (req,res)->{
          return new ModelAndView (map, "./Dashboard/newAdmin.mustache");
        }, new MustacheTemplateEngine());

        //metodo para agregar usuario administrador
        post("/admin/new", (req,res)->{
          String username = req.queryParams("nickname");
    			String password = req.queryParams("password");
    			String email = req.queryParams("email");
          User newAdmin = userDao.createAdmin(username, password, email);
          if (newAdmin==null){
            return new ModelAndView(null, "./405.mustache");
          }
           return new ModelAndView(null, "./Dashboard/manage.mustache");
        }, new MustacheTemplateEngine());
    }
}
