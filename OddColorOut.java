
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.imageio.*;
import javax.imageio.stream.*;

/**
 * OddColorOut by Priydarshi Singh
 */

public class OddColorOut
{
    JFrame mainFrame = new JFrame("OddColorOut");
    JLabel timeLabel = new JLabel("60");
    JPanel timePanel = new JPanel(), mainPanel = new JPanel();
    javax.swing.Timer timer;
    Random randomNumberGenerator = new Random();
    Color correctColor, normalColor;
    int gridSize=3, gridsOfThisSizeAlreadyMade=0, score=0;
    
    public void init(){
        change();
        mainFrame.add(timePanel, BorderLayout.NORTH);
        timePanel.add(timeLabel);
        timePanel.setBackground(Color.WHITE);
        timeLabel.setFont(new Font("Ubuntu Mono", Font.BOLD, 16));
        timeLabel.setForeground(new Color(0,255,0));
        ActionListener timerAction = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    int currentTime = Integer.parseInt(timeLabel.getText());
                    if(currentTime==0) end();
                    else{
                        timeLabel.setText(String.valueOf(currentTime-1));
                        timeLabel.setForeground(new Color((255*(60-currentTime))/60,(255*currentTime)/60,0));
                    }
                }
            };
        timer = new javax.swing.Timer(1000,timerAction);
        timer.start();
        mainFrame.addWindowListener(new WindowAdapter(){
            public void windowClosed(WindowEvent e){
                end();
            }
        });
    }
    
    public void change()
    {
        mainFrame.remove(mainPanel);
        mainPanel.removeAll();
        mainPanel = new JPanel(new GridLayout(gridSize,gridSize));
        normalColor = new Color(100+randomNumberGenerator.nextInt(155), 100+randomNumberGenerator.nextInt(155), 100+randomNumberGenerator.nextInt(155));
        correctColor = getCorrectColor(normalColor);
        int pos = randomNumberGenerator.nextInt(gridSize*gridSize);
        for(int i=0; i<gridSize*gridSize; i++)
            mainPanel.add(new ColoredPane( (i==pos) ? correctColor : normalColor , gridSize));
        gridsOfThisSizeAlreadyMade++;
        if(gridsOfThisSizeAlreadyMade==3)
        {
            gridSize++;
            gridsOfThisSizeAlreadyMade=0;
        }
        mainFrame.add(mainPanel);
        mainPanel.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                ColoredPane cp = (ColoredPane)mainPanel.getComponentAt(new Point(e.getX(), e.getY()));
                answered(cp.getColor());
            }
        });
    }
    
    public void answered(Color x){
        ActionListener resetBackgroundAction = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    timePanel.setBackground(Color.WHITE);
                }
            };
        javax.swing.Timer resetBackgroundTimer = new javax.swing.Timer(200, resetBackgroundAction);
        resetBackgroundTimer.setRepeats(false);
        if(x.equals(correctColor))
        {
            score+=200;
            timePanel.setBackground(Color.GREEN);
            resetBackgroundTimer.start();
            change();
        }else{
            score-=50;
            timePanel.setBackground(Color.RED);
            resetBackgroundTimer.start();
        }
    }
    
    public Color getCorrectColor(Color c)
    {
        int r=c.getRed(),g=c.getGreen(),b=c.getBlue();
        int nr = (randomNumberGenerator.nextInt(100)%2==0) ? r+(randomNumberGenerator.nextInt(10)+5) : r-(randomNumberGenerator.nextInt(10)+5);
        int ng = (randomNumberGenerator.nextInt(100)%2==0) ? g+(randomNumberGenerator.nextInt(10)+5) : g-(randomNumberGenerator.nextInt(10)+5);
        int nb = (randomNumberGenerator.nextInt(100)%2==0) ? b+(randomNumberGenerator.nextInt(10)+5) : b-(randomNumberGenerator.nextInt(10)+5);
        if(nr>255 || nb>255 || ng>255 || ng<0 || nr<0 || nb<0) return getCorrectColor(c);
        return new Color(nr,ng,nb);
    }
    
    public void end(){
        mainFrame.dispose();
        JOptionPane.showMessageDialog(null, "Game Over \n\n"
                                          + "Score : " + score +"\n\n"
                                          + "Well Done!", 
                                          "OddColorOut", -1);
        timer.stop();
        System.exit(0);
    }
    
    public static void main(String args[])
    {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
        OddColorOut app = new OddColorOut();
        try{app.mainFrame.setIconImage(ImageIO.read(new FileImageInputStream(new File("images/OddColorOut.png"))));}catch(Exception e){}
        app.mainFrame.setSize(600,600);
        app.mainFrame.setLocationRelativeTo(null);
        app.mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        app.mainFrame.setDefaultLookAndFeelDecorated(true);
        app.mainFrame.setVisible(true);
        app.init();
    }
    
    class ColoredPane extends JPanel{
        Color color;
        int gridSize;
        public ColoredPane(Color c, int gs){
            super();
            color = c;
            gridSize = gs;
        }
        
        public Color getColor()
        {
            return color;
        }
        
        public void paintComponent(Graphics g)
        {
            Graphics2D g2d = (Graphics2D)g.create();
            float fr[] = {.4f,0.6f};
            Color ca[] = {color, Color.black};
            g2d.setPaint(new RadialGradientPaint(new Point(getWidth()/2,getHeight()/2), 500f/gridSize, fr,ca , RadialGradientPaint.CycleMethod.REFLECT));
            g2d.fillRect(0,0, getWidth(), getHeight());
        }
    }
}







