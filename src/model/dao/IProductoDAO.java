package model.dao;

import model.dto.ProductoDTO;

/**
 * Interface Segregation Principle: Interfaz específica para operaciones de Producto
 */
public interface IProductoDAO extends CrudDAO<ProductoDTO> {
    
    /**
     * Busca un producto por código
     */
    ProductoDTO buscarPorCodigo(String codigo);
    
    /**
     * Actualiza el stock de un producto
     */
    boolean actualizarStock(int productoId, int nuevoStock);
}


