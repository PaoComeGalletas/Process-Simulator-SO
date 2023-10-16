
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joela
 */
public class Proceso {
    
    JTable tabla;
    JLabel hora;
    Cuadrante2 c2;
    Cuadrante4 c4;
    int id, prio, suc, esperar, size;
    boolean activo, suceso, procesador, virtual;
    
    public Proceso(JTable tabla, JLabel hora, int id, Cuadrante2 c2, Cuadrante4 c4){
        this.tabla = tabla;
        this.hora = hora;
        this.id = id;
        this.c2 = c2;
        this.c4 = c4;
        prio = (int) tabla.getValueAt(id, 4);
        activo = false;
        suceso = false;
        procesador = false;
        virtual = false;
        size = (int) tabla.getValueAt(id, 16) / 32;
    }
    
    public int getID() {
        return 101 + id;
    }
    
    public void verificar() {
        if (activo) {
            if (suceso)
                switch (suc) {
                    case 1:
                        tabla.setValueAt((int) tabla.getValueAt(id, 8) - 1, id, 8);
                        break;
                    case 2:
                        tabla.setValueAt((int) tabla.getValueAt(id, 11) - 1, id, 11);
                        break;
                    case 3:
                        tabla.setValueAt((int) tabla.getValueAt(id, 13) - 1, id, 13);
                        break;
                    case 4:
                        tabla.setValueAt((int) tabla.getValueAt(id, 15) - 1, id, 15);
                        break;
                    default:
                }
            else if (!c2.listo.contains(this) && !procesador)
                c2.colas(0, id);
            if (!suceso)
                switch (suc) {
                    case 1:
                        if (hora.getText().equals((String) tabla.getValueAt(id, 7))){
                            c2.colas(1, id);
                            suceso = true;
                        }
                        break;
                    case 2:
                        if (hora.getText().equals((String) tabla.getValueAt(id, 10))){
                            c2.colas(1, id);
                            suceso = true;
                        }
                        break;
                    case 3:
                        if (hora.getText().equals((String) tabla.getValueAt(id, 12))){
                            c2.colas(2, id);
                            c4.exit(id);
                            virtual = true;
                            suceso = true;
                            System.out.println("Proceso " + (id + 1) + " entró a Virtual");
                        }
                        break;
                    case 4:
                        if (hora.getText().equals((String) tabla.getValueAt(id, 14))){
                            c2.colas(3, id);
                            c4.exit(id);
                            virtual = true;
                            suceso = true;
                            System.out.println("Proceso " + (id + 1) + " entró a Virtual");
                        }
                }
            else {
                switch (suc) {
                    case 1:
                        if ((int) tabla.getValueAt(id, 8) == 0){
                            c2.colas(0, id);
                            suceso = false;
                            suc++;
                        }
                        break;
                    case 2:
                        if ((int) tabla.getValueAt(id, 11) == 0){
                            c2.colas(0, id);
                            suceso = false;
                            suc++;
                        }
                        break;
                    case 3:
                        if ((int) tabla.getValueAt(id, 13) == 0){
                            c2.colas(0, id);
                            c4.enter(id);
                            suceso = false;
                            suc++;
                        }
                        break;
                    case 4:
                        if ((int) tabla.getValueAt(id, 15) == 0){
                            c2.colas(0, id);
                            c4.enter(id);
                            suceso = false;
                            suc++;
                        }
                }
            }
            if (procesador)
                if (esperar <= 0) {
                    c2.colas(0, id);
                    procesador = false;
                } 
                else {
                    tabla.setValueAt((int) tabla.getValueAt(id, 2) - 1, id, 2);
                    tabla.setValueAt((int) tabla.getValueAt(id, 3) - 1, id, 3);
                    esperar--;
                }
            if ((int) tabla.getValueAt(id, 2) == 0){
                tabla.setValueAt("Terminado", id, 5);
                c2.colas(4, id);
                c4.exit(id);
                activo = false;
            }
        }
        else if (hora.getText().equals((String) tabla.getValueAt(id, 1))) {
            activo = true;
            suc = 1;
            c2.colas(0, id);
            c4.enter(id);
            System.out.println("Proceso " + (id + 1) + " inició");
        }
    }
    
}
