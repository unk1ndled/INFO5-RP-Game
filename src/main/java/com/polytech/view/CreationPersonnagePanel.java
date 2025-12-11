package com.polytech.view;

import com.polytech.controller.PersonnageController;
import com.polytech.model.*;
import com.polytech.repository.PersonnageRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CreationPersonnagePanel extends JPanel {
    private ApplicationGUI mainApp;
    private PersonnageController personnageCtrl;

    // Player creation components
    private JTextField txtNom;
    private JTextField txtProfession;
    private JTextField txtDateNaissance;
    private JComboBox<String> cmbUnivers;
    private JTextArea txtBiographieInitiale;
    private JButton btnProposerPersonnage;

    // MJ validation components
    private JTable tblPersonnagesProposes;
    private JButton btnAccepter;
    private JButton btnRefuser;
    private DefaultTableModel tableModel;

    public CreationPersonnagePanel(ApplicationGUI mainApp) {
        this.mainApp = mainApp;
        this.personnageCtrl = mainApp.getPersonnageController();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Create tabbed pane for different roles
        JTabbedPane tabbedPane = new JTabbedPane();

        // Player tab
        JPanel playerPanel = createPlayerPanel();
        tabbedPane.addTab("Création (Joueur)", playerPanel);

        // MJ tab
        JPanel mjPanel = createMJPanel();
        tabbedPane.addTab("Validation (MJ)", mjPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel titleLabel = new JLabel("Création d'un Nouveau Personnage");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Name field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        txtNom = new JTextField(20);
        panel.add(txtNom, gbc);

        // Profession field
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Profession:"), gbc);
        gbc.gridx = 1;
        txtProfession = new JTextField(20);
        panel.add(txtProfession, gbc);

        // Birth date field
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date de Naissance:"), gbc);
        gbc.gridx = 1;
        txtDateNaissance = new JTextField(20);
        panel.add(txtDateNaissance, gbc);

        // Universe selection
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Univers:"), gbc);
        gbc.gridx = 1;
        cmbUnivers = new JComboBox<>();
        cmbUnivers.addItem("Univers des Pirates");
        cmbUnivers.addItem("Univers de la Guerre des Étoiles");
        cmbUnivers.addItem("Univers Médiéval Fantastique");
        cmbUnivers.addItem("Univers Cyberpunk");
        panel.add(cmbUnivers, gbc);

        // Initial biography field
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Biographie Initiale:"), gbc);
        gbc.gridx = 1;
        txtBiographieInitiale = new JTextArea(4, 20);
        txtBiographieInitiale.setLineWrap(true);
        txtBiographieInitiale.setWrapStyleWord(true);
        JScrollPane bioScroll = new JScrollPane(txtBiographieInitiale);
        panel.add(bioScroll, gbc);

        // Create button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        btnProposerPersonnage = new JButton("Proposer Personnage");
        btnProposerPersonnage.addActionListener(new CreationListener());
        panel.add(btnProposerPersonnage, gbc);

        return panel;
    }

    private JPanel createMJPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Validation des Personnages Proposés", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Table for proposed characters
        String[] columnNames = {"Nom", "Profession", "Date Naissance", "Joueur"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPersonnagesProposes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblPersonnagesProposes);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        btnAccepter = new JButton("Accepter");
        btnAccepter.addActionListener(new ValidationListener());
        btnRefuser = new JButton("Refuser");
        btnRefuser.addActionListener(new ValidationListener());

        buttonsPanel.add(btnAccepter);
        buttonsPanel.add(btnRefuser);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void refresh() {
        refreshMJTable();
    }

    private void refreshMJTable() {
        tableModel.setRowCount(0);
        PersonnageRepository repo = PersonnageRepository.getInstance();
        List<Personnage> personnages = repo.getPersonnages();

        for (Personnage p : personnages) {
            if (p.getMeneurDeJeu() == null) { // Only show unvalidated characters
                Object[] row = {
                    p.getNom(),
                    p.getProfession(),
                    p.getDateNaissance(),
                    p.getJoueur().getPseudo()
                };
                tableModel.addRow(row);
            }
        }
    }

    private class CreationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainApp.getCurrentUser() instanceof Joueur joueur) {
                try {
                    String nom = txtNom.getText().trim();
                    String profession = txtProfession.getText().trim();
                    String dateNaissance = txtDateNaissance.getText().trim();
                    String biographieInitiale = txtBiographieInitiale.getText().trim();

                    if (nom.isEmpty() || profession.isEmpty() || dateNaissance.isEmpty()) {
                        JOptionPane.showMessageDialog(mainApp,
                            "Veuillez remplir tous les champs obligatoires (nom, profession, date de naissance).",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String universSelectionne = (String) cmbUnivers.getSelectedItem();
                    Personnage personnage = personnageCtrl.creerPersonnage(
                        nom, dateNaissance, profession, "",
                        universSelectionne, joueur.getPseudo());

                    // Add initial biography episode if provided
                    if (!biographieInitiale.isEmpty()) {
                        Episode episodeInitial = new Episode(dateNaissance); // Use birth date as episode date
                        ParagrapheSecret paraBio = new ParagrapheSecret(biographieInitiale, false); // Public initial biography
                        episodeInitial.getParagraphesSecrets().add(paraBio);
                        personnage.getBiographie().ajouterEpisode(episodeInitial);
                    }

                    JOptionPane.showMessageDialog(mainApp,
                        "Personnage '" + nom + "' proposé avec succès!\nEn attente de validation par un MJ.",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                    // Clear fields
                    txtNom.setText("");
                    txtProfession.setText("");
                    txtDateNaissance.setText("");
                    txtBiographieInitiale.setText("");

                    // Refresh MJ view
                    refreshMJTable();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainApp,
                        "Erreur lors de la création: " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainApp,
                    "Seul un joueur peut créer un personnage.",
                    "Permission refusée",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class ValidationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!(mainApp.getCurrentUser() instanceof MeneurDeJeu)) {
                JOptionPane.showMessageDialog(mainApp,
                    "Seul un MJ peut valider les personnages.",
                    "Permission refusée",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int selectedRow = tblPersonnagesProposes.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(mainApp,
                    "Veuillez sélectionner un personnage.",
                    "Sélection requise",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nomPersonnage = (String) tableModel.getValueAt(selectedRow, 0);

            try {
                if (e.getSource() == btnAccepter) {
                    personnageCtrl.accepterPersonnage(nomPersonnage, mainApp.getCurrentUser().getPseudo());
                    JOptionPane.showMessageDialog(mainApp,
                        "Personnage '" + nomPersonnage + "' accepté!",
                        "Validation réussie",
                        JOptionPane.INFORMATION_MESSAGE);
                } else if (e.getSource() == btnRefuser) {
                    personnageCtrl.refuserPersonnage(nomPersonnage);
                    JOptionPane.showMessageDialog(mainApp,
                        "Personnage '" + nomPersonnage + "' refusé et supprimé.",
                        "Refus confirmé",
                        JOptionPane.INFORMATION_MESSAGE);
                }

                refreshMJTable();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainApp,
                    "Erreur lors de la validation: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
