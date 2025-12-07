package model.memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Patrón Memento: Cuidador que gestiona los mementos (historial)
 */
public class CuidadorCarrito {
    
    private Stack<MementoCarrito> historial;
    private static final int MAX_HISTORIAL = 10; // Limitar tamaño del historial
    
    public CuidadorCarrito() {
        this.historial = new Stack<>();
    }
    
    /**
     * Guarda un memento en el historial
     */
    public void guardarMemento(MementoCarrito memento) {
        if (memento == null) {
            return;
        }
        
        historial.push(memento);
        
        // Limitar tamaño del historial
        if (historial.size() > MAX_HISTORIAL) {
            // Eliminar el más antiguo (FIFO)
            Stack<MementoCarrito> temp = new Stack<>();
            while (historial.size() > MAX_HISTORIAL - 1) {
                temp.push(historial.pop());
            }
            historial.clear();
            while (!temp.isEmpty()) {
                historial.push(temp.pop());
            }
        }
    }
    
    /**
     * Restaura el último memento guardado (deshacer)
     */
    public MementoCarrito deshacer() {
        if (historial.isEmpty()) {
            return null;
        }
        return historial.pop();
    }
    
    /**
     * Obtiene el último memento sin eliminarlo
     */
    public MementoCarrito obtenerUltimo() {
        if (historial.isEmpty()) {
            return null;
        }
        return historial.peek();
    }
    
    /**
     * Obtiene todos los mementos guardados
     */
    public List<MementoCarrito> obtenerHistorial() {
        return new ArrayList<>(historial);
    }
    
    /**
     * Limpia todo el historial
     */
    public void limpiarHistorial() {
        historial.clear();
    }
    
    /**
     * Verifica si hay mementos guardados
     */
    public boolean tieneHistorial() {
        return !historial.isEmpty();
    }
    
    /**
     * Obtiene el tamaño del historial
     */
    public int tamañoHistorial() {
        return historial.size();
    }
}


