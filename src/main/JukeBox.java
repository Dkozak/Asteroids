package main;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.sound.sampled.*;
/**
 *
 * @author Dimitri
 */
public class JukeBox {
  //Muziekbestanden koppelen aan een variabele
  public static ArrayList<Clip> loops = new ArrayList();
  public static URL ASTEROID_EXPLOSION_LARGE = JukeBox.class.getResource("/audio/asteroid_explosion_large.wav");
  public static URL ASTEROID_EXPLOSION_MEDIUM = JukeBox.class.getResource("/audio/asteroid_explosion_medium.wav");
  public static URL ASTEROID_EXPLOSION_SMALL = JukeBox.class.getResource("/audio/asteroid_explosion_small.wav");
  public static URL MENU_CHANGE = JukeBox.class.getResource("/audio/menu_change.wav");
  public static URL MENU_SELECT = JukeBox.class.getResource("/audio/menu_select.wav");
  public static URL MUSIC = JukeBox.class.getResource("/audio/music.wav");
  public static URL PLAYER_HIT = JukeBox.class.getResource("/audio/player_hit.wav");
  public static URL PLAYER_SHOOT = JukeBox.class.getResource("/audio/player_shoot.wav");
  public static URL SAUCER_SHOOT = JukeBox.class.getResource("/audio/saucer_shoot.wav");
  public static URL SPECIAL_ATTACK = JukeBox.class.getResource("/audio/special_attack.wav");
  public static URL UPGRADE_DONE = JukeBox.class.getResource("/audio/play.wav");
  private static long lastSound = System.currentTimeMillis();
  
  public static synchronized void play(String s)
  {
    long thisSound = System.currentTimeMillis();
    if (thisSound - lastSound < 10L) {
      return;
    }
    lastSound = thisSound;
    
    Clip clip = null;
    try
    {
      clip = AudioSystem.getClip();
      AudioInputStream ais = AudioSystem.getAudioInputStream(JukeBox.class.getResource("/audio/" + s));
      
      clip.open(ais);
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedAudioFileException e)
    {
      e.printStackTrace();
    }
    clip.start();
  }
  
  public static synchronized void loop(String s)
  {
    Clip clip = null;
    try
    {
      clip = AudioSystem.getClip();
      AudioInputStream ais = AudioSystem.getAudioInputStream(JukeBox.class.getResource("/audio/" + s));
      clip.open(ais);
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedAudioFileException e)
    {
      e.printStackTrace();
    }
    loops.add(clip);
    
    clip.loop(-1);
  }
  
  public static void stopLoops()
  {
    for (int i = 0; i < loops.size(); i++)
    {
      ((Clip)loops.get(i)).stop();
      loops.remove(i);
      i--;
    }
  }
}

