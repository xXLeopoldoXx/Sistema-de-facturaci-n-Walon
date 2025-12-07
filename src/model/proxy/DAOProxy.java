package model.proxy;

import model.dao.CrudDAO;
import model.dto.ClienteDTO;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Patrón Proxy: Controla el acceso a los DAOs reales agregando funcionalidad
 * adicional como logging, validación, cache, etc.
 */
public class DAOProxy implements CrudDAO<ClienteDTO> {
    
    private static final Logger logger = Logger.getLogger(DAOProxy.class.getName());
    private final CrudDAO<ClienteDTO> daoReal;
    
    public DAOProxy(CrudDAO<ClienteDTO> daoReal) {
        this.daoReal = daoReal;
    }
    
    @Override
    public boolean insertar(ClienteDTO t) {
        logger.log(Level.INFO, "Proxy: Intentando insertar cliente - DNI: {0}", t.getDni());
        long inicio = System.currentTimeMillis();
        
        boolean resultado = daoReal.insertar(t);
        
        long tiempo = System.currentTimeMillis() - inicio;
        if (resultado) {
            logger.log(Level.INFO, "Proxy: Cliente insertado exitosamente en {0}ms", tiempo);
        } else {
            logger.log(Level.WARNING, "Proxy: Error al insertar cliente");
        }
        
        return resultado;
    }
    
    @Override
    public boolean actualizar(ClienteDTO t) {
        logger.log(Level.INFO, "Proxy: Intentando actualizar cliente ID: {0}", t.getId());
        long inicio = System.currentTimeMillis();
        
        boolean resultado = daoReal.actualizar(t);
        
        long tiempo = System.currentTimeMillis() - inicio;
        if (resultado) {
            logger.log(Level.INFO, "Proxy: Cliente actualizado exitosamente en {0}ms", tiempo);
        } else {
            logger.log(Level.WARNING, "Proxy: Error al actualizar cliente");
        }
        
        return resultado;
    }
    
    @Override
    public boolean eliminar(int id) {
        logger.log(Level.INFO, "Proxy: Intentando eliminar cliente ID: {0}", id);
        long inicio = System.currentTimeMillis();
        
        boolean resultado = daoReal.eliminar(id);
        
        long tiempo = System.currentTimeMillis() - inicio;
        if (resultado) {
            logger.log(Level.INFO, "Proxy: Cliente eliminado exitosamente en {0}ms", tiempo);
        } else {
            logger.log(Level.WARNING, "Proxy: Error al eliminar cliente");
        }
        
        return resultado;
    }
    
    @Override
    public List<ClienteDTO> listar() {
        logger.log(Level.INFO, "Proxy: Obteniendo lista de clientes");
        long inicio = System.currentTimeMillis();
        
        List<ClienteDTO> resultado = daoReal.listar();
        
        long tiempo = System.currentTimeMillis() - inicio;
        logger.log(Level.INFO, "Proxy: Lista obtenida con {0} elementos en {1}ms", 
                   new Object[]{resultado.size(), tiempo});
        
        return resultado;
    }
    
    @Override
    public ClienteDTO buscarPorId(int id) {
        logger.log(Level.INFO, "Proxy: Buscando cliente por ID: {0}", id);
        long inicio = System.currentTimeMillis();
        
        ClienteDTO resultado = daoReal.buscarPorId(id);
        
        long tiempo = System.currentTimeMillis() - inicio;
        if (resultado != null) {
            logger.log(Level.INFO, "Proxy: Cliente encontrado en {0}ms", tiempo);
        } else {
            logger.log(Level.INFO, "Proxy: Cliente no encontrado en {0}ms", tiempo);
        }
        
        return resultado;
    }
}


