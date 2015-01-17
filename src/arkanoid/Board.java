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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Klasa Board nasledjuje JPanel i implementira interface Runnable.
 * 
 * @author Violeta
 */
class Board extends JPanel implements Runnable
{
    /**
     * Sirina table
     */
    public static final int PANEL_WIDTH = 800;
    
    /**
     * Visina table
     */
    public static final int PANEL_HEIGHT = 615;
    
    /*
    Rastojanje izmedju meta po y koordinati.
    */
    public static int Y_SPACE_TARGET = 35;
    
    final Color BACKGROUND_COLOR = Color.CYAN;
    final Thread runner;
    
    // Bodovi u igri
    
    int myScore = 0;
    
    Boolean inGame;
    
    
    // Objekti u igri
    
    Ball ball;
    Pad pad;
    
    String message;
    
    ArrayList<Target> listTargets = null;
    
    //broj zivota
    private int numberOfLife;
    
    /**
     * Osnovni konstruktor koji postavlja osnovna podesavanja prozora za igricu.
     */
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
        
        //dodajemo osluskivac na Board za tastaturu
        addKeyListener(new GameKeyAdapter());
        
        generateTargets();
        
        //dodajemo proces za board
        runner = new Thread(this);
        //startujemo proces
        runner.start();
    }
    
    /**
     * Funkcija postavlja parametre za pocetak igre.
     */
    public void startGame() 
    {
        myScore = 0;
        inGame = true;
        
        setNumberOfLife(5);
        
        generateTargets();
        
        ball.reset();
        pad.reset();
    }
    
    /**
     * Funkcija postavlja parametre za stopiranje igre.
     * 
     * @param message Poruka koja se ispisuje na ekran.
     */
    public void stopGame(String message) 
    {
        inGame = false;
        this.message = message;
    }
    
    /**
     * Dodaju se bodovi.
     * 
     * @param number Broj bodova koji se dodaje na postojece bodove.
     */
    public void countScore(int number) {
        myScore += number;
    }
    
    /**
     * Funckija vrsi iscrtavanje osnovnih parametara prozora, kao sto su loptica, reket, poruke za bodove i zivote, kao i mete.
     * 
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

            // Iscrtaj sve objekte
            
            pad.draw(g2);
            
            for (int i = 0; i < listTargets.size(); i++)
            {
                listTargets.get(i).draw(g2);
            }
            
            ball.draw(g2);

            // Iscrtaj rezultat

            g2.drawString("" + myScore, 10, 20);
            
            //Iscrtaj broj zivota
            
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
    
    /**
     * Metoda vrsi azuriranje loptice i reketa.
     */
    private void update() 
    {
        ball.move();
        pad.move();
    }
    
    /**
     * Funkcija detektuje poklapanje loptice sa reketom i loptice sa metom.
     */
    private void detectCollision()
    {
        if (ball.intersects(pad)) //ako se loptica poklapa sa reketom
        {
            ball.bouceVertical();
        }
        
        Rectangle2D ballBounds = ball.getBounds();
        
        /*
        Prolazimo kroz sve objekte meta i ispituje da li se poklapaju sa lopticom.
        U slucaju poklapanja testiramo koja je boja i u zavisnosti toga dodeljujemo odredjen broj bodova.
        Kasnije se pogodjena meta uklanja iz liste.
        */
        
        for (int i = 0; i < listTargets.size(); i++)
        {
            Target tempTarget = listTargets.get(i);
            
            if (tempTarget.ellipseForDrawing.intersects(ballBounds))
            {
                if(tempTarget.getColor() == Color.LIGHT_GRAY)
                {
                    countScore(1);
                }
                else if(tempTarget.getColor() == Color.BLUE)
                {
                    countScore(2);
                }
                else
                {
                    countScore(3);
                }
                
                this.listTargets.remove(i);
                ball.bouceVertical();
            }
        }
        
        //U slucaju da je pogodjena zadanja meta, zaustavlja se igrica i korisniku se cestita na pobedi.
        if(listTargets.size() == 0)
        {
            stopGame("Čestitamo pobedili ste, Vaš skor je " + this.myScore + ".");
        }
    }
    
    /**
     * Funkcija pokrece proces za board.
     */
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
        /**
         * Funkcija se izvrsava na svako pritisnuto dugme sa tastature.
         * 
         * @param e Parametar koji nosi podatke o dugmetu koje je pozvalo osluskivac.
         */
        @Override
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            if (keyCode == KeyEvent.VK_LEFT) //dugme lijevo
                pad.moveLeft();
            else if (keyCode == KeyEvent.VK_RIGHT) //dugme desno
                pad.moveRight();
        }

        /**
         * Funkcija se izvršava kada se dugme otpusti.
         * 
         * @param e Parametar koji nosi podatke o dugmetu koje ga je pozvalo.
         */
        @Override
        public void keyReleased(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
                pad.stopMoving();
        }
    }
    
    /**
     * Funkcija generise mete sa lokaciom i smesta ih u listu.
     */
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
