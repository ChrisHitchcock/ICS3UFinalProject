/*
 * Group Members:       Chris, Evan, Zhibo
 * Date of Submission:  June 24th, 2016
 */
package gameproject;

//Imports
import java.util.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;

public class Game extends BasicGame {
    //Call everything
    static boolean fullscreen = false;
    static boolean showFPS = true;
    static String title = "Flappy Bird";
    static int fpslimit = 60;
    public int Y = 400;
    double pipe1X = 300;
    double pipe2X = 650;
    Image topPipe1, bottomPipe1, topPipe2, bottomPipe2, bg;
    int topPipe1Y = -250;
    int bottomPipe1Y = 275;
    int topPipe2Y = -200;
    int bottomPipe2Y = 325;
    int change, check, current, highscore = 0;
    int count = 0;
    double gravity = 8;
    public Rectangle tP1H, bP1H, tP2H, bP2H;
    Random r = new Random();
    Font font;
    private Animation move;
    public Game(String title) {
        super(title);
    }
    private Input input;
    private Image bird;
    public int x, width, height, speed;
    public double oldY, y;
    private Rectangle hitbox;
    static enum GameState {

        intro, gameplay, gameover
    }
    static GameState state = GameState.intro;
    public boolean collision(Shape other) {
        return this.hitbox.intersects(other);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        //Initialize everything
        bird = new Image("assets/Images/BirdDown.png");
        Image[] movement = {new Image("assets/Images/Bird1.png"), new Image("assets/Images/Bird2.png"), new Image("assets/Images/Bird3.png")};
        int[] duration = {50, 50, 50};
        this.x = 100;
        this.y = 200;
        this.speed = 1;
        this.width = 51;
        this.height = 36;
        this.hitbox = new Rectangle(this.x, (int) this.y, this.width, this.height);
        topPipe1 = new Image("assets/Images/TopPipe1.png");
        bottomPipe1 = new Image("assets/Images/BottomPipe1.png");
        topPipe2 = new Image("assets/Images/TopPipe1.png");
        bottomPipe2 = new Image("assets/Images/BottomPipe1.png");
        bg = new Image("assets/Images/bg.png");
        tP1H = new Rectangle((int) pipe1X, topPipe1Y, 155, 368);
        bP1H = new Rectangle((int) pipe1X, bottomPipe1Y, 155, 368);
        tP2H = new Rectangle((int) pipe2X, topPipe2Y, 155, 368);
        bP2H = new Rectangle((int) pipe2X, bottomPipe2Y, 155, 368);
        input = gc.getInput();
        this.input = gc.getInput();
        move = new Animation(movement, duration, true);

    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        //Change state when up arrow is pressed
        if (state == GameState.intro) {
            if (input.isKeyDown(Input.KEY_SPACE)) {
                //Reset pipeXs and count when switched to gameplay
                pipe1X = 300;
                pipe2X = 650;
                count = 0;
                state = GameState.gameplay;
            }
        }

        switch (state) {
            case intro: {
                //Reset
                this.width = 51;
                this.height = 36;
                break;
            }
            case gameplay: {
                //Movement of bird
                this.width = 51;
                this.height = 36;
                if (input.isKeyDown(Input.KEY_SPACE)) {
                    if (y > oldY - check) { 
                    gravity = 12;
                    y -= gravity;
                    }
                } else if (y==oldY+check)
                {
                    y=oldY+check;
                    oldY=y;
                } else {
                    gravity = 3.5;
                    y += gravity;
                }
                pipe1X = pipe1X - 0.2 * delta;
                pipe2X = pipe2X - 0.2 * delta;
                break;

            }
            case gameover: {
                //Bird falls when it hits a pipe
                this.y = this.y + 0.2 * delta;
                this.width = 36;
                this.height = 51;
                break;
            }
        }
        //Reset pipes to new random position as the move off screen
        if ((int) pipe1X < -160) {
            change = (int) (Math.random() * -250.0 - 100);
            topPipe1Y = change;
            bottomPipe1Y = topPipe1Y + 525;
            pipe1X = 560;
            tP1H = new Rectangle((int) pipe1X, topPipe1Y, 155, 368);
            tP1H.setLocation((int) pipe1X, topPipe1Y);
            bP1H = new Rectangle((int) pipe1X, bottomPipe1Y, 155, 368);
            bP1H.setLocation((int) pipe1X, bottomPipe1Y);
        }
        if ((int) pipe2X < -160) {
            change = (int) (Math.random() * -250.0 - 100);
            topPipe2Y = change;
            bottomPipe2Y = topPipe2Y + 525;
            pipe2X = 560;
            tP2H = new Rectangle((int) pipe2X, topPipe2Y, 155, 368);
            tP2H.setLocation((int) pipe2X, topPipe2Y);
            bP2H = new Rectangle((int) pipe2X, bottomPipe2Y, 155, 368);
            bP2H.setLocation((int) pipe2X, bottomPipe2Y);
        }
        //Set hitboxes for pipes
        tP1H.setLocation((int) pipe1X, topPipe1Y);
        bP1H.setLocation((int) pipe1X, bottomPipe1Y);
        tP2H.setLocation((int) pipe2X, topPipe2Y);
        bP2H.setLocation((int) pipe2X, bottomPipe2Y);

        //Change state when up arrow is pressed
        if (state == GameState.intro) {
            if (input.isKeyDown(Input.KEY_SPACE)) {
                state = GameState.gameplay;
            }
        }
        //Reset bird position and hitbox
        if (input.isKeyPressed(Input.KEY_ENTER) && state == GameState.gameover) {
            this.y = 200;
            this.width = 51;
            this.height = 36;
            this.hitbox = new Rectangle(this.x, (int) this.y, this.width, this.height);
        }
        //To create jump bounds
        if (this.input.isKeyPressed(Input.KEY_SPACE) && state == GameState.gameplay) {
            check = 75;
            oldY = y;
        }
        //Bird boundaries
        if (this.x > gc.getWidth() - this.width) {
            this.x = gc.getWidth() - this.width;
        } else if (this.x < 0) {
            this.x = 0;
        }
        if (this.y > gc.getHeight() - this.height) {
            this.y = gc.getHeight() - this.height;
        } else if (this.y < 0) {
            this.y = 0;
        }
        //Set bird's hitbox
        hitbox.setBounds(this.x, (int) this.y, this.width, this.height);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        move.draw(x, (int) y, width, height);
        //Draw background
        bg.draw(0, 0, 260, 480);
        bg.draw(260, 0, 260, 480);

        //Render images based on state/collision check
        switch (state) {
            case intro: {
                //Draw intro
                move.draw(x, (int) y, width, height);
                g.drawString("Press Space to start", 225, 175);
                g.drawString("Use the Spacebar to jump.\n Try to get through the pipes!",225,225);
                break;
            }
            case gameplay: {
                //Draw all the images and add collision/highscore creator
                topPipe1.draw((int) pipe1X, topPipe1Y);
                bottomPipe1.draw((int) pipe1X, bottomPipe1Y);
                topPipe2.draw((int) pipe2X, topPipe2Y);
                bottomPipe2.draw((int) pipe2X, bottomPipe2Y);
                move.draw(x, (int) y, width, height);
                if (collision(tP1H) == true || collision(bP1H) == true) {
                    state = GameState.gameover;
                    current=count;
                    if (current>highscore)
                    {
                        highscore=current;
                    }
                }
                if (collision(tP2H) == true || collision(bP2H) == true) {
                    state = GameState.gameover;
                    current=count;
                    if (current>highscore)
                    {
                        highscore=current;
                    }
                }
                //Add to score
                if (pipe1X > 99 && pipe1X < 102) {
                    count += 1;
                }
                if (pipe2X > 99 && pipe2X < 102) {
                    count += 1;
                }
                break;
            }
            case gameover: {
                //Draw pipes and Strings, as well as being able to restart to intro
                topPipe1.draw((int) pipe1X, topPipe1Y);
                bottomPipe1.draw((int) pipe1X, bottomPipe1Y);
                topPipe2.draw((int) pipe2X, topPipe2Y);
                bottomPipe2.draw((int) pipe2X, bottomPipe2Y);
                bird.draw(x, (int) y, width, height);
                g.drawString("Game Over", 225, 190);
                g.drawString("Press Enter to Start Again", 150, 240);
                g.drawString("Score: " + count, 225, 290);
                g.drawString("High Score: "+Integer.toString(highscore),100,100);
                if (input.isKeyDown(Input.KEY_ENTER) && state == GameState.gameover) {
                    state = GameState.intro;
                }
                break;
            }
        }
        if ((int) pipe1X == 650) {
            topPipe1.draw((int) pipe1X, topPipe1Y);
        }
        //Show the score
        if (state == GameState.gameplay) {
            g.drawString(Integer.toString(count), 255, 50);

        }
    }
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game(title));
        app.setDisplayMode(520, 480, fullscreen);
        app.setSmoothDeltas(true);
        app.setTargetFrameRate(fpslimit);
        app.setShowFPS(showFPS);
        app.start();
    }
}