package com.polytech.view;

import com.polytech.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserSelectionPanel extends JPanel {
    private ApplicationGUI mainApp;
    private JButton btnVisitor;
    private JButton btnAlice;
    private JButton btnAbdelRaouf;
    private JButton btnBob;

    public UserSelectionPanel(ApplicationGUI mainApp) {
        this.mainApp = mainApp;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Sélection de l'Utilisateur Actif");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(titleLabel, gbc);

        btnVisitor = new JButton("Visiteur (Lecteur seul)");
        btnVisitor.addActionListener(new UserSelectionListener());

        btnAlice = new JButton("Alice (Joueuse - Propriétaire)");
        btnAlice.addActionListener(new UserSelectionListener());

        btnAbdelRaouf = new JButton("Abdel Raouf (Joueur)");
        btnAbdelRaouf.addActionListener(new UserSelectionListener());

        btnBob = new JButton("Bob (Meneur de Jeu)");
        btnBob.addActionListener(new UserSelectionListener());

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(btnVisitor, gbc);

        gbc.gridx = 1;
        add(btnAlice, gbc);

        gbc.gridx = 2;
        add(btnAbdelRaouf, gbc);

        gbc.gridx = 3;
        add(btnBob, gbc);

        JLabel instructions = new JLabel("<html><center>Sélectionnez un utilisateur pour tester les permissions:<br/>" +
                "• Visiteur: Ne voit que le contenu public<br/>" +
                "• Alice/Abdel Raouf: Peuvent créer et valider des épisodes<br/>" +
                "• Bob: Peut valider les personnages et épisodes</center></html>");
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(instructions, gbc);
    }

    private class UserSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Utilisateur selectedUser = null;

            if (e.getSource() == btnVisitor) {
                selectedUser = mainApp.findUserByPseudo("Visiteur");
            } else if (e.getSource() == btnAlice) {
                selectedUser = mainApp.findUserByPseudo("Alice");
            } else if (e.getSource() == btnAbdelRaouf) {
                selectedUser = mainApp.findUserByPseudo("Abdel Raouf");
            } else if (e.getSource() == btnBob) {
                selectedUser = mainApp.findUserByPseudo("Bob");
            }

            if (selectedUser != null) {
                mainApp.setCurrentUser(selectedUser);
                JOptionPane.showMessageDialog(mainApp,
                    "Utilisateur changé: " + selectedUser.getPseudo(),
                    "Changement d'utilisateur",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
