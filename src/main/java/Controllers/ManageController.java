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
            return new ModelAndView (map, "./Dashboard/index.mustache");
          }
      	}, new MustacheTemplateEngine());

        //Ruta para ver todos los usuarios registrados
        get("/userList", (req,res)->{
          Map<String, List<User>> map = new HashMap<>();
          List<User> listaUsuarios = userDao.getAllUsers();
          map.put("Usuarios", listaUsuarios);
          return new ModelAndView (map, "./Dashboard/userList.mustache");

        }, new MustacheTemplateEngine());
    }
}
