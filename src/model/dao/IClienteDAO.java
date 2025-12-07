package model.dao;

import model.dto.ClienteDTO;

/**
 * Interface Segregation Principle: Interfaz específica para operaciones de Cliente
 * Separa las operaciones de Cliente de las operaciones genéricas CRUD
 */
public interface IClienteDAO extends CrudDAO<ClienteDTO> {
    
    /**
     * Busca un cliente por DNI
     */
    ClienteDTO buscarPorDni(String dni);
}


