package savegame;
import java.io.*;
/**
 *
 * @author Dimitri
 */
public class SaveGame{
  private String path;
  private String name;
  private int acceleration = 0;
  private int maxSpeed = 0;
  private int rotationspeed = 0;
  private int firerate = 0;
  private int bulletspeed = 0;
  private int specialAttackCD = 0;
  private int specialAttackNumBullets = 0;
  private int numBulletsPerShot = 0;
  private int specialAttackOnDeath = 0;
  private int bulletLifeTime = 0;
  private int lastGameScore;
  private boolean free;
  
  public SaveGame(String path)
  {
    this.path = path;
    this.lastGameScore = -1;
    load();
  }
  
  private void load()
  {
    //Nieuw bestand aanmaken  
    File file = new File(this.path);
    //Nieuwe map aanmaken
    File Map = new File(this.path.substring(0, this.path.length() -13));
    //Controleren of de map al bestaat
    if (!Map.exists()){
      try{
          Map.mkdir();
      }
      catch (SecurityException se){
          
      }
    }
    //Controleren of het bestand al bestaat
    if (!file.exists()) {
      try
      {
        file.createNewFile();
        
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    //Als het savegame bestand leeg is dan krijgt het de naam Empty
    if (file.length() == 0)
    {
      this.name = "Empty";
      this.free = true;
      return;
    }
    FileInputStream is = null;
    try
    {
      is = new FileInputStream(file);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    InputStreamReader streamReader = new InputStreamReader(is);
    BufferedReader reader = new BufferedReader(streamReader);
    try
    {
      this.name = new String(reader.readLine());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (NumberFormatException e)
    {
      clear();
      
      e.printStackTrace();
    }
  }
  //Nieuwe user wegschrijven naar het bestand
  public void save()
  {
    this.free = false;
    
    PrintWriter pw = null;
    File file = new File(this.path);
    try
    {
      FileWriter fw = new FileWriter(file);
      pw = new PrintWriter(new BufferedWriter(fw));
      pw.println(new String (this.name.getBytes()));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (pw != null) {
        pw.flush();
      }
    }
  }
  
  //User verwijderen
  public void clear()
  {
    File file = new File(this.path);
    try
    {
      FileOutputStream writer = new FileOutputStream(file);
      writer.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    load();
  }
  
  public String getName()
  {
    return this.name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  
  public boolean isFree()
  {
    return this.free;
  }
  
  public int getLastGamePlacement()
  {
    return this.lastGameScore;
  }
  
  public void setLastGameScore(int i)
  {
    this.lastGameScore = i;
  }
}

