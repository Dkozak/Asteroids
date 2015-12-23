package state;
import animatie.TekstAnimatie;
import item.Speler;
import java.awt.*;
import java.util.*;
import main.Game;
import main.GameKeys;
import main.Colors;
import main.JukeBox;
import savegame.SaveGame;
import savegame.SaveGameManager;
/**
 *
 * @author Dimitri
 */
public class PrepareState extends GameState
{
    //Variabelen defigneren
    private SaveGameManager sgm;
    private ArrayList<Upgrade> items;
    private Font itemDescriptionFont;
    private ArrayList<TekstAnimatie> textAnimations;
    private int selected;
    private HashMap<Integer, SaveGameManager.Score> highscore;
    SaveGame sg;
    private float dif;
    private int difAmount;
    private Speler indicator;
    private boolean indicatorMove;
    
    public PrepareState(final GameStateManager gsm, final SaveGameManager sgm) {
        super(gsm);
        this.sgm = sgm;
        this.itemDescriptionFont = new Font("Lucida Sans Regular", 1, 20);
        this.difAmount = 80;
    }
    
    @Override
    public void init() {
        this.sg = this.sgm.getCurrent();
        this.items = new ArrayList<Upgrade>();
        this.textAnimations = new ArrayList<TekstAnimatie>();
        this.selected = 0;
        this.indicatorMove = false;
        //spaceship in menu laten bewegen
        (this.indicator = new Speler(0, 0)).setAcceleration(1000);
        this.indicator.setAcceleration(1000);
        this.indicator.setMaxSpeed(1500);
        this.indicator.setRotationspeed(0);
        this.indicator.update(5);
        this.highscore = this.sgm.getHighscore();
        this.items.add(new Upgrade("Play", "Start het spel!", 0, new int[] { 0 }, true) {
            @Override
            public void onUpgrade() {
                PrepareState.this.indicatorMove = true;
                PrepareState.this.gsm.switchState(1);
            }
        });
        this.items.add(new Upgrade("Load", "Load another savegame", 0, new int[0], true) {
            @Override
            public void onUpgrade() {
                PrepareState.this.indicatorMove = true;
                PrepareState.this.gsm.switchState(2);
            }
        });
        this.items.add(new Upgrade("Quit", "Don't!",0, new int[0], true) {
            @Override
            public void onUpgrade() {
                System.exit(0);
            }
        });
    }
    
    @Override
    public void update(final float delta) {
        for (int i = 0; i < this.textAnimations.size(); ++i) {
            this.textAnimations.get(i).update(delta);
        }
        //Up arrow en selectie naar boven gaan
        if (GameKeys.isPressed(0)) {
            --this.selected;
            if (this.selected < 0) {
                this.selected = this.items.size() - 1;
                this.dif = this.difAmount * this.items.size();
            }
            else {
                this.dif -= this.difAmount;
            }
            JukeBox.play("menu_change.wav");
        }
        //Down Arrow en selectie naar beneden gaan
        if (GameKeys.isPressed(3)) {
            ++this.selected;
            if (this.selected > this.items.size() - 1) {
                this.selected = 0;
                this.dif = -this.difAmount * this.items.size();
            }
            else {
                this.dif += this.difAmount;
            }
            JukeBox.play("menu_change.wav");
        }
        if (this.dif > 0) {
            this.dif -= this.dif * 4 * delta;
            if (this.dif < 0) {
                this.dif = 0;
            }
        }
        else if (this.dif < 0) {
            this.dif -= this.dif * 4 * delta;
            if (this.dif > 0) {
                this.dif = 0;
            }
        }
        //Enter 
        if (GameKeys.isPressed(5)) { 
            this.items.get(this.selected).doUpgrade();
        }
        if (this.indicatorMove) {
            this.indicator.accelerate(true);
            this.indicator.update(delta);
        }
        if (GameKeys.isPressed(6)) {
            this.gsm.switchState(2);
        }
    }
    
    @Override
    public void draw(final Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1920, 1080);
        g.setFont(Game.mainGameFont.deriveFont((float)32));
        final int fontHeight = g.getFontMetrics().getHeight() * 2;
        g.setColor(Color.WHITE);
        g.drawString(this.sg.getName(), 960, fontHeight);
        final int itemx = 100;
        int y = -this.selected * (fontHeight + 40) + 540 + (int)this.dif;
        for (int i = 0; i < this.items.size(); ++i) {
            if (i == this.selected) {
                g.setColor(Color.WHITE);
                g.setFont(Game.mainGameFont.deriveFont((float)48));
                if (!this.indicatorMove) {
                    this.indicator.setPosition(itemx - 40, 540 - fontHeight / 2);
                }
                this.indicator.draw(g);
            }
            else {
                float fs = (this.selected - i) * 2;
                if (this.selected > i) {
                    fs = -fs;
                }
                g.setColor(Color.DARK_GRAY);
                g.setFont(Game.mainGameFont.deriveFont((float)32 + fs));
            }
            g.drawString(this.items.get(i).getName(), itemx, y);
            if (i == this.selected) {
                g.setFont(this.itemDescriptionFont);
                final int height = g.getFontMetrics().getHeight();
                if (this.items.get(i).isAlwaysUpgradable()) {
                    g.setColor(Colors.GREEN);
                    g.drawString(this.items.get(i).getDescription(), itemx, y + fontHeight / 2);
                }
                g.setFont(Game.mainGameFont.deriveFont((float)24));
                y += height * 3;
            }
            y += fontHeight + 40;
        }
        this.drawHighscore(g, 960, 200, 480);
        for (int i = 0; i < this.textAnimations.size(); ++i) {
            this.textAnimations.get(i).draw(g);
        }
    }
    //Highscore tabel laten zien
    private void drawHighscore(final Graphics2D g, final int x, final int y, final int width) {
        g.setFont(Game.mainGameFont.deriveFont((float)48));
        g.setColor(Colors.BLUE);
        g.drawString("Highscore", x, y);
        g.setFont(Game.mainGameFont.deriveFont((float)32));
        final int itemHeight = g.getFontMetrics().getHeight() * 2;
        final int height = itemHeight + 200;
        int itemy = 60;
        g.setColor(Color.BLACK);
        g.fillRect(x, itemy + y, width / 2, height);
        g.setColor(Color.WHITE);
        for (int i = 0; i < 10; ++i) {
            g.drawString(this.highscore.get(i).getName(), x, y + itemy);
            itemy += itemHeight;
        }
        itemy = 60;
        g.setColor(Color.BLACK);
        g.fillRect(x + width / 2, itemy + y, width / 2, height);
        g.setColor(Color.WHITE);
        for (int i = 0; i < 10; ++i) {
            if (i == this.sg.getLastGamePlacement()) {
                g.setColor(Colors.GREEN);
            }
            else if (this.highscore.get(i).getName().equals(this.sg.getName())) {
                g.setColor(Colors.YELLOW);
            }
            else {
                g.setColor(Color.WHITE);
            }
            final int posx = x + width - (int)g.getFontMetrics().getStringBounds(Integer.toString(this.highscore.get(i).getScore()), g).getWidth();
            g.drawString(Integer.toString(this.highscore.get(i).getScore()), posx, y + itemy);
            itemy += itemHeight;
        }
    }
    
    //Als we upgraden (klikken) 
    private abstract class Upgrade
    {
        private String name;
        private String description;
        private int currentlevel;
        private int[] costs;
        private boolean alwaysUpgradable;
        
        public Upgrade(final String name, final String description, final int currentlevel, final int[] costArray, final boolean upgradable) {
            this.name = name;
            this.description = description;
            this.currentlevel = currentlevel;
            this.costs = costArray;
            this.alwaysUpgradable = upgradable;
        }
        
        public void doUpgrade() {
                this.onUpgrade();
                PrepareState.this.sg.save();
                JukeBox.play("play.wav");
            }
        
        public boolean isAlwaysUpgradable() {
            return this.alwaysUpgradable;
        }
        
        public abstract void onUpgrade();
        
        public String getName() {
            return this.name;
        }
        
        public String getDescription() {
            return this.description;
        }
    }
}