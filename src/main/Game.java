package main;
import state.GameStateManager;
import java.awt.*;
import java.io.IOException;
import javax.swing.JPanel;
import java.awt.event.*;

/**
 *
 * @author Dimitri
 */

public class Game extends JPanel implements Runnable, KeyListener{
  public static Font mainGameFont;
  public static final int WIDTH = 1920;
  public static final int HEIGHT = 1080;
  private static int screenWidth;
  private static int screenHeight;
  public static final int FPS = 60;
  private float scalex;
  private float scaley;
  private boolean running;
  private GameStateManager gsm;
  
  static
  {
    try
    {
      mainGameFont = Font.createFont(0, Game.class.getResourceAsStream("/lettertype/courier.ttf"));
    }
    catch (FontFormatException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public Game()
  {
    setPreferredSize(new Dimension(1920, 1080));
    gsm = new GameStateManager();
  }
  
  public void init()
  {
    addKeyListener(this);
    setFocusable(true);
    requestFocus();
    addComponentListener(new ComponentAdapter()
    {
      @Override  
      public void componentResized(ComponentEvent e)
      {
        super.componentResized(e);  
        screenWidth=(Game.this.getWidth());
        screenHeight=(Game.this.getHeight());
        Game.this.scalex = (Game.screenWidth * 1 / (float)1920);
        Game.this.scaley = (Game.screenHeight * 1 / (float)1080);
      }
    });
    Thread gameThread = new Thread(this, "Diminoids");
    gameThread.start();
  }
  @Override
  public void run()
  {
    this.running = true;
    long lastFrame = System.currentTimeMillis();
    while (this.running){
      long thisFrame = System.currentTimeMillis();
      float delta = (float)(thisFrame - lastFrame) / (float)1000;
      lastFrame = thisFrame;
      gsm.update(delta);
      repaint();
      GameKeys.update();
      int sleeptime = (int)(16 - delta);
      if (sleeptime < 0) {
        sleeptime = 0;
      }
      try
      {
        Thread.sleep(sleeptime);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.scale(scalex, scaley);
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 1920, 1080);
    gsm.draw(g2);
  }
  @Override
  public void keyPressed(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      GameKeys.setKey(0, true);
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      GameKeys.setKey(1, true);
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      GameKeys.setKey(2, true);
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      GameKeys.setKey(3, true);
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      GameKeys.setKey(4, true);
    }
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      GameKeys.setKey(5, true);
    }
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      GameKeys.setKey(6, true);
    }
    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
      GameKeys.setKey(7, true);
    }
  }
  @Override
  public void keyReleased(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      GameKeys.setKey(0, false);
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      GameKeys.setKey(1, false);
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      GameKeys.setKey(2, false);
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      GameKeys.setKey(3, false);
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      GameKeys.setKey(4, false);
    }
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      GameKeys.setKey(5, false);
    }
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      GameKeys.setKey(6, false);
    }
    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
      GameKeys.setKey(7, false);
    }
  }
  
  public void keyTyped(KeyEvent e) {}
}
