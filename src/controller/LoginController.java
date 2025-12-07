package controller;

import view.LoginView;
import view.MenuView;
import model.dao.UsuarioDAO;
import model.dto.UsuarioDTO;
import javax.swing.*;

public class LoginController {
    private LoginView view;
    private UsuarioDAO dao;

    public LoginController(LoginView v){
        view = v;
        dao = new UsuarioDAO();
        init();
    }

    private void init(){
        view.btnIngresar.addActionListener(e -> login());
    }

    private void login(){
        String user = view.txtUser.getText().trim();
        String pass = new String(view.txtPass.getPassword()).trim();

        if(user.isEmpty() || pass.isEmpty()){
            JOptionPane.showMessageDialog(view, "Ingresa usuario y contraseña");
            return;
        }

        UsuarioDTO u = dao.login(user, pass);

        if(u != null){
            JOptionPane.showMessageDialog(view, "Bienvenido " + u.getUsername());
            view.dispose();
            MenuView m = new MenuView();
            m.setVisible(true);
            m.setLocationRelativeTo(null);
        }else{
            JOptionPane.showMessageDialog(view, "Usuario o clave incorrecta");
        }
    }
}

