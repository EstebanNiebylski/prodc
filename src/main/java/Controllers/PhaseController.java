package Controllers;

import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.*;


import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.javalite.activejdbc.Base;

import Dao.*;
import Model.Phase;
import spark.ModelAndView;

import java.io.*;
import java.nio.file.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class PhaseController {

	public PhaseController() {		
	
	//Validar que el usuario este logueado
	before("/phase/*",(request, response) -> {
		boolean authenticated = request.session().attribute("USER")!= null;
		if (!authenticated) {
			halt(401, "You are not welcome here");
		}
	});

	//formulario para cargar fecha limite de la fase
	get("/phase/update/:id", (req, res)->{
		Map<String, Object> map = new HashMap<>();
		int id = Integer.parseInt(req.params(":id"));
		Phase fase = Phase.getPhaseId(id);
		map.put("phase",fase);
		map.put("nombre",req.session().attribute("USER"));
		return new ModelAndView (map, "./Dashboard/phaseupdate.mustache");
	}, new MustacheTemplateEngine());


	//carga fecha limite de la fase
	post("/phase/save", (req, res) -> {
		int id = Integer.parseInt(req.queryParams("id"));		
		String expires = req.queryParams("fecha");
		Phase fase = Phase.getPhaseId(id);
		String mensaje = "Se actualizo exitosamente Fecha";
		try{
			fase.saveExpires(expires);
		}catch (Exception e){
			mensaje = "Error no se pudo actualizar Fecha";
			System.out.println(e);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("phase",fase);
		map.put("nombre",req.session().attribute("USER"));
		map.put("mensaje",mensaje);
		return new ModelAndView (map, "./Dashboard/phaseupdate.mustache");	
	}, new MustacheTemplateEngine());
	
  }
}

