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
 *          Frame Class: 'FrameUPC'          *
 *                                           *
 *    Main frame where everything is done    *
 *                                           *
 ********************************************/

package upc;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.datatransfer.*;
import javax.swing.filechooser.*;


public class FrameUPC extends JFrame implements ActionListener, WindowListener {

    private JButton cargar,eliminar,refresh;
    private JLabel img, titulo, lnombre, lapellido;
    private JTextField tnombre, tapellido;

    private JPanel panel1, panel2, panel3;
        
    private final Object[] NOMBRE_TABLA = {"APELLIDO Y NOMBRE DEL ALUMNO", "1° PARCIAL", "2° PARCIAL", "RECUPERATORIO", "ASISTENCIA","CONDICIÓN","IEFI", "NOTA FINAL"};
    private Object[] datosTabla = new Object[8];
    
    private DefaultTableModel model;
    public JTable table;
    
    private JMenuBar menubar;
    private JMenu menu1, menu2, menu3; 
        
    private JMenuItem nuevo, abrir, guardar, guardar_como, cerrar, cortar, copiar, pegar, select, acerca_de;
    
    private static final String LINE_BREAK = "\n"; 
    private static final String CELL_BREAK = "\t"; 
    private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    
    private File remoto;
    private Boolean carga=false;
          
    //****** Frame's main method (constructor) ******
    
    public FrameUPC(){
        
        //Frame's main properties:
        
        super("Sistema de gestión de alumnos - UPC");
        setSize(1000,700);
        setBackground(SystemColor.control);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(this);
        
        //*** Setting Window's Icon ***
        
        try {
            
            URL url = Intro.class.getResource("/UPC_ICON.jpg");
            BufferedImage img = ImageIO.read(url); 

            setIconImage(img);
            
        }
        catch (IOException e){  
        }
 
        //*** Frame's Layout ***
              
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));

        
        //**** Main FrameUPC's Components ****   
        
            //*** Panel 1 ***//
        
        panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        panel1.setPreferredSize(new Dimension(1000, 150));

        
        JPanel panel11 = new JPanel(); 
              
        try {
    
           URL url = Intro.class.getResource("/UPC_LOGO.jpg");
           BufferedImage imag = ImageIO.read(url); 

           img = new JLabel(new ImageIcon(imag));
           img.setHorizontalAlignment(JLabel.LEFT);  
           panel11.add(img);
            
        }
        catch (IOException e){   
        }
        
        
        JPanel panel12 = new JPanel();
        panel12.setLayout(new FlowLayout(FlowLayout.RIGHT,163,50));
        
        titulo = new JLabel ("SISTEMA DE GESTIÓN DE ALUMNOS");
        titulo.setFont(new Font("Verdana",Font.PLAIN,23));
        titulo.setHorizontalAlignment(JLabel.RIGHT);   
        panel12.add(titulo);
        
        panel1.add(panel11);
        panel1.add(panel12);
        
            //*** Panel 2 ***//
          
        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1,3));
        
                //*** JPanel 2.1 ***///
                
        JPanel panel21 = new JPanel();
        panel21.setLayout(new FlowLayout(FlowLayout.LEFT,8,20));
                
        JLabel datos = new JLabel("Carga de datos:");
        datos.setFont(new Font("Verdana",Font.BOLD,12));
        datos.setHorizontalAlignment(JLabel.LEFT);  
        
        lnombre = new JLabel("Nombre:");
        tnombre = new JTextField(8);
        lapellido = new JLabel("Apellido:");
        tapellido = new JTextField(8);
     
        panel21.add(datos);
        panel21.add(lapellido);
        panel21.add(tapellido);
        panel21.add(lnombre);
        panel21.add(tnombre);
        

                //*** JPanel 2.2 ***///
        
        JPanel panel22 = new JPanel();
        panel22.setLayout(new FlowLayout(FlowLayout.LEFT,3,17));
        
        cargar = new JButton ("Cargar");
        cargar.addActionListener(this);
        
        this.getRootPane().setDefaultButton(cargar);
        cargar.requestFocus();
        panel22.add(cargar);
        
                //*** JPanel 2.3 ***///
        
        JPanel panel23 = new JPanel();
        panel23.setLayout(new FlowLayout(FlowLayout.LEFT,21,10));
  
        refresh = new JButton ("refresh");
        refresh.addActionListener(this);
        
        try {
    
           URL url = Intro.class.getResource("/refresh.png");
           BufferedImage imag = ImageIO.read(url); 
           
           refresh.setIcon(new ImageIcon(imag));
           refresh.setBackground(SystemColor.control);
           refresh.setPreferredSize(new Dimension(40, 40));

           panel23.add(refresh);
 
        }
        catch (IOException e){   
        }
        
        eliminar = new JButton ();
        eliminar.addActionListener(this);
        
        try {
    
           URL url = Intro.class.getResource("/delete.png");
           BufferedImage imag = ImageIO.read(url); 
           
           eliminar.setIcon(new ImageIcon(imag));
           eliminar.setBackground(SystemColor.control);
           eliminar.setPreferredSize(new Dimension(40, 40));
           
           panel23.add(eliminar);
            
        }
        catch (IOException e){   
        }

        panel2.add(panel21);
        panel2.add(panel22);
        panel2.add(panel23);
 
            //*** Panel 3 ***//
            
        panel3 = new JPanel ();
        panel3.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        
        
        model = new DefaultTableModel();         
        table = new JTable () {

            private static final long serialVersionUID = 1L;

            /*@Override
            public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
            }*/
            
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 4:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                
                
                if(column==1){
                    
                    return true;
                    
                } else if (column==2){
                        
                    return true;
                    
                } else if (column==3){
                        
                    return true;
    
                } else if (column==4){
                        
                    return true;
    
                } else if (column==6){
                    
                    if (getValueAt(row,5).equals("Promocional")){

                            return true; 
                            
                        } else {
                            
                            return false;
                        }
                    
                } else {
                    
                    return false;
                    
                }

            }
        };
                
        table.getTableHeader().setReorderingAllowed(false);
        table.setPreferredScrollableViewportSize(new Dimension(963, 417));
                 
        JScrollPane scrollpane = new JScrollPane(table);
        
        JTable rowTable = new RowNumberTable(table);
        scrollpane.setRowHeaderView(rowTable);
        scrollpane.setCorner(JScrollPane.UPPER_LEFT_CORNER,rowTable.getTableHeader());
        
        model.setColumnIdentifiers(NOMBRE_TABLA);
        table.setModel(model);
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);       
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );       
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(240);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(110);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(7).setPreferredWidth(80);

        panel3.add(scrollpane,BorderLayout.CENTER);

 
            //*** Adding Panels to Frame ***// 
            
        add(panel1);
        add(panel2);
        add(panel3);
        
        
            //*** Adding MENUBAR to Frame ***// 
        
        menubar = new JMenuBar();
        
        menu1 = new JMenu("Archivo");
        
        nuevo = new JMenuItem("Nuevo");
        nuevo.addActionListener(this);
        abrir = new JMenuItem("Abrir...");
        abrir.addActionListener(this);
        guardar = new JMenuItem("Guardar");
        guardar.addActionListener(this);
        guardar_como = new JMenuItem("Guardar como...");
        guardar_como.addActionListener(this);
        cerrar = new JMenuItem("Salir");
        cerrar.addActionListener(this);
        
        menu1.add(nuevo);
        menu1.add(abrir);
        menu1.add(guardar);
        menu1.add(guardar_como);
        menu1.addSeparator();
        menu1.add(cerrar);
        
        menubar.add(menu1);
        
        menu2 = new JMenu("Edición");
        
        cortar = new JMenuItem("Cortar");
        cortar.addActionListener(this);
        copiar = new JMenuItem("Copiar");
        copiar.addActionListener(this);
        pegar = new JMenuItem("Pegar");
        pegar.addActionListener(this);
        select = new JMenuItem("Seleccionar todo");
        select.addActionListener(this);
        
        menu2.add(cortar);
        menu2.add(copiar);
        menu2.add(pegar);
        menu2.addSeparator();
        menu2.add(select);
       
        menubar.add(menu2);
        
        menu3 = new JMenu("Ayuda");
        
        acerca_de = new JMenuItem("Acerca de");
        acerca_de.addActionListener(this);
        
        menu3.add(acerca_de);
        
        menubar.add(menu3);
        
        setJMenuBar(menubar);   
        
        ClipboardKeyAdapter aa = new ClipboardKeyAdapter(table);

        setVisible(true); 
        
    }
    
    //** Loading data from "*.dat" **//
            
    public void Cargar (){
        
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.dat","dat");
        fileChooser.setFileFilter(filter);
        
        int seleccion = fileChooser.showOpenDialog(table);
        
        if (seleccion == JFileChooser.APPROVE_OPTION){
            
            remoto = fileChooser.getSelectedFile();
            
            try {
                
                table.selectAll();
                int[] row = table.getSelectedRows();
                int del=1;
                for(int j=0; j<row.length; j++){

                    row[j] = table.convertRowIndexToModel(row[j]);
                    model.removeRow(row[j]);
                    
                    if(j<row.length-1){
                
                        row[j+1] = row[j+1]-del;
                        del = del+1;
                    }
                } 

                FileReader reader = new FileReader(remoto);
                BufferedReader buf = new BufferedReader(reader);

                int contador = Integer.parseInt(buf.readLine());

                for (int i = 0; i<contador;i++){

                    for (int j = 0; j<table.getColumnCount();j++){

                        String txt = buf.readLine();

                        if (txt.equals("true")){

                            datosTabla[j]=true;

                        } else if (txt.equals("false")){

                            datosTabla[j]=false;

                        } else {

                            datosTabla[j]=txt;

                        }     

                    }

                    model.addRow(datosTabla);
                }

            buf.close();
            carga = true;

            }
            catch(IOException ioe){

                System.out.print("No se pudieron cargar los datos");
            }
            
        }
 
        Refresh();    
    }
    
    //** Saving data from Jtable to already created "*.dat" **//
    
    public void Guardar(){
        
        ArrayList<String[]> datos = new ArrayList<String[]>();
               
        for (int i = 0; i<table.getRowCount(); i++){
                                       
            String[] col = new String[table.getColumnCount()];
                    
            for (int j = 0; j<table.getColumnCount(); j++){
                         
                if (table.getColumnClass(j)==Boolean.class){
                            
                    Boolean valor = ((Boolean)table.getValueAt(i,j)).booleanValue();
                            
                    if (valor){
                                
                        col[j]="true";
                                
                    } else if (!valor){
                                
                        col[j]="false";
                    }
                            
                } else {
                            
                    if (table.getValueAt(i,j)==null){
                                
                        table.setValueAt("", i, j);
                        col[j]=table.getValueAt(i, j).toString();
            
                    } else if (table.getValueAt(i,j)!=null){
                                
                        col[j]=table.getValueAt(i, j).toString();
                                
                    }
             
                }
     
            }
                    
            datos.add(col);
        }

        if (remoto != null){
            try {
                
                if (carga){
                    
                    FileWriter writer = new FileWriter(remoto);
                    BufferedWriter buf = new BufferedWriter(writer);

                    PrintWriter w = new PrintWriter(remoto);
                    w.print("");
                    w.close();

                    buf.write(String.valueOf(table.getRowCount()));
                    buf.newLine();

                    for (int i = 0; i<datos.size(); i++){

                        String[] retorno = datos.get(i);

                        for (int j = 0; j<retorno.length;j++){

                            buf.write(retorno[j]);
                            buf.newLine();

                        }

                    }

                    buf.close();
                    
                } else {
                    
                    FileWriter writer = new FileWriter(remoto+".dat");
                    BufferedWriter buf = new BufferedWriter(writer);

                    PrintWriter w = new PrintWriter(remoto+".dat");
                    w.print("");
                    w.close();

                    buf.write(String.valueOf(table.getRowCount()));
                    buf.newLine();

                    for (int i = 0; i<datos.size(); i++){

                        String[] retorno = datos.get(i);

                        for (int j = 0; j<retorno.length;j++){

                            buf.write(retorno[j]);
                            buf.newLine();

                        }

                    }

                    buf.close();
                }
            }
            
            catch(IOException ioe){

                    System.out.print("Error al guardar los datos");
            }
        }    
    }
    
    //** Saving as data from Jtable to "*.dat" **//
    
    public void GuardarComo(){
        
        ArrayList<String[]> datos = new ArrayList<String[]>();
               
        for (int i = 0; i<table.getRowCount(); i++){
                                       
            String[] col = new String[table.getColumnCount()];
                    
            for (int j = 0; j<table.getColumnCount(); j++){
                         
                if (table.getColumnClass(j)==Boolean.class){
                            
                    Boolean valor = ((Boolean)table.getValueAt(i,j)).booleanValue();
                            
                    if (valor){
                                
                        col[j]="true";
                                
                    } else if (!valor){
                                
                        col[j]="false";
                    }
                            
                } else {
                            
                    if (table.getValueAt(i,j)==null){
                                
                        table.setValueAt("", i, j);
                        col[j]=table.getValueAt(i, j).toString();
            
                    } else if (table.getValueAt(i,j)!=null){
                                
                        col[j]=table.getValueAt(i, j).toString();
                                
                    }
             
                }
     
            }
                    
            datos.add(col);
        }
        
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showSaveDialog(table);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            
            remoto = fileChooser.getSelectedFile();
            
            try {
            
                FileWriter writer = new FileWriter(remoto+".dat");
                BufferedWriter buf = new BufferedWriter(writer);

                PrintWriter w = new PrintWriter(remoto+".dat");
                w.print("");
                w.close();

                buf.write(String.valueOf(table.getRowCount()));
                buf.newLine();

                for (int i = 0; i<datos.size(); i++){

                    String[] retorno = datos.get(i);

                    for (int j = 0; j<retorno.length;j++){

                        buf.write(retorno[j]);
                        buf.newLine();

                    }

                }

            buf.close();
                    
            }
            catch(IOException ioe){

                System.out.print("Error al guardar los datos");
            }
        }          
    }
    
    //** Refresh method - Condicion method **//
            
    public void Refresh (){ 
        
        Condicion c = new Condicion();
        int primerParcial, segundoParcial, recuperatorio;    
        Boolean asistencia;
        
        for (int i = 0; i<table.getRowCount();i++){

            if (table.getValueAt(i, 1)==null || table.getValueAt(i, 1).toString().equals("")){
                
                primerParcial = 0;
                
            } else {
                
                primerParcial = Integer.parseInt(table.getValueAt(i, 1).toString() );
            }
            
            if (table.getValueAt(i, 2)==null || table.getValueAt(i, 2).toString().equals("")){
                
                segundoParcial = 0;
                
            } else {
                
                segundoParcial = Integer.parseInt(table.getValueAt(i, 2).toString());
            }
            
            if (table.getValueAt(i, 3)==null || table.getValueAt(i, 3).toString().equals("")){
                
                recuperatorio = 0;
                
            } else {
                
                recuperatorio = Integer.parseInt(table.getValueAt(i, 3).toString()); 
            }
  
            asistencia = ((Boolean)table.getValueAt(i,4)).booleanValue();
            
            int[] mayores = new int[3];
            mayores[0] = primerParcial;
            mayores[1] = segundoParcial;
            mayores[2] = recuperatorio;
                
            Arrays.sort(mayores);
          
            c.Condicion(mayores[1], mayores[2], asistencia);
            table.setValueAt(c.getCondicion(), i, 5); 
                     
            if (c.getCondicion().equals("Promocional")){
                
                if (table.getValueAt(i, 6)==null || table.getValueAt(i, 6).toString().equals("") || table.getValueAt(i, 6).toString().equals("-")){
                
                    table.setValueAt(null, i, 6);
                    table.setValueAt("Coloquio", i, 7);
                
                } else {

                    float primerProm = (float) (mayores[1]+mayores[2])/2;                                    
                    int iefi = Integer.parseInt(table.getValueAt(i, 6).toString());               
                    int promedio = Math.round((primerProm+iefi)/2);

                    if (iefi<7){
                        
                       table.setValueAt("Coloquio", i, 7); 
                       
                    } else {
                        
                        table.setValueAt(String.valueOf(promedio), i, 7);
                    }
                }
                
            } else if (c.getCondicion().equals("Regular")){
                
                table.setValueAt("Examen final", i, 7);
                
            } else {
      
                table.setValueAt("-", i, 6); 
                table.setValueAt("-", i, 7); 
            } 
        }      
    }
   
    //** Cut-Copy-Paste methods **//
    
    private void copyToClipboard(boolean isCut) { 
        
                int numCols=table.getSelectedColumnCount(); 
                int numRows=table.getSelectedRowCount(); 
                int[] rowsSelected=table.getSelectedRows(); 
                int[] colsSelected=table.getSelectedColumns(); 
                if (numRows!=rowsSelected[rowsSelected.length-1]-rowsSelected[0]+1 || numRows!=rowsSelected.length || 
                                numCols!=colsSelected[colsSelected.length-1]-colsSelected[0]+1 || numCols!=colsSelected.length) {

                        JOptionPane.showMessageDialog(null, "Invalid Copy Selection", "Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
                        return; 
                } 
                
                StringBuffer excelStr=new StringBuffer(); 
                for (int i=0; i<numRows; i++) { 
                        for (int j=0; j<numCols; j++) { 
                                excelStr.append(escape(table.getValueAt(rowsSelected[i], colsSelected[j]))); 
                                if (isCut) { 
                                        table.setValueAt(null, rowsSelected[i], colsSelected[j]); 
                                } 
                                if (j<numCols-1) { 
                                        excelStr.append(CELL_BREAK); 
                                } 
                        } 
                        excelStr.append(LINE_BREAK); 
                } 
                
                StringSelection sel  = new StringSelection(excelStr.toString()); 
                CLIPBOARD.setContents(sel, sel); 
    } 
    
    private void cancelEditing() { 
                if (table.getCellEditor() != null) { 
                        table.getCellEditor().cancelCellEditing(); 
            } 
    } 
        
    private String escape(Object cell) { 
                return cell.toString().replace(LINE_BREAK, " ").replace(CELL_BREAK, " "); 
    } 
    
    private void pasteFromClipboard() { 
                int startRow=table.getSelectedRows()[0]; 
                int startCol=table.getSelectedColumns()[0];

                String pasteString = ""; 
                try { 
                        pasteString = (String)(CLIPBOARD.getContents(this).getTransferData(DataFlavor.stringFlavor)); 
                } catch (Exception e) { 
                        JOptionPane.showMessageDialog(null, "Invalid Paste Type", "Invalid Paste Type", JOptionPane.ERROR_MESSAGE);
                        return; 
                } 
                
                String[] lines = pasteString.split(LINE_BREAK); 
                for (int i=0 ; i<lines.length; i++) { 
                        String[] cells = lines[i].split(CELL_BREAK); 
                        for (int j=0 ; j<cells.length; j++) { 
                                if (table.getRowCount()>startRow+i && table.getColumnCount()>startCol+j) { 
                                     
                                    if (cells[j].equals("true")){

                                        table.setValueAt(true, startRow+i, startCol+j); 
                                        
                                    } else if (cells[j].equals("false")){
                                        
                                        table.setValueAt(false, startRow+i, startCol+j); 
                                        
                                    } else {
                                    
                                        table.setValueAt(cells[j], startRow+i, startCol+j); 
                                    
                                    }
                                } 
                        } 
                } 
    } 
    
    public class ClipboardKeyAdapter extends KeyAdapter {

        private final String LINE_BREAK = "\n"; 
        private final String CELL_BREAK = "\t"; 
        private final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard(); 
        
        private final JTable table; 
        
        public ClipboardKeyAdapter(JTable table) { 
                this.table = table; 
        } 
        
        @Override 
        public void keyReleased(KeyEvent event) { 
                if (event.isControlDown()) { 
                        if (event.getKeyCode()==KeyEvent.VK_C) { // Copy                        
                                cancelEditing(); 
                                copyToClipboard(false); 
                        } else if (event.getKeyCode()==KeyEvent.VK_X) { // Cut 
                                cancelEditing(); 
                                copyToClipboard(true); 
                        } else if (event.getKeyCode()==KeyEvent.VK_V) { // Paste 
                                cancelEditing(); 
                                pasteFromClipboard();           
                        } 
                } 
        } 
        
        private void copyToClipboard(boolean isCut) { 
                int numCols=table.getSelectedColumnCount(); 
                int numRows=table.getSelectedRowCount(); 
                int[] rowsSelected=table.getSelectedRows(); 
                int[] colsSelected=table.getSelectedColumns(); 
                if (numRows!=rowsSelected[rowsSelected.length-1]-rowsSelected[0]+1 || numRows!=rowsSelected.length || 
                                numCols!=colsSelected[colsSelected.length-1]-colsSelected[0]+1 || numCols!=colsSelected.length) {

                        JOptionPane.showMessageDialog(null, "Invalid Copy Selection", "Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
                        return; 
                } 
                
                StringBuffer excelStr=new StringBuffer(); 
                for (int i=0; i<numRows; i++) { 
                        for (int j=0; j<numCols; j++) { 
                                excelStr.append(escape(table.getValueAt(rowsSelected[i], colsSelected[j]))); 
                                if (isCut) { 
                                        table.setValueAt(null, rowsSelected[i], colsSelected[j]); 
                                } 
                                if (j<numCols-1) { 
                                        excelStr.append(CELL_BREAK); 
                                } 
                        } 
                        excelStr.append(LINE_BREAK); 
                } 
                
                StringSelection sel  = new StringSelection(excelStr.toString()); 
                CLIPBOARD.setContents(sel, sel); 
        } 
        
        private void pasteFromClipboard() { 
                int startRow=table.getSelectedRows()[0]; 
                int startCol=table.getSelectedColumns()[0];

                String pasteString = ""; 
                try { 
                        pasteString = (String)(CLIPBOARD.getContents(this).getTransferData(DataFlavor.stringFlavor)); 
                } catch (Exception e) { 
                        JOptionPane.showMessageDialog(null, "Invalid Paste Type", "Invalid Paste Type", JOptionPane.ERROR_MESSAGE);
                        return; 
                } 
                
                String[] lines = pasteString.split(LINE_BREAK); 
                for (int i=0 ; i<lines.length; i++) { 
                        String[] cells = lines[i].split(CELL_BREAK); 
                        for (int j=0 ; j<cells.length; j++) { 
                                if (cells[j].equals("true")){

                                        table.setValueAt(true, startRow+i, startCol+j); 
                                        
                                } else if (cells[j].equals("false")){
                                        
                                        table.setValueAt(false, startRow+i, startCol+j); 
                                        
                                } else {
                                    
                                        table.setValueAt(cells[j], startRow+i, startCol+j); 
                                    
                                } 
                        } 
                } 
        } 
        
        private void cancelEditing() { 
                if (table.getCellEditor() != null) { 
                        table.getCellEditor().cancelCellEditing(); 
            } 
        } 
        
        private String escape(Object cell) { 
                return cell.toString().replace(LINE_BREAK, " ").replace(CELL_BREAK, " "); 
        } 
    } 
    
    //** Buttons &MenuItems' Actions! **//
 
    public void actionPerformed (ActionEvent e){

        if (e.getSource()==cargar){
            
            datosTabla[0]="";
            datosTabla[1]="";
            datosTabla[2]="";
            datosTabla[3]="";
            datosTabla[4]=false;
           
            datosTabla[0]=(tapellido.getText()).toUpperCase();
            
            
            if (tnombre.getText().equals("")&& tapellido.getText().equals("")){
                
                datosTabla[0]=((tapellido.getText()).toUpperCase())+"*FALTA APELLIDO Y NOMBRE*";
                
            } else if (tapellido.getText().equals("")){
                
                datosTabla[0]=("*SIN APELLIDO*, "+(tnombre.getText()).toUpperCase());
            
            } else if (tnombre.getText().equals("")){
                
                datosTabla[0]=((tapellido.getText()).toUpperCase()+", *SIN NOMBRE*");
            
            } else {
                
                datosTabla[0]=((tapellido.getText()).toUpperCase())+", "+((tnombre.getText()).toUpperCase());
            }         
    
            tapellido.setText("");
            tnombre.setText("");
            
            tapellido.requestFocus();
            
            model.addRow(datosTabla);
            Refresh();

        } else if (e.getSource()==eliminar){
               
            int selected = table.getSelectedRow();
            
            if (selected>=0){
                
                JDialog d = new JDialog(this, "Sistema de gestión de alumnos - UPC", true);
                d.setSize(320,150); 
                d.setBackground(SystemColor.control);
                d.setLayout(new GridLayout(0,1));
                d.setLocationRelativeTo(null);
                d.setDefaultCloseOperation(d.DO_NOTHING_ON_CLOSE);
                d.setResizable(false);

                try {

                    URL url = Intro.class.getResource("/UPC_ICON.jpg");
                    BufferedImage img = ImageIO.read(url); 

                    d.setIconImage(img);

                }
                catch (IOException ioe){

                }

                JPanel p1 = new JPanel();
                p1.setLayout(new FlowLayout(FlowLayout.CENTER,0,25));

                JLabel label = new JLabel("¿Está seguro que desea eliminar estos datos?");
                label.setFont(new Font("Verdana",Font.BOLD,12));

                JPanel p2 = new JPanel();
                p2.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));

                JButton b = new JButton ("Eliminar");
                b.setPreferredSize(new Dimension(90, 27));
                //b.setBackground(SystemColor.control);

                JButton c = new JButton ("Cancelar");
                c.setPreferredSize(new Dimension(90, 27));
                //f.setBackground(SystemColor.control);

                b.addActionListener (new ActionListener()  {  
                    public void actionPerformed( ActionEvent e ) {  

                        int[] row = table.getSelectedRows();

                        int del=1;

                        for(int j=0; j<row.length; j++){

                            row[j] = table.convertRowIndexToModel(row[j]);
                            model.removeRow(row[j]);
                            if(j<row.length-1){
                                row[j+1] = row[j+1]-del;
                                del = del+1;
                            }
                        } 

                        d.dispose();
                        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }  
                });  


                c.addActionListener (new ActionListener()  {  
                    public void actionPerformed( ActionEvent e ) {  

                        d.dispose();
                        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }  
                });  

                p1.add(label);  

                p2.add(b); 
                p2.add(c);

                d.add(p1);
                d.add(p2);

                d.setVisible(true);  
                
            }
    
        } else if (e.getSource()==refresh){ 
            
            Refresh();
            
        } else if (e.getSource()==nuevo){ 
            
            JDialog d = new JDialog(this, "Sistema de gestión de alumnos - UPC", true);
            d.setSize(280,150); 
            d.setBackground(SystemColor.control);
            d.setLayout(new GridLayout(0,1));
            d.setLocationRelativeTo(null);
            d.setDefaultCloseOperation(d.DO_NOTHING_ON_CLOSE);
            d.setResizable(false);

            try {

                URL url = Intro.class.getResource("/UPC_ICON.jpg");
                BufferedImage img = ImageIO.read(url); 

                d.setIconImage(img);

            }
            catch (IOException ioe){

            }

            JPanel p1 = new JPanel();
            p1.setLayout(new FlowLayout(FlowLayout.CENTER,0,25));

            JLabel label = new JLabel("¿Desea crear una nueva plantilla?");
            label.setFont(new Font("Verdana",Font.BOLD,12));

            JPanel p2 = new JPanel();
            p2.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));

            JButton b = new JButton ("Aceptar");
            b.setPreferredSize(new Dimension(85, 27));
            //b.setBackground(SystemColor.control);

            JButton c = new JButton ("Cancelar");
            c.setPreferredSize(new Dimension(85, 27));
            //f.setBackground(SystemColor.control);

            b.addActionListener (new ActionListener()  {  
                public void actionPerformed( ActionEvent e ) {  

                    table.selectAll();
            
                    int[] row = table.getSelectedRows();

                    int del=1;

                    for(int j=0; j<row.length; j++){

                        row[j] = table.convertRowIndexToModel(row[j]);
                        model.removeRow(row[j]);
                        if(j<row.length-1){
                            row[j+1] = row[j+1]-del;
                            del = del+1;
                        }
                    } 
                    
                    d.dispose();
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }  
            });  


            c.addActionListener ( new ActionListener()  {  
                public void actionPerformed( ActionEvent e ) {  

                    d.dispose();
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }  
            });  

            p1.add(label);  

            p2.add(b); 
            p2.add(c);
           
            d.add(p1);
            d.add(p2);

            d.setVisible(true);  
            
        } else if (e.getSource()==abrir){ 
            
            Cargar();
            
        } else if (e.getSource()==guardar){ 
            
            Guardar();
            
        } else if (e.getSource()==guardar_como){ 
            
            GuardarComo();
            
        } else if (e.getSource()==cerrar){ 
            
            Close();
            
        } else if (e.getSource()==cortar){ 
            
            cancelEditing(); 
            copyToClipboard(true); 
            
        } else if (e.getSource()==copiar){ 
            
            cancelEditing(); 
            copyToClipboard(false); 
            
        } else if (e.getSource()==pegar){ 
            
            cancelEditing(); 
            pasteFromClipboard();  
              
        } else if (e.getSource()==select){ 
            
            table.selectAll();
            
        } else if (e.getSource()==acerca_de){ 
            
            JDialog d = new JDialog(this, "Sistema de gestión de alumnos - UPC", true);
            d.setSize(450,330); 
            d.setBackground(SystemColor.control);
            d.setLayout(new BorderLayout());
            d.setLocationRelativeTo(null);
            d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            d.setResizable(false);

            try {

                URL url = Intro.class.getResource("/UPC_ICON.jpg");
                BufferedImage img = ImageIO.read(url); 

                d.setIconImage(img);

            }
            catch (IOException ioe){

            }
                //*** PANEL 1 ***
                
            JPanel p1 = new JPanel();
            p1.setPreferredSize(new Dimension(500, 80));
            p1.setLayout(new FlowLayout(FlowLayout.CENTER,15,20));
            
            try {

                URL url = Intro.class.getResource("/UPC_LOGO.jpg");
                BufferedImage img1 = ImageIO.read(url);   
                ImageIcon icon = new ImageIcon(img1);
                Image scaleImage = icon.getImage().getScaledInstance(98, 55,Image.SCALE_DEFAULT);
                ImageIcon icon1 = new ImageIcon(scaleImage);
                
                JLabel label1= new JLabel (icon1);
                
                p1.add(label1);
            } 
       
            catch (IOException ee){

            }
            
            JLabel label2 = new JLabel("SISTEMA DE GESTIÓN DE ALUMNOS");
            label2.setFont(new Font("Verdana",Font.PLAIN,15));
            
            p1.add(label2);
            
                //*** PANEL 2 ***
                
            JPanel p2 = new JPanel();
            p2.setLayout(new BorderLayout());
            
            JLabel label3 = new JLabel("_______________________________________________________", JLabel.CENTER);
                       
            p2.add(label3,BorderLayout.NORTH);
 
            JLabel label4 = new JLabel("<html><br/> &nbsp &nbsp &nbsp Sistema de gestión de alumnos - UPC <br/> <br/>"
                                         + " &nbsp &nbsp &nbsp Versión 1.0 <br/> <br/>"
                                         + " &nbsp &nbsp &nbsp Desarrollado por Federico Castellano <br/> <br/>"
                                         + " &nbsp &nbsp &nbsp fedec15@gmail.com <br/> <br/>"
                                         + " &nbsp &nbsp &nbsp Software libre - @Copyleft 2018 <br/> <br/> </html>");
            
            label4.setFont(new Font("Verdana",Font.PLAIN,13));
            p2.add(label4, BorderLayout.CENTER);
            
            d.add(p1,BorderLayout.NORTH);
            d.add(p2,BorderLayout.CENTER);

            d.setVisible(true);  
            
        }              
    } 
    
    //** Closing's program method **//   
    
    public void Close (){
        
        JDialog d = new JDialog(this, "Sistema de gestión de alumnos - UPC", true);
        d.setSize(310,150); 
        d.setBackground(SystemColor.control);
        d.setLayout(new GridLayout(0,1));
        d.setLocationRelativeTo(null);
        d.setDefaultCloseOperation(d.DO_NOTHING_ON_CLOSE);
        d.setResizable(false);
        
        try {
            
            URL url = Intro.class.getResource("/UPC_ICON.jpg");
            BufferedImage img = ImageIO.read(url); 

            d.setIconImage(img);
            
        }
        catch (IOException ioe){
        
        }
        
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.CENTER,0,25));
        
        JLabel label = new JLabel("¿Desea guardar los cambios realizados?");
        label.setFont(new Font("Verdana",Font.BOLD,12));
        
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout(FlowLayout.CENTER,10,5));
                        
        JButton b = new JButton ("Guardar");
        b.setPreferredSize(new Dimension(85, 27));
        //b.setBackground(SystemColor.control);
        
        JButton c = new JButton ("No guardar");
        c.setPreferredSize(new Dimension(98, 27));
        //c.setBackground(SystemColor.control);
        
        JButton f = new JButton ("Cancelar");
        f.setPreferredSize(new Dimension(85, 27));
        //f.setBackground(SystemColor.control);
        
        
        b.addActionListener (new ActionListener()  {  
            public void actionPerformed( ActionEvent e ) {  
                
                if (remoto==null){
                    
                    GuardarComo();
                    
                } else {
                    
                    Guardar();
                }
                
                dispose();
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }  
        });  
        
        
        c.addActionListener ( new ActionListener()  {  
            public void actionPerformed( ActionEvent e ) {  
                
                dispose();
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }  
        });  
        
        f.addActionListener ( new ActionListener()  {  
            public void actionPerformed( ActionEvent e ) {  
                
                d.dispose();
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
     
            }  
        });
        
        p1.add(label);  
        
        p2.add(b); 
        p2.add(c);
        p2.add(f);
        
        d.add(p1);
        d.add(p2);
               
        d.setVisible(true);  
        
    }
    
    //** WindowListener methods **// 
    
    public void windowClosing(WindowEvent e){
        
        Close(); // calling to the "close method", a method that triggers the "Guardar, No Guardar, Cancelar" before closing program!
                
    }
    
    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){} 
    public void windowOpened(WindowEvent e){}
    public void windowClosed(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}    

     
}
