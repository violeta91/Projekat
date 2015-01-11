/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * @author Violeta
 */
class Board extends JPanel implements Runnable
{
    public static final int PANEL_WIDTH = 800;
    
    public static final int PANEL_HEIGHT = 615;
    
    public static int Y_SPACE_TARGET = 35;
    
    final Color BACKGROUND_COLOR = Color.CYAN;
    final Thread runner;
    
    Boolean inGame;
    
     // Objekti u igri
    
    Ball ball;
    Pad pad;
    
    String message;
    
    ArrayList<Target> listTargets = null;
    
    //broj zivota
    private int numberOfLife;
    
    public Board()
    {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);
        setFont(getFont().deriveFont(Font.BOLD, 18f));
        setDoubleBuffered(true);
        
        inGame = false;
        message = "ARKANOID";
        
        ball = new Ball(this);
        pad = new Pad(this, PANEL_WIDTH/2 - Pad.w/2, PANEL_HEIGHT - Pad.h);
        
        addKeyListener(new GameKeyAdapter());
        
        generateTargets();
        
        runner = new Thread(this);
        
        runner.start();
    }
    
    public void startGame() 
    {
        inGame = true;
        
        setNumberOfLife(5);
        
        ball.reset();
        pad.reset();
    } 
    
    public void stopGame(String message) 
    {
        inGame = false;
        this.message = message;
    }
    
    /**
     * @param g Paramtar koji sluzi za dohvatanje funkcija za iscrtavanje.
     */
    @Override
    public void paint(Graphics g) 
    {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        if (inGame) 
        {
            // Saveti pri iscrtavanju
        
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            pad.draw(g2);
            
            for (int i = 0; i < listTargets.size(); i++)
            {
                listTargets.get(i).draw(g2);
            }
             
            ball.draw(g2);
            
            g2.drawString("Broj života: " + getNumberOfLife(), PANEL_WIDTH-160, 20);
            
            // Sinhronizovanje sa grafickom kartom
            Toolkit.getDefaultToolkit().sync();

            // Optimizacija upotrebe RAM-a, 
            g.dispose();
        } else {
            int messageWidth = getFontMetrics(getFont()).stringWidth(message);
            g2.drawString(message, PANEL_WIDTH/2 - messageWidth/2, PANEL_HEIGHT/2);
        }
    }
    
    private void detectCollision()
    {
        if (ball.intersects(pad)) //ako se loptica poklapa sa reketom
        {
            ball.bouceVertical();
        }
    }
    
    private void update() 
    {
        ball.move();
        pad.move();
    }

    @Override
    public void run()
    {
        while(true) 
        {
            update();
            detectCollision();
            repaint();

            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
    /**
     * @return the numberOfLife
     */
    public int getNumberOfLife()
    {
        return numberOfLife;
    }

    /**
     * @param numberOfLife the numberOfLife to set
     */
    public void setNumberOfLife(int numberOfLife)
    {
        this.numberOfLife = numberOfLife;
    }
    
    private class GameKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            if (keyCode == KeyEvent.VK_LEFT)
                pad.moveLeft();
            else if (keyCode == KeyEvent.VK_RIGHT)
                pad.moveRight();
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
                pad.stopMoving();
        }
    }
    
    public void generateTargets()
    {
         //Lista meta
        listTargets = new ArrayList<Target>();
        
        int yLocal = 50;
        
        int xLocal = 150;
        for (int i = 0; i < 4; i++, xLocal += 125)
        {
            listTargets.add(new Target(xLocal, yLocal));
        }
        
        xLocal = 100;
        yLocal += Y_SPACE_TARGET;
        for (int i = 4; i < 9; i++, xLocal += 125)
        {
            listTargets.add(new Target(xLocal, yLocal));
        }

        xLocal = 50;
        yLocal += Y_SPACE_TARGET;
        for (int i = 9; i < 15; i++, xLocal += 125)
        {
            listTargets.add(new Target(xLocal, yLocal));
        }

        xLocal = 100;
        yLocal += Y_SPACE_TARGET;
        for (int i = 15; i < 20; i++, xLocal += 125)
        {
            listTargets.add(new Target(xLocal, yLocal));
        }

        xLocal = 150;
        yLocal += Y_SPACE_TARGET;
        for (int i = 20; i < 24; i++, xLocal += 125)
        {
            listTargets.add(new Target(xLocal, yLocal));
        }
    }
}
