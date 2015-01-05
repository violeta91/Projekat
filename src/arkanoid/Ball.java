package arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Violeta
 */
public class Ball extends Rectangle.Double implements GameObject {
    private final int w = 20;
    private final int h = 20;
    
    // Minimalni, maksimalni intenzitet brzine lopte i korak ubrzanja
    private final int DX = 4;    
    private final int DY = 4;
    
    // Predstavljaju intenzitet po x i po y koordinati
    private int dx;
    private int dy;
    
    // Predstavljaju smer po x i po y koordinati
    private int directionX;
    private int directionY;
    
    private Board board;
    private Ellipse2D.Double ellipseForDrawing;
    
    private Color fillColor = Color.RED;
    private Color borderColor = Color.BLACK;

    public Ball(Board board) {
        this.board = board;
        directionX = 1;
        directionY = 1;
        width = w;
        height = h;
    }
    
    /**
     * Menja smer lopte po x osi.
     */
    public void bouceHorizontal() {
        directionX = -directionX;
    }
    
    /**
     * Menja smer lopte po y osi.
     */
    public void bouceVertical() {
        directionY = -directionY;
    }
    
    public void reset() {
        x = Board.PANEL_WIDTH/2 - w/2;
        y = Board.PANEL_HEIGHT - h -20;
        
        dx = DX;
        dy = DY;
    }
    
    @Override
    public void move() 
    {
        x += dx * directionX;
        y += dy * directionY;
    }
    
    @Override
    public void draw(Graphics2D g2) 
    {
        ellipseForDrawing = new Ellipse2D.Double(x, y, w, h);
        
        g2.setPaint(fillColor);
        g2.fill(ellipseForDrawing); 
        
        g2.setPaint(borderColor);
        g2.draw(ellipseForDrawing); 
    }
}
