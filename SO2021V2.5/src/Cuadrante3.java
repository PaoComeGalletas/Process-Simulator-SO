
import java.awt.Color;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTable;


public class Cuadrante3 extends JPanel {
    
    JTable tblDatos, tblMemory;
    float[] tcpu, dif;
    boolean seguir;
    int proc;
    
    public Cuadrante3() {
        
    }
    
    public Cuadrante3(JTable tblDatos, JTable tblMemory) {
        this.tblDatos = tblDatos;
        this.tblMemory = tblMemory;
        proc = 1;
    }
    
    public void inicio() {
        try {
            tcpu = new float[10];
            for (int i = 0; i < 10; i++)
                tcpu[i] = (int) tblDatos.getValueAt(i, 2) / 60;
            dif = new float[19];
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            for (int i = 0; i < 9; i++) {
                start.setTime(format.parse((String) tblDatos.getValueAt(i, 1)));
                end.setTime(format.parse((String) tblDatos.getValueAt(i + 1, 1)));
                dif[i] = (end.getTimeInMillis() - start.getTimeInMillis()) / 1000;
                //System.out.println("Diferencia intervalo " + (i + 1) + ": " + dif[i]);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Cuadrante3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void grafica() {
        float aux;
        for (int i = 1; i < 10; i++) {
            System.out.println("\nINTERVALO " + i + "\nTiempo:" + dif[i - 1]);
            for (int j = 0; j < proc; j++) {
                aux = dif[i - 1] * Float.parseFloat(tblMemory.getValueAt(1, i).toString()) / proc / 60;
                tcpu[j] -= aux;
                
                System.out.println("Tiempo proceso " + (j + 1) + ": " + aux);
            }
            System.out.println("");
            for (int j = 0; j < proc; j++) {
                System.out.println("Tiempo restante proceso " + (j + 1) + ": " + tcpu[j]);
            }
            proc++;
        }
        for (int i = 10; i < 20; i++) {
            aux = menor();
            dif[i - 1] = aux / Float.parseFloat(tblMemory.getValueAt(2, proc).toString());
            System.out.println("\nINTERVALO " + i + "\nTiempo:" + dif[i - 1]);
            for (int j = 0; j < 10; j++) {
                if (tcpu[j] > 0)
                    tcpu[j] -= aux;
                System.out.println("Tiempo proceso " + (j + 1) + ": " + aux);
            }
            System.out.println("");
            for (int j = 0; j < 10; j++) {
                System.out.println("Tiempo restante proceso " + (j + 1) + ": " + tcpu[j]);
            }
            proc--;
        }
        repaint();
    }
   
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(50, 200, 1500, 500);
        g.setColor(Color.blue);
        g.drawOval(100, 250, 100, 100);
    }
    
    private float menor() {
        float aux = tcpu[0];
        for (int i = 1; i < 10; i++)
            if (tcpu[i] != 0 && tcpu[i] < aux || aux == 0)
                aux = tcpu[i];
        return aux;
    }
    
}
