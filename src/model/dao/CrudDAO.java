package model.dao;
import java.util.List;

public interface CrudDAO<T> {
    boolean insertar(T t);
    boolean actualizar(T t);
    boolean eliminar(int id);
    List<T> listar();
    T buscarPorId(int id);
}

