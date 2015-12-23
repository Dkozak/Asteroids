package item;
import java.util.Random;
import java.util.ArrayList;
import main.JukeBox;
/**
 *
 * @author Dimitri
 */

//Op basis van UFO
public class GroteUfo extends Ufo{
  public GroteUfo()
  {
    //Firerate, grootte en aantal punten
    super(null);
    this.size = 60;
    this.firerate = 2;
    this.points = 50;
    setShape();
  }
  
  protected void shoot()
  {
    Random r = new Random();
    this.bullets.add(new Kogel(this.x, this.y, r.nextInt(360), this.bulletspeed));
    JukeBox.play("saucer_shoot.wav");
  }
}
