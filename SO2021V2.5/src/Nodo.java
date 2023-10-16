
public class Nodo {
    
    Proceso proceso;
    int size;
    
    public Nodo(Proceso proceso) {
        this.proceso = proceso;
        size = proceso.size;
    }
    
    public Nodo(int size) {
        this.size = size;
    }
    
    public String getID() {
        if (proceso == null)
            return "H";
        else
            return proceso.id + "";
    }
    
    public String showID() {
        if (proceso == null)
            return "H";
        else 
            return proceso.id != 9 ? "10" + (proceso.id + 1) : "110";
    }
    
}
