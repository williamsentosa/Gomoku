/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.prompt.PromptSupport;

/**
 *
 * @author natanelia
 */
public class LoginPanel extends JPanel {
    private static final String backgroundColor = "#f0f5f9";
    public JTextField txtUserName = new JTextField(20);
    public JButton btnLogin = new JButton("Login");
    
    public LoginPanel() {
        super();
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode(backgroundColor));

        GridBagConstraints c = new GridBagConstraints();
        JLabel title = new JLabel("Gomoku");
        title.setFont(new Font("Roboto", Font.PLAIN, 120));
        title.setForeground(Color.decode("#2a4d69"));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(100, 0, 0, 0);
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(title, c);
        
        initTxtUserName(c);
        initBtnLogin(c);
    }
    
    private void initTxtUserName(GridBagConstraints c) {
        PromptSupport.setPrompt("Nickname", txtUserName);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, txtUserName);
        txtUserName.setFont(new Font("Sniglet", Font.PLAIN, 25));
        txtUserName.setForeground(Color.decode("#2a4d69"));
        txtUserName.setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 1));

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 0, 0);
        
        this.add(txtUserName, c);
    }
    
    private void initBtnLogin(GridBagConstraints c) {
        btnLogin.setFont(new Font("Sniglet", Font.PLAIN, 25));
        btnLogin.setOpaque(true);
        btnLogin.setBackground(Color.decode("#2a4d69"));
        btnLogin.setForeground(Color.decode("#e7eff6"));
        //btnLogin.setBorder(BorderFactory.createLineBorder(Color.decode("#9bb6cc"), 1));
        btnLogin.setFocusPainted(false);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0, 0, 250, 0);
        //c.gridwidth = 2;   //2 columns wide
        c.anchor = GridBagConstraints.PAGE_END;
        
        this.add(btnLogin, c);
    }
}
