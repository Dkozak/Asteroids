package savegame;
import state.GameStateManager;
import java.io.*;
import java.net.*;
import java.util.HashMap;
/**
 *
 * @author Dimitri
 */
public class SaveGameManager
{

  private SaveGame[] savegames;
  private int current;
  private HashMap<Integer, Score> highscore;
  public static final int HIGHSCORESIZE = 10;
  
  public SaveGameManager()
  {
    //Nieuwe savegame bestanden maken (in dit geval 4)
    this.savegames = new SaveGame[4];
    for (int i = 0; i < 4; i++) {
      this.savegames[i] = new SaveGame(getDirectory() + "/save/savegame" + (i + 1) + ".txt");
    }
    loadHighscore();
  }
  //Als de savegame leeg is naar naamstate gaan anders naar upgrade state gaan
  public void select(int i, GameStateManager gsm)
  {
    this.current = i;
    if (this.savegames[i].isFree())
    {
      gsm.switchState(4);
      return;
    }
    gsm.switchState(3);
  }
  
  public void createNew(SaveGame sg, String name)
  {
    sg.clear();
    sg.setName(name);
    sg.save();
  }
  
  //highscore bestand aanmaken
  private void loadHighscore()
  {
    this.highscore = new HashMap();
    
    File file = new File(getDirectory() + "/save/highscore.txt");
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
      for (int i = 0; i < 10; i++)
      {
        String name = new String (reader.readLine());
        Integer score = Integer.valueOf(reader.readLine());
        
        Score s = new Score(name, score.intValue());
        
        this.highscore.put(Integer.valueOf(i), s);
      }
    }
    catch (Exception e)
    {
      e = 
      
        e;cleanHighscore();e.printStackTrace();
    }
    finally {}
  }
  //Highscore opslaan
  private void saveHighscore()
  {
    PrintWriter pw = null;
    
    File file = new File(getDirectory() + "/save/highscore.txt");
    try
    {
      FileWriter fw = new FileWriter(file);
      pw = new PrintWriter(new BufferedWriter(fw));
      for (int i = 0; i < 10; i++)
      {
        Score s = (Score)this.highscore.get(Integer.valueOf(i));
        pw.println(new String (s.getName().getBytes()));
        pw.println(new String (Integer.toString(s.getScore()).getBytes()));
      }
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
  
  private void cleanHighscore()
  {
    for (int i = 0; i < 10; i++) {
      if (!this.highscore.containsValue(Integer.valueOf(i))) {
        this.highscore.put(Integer.valueOf(i), new Score("-", 0));
      }
    }
    saveHighscore();
  }
  
  public int checkScore(String name, int score)
  {
    int place = -1;
    for (int i = 0; i < 10; i++) {
      if (score > ((Score)this.highscore.get(Integer.valueOf(i))).getScore())
      {
        String n = ((Score)this.highscore.get(Integer.valueOf(i))).getName();
        int s = ((Score)this.highscore.get(Integer.valueOf(i))).getScore();
        this.highscore.put(Integer.valueOf(i), new Score(name, score));
        place = i;
        checkScore(n, s);
        break;
      }
    }
    saveHighscore();
    this.savegames[this.current].setLastGameScore(place);
    return place;
  }
  
  public SaveGame get(int i)
  {
    return this.savegames[i];
  }
  
  public SaveGame getCurrent()
  {
    return this.savegames[this.current];
  }
  
  public String getDirectory()
  {
    String path = SaveGameManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    try
    {
      String decodedPath = URLDecoder.decode(path, "UTF-8");
      return new File(decodedPath).getParentFile().getPath().replace('\\', '/');
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public HashMap<Integer, Score> getHighscore()
  {
    return this.highscore;
  }
  
  public class Score
  {
    private String name;
    private int score;
    
    public Score(String name, int score)
    {
      this.name = name;
      this.score = score;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public int getScore()
    {
      return this.score;
    }
  }
}

