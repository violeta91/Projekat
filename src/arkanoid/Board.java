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
import javax.swing.JPanel;

/**
 * @author Violeta
 */
class Board extends JPanel implements Runnable
{
    public static final int PANEL_WIDTH = 800;
    
    public static final int PANEL_HEIGHT = 615;
    
    final Color BACKGROUND_COLOR = Color.CYAN;
    final Thread runner;
    
    Boolean inGame;
    
     // Objekti u igri
    
    Ball ball;
    
    String message;
    
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
        
        runner = new Thread(this);
    }
    
    public void startGame() 
    {
        inGame = true;
        
        ball.reset();
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

            ball.draw(g2);
            
            // Sinhronizovanje sa grafickom kartom
            Toolkit.getDefaultToolkit().sync();

            // Optimizacija upotrebe RAM-a, 
            g.dispose();
        } else {
            int messageWidth = getFontMetrics(getFont()).stringWidth(message);
            g2.drawString(message, PANEL_WIDTH/2 - messageWidth/2, PANEL_HEIGHT/2);
        }
    }
    
    private void update() 
    {
        ball.move();
    }

    @Override
    public void run()
    {
        while(true) 
        {
            update();
            repaint();

            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
}
