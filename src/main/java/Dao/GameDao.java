package Dao;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import static spark.Spark.*;
import java.util.List;

import Model.*;

public class GameDao {

	//Actualiza la tabla juego solo si es administrador
	/*pre: lista de equipos cargada correctamente
    post: escribe los juegos de una fecha en la base de datos
	 */
	public void updateGames(int fecha, String[] equipos) {
		String equipo_local;
		String equipo_visitante;
		int nroPartido = PredictionDao.selector(fecha);
		for (int i=0; i < equipos.length; i++) {
			//Busca el juego para despues actualizar los equipo
			equipo_local = equipos[i];
			equipo_visitante = equipos[i+1];
			Game.update("team_loc=?", "id=?", equipo_local, nroPartido);
			Game.update("team_vis=?", "id=?", equipo_visitante, nroPartido);
			updatePoints(nroPartido);
			i++;
			nroPartido++;
		}
	}


	//Actualiza los puntos de los usuarios en un determinado juego
	private void updatePoints(int id_game){
		int score;
		List<User> users = User.findBySQL("SELECT a.* FROM users a INNER JOIN predictions b ON a.id = b.id_user INNER JOIN games c ON b.id_game = c.id WHERE b.team_local = c.team_loc AND b.team_visitante = c.team_vis AND b.team_visitante != '' AND c.id = ?;",id_game);
		for(User p: users){
			score = Integer.parseInt(p.getScore()) + 1;
			p.set("score",score).saveIt();
		}
	}

	//Retorna los 10 jugadores con mayor puntaje
	public List<User> listPoints(){
		List<User> lu = User.findBySQL("SELECT * FROM users WHERE id != 1 ORDER BY score DESC LIMIT 10");
		return lu;
	}

	//Retorna todos los equipos
	public List<Team> listTeams(){
		List<Team> lt = Team.findAll();
		return lt;
	}

	public List<Team> listTeamsByGroupLetter(String letter){
		List<Team>lt = Team.findBySQL("SELECT * FROM teams WHERE group_letter = '"+letter+"';");
		return lt;
	}

	//Retorna todos los juegos actualizados
	public List<Game> listGames(){
		List<Game> lg = Game.findBySQL("SELECT * FROM games WHERE team_loc != '' AND team_vis != '';");
		return lg;
	}

}
