package Model;

import org.javalite.activejdbc.Model;

public class Team extends Model {
  static{
    validatePresenceOf();
  }
  public Team(){}

  public Team(String nombre, String image, String group_letter) {
	  set("name", nombre);
	  set("image", image);
    set("group_letter", group_letter);
	  saveIt();
  }

  public Team(String nombre, String image) {
    set("name", nombre);
    set("image", image);
    saveIt();
  }

  public String getImage() {
	  return (getString("image"));
  }

  public String getName() {
	  return (getString("name"));
  }

  public String getGroup_letter(){
    return(getString("group_letter"));
  }


}
