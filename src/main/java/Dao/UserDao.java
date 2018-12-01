 package Dao;

import java.util.List;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import static spark.Spark.*;

import Model.User;

public class UserDao {

  //Lista todos los usuarios
	public List<User> getAllUsers() {
    List<User> users;
    users = User.findAll();
    return users;
	}

  //Retorna un usuario por id
	public User getUser(String id) {
    User user;
    user = User.findFirst("nick = ?", id);
	  return user;
	}

  //Crea un usuario - retornar un usuario
	public User createUser(String name, String password, String email) {
    User user;
		user = new User(name, email, password);
		return user;
	}

  //Verifica si el usuario existe - retorna un usuario
  public User isUser(String name, String password){
    User user = User.findFirst("nick = ? AND password = ?", name, password);
    return user;
  }

  //Crea un usuario administrador
  public User createAdmin(String name, String password, String email){
    User newAdmin = new User(name, email, password);
    return newAdmin;
  }
  
}
