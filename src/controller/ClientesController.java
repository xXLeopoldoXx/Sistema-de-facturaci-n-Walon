package controller;

import model.dao.ClienteDAO;
import model.dto.ClienteDTO;
import view.ClientesView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClientesController {

    private ClientesView view;
    private ClienteDAO dao;
    private DefaultTableModel modelo;

    private int idSeleccionado = -1;

    public ClientesController(ClientesView v){
        view = v;
        dao = new ClienteDAO();
        init();
        cargarTabla();
    }

    private void init(){
        view.btnNuevo.addActionListener(e -> limpiar());
        view.btnGuardar.addActionListener(e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());

        view.tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarFila();
            }
        });
    }

    private void cargarTabla(){
        modelo = (DefaultTableModel) view.tablaClientes.getModel();
        modelo.setRowCount(0);

        List<ClienteDTO> lista = dao.listar();
        for(ClienteDTO c: lista){
            modelo.addRow(new Object[]{
                    c.getId(),
                    c.getDni(),
                    c.getNombre(),
                    c.getDireccion(),
                    c.getTelefono()
            });
        }

        // ocultar ID
        view.tablaClientes.getColumnModel().getColumn(0).setMinWidth(0);
        view.tablaClientes.getColumnModel().getColumn(0).setMaxWidth(0);
        view.tablaClientes.getColumnModel().getColumn(0).setWidth(0);
    }

    private void guardar(){
        String dni = view.txtDni.getText().trim();
        String nombre = view.txtNombre.getText().trim();
        String direccion = view.txtDireccion.getText().trim();
        String telefono = view.txtTelefono.getText().trim();

        if(dni.isEmpty() || nombre.isEmpty()){
            JOptionPane.showMessageDialog(view, "DNI y Nombre son obligatorios");
            return;
        }
        if(dni.length() != 8){
            JOptionPane.showMessageDialog(view, "DNI debe tener 8 dígitos");
            return;
        }

        ClienteDTO c = new ClienteDTO(dni, nombre, direccion, telefono);

        if(dao.insertar(c)){
            JOptionPane.showMessageDialog(view, "Cliente guardado");
            cargarTabla();
            limpiar();
        }else{
            JOptionPane.showMessageDialog(view, "No se pudo guardar");
        }
    }

    private void actualizar(){
        if(idSeleccionado == -1){
            JOptionPane.showMessageDialog(view, "Selecciona un cliente en la tabla");
            return;
        }

        String dni = view.txtDni.getText().trim();
        String nombre = view.txtNombre.getText().trim();
        String direccion = view.txtDireccion.getText().trim();
        String telefono = view.txtTelefono.getText().trim();

        if(dni.isEmpty() || nombre.isEmpty()){
            JOptionPane.showMessageDialog(view, "DNI y Nombre son obligatorios");
            return;
        }

        ClienteDTO c = new ClienteDTO(idSeleccionado, dni, nombre, direccion, telefono);

        if(dao.actualizar(c)){
            JOptionPane.showMessageDialog(view, "Cliente actualizado");
            cargarTabla();
            limpiar();
        }else{
            JOptionPane.showMessageDialog(view, "No se pudo actualizar");
        }
    }

    private void eliminar(){
        if(idSeleccionado == -1){
            JOptionPane.showMessageDialog(view, "Selecciona un cliente en la tabla");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(
                view,
                "¿Seguro de eliminar este cliente?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if(ok == JOptionPane.YES_OPTION){
            if(dao.eliminar(idSeleccionado)){
                JOptionPane.showMessageDialog(view, "Cliente eliminado");
                cargarTabla();
                limpiar();
            }else{
                JOptionPane.showMessageDialog(view, "No se pudo eliminar");
            }
        }
    }

    private void seleccionarFila(){
        int fila = view.tablaClientes.getSelectedRow();
        if(fila == -1) return;

        idSeleccionado = Integer.parseInt(view.tablaClientes.getValueAt(fila, 0).toString());

        view.txtDni.setText(view.tablaClientes.getValueAt(fila, 1).toString());
        view.txtNombre.setText(view.tablaClientes.getValueAt(fila, 2).toString());
        view.txtDireccion.setText(view.tablaClientes.getValueAt(fila, 3).toString());
        view.txtTelefono.setText(view.tablaClientes.getValueAt(fila, 4).toString());
    }

    private void limpiar(){
        view.txtDni.setText("");
        view.txtNombre.setText("");
        view.txtDireccion.setText("");
        view.txtTelefono.setText("");
        idSeleccionado = -1;
        view.txtDni.requestFocus();
    }
}

