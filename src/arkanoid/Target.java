/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Violeta
 */
public class Target implements GameObject
{
    public static final float WIDTH = 80;
    public static final float HEIGHT = 25;
    
    public RoundRectangle2D.Float ellipseForDrawing = null;
    
    float locationX;
    float locationY;
    
    private Color color = null;
    
    public Target(int x, int y)
    {
        locationX = x;
        locationY = y;
        
        this.ellipseForDrawing = new RoundRectangle2D.Float(locationX, locationY, WIDTH, HEIGHT, 10, 10);
        
        color = Color.BLUE;
    }
    
    @Override
    public void move()
    {
    }

    @Override
    public void draw(Graphics2D g2)
    {
        g2.setPaint(getColor());
        g2.fill(ellipseForDrawing);
        
        g2.setPaint(Color.BLACK);
        g2.draw(ellipseForDrawing);
    }

    /**
     * @return the color
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color)
    {
        this.color = color;
    }
    
}
