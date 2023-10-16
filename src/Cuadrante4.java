
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JTable;


public class Cuadrante4 {
    
    JTable[] nodos;
    JTable ram, virtual;
    LinkedList<Nodo> lista;
    LinkedList<Proceso> wait;
    Proceso[] pro;
    Cuadrante2 c2;
    
    public Cuadrante4(JTable[] nodos, JTable ram, JTable virtual, Cuadrante2 c2){
        this.nodos = nodos;
        this.ram = ram;
        this.virtual = virtual;
        this.c2 = c2;
        lista = new LinkedList();
        lista.add(new Nodo(64));
        wait = new LinkedList();
        pro = c2.pro;
    }
    
    public boolean enter(int id) {
        int index = 0, size = 65;
        boolean found = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).proceso == null) {
                if (lista.get(i).size == pro[id].size) {
                    lista.get(index).proceso = pro[id];
                    pro[id].virtual = false;
                    return true;
                }
                if (lista.get(i).size < size && lista.get(i).size > pro[id].size) {
                    size = lista.get(i).size;
                    index = i;
                    found = true;
                }
            }
        }
        if (found) {
            System.out.println("\n\nAntes");
            for (Nodo nodo : lista)
                System.out.println(nodo.size);
            lista.add(index, new Nodo(pro[id]));
            lista.get(index + 1).size = size - pro[id].size;
            //System.out.println("Proceso " + (id + 1) + " entró a RAM");
            System.out.println("\nDespués");
            for (Nodo nodo : lista)
                System.out.println(nodo.size);
            pro[id].virtual = false;
            return true;
        }
        else
            return false;
    }
    
    public void exit(int id) {
        for (int i = 0; i < lista.size(); i++)
            if (lista.get(i).proceso == pro[id])
                lista.get(i).proceso = null;
        holes();
        //System.out.println("Proceso " + (id + 1) + " salió de RAM");
    }
    
    public void virtual(int id) {
        wait.add(pro[id]);
        c2.colas(3, id);
        pro[id].virtual = true;
        //System.out.println("Proceso " + (id + 1) + " entró a Virtual");
    }
    
    private void holes() {
        for (int i = 0; i < lista.size(); i++)
            if (i + 1 < lista.size() && lista.get(i).proceso == null && lista.get(i + 1).proceso == null) {
                lista.get(i).size += lista.get(i + 1).size;
                lista.remove(i + 1);
                i--;
            }
    }
    
    public void espera() {
        for (Iterator<Proceso> i = wait.iterator(); i.hasNext();) {
            Proceso next = i.next();
            if (enter(next.id)) {
                c2.colas(0, next.id);
                next.virtual = false;
                i.remove();
            }
        }
    }
    
    public void update() {
        int aux = 0;
        for (int i = 0; i < nodos.length; i++) {
            nodos[i].setValueAt("", 0, 0);
            nodos[i].setValueAt("", 0, 1);
            nodos[i].setValueAt("", 0, 2);
        }
        for (int i = 0; i < 10; i++)
            virtual.setValueAt("", i, 0);
        for (int i = 0; i < lista.size(); i++) {
            nodos[i].setValueAt(lista.get(i).showID(), 0, 0);
            nodos[i].setValueAt(aux, 0, 1);
            nodos[i].setValueAt(lista.get(i).size, 0, 2);
            aux += lista.get(i).size;
        }
        int x = 0, y = 0;
        for (Nodo nodo : lista)
            for (int i = nodo.size; i > 0; i--, x++) {
                if (x >= 8) {
                    x = 0;
                    y++;
                }
                ram.setValueAt(nodo.showID(), y, x);
            }
        for (int i = 0; i < pro.length; i++)
            if (pro[i].virtual)
                virtual.setValueAt(pro[i].getID() + "     " + pro[i].getID() + "     " + pro[i].getID(), i, 0);
    }
    
}
