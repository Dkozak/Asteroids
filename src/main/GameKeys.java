package main;

/**
 *
 * @author Dimitri
 */
public class GameKeys
{
  //Knoppen instellen
  private static final int NUM_KEYS = 8;
  private static boolean[] keys = new boolean[NUM_KEYS];
  private static boolean[] pkeys = new boolean[NUM_KEYS];
  public static final int UP = 0;
  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  public static final int DOWN = 3;
  public static final int SPACE = 4;
  public static final int ENTER = 5;
  public static final int ESCAPE = 6;
  public static final int DEL = 7;
  
  public static void update()
  {
    for (int i = 0; i < NUM_KEYS; i++) {
      pkeys[i] = keys[i];
    }
  }
  
  public static void setKey(int key, boolean b)
  {
   keys[key] = b;
  }
  
  public static boolean isDown(int k)
  {
    return keys[k];
  }
  
  public static boolean isPressed(int k)
  {
    boolean b = keys[k] && !pkeys[k];
    pkeys[k] = keys[k];
    return b;
  }
}

