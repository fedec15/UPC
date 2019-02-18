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
 *    Calculates the students' condition     *
 *                                           *
 ********************************************/

package upc;


public class Condicion {
    
    private static final String REG = "Regular";
    private static final String PROM = "Promocional";
    private static final String LIBRE = "Libre";
    private String condicion;
       
    public void Condicion(float mayor1, float mayor2, boolean asistencia){
            
        if (mayor1>=7 && mayor2>=7 && asistencia==true){
            
            condicion= PROM;

        } 
        else if (mayor1 >= 4 && mayor1 <7 && mayor2 >=4 && asistencia == true || mayor2 >= 4 && mayor2 <7 && mayor1 >=4 && asistencia == true) {
            
            condicion=REG;

        } else {
            
            condicion=LIBRE;

        }
        
    }
    
    public String getCondicion(){
        
        return condicion;
        
    }
    
    
}
