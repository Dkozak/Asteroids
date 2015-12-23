package animatie;
import java.awt.*;
import java.util.Random;
/**
 *
 * @author Dimitri
 */
public class Explosie {
  private Particle[] particles;
  private boolean finished;
  
  public Explosie(float x, float y, int numParticles)
  {
    this.particles = new Particle[numParticles];
    Random r = new Random();
    for (int i = 0; i < this.particles.length; i++) {
      this.particles[i] = new Particle(x, y, r.nextInt(360));
    }
  }
  
  public void update(double delta)
  {
    this.finished = true;
    for (int i = 0; i < this.particles.length; i++) {
      if (!this.particles[i].hasFinished())
      {
        this.particles[i].update(delta);
        this.finished = false;
      }
    }
  }
  
  public boolean hasFinished()
  {
    return this.finished;
  }
  
  public void draw(Graphics2D g)
  {
    for (int i = 0; i < this.particles.length; i++) {
      this.particles[i].draw(g);
    }
  }
}

