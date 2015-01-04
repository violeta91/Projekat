/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Violeta
 */
class Frame extends JFrame
{   
    Board board = new Board();
    
    public Frame()
    {  
        setJMenuBar(initMenu());
        
        pack();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Arkanoid");
        
        setVisible(true);
    }
    
    /**
     * @return menuBar
     */
    final JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu gameMenu = new JMenu("Game");
        
        JMenuItem newGame = new JMenuItem("New game");
        
        gameMenu.add(newGame);
        
        menuBar.add(gameMenu);
        
        return menuBar;
    }
}
