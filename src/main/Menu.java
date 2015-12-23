package main;
import item.Speler;
import java.awt.*;
import java.util.ArrayList;
import main.*;
/**
 *
 * @author Dimitri
 */
public class Menu {
  private ArrayList<MenuItem> menuItems;
  private Font itemFont;
  private int selected;
  private Speler indicator;
  private boolean indicatorMove;
  private float lastChangeTimer;
  private float changeTime;
  
  public Menu()
  {
      
    menuItems = new ArrayList();
    selected = 0;
    itemFont = Game.mainGameFont.deriveFont((float)64);
    changeTime = (float)0.4;
    
    indicator = new Speler(0, 0);
    indicator.setAcceleration(1000);
    indicator.setMaxSpeed(1500);
    indicator.setRotationspeed(0);
    indicator.update(5);
    
    init();
  }
  
  public void init()
  {
    selected = 0;
    indicatorMove = false;
    indicator.stop();
  }
  
  public void update(float delta)
  {
    lastChangeTimer += delta;
    if (!indicatorMove)
    {
      //Als we op Up klikken dan gaan we een selectie naar boven  
      if (checkKey(0))
      {
        selected -= 1;
        
        lastChangeTimer = 0;
        if (selected < 0) {
          selected = (menuItems.size() - 1);
        }
        JukeBox.play("menu_change.wav");
      }
      //Als we op Down klikken dan gaan we een selectie naar beneden
      if (checkKey(3))
      {
        selected += 1;
        
        lastChangeTimer = 0;
        if (selected > menuItems.size() - 1) {
          selected = 0;
        }
        JukeBox.play("menu_change.wav");
      }
      //Als we op enter drukken dan voeren we de achterliggende opdracht uit
      if (checkKey(5))
      {
        lastChangeTimer = 0;
        
        indicatorMove = true;
        ((MenuItem)menuItems.get(selected)).select();
        
        JukeBox.play("menu_select.wav");
      }
    }
    else
    {
      indicator.accelerate(true);
      indicator.update(delta);
    }
  }
  
  public void draw(Graphics2D g, int x, int y)
  {
    g.setFont(itemFont);
    for (int i = 0; i < menuItems.size(); i++)
    {
      ((MenuItem)menuItems.get(i)).draw(g, x, y);
      if (selected == i)
      {
        if (!indicatorMove) {
          indicator.setPosition(x - indicator.getLength() - 10 - ((MenuItem)menuItems.get(i)).getWidth() / 2, y - g.getFontMetrics().getHeight() / 2);
        }
        indicator.draw(g);
      }
      y += ((MenuItem)menuItems.get(i)).getHeight();
    }
  }
  
  public void add(MenuItem item)
  {
    menuItems.add(item);
  }
  
  public void setItemFont(Font font)
  {
    itemFont = font;
  }
  
  public int getPosition()
  {
    return selected;
  }
  
  public void setItemColor(Color color)
  {
    for (int i = 0; i < menuItems.size(); i++) {
      ((MenuItem)menuItems.get(i)).setColor(color);
    }
  }
  
  public void clear()
  {
    menuItems.clear();
  }
  
  private boolean checkKey(int key)
  {
    return (GameKeys.isPressed(key)) || ((canChange()) && (GameKeys.isDown(key)));
  }
  
  private boolean canChange()
  {
    return lastChangeTimer > changeTime;
  }
}
