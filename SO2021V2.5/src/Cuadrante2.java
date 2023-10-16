
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
public class Cuadrante2 extends JPanel implements Runnable {
    
    JTable tabla, tlisto, tbloq, tbs, tls;
    JLabel hora, pro1, pro2;
    boolean proc1, proc2, iniciar;
    LinkedList<Proceso> listo, bloq, bs, ls;
    Proceso[] pro;
    String spro1, spro2;
    Proceso p;
    int cont;
    Cuadrante4 c4;
    
    public Cuadrante2(JTable tabla, JTable tlisto, JTable tbloq, JTable tbs, JTable tls, JLabel hora, JLabel pro1, JLabel pro2) {
        this.tabla = tabla;
        this.tlisto = tlisto;
        this.tbloq = tbloq;
        this.tbs = tbs;
        this.tls = tls;
        this.hora = hora;
        this.pro1 = pro1;
        this.pro2 = pro2;
        proc1 = proc2 = true;
        pro = new Proceso[10];
        spro1 = spro2 = "000";
        listo = new LinkedList<>();
        bloq = new LinkedList<>();
        bs = new LinkedList<>();
        ls = new LinkedList<>();
    }
    
    public void colas(int cola, int id) {
        boolean b = true;
        //System.out.println("El proceso " + (id + 1) + " entró a la cola " + cola);
        listo.remove(pro[id]);
        bloq.remove(pro[id]);
        bs.remove(pro[id]);
        ls.remove(pro[id]);
        vaciarProcesador(id + 1);
        switch (cola){
            case 0:
                if (listo.isEmpty())
                    listo.add(pro[id]);
                else {
                    for (int i = 0; i < listo.size(); i++)
                        if (listo.get(i).prio > pro[id].prio){
                            listo.add(i, pro[id]);
                            b = false;
                            break;
                        }
                    if (b)
                        listo.add(pro[id]);
                }
                tabla.setValueAt("Listo", id, 5);
                break;
            case 1:
                bloq.add(pro[id]);
                tabla.setValueAt("Bloqueado", id, 5);
                break;
            case 2:
                bs.add(pro[id]);
                tabla.setValueAt("Bloq/Sus", id, 5);
                break;
            case 3:
                ls.add(pro[id]);
                tabla.setValueAt("Listo/Sus", id, 5);
        }
    }
    
    public void vaciarProcesador(int id) {
        if (spro1.equals(id == 10 ? "110" : "10" + id)){
            pro[id - 1].procesador = false;
            spro1 = "000";
            pro1.setText(spro1);
            tabla.setValueAt(5, id - 1, 3);
            proc1 = true;
        }
        if (spro2.equals(id == 10 ? "110" : "10" + id)) {
            pro[id - 1].procesador = false;
            spro2 = "000";
            pro2.setText(spro2);
            tabla.setValueAt(5, id - 1, 3);
            proc2 = true;
        }
    }
    
    public void actualizarColas() {
        for (int i = 0; i < 10; i++)
                    tlisto.setValueAt("", 0, i);
            if (!listo.isEmpty())
                for (int i = 0; i < listo.size(); i++)
                    tlisto.setValueAt(listo.get(i).id == 9 ? "110" : "10" + (listo.get(i).id + 1), 0, 9 - i);
            for (int i = 0; i < 10; i++)
                    tbloq.setValueAt("", 0, i);
            if (!bloq.isEmpty())
                for (int i = 0; i < bloq.size(); i++)
                    tbloq.setValueAt(bloq.get(i).id == 9 ? "110" : "10" + (bloq.get(i).id + 1), 0, i);
            for (int i = 0; i < 10; i++)
                    tbs.setValueAt("", 0, i);
            if (!bs.isEmpty())
                for (int i = 0; i < bs.size(); i++)
                    tbs.setValueAt(bs.get(i).id == 9 ? "110" : "10" + (bs.get(i).id + 1), 0, i);
            for (int i = 0; i < 10; i++)
                    tls.setValueAt("", 0, i);
            if (!ls.isEmpty())
                for (int i = 0; i < ls.size(); i++)
                    tls.setValueAt(ls.get(i).id == 9 ? "110" : "10" + (ls.get(i).id + 1), 0, i);
    }

    @Override
    public void run() {
        while (true) {
            if (iniciar){
                for (int i = 0; i < 10; i++)
                    pro[i] = new Proceso(tabla, hora, i, this, c4);
                iniciar = false;
            }
            for (int i = 0; i < 10; i++)
                pro[i].verificar();
            actualizarColas();
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cuadrante2.class.getName()).log(Level.SEVERE, null, ex);
            }
            c4.espera();
            c4.update();
            if (!listo.isEmpty() && proc1){
                p = listo.poll();
                spro1 = "10" + (p.id + 1);
                if (spro1.equals("1010"))
                    spro1 = "110";
                pro1.setText(spro1);
                p.esperar = 5;
                p.procesador = true;
                proc1 = false;
                tabla.setValueAt("Ejecutando", p.id, 5);
                //System.out.println("Proceso " + (p.id + 1) + " entró al Procesador 1 String " + spro1);
            }
            if (!listo.isEmpty() && proc2){
                p = listo.poll();
                spro2 = "10" + (p.id + 1);
                if (spro2.equals("1010"))
                    spro2 = "110";
                pro2.setText(spro2);
                p.esperar = 5;
                p.procesador = true;
                proc2 = false;
                tabla.setValueAt("Ejecutando", p.id, 5);
                //System.out.println("Proceso " + (p.id + 1) + " entró al Procesador 2 String " + spro2);
            }
            actualizarColas();
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cuadrante2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
