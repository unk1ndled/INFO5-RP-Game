package com.polytech.view;

import com.polytech.*;
import com.polytech.controller.*;
import com.polytech.model.*;
import com.polytech.repository.*;

import javax.swing.*;
import java.awt.*;

public class ApplicationGUI extends JFrame {
    private Utilisateur currentUser;
    private PersonnageController personnageCtrl;
    private EpisodeController episodeCtrl;

    // Panels
    private UserSelectionPanel userPanel;
    private CreationPersonnagePanel creationPanel;
    private EpisodeManagementPanel episodePanel;
    private BiographiePanel biographiePanel;

    public ApplicationGUI() {
        initializeControllers();
        initializeGUI();
        setDefaultUser(); // Start with visitor
    }

    private void initializeControllers() {
        personnageCtrl = new PersonnageController();
        episodeCtrl = new EpisodeController();
    }

    private void initializeGUI() {
        setTitle("Application de Gestion de Rôles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panels
        userPanel = new UserSelectionPanel(this);
        creationPanel = new CreationPersonnagePanel(this);
        episodePanel = new EpisodeManagementPanel(this);
        biographiePanel = new BiographiePanel(this);

        // Tabbed pane for different views
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Sélection Utilisateur", userPanel);
        tabbedPane.addTab("Création Personnage", creationPanel);
        tabbedPane.addTab("Gestion Épisodes", episodePanel);
        tabbedPane.addTab("Biographie", biographiePanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        add(statusPanel, BorderLayout.SOUTH);

        updateStatusBar();

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void setDefaultUser() {
        // Initialize default data
        DataInitializer.initializeDefaultData();

        // Start as visitor
        Utilisateur visitor = UtilisateurRepository.getInstance().findByPseudo("Visiteur").orElse(null);
        if (visitor != null) {
            setCurrentUser(visitor);
        }
    }

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        updateStatusBar();
        refreshAllViews();
    }

    private void updateStatusBar() {
        // Update status bar with current user
        JPanel statusPanel = (JPanel) getContentPane().getComponent(1);
        statusPanel.removeAll();

        String userInfo = currentUser != null ?
            "Utilisateur actuel: " + currentUser.getPseudo() +
            (currentUser instanceof Joueur ? " (Joueur)" :
             currentUser instanceof MeneurDeJeu ? " (MJ)" : " (Visiteur)") :
            "Aucun utilisateur sélectionné";

        statusPanel.add(new JLabel(userInfo));
        statusPanel.revalidate();
        statusPanel.repaint();
    }

    public void refreshAllViews() {
        creationPanel.refresh();
        episodePanel.refresh();
        biographiePanel.refresh();
    }

    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    public PersonnageController getPersonnageController() {
        return personnageCtrl;
    }

    public EpisodeController getEpisodeController() {
        return episodeCtrl;
    }

    public Utilisateur findUserByPseudo(String pseudo) {
        return UtilisateurRepository.getInstance().findByPseudo(pseudo).orElse(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ApplicationGUI().setVisible(true);
        });
    }
}
