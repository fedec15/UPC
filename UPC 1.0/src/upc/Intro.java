/*********************************************
 *                                           *
 *  Sistema de gestión de alumnos - UPC 1.0  *
 *     Developed by Federico Castellano      *           
 *                                           *
 *     Software libre - @Copyleft 2018       *
 *                                           *
 *           fedec15@gmail.com               *
 *                                           *
 *********************************************/

/*********************************************
 *                                           *
 *          Intro Class: 'Intro'             *
 *                                           *
 *       It runs an intro to UPC 1.0         *
 *                                           *
 ********************************************/

package upc;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.net.*;


public class Intro extends JFrame implements ActionListener {
    
    //**** Main Values ****
    
    private JButton bIn, bOut;
    private JLabel label1, label2;
 
    //**** Frame's Contructor ****
    
    public Intro (){
        
        super("Sistema de gestión de alumnos - UPC");
        setSize(490,370);
        setBackground(SystemColor.control);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //*** Setting Window's Icon ***
        
        try {
            
            URL url = Intro.class.getResource("/UPC_ICON.jpg");
            BufferedImage img = ImageIO.read(url); 

            setIconImage(img);
            
        }
        catch (IOException e){
            
            
        }
 
        //*** Frame's Layout ***
              
        setLayout(new FlowLayout());
        
        
        //**** Main Intro's Components ****
    
            //***Panel 1***///
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER,0,23));
        panel1.setPreferredSize(new Dimension(500, 70));

        label1 = new JLabel("Bienvenido al sistema de gestión de alumnos");
        label1.setFont(new Font("Verdana",Font.PLAIN,20));
        label1.setHorizontalAlignment(JLabel.CENTER);   
        panel1.add(label1);
        
            //***Panel 2 (Logo)***///
            
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout()); 
        panel2.setPreferredSize(new Dimension(500, 178));
        
            //***Loading Image!***///
            
        try {

            URL url = Intro.class.getResource("/UPC_LOGO.jpg");
            BufferedImage img1 = ImageIO.read(url);   
           
            label2= new JLabel (new ImageIcon(img1));
            panel2.add(label2);

        } 
        
        catch (IOException e){
             
        }
              
            //***Panel 3***///
        
        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER,35,0));
        panel3.setPreferredSize(new Dimension(580, 65));
            
        bIn = new JButton ("Ingresar");
        //bIn.setBackground(SystemColor.control);
        bIn.setPreferredSize(new Dimension(84, 27));
        panel3.add(bIn);
        
        bOut = new JButton ("Salir");
        //bOut.setBackground(SystemColor.control);
        bOut.setPreferredSize(new Dimension(84, 27));
        panel3.add(bOut);
        
            //***Panel 3***///
            
        JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout());
              
                     
        //***Adding Panels to Intro's Frame***///
            
        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        
        //***Adding ActionListener to Buttons***///
            
        bIn.addActionListener(this);
        bOut.addActionListener(this);
        
        setVisible(true);

    }
    
    //** WindowListener's Method  
       
    public void actionPerformed (ActionEvent e){

        if (e.getSource()==bIn){
            
            dispose();
            new FrameUPC();
        }
        else if (e.getSource()==bOut){
            
            dispose();
            System.exit(0);
            
        }    
       
    }      
       
}
