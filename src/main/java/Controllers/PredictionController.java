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

public class PredictionController {

    static Map map = new HashMap();

    public PredictionController(final PredictionDao predictionDao, final GameDao gameDao) {

      //Direcciona a la pagina correspondiente
      post("/prediction", (req, res) -> {
        Map<String, Object> map = new HashMap<>();
        List<Team> lt = gameDao.listTeams();
        List<Team> ltA = gameDao.listTeamsByGroupLetter("A");
        List<Team> ltB = gameDao.listTeamsByGroupLetter("B");
        List<Team> ltC = gameDao.listTeamsByGroupLetter("C");
        List<Team> ltD = gameDao.listTeamsByGroupLetter("D");
        List<Team> ltE = gameDao.listTeamsByGroupLetter("E");
        List<Team> ltF = gameDao.listTeamsByGroupLetter("F");
        List<Team> ltG = gameDao.listTeamsByGroupLetter("G");
        List<Team> ltH = gameDao.listTeamsByGroupLetter("H");

        map.put("teams",lt);
        map.put("teamsA",ltA);
        map.put("teamsB",ltB);
        map.put("teamsC",ltC);
        map.put("teamsD",ltD);
        map.put("teamsE",ltE);
        map.put("teamsF",ltF);
        map.put("teamsG",ltG);
        map.put("teamsH",ltH);
        String option = req.queryParams("option");
        map.put("nombre",req.session().attribute("USER"));
        String type = req.session().attribute("TYPE");
		if(type.equalsIgnoreCase("1")){
			map.put("admin",req.session().attribute("TYPE"));
		}
		
        switch(option){
        case "Octavos":
        Phase octavos = Phase.getPhaseId(0);
        map.put("phase", octavos);
          return new ModelAndView(map, "./Dashboard/octavos.mustache");
        case "Cuartos":
         Phase cuartos = Phase.getPhaseId(1);
        map.put("phase", cuartos);
          return new ModelAndView(map, "./Dashboard/cuartos.mustache");
        case "Semifinales":
         Phase semifinales = Phase.getPhaseId(2);
        map.put("phase", semifinales);
          return new ModelAndView(map, "./Dashboard/semifinales.mustache");
        case "Finales":
         Phase finales = Phase.getPhaseId(3);
        map.put("phase", finales);
          return new ModelAndView(map, "./Dashboard/finales.mustache");
        case "Ganador":
         Phase ganador = Phase.getPhaseId(4);
        map.put("phase", ganador);
          return new ModelAndView(map, "./Dashboard/ganador.mustache");
        }
        return new ModelAndView(map, "./Dashboard/index.mustache");
      }, new MustacheTemplateEngine());

      //Crea una nueva prediccion para un usuario
      post("/prediction/new", (req, res) -> {
        Map map = new HashMap();
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
