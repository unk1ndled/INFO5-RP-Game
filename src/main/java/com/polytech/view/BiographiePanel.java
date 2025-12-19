package com.polytech.view;

import com.polytech.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BiographiePanel extends JPanel {
    private ApplicationGUI mainApp;

    private JComboBox<String> cmbPersonnages;
    private JButton btnRefresh;

    private JEditorPane txtBiographie;
    private JScrollPane scrollPane;

    private JButton btnRevelerSecret;
    private JList<String> lstSecrets;

    public BiographiePanel(ApplicationGUI mainApp) {
        this.mainApp = mainApp;
        initializeComponents();
        refresh();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Sélectionner un Personnage:"));
        cmbPersonnages = new JComboBox<>();
        cmbPersonnages.addActionListener(e -> displayBiographie());
        topPanel.add(cmbPersonnages);

        btnRefresh = new JButton("Actualiser");
        btnRefresh.addActionListener(e -> refresh());
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        txtBiographie = new JEditorPane();
        txtBiographie.setEditable(false);
        txtBiographie.setContentType("text/html");
        txtBiographie.setFont(new Font("Arial", Font.PLAIN, 12));
        txtBiographie.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(txtBiographie);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Biographie"));
        centerSplit.setLeftComponent(scrollPane);

        JPanel revelationPanel = createRevelationPanel();
        centerSplit.setRightComponent(revelationPanel);
        centerSplit.setDividerLocation(600);

        add(centerSplit, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JLabel instructions = new JLabel("<html><center>" +
                "Test des permissions d'accès:<br/>" +
                "• <font color='blue'>Texte bleu = SECRET visible au MJ/propriétaire</font><br/>" +
                "• Texte noir = Contenu public<br/>" +
                "• Utilisez le panneau de droite pour révéler des secrets" +
                "</center></html>");
        instructions.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(instructions);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        refreshPersonnages();
        displayBiographie();
    }

    private void refreshPersonnages() {
        cmbPersonnages.removeAllItems();
        List<Personnage> personnages = mainApp.getPersonnageController().getPersonnages();

        for (Personnage p : personnages) {
            cmbPersonnages.addItem(p.getNom() + " (" + p.getJoueur().getPseudo() + ")");
        }
    }

    private void displayBiographie() {
        String selected = (String) cmbPersonnages.getSelectedItem();
        if (selected == null) {
            txtBiographie.setText("Veuillez sélectionner un personnage.");
            updateRevelationPanel(null, null);
            return;
        }

        String nomPersonnage = selected.split(" \\(")[0];
        Personnage personnage = mainApp.getPersonnageController().findByNom(nomPersonnage);

        if (personnage == null) {
            txtBiographie.setText("Personnage non trouvé.");
            updateRevelationPanel(null, null);
            return;
        }

        if (personnage.getMeneurDeJeu() == null) {
            txtBiographie.setText("Ce personnage n'a pas encore été validé par un MJ.");
            updateRevelationPanel(null, null);
            return;
        }

        Utilisateur currentUser = mainApp.getCurrentUser();
        StringBuilder biographyText = new StringBuilder();

        biographyText.append("=== BIOGRAPHIE DE ").append(personnage.getNom().toUpperCase()).append(" ===\n\n");
        biographyText.append("Nom: ").append(personnage.getNom()).append("\n");
        biographyText.append("Profession: ").append(personnage.getProfession()).append("\n");
        biographyText.append("Date de Naissance: ").append(personnage.getDateNaissance()).append("\n");
        biographyText.append("Univers: ").append(personnage.getUnivers().getNom()).append("\n");
        biographyText.append("Propriétaire: ").append(personnage.getJoueur().getPseudo()).append("\n");
        biographyText.append("MJ Assigné: ").append(personnage.getMeneurDeJeu().getPseudo()).append("\n\n");

        List<Episode> episodes = personnage.getBiographie().getEpisodes()
                .stream()
                .filter(ep -> ep.getStatut() == Episode.Status.VALIDATED) // Only validated episodes in biography
                .sorted((e1, e2) -> e1.getDateRelative().compareTo(e2.getDateRelative()))
                .toList();

        if (episodes.isEmpty()) {
            biographyText.append("Aucun épisode validé dans la biographie.\n");
        } else {
            biographyText.append("=== ÉPISODES ===\n\n");

            for (Episode episode : episodes) {
                biographyText.append("--- ").append(episode.getDateRelative()).append(" ---\n");

                boolean hasVisibleContent = false;
                for (ParagrapheSecret para : episode.getParagraphesSecrets()) {
                    if (!para.isSecret() ||
                        currentUser.equals(personnage.getJoueur()) ||
                        currentUser.equals(personnage.getMeneurDeJeu())) {

                        if (para.isSecret()) {
                            biographyText.append("<font color='blue'>").append(para.getTexte()).append(" [SECRET]</font>\n\n");
                        } else {
                            biographyText.append("<font color='black'>").append(para.getTexte()).append("</font>\n\n");
                        }
                        hasVisibleContent = true;
                    }
                }

                if (!hasVisibleContent) {
                    biographyText.append("<font color='gray'>(Contenu non visible avec vos permissions)</font>\n\n");
                }

                biographyText.append("\n");
            }
        }

        txtBiographie.setContentType("text/html");
        txtBiographie.setText("<html><body style='font-family: Arial;'>" +
                biographyText.toString().replace("\n", "<br/>") + "</body></html>");

        updateRevelationPanel(personnage, currentUser);
    }

    private JPanel createRevelationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Révélation de Secrets"));
        panel.setPreferredSize(new Dimension(300, 400));

        lstSecrets = new JList<>();
        lstSecrets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(lstSecrets);
        panel.add(listScroll, BorderLayout.CENTER);

        btnRevelerSecret = new JButton("Révéler le Secret Sélectionné");
        btnRevelerSecret.addActionListener(new RevelationListener());
        btnRevelerSecret.setEnabled(false); // Initially disabled
        panel.add(btnRevelerSecret, BorderLayout.SOUTH);

        JLabel instructionLabel = new JLabel("<html><center>Sélectionnez un paragraphe secret<br/>" +
                "et cliquez pour le révéler.<br/>" +
                "Cette action est IRRÉVERSIBLE!</center></html>");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instructionLabel, BorderLayout.NORTH);

        return panel;
    }

    private void updateRevelationPanel(Personnage personnage, Utilisateur currentUser) {
        if (personnage == null || currentUser == null) {
            lstSecrets.setListData(new String[0]);
            btnRevelerSecret.setEnabled(false);
            return;
        }

        boolean canReveal = currentUser.equals(personnage.getJoueur());

        if (!canReveal) {
            lstSecrets.setListData(new String[]{"Fonction réservée au propriétaire du personnage"});
            btnRevelerSecret.setEnabled(false);
            return;
        }

        java.util.List<String> secretItems = new java.util.ArrayList<>();
        for (Episode episode : personnage.getBiographie().getEpisodes()) {
            for (int i = 0; i < episode.getParagraphesSecrets().size(); i++) {
                ParagrapheSecret para = episode.getParagraphesSecrets().get(i);
                if (para.isSecret()) {
                    String item = String.format("%s - Épisode %s: %s...",
                            episode.getDateRelative(),
                            episode.getDateRelative(),
                            para.getTexte().substring(0, Math.min(50, para.getTexte().length())));
                    secretItems.add(item);
                }
            }
        }

        lstSecrets.setListData(secretItems.toArray(new String[0]));
        btnRevelerSecret.setEnabled(!secretItems.isEmpty());
    }

    private class RevelationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = lstSecrets.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(mainApp,
                    "Veuillez sélectionner un secret à révéler.",
                    "Sélection requise",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(mainApp,
                "Êtes-vous sûr de vouloir révéler ce secret?\n\n" +
                "Cette action est IRRÉVERSIBLE et rendra le contenu visible à tous.",
                "Confirmation de révélation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }

            String selected = (String) cmbPersonnages.getSelectedItem();
            if (selected != null) {
                String nomPersonnage = selected.split(" \\(")[0];
                Personnage personnage = mainApp.getPersonnageController().findByNom(nomPersonnage);

                if (personnage != null) {
                    int secretCount = 0;
                    boolean found = false;
                    for (Episode episode : personnage.getBiographie().getEpisodes()) {
                        for (int i = 0; i < episode.getParagraphesSecrets().size(); i++) {
                            ParagrapheSecret para = episode.getParagraphesSecrets().get(i);
                            if (para.isSecret()) {
                                if (secretCount == selectedIndex) {
                                    mainApp.getEpisodeController().revelerSecret(
                                        nomPersonnage, episode.getDateRelative(), i);
                                    found = true;
                                    break;
                                }
                                secretCount++;
                            }
                        }
                        if (found) break;
                    }

                    if (found) {
                        JOptionPane.showMessageDialog(mainApp,
                            "Secret révélé avec succès!\nLe contenu est maintenant public.",
                            "Révélation réussie",
                            JOptionPane.INFORMATION_MESSAGE);

                        displayBiographie();
                    }
                }
            }
        }
    }
}
