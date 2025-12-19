package com.polytech.view;

import com.polytech.controller.EpisodeController;
import com.polytech.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EpisodeManagementPanel extends JPanel {
    private ApplicationGUI mainApp;
    private EpisodeController episodeCtrl;

    private JComboBox<String> cmbPersonnages;
    private JButton btnRefreshPersonnages;

    private JTextField txtEpisodeDate;
    private JTextArea txtParagraphContent;
    private JCheckBox chkParagraphSecret;
    private JButton btnAddParagraph;
    private JButton btnRemoveParagraph;
    private JList<String> lstParagraphs;
    private DefaultListModel<String> paragraphModel;
    private JButton btnSaveEpisodeDraft;

    private JList<String> lstDrafts;
    private DefaultListModel<String> draftModel;
    private JButton btnConfirmDraft;

    private JLabel lblStatus;
    private JTextArea txtDisplayContent;

    private Episode currentEpisode;
    private Personnage selectedPersonnage;

    private java.util.List<ParagrapheSecret> episodeParagraphs;

    public EpisodeManagementPanel(ApplicationGUI mainApp) {
        this.mainApp = mainApp;
        this.episodeCtrl = mainApp.getEpisodeController();
        initializeComponents();
        refresh();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Personnage:"));
        cmbPersonnages = new JComboBox<>();
        cmbPersonnages.addActionListener(e -> onPersonnageSelected());
        panel.add(cmbPersonnages);

        btnRefreshPersonnages = new JButton("Actualiser");
        btnRefreshPersonnages.addActionListener(e -> refreshPersonnages());
        panel.add(btnRefreshPersonnages);

        return panel;
    }

    private JPanel createCenterPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        episodeParagraphs = new java.util.ArrayList<>();

        JPanel creationPanel = new JPanel(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JPanel leftPanel = createParagraphCreationPanel();
        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = createEpisodeBuildingPanel();
        splitPane.setRightComponent(rightPanel);

        splitPane.setDividerLocation(400);
        creationPanel.add(splitPane, BorderLayout.CENTER);

        tabbedPane.addTab("Créer Épisode", creationPanel);

        JPanel confirmationPanel = createDraftConfirmationPanel();
        tabbedPane.addTab("Confirmer Brouillons", confirmationPanel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createParagraphCreationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Création de Paragraphes"));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Date de l'épisode:"));
        txtEpisodeDate = new JTextField(15);
        txtEpisodeDate.setText("An 1"); // Default value
        datePanel.add(txtEpisodeDate);
        panel.add(datePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder("Contenu du Paragraphe"));

        txtParagraphContent = new JTextArea(8, 30);
        txtParagraphContent.setLineWrap(true);
        txtParagraphContent.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(txtParagraphContent);
        contentPanel.add(contentScroll, BorderLayout.CENTER);

        JPanel secretPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkParagraphSecret = new JCheckBox("Marquer comme SECRET");
        chkParagraphSecret.setToolTipText("Ce paragraphe ne sera visible que par le propriétaire et le MJ");
        secretPanel.add(chkParagraphSecret);
        contentPanel.add(secretPanel, BorderLayout.SOUTH);

        panel.add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAddParagraph = new JButton("Ajouter ce Paragraphe");
        btnAddParagraph.addActionListener(new ParagraphActionListener());
        buttonPanel.add(btnAddParagraph);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEpisodeBuildingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Construction de l'Épisode"));

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Paragraphes de l'épisode"));

        paragraphModel = new DefaultListModel<>();
        lstParagraphs = new JList<>(paragraphModel);
        JScrollPane listScroll = new JScrollPane(lstParagraphs);
        listPanel.add(listScroll, BorderLayout.CENTER);

        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRemoveParagraph = new JButton("Retirer le Paragraphe Sélectionné");
        btnRemoveParagraph.addActionListener(new ParagraphActionListener());
        removePanel.add(btnRemoveParagraph);
        listPanel.add(removePanel, BorderLayout.SOUTH);

        panel.add(listPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout());
        btnSaveEpisodeDraft = new JButton("Sauvegarder Épisode comme Brouillon");
        btnSaveEpisodeDraft.addActionListener(new EpisodeActionListener());

        actionPanel.add(btnSaveEpisodeDraft);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Statut: "));
        lblStatus = new JLabel("Aucun épisode sélectionné");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        statusPanel.add(lblStatus);
        panel.add(statusPanel, BorderLayout.NORTH);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder("Contenu Affiché"));
        txtDisplayContent = new JTextArea(5, 50);
        txtDisplayContent.setEditable(false);
        txtDisplayContent.setBackground(Color.LIGHT_GRAY);
        JScrollPane displayScroll = new JScrollPane(txtDisplayContent);
        displayPanel.add(displayScroll, BorderLayout.CENTER);
        panel.add(displayPanel, BorderLayout.CENTER);

        return panel;
    }

    public void refresh() {
        refreshPersonnages();
        updateDisplay();
        updateDraftList();
    }

    private void refreshPersonnages() {
        cmbPersonnages.removeAllItems();
        List<Personnage> personnages = mainApp.getPersonnageController().getPersonnages();

        for (Personnage p : personnages) {
            cmbPersonnages.addItem(p.getNom() + " (" + p.getJoueur().getPseudo() + ")");
        }
    }

    private void onPersonnageSelected() {
        String selected = (String) cmbPersonnages.getSelectedItem();
        if (selected != null) {
            String nomPersonnage = selected.split(" \\(")[0]; 
            selectedPersonnage = mainApp.getPersonnageController().findByNom(nomPersonnage);
            updateDisplay();
            updateDraftList();
        }
    }

    private void updateDisplay() {
        if (selectedPersonnage == null) {
            lblStatus.setText("Aucun personnage sélectionné");
            txtDisplayContent.setText("");
            return;
        }

        if (selectedPersonnage.getMeneurDeJeu() == null) {
            lblStatus.setText("Personnage non validé par MJ");
            txtDisplayContent.setText("Ce personnage n'a pas encore été validé par un MJ.");
            return;
        }

        List<Episode> episodes = selectedPersonnage.getBiographie().getEpisodes()
                .stream()
                .sorted((e1, e2) -> e1.getDateRelative().compareTo(e2.getDateRelative()))
                .toList();

        if (episodes.isEmpty()) {
            lblStatus.setText("Aucun épisode");
            txtDisplayContent.setText("Ce personnage n'a encore aucun épisode.");
            return;
        }

        lblStatus.setText("Épisodes: " + episodes.size() + " (triés chronologiquement)");

        Utilisateur currentUser = mainApp.getCurrentUser();
        StringBuilder content = new StringBuilder();

        for (Episode episode : episodes) {
            String episodeTitle = "=== " + episode.getDateRelative();
            if (episode.getStatut() == Episode.Status.DRAFT) {
                episodeTitle += " [BROUILLON]";
            }
            episodeTitle += " ===\n";
            content.append(episodeTitle);

            boolean hasVisibleContent = false;
            for (ParagrapheSecret para : episode.getParagraphesSecrets()) {
                if (!para.isSecret() ||
                    currentUser.equals(selectedPersonnage.getJoueur()) ||
                    currentUser.equals(selectedPersonnage.getMeneurDeJeu())) {
                    content.append(para.getTexte());
                    if (para.isSecret()) {
                        content.append(" (SECRET)");
                    }
                    content.append("\n\n");
                    hasVisibleContent = true;
                }
            }

            if (!hasVisibleContent) {
                content.append("(Contenu non visible avec vos permissions)\n\n");
            }
            content.append("\n");
        }

        if (content.length() == 0) {
            content.append("Aucun épisode visible avec vos permissions.");
        }

        txtDisplayContent.setText(content.toString());
    }

    private class ParagraphActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnAddParagraph) {
                String content = txtParagraphContent.getText().trim();
                if (content.isEmpty()) {
                    JOptionPane.showMessageDialog(mainApp,
                        "Veuillez saisir du contenu pour le paragraphe.",
                        "Contenu requis",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean isSecret = chkParagraphSecret.isSelected();
                ParagrapheSecret paragraph = new ParagrapheSecret(content, isSecret);
                episodeParagraphs.add(paragraph);

                String displayText = (isSecret ? "[SECRET] " : "[PUBLIC] ") +
                    content.substring(0, Math.min(50, content.length())) +
                    (content.length() > 50 ? "..." : "");
                paragraphModel.addElement(displayText);

                txtParagraphContent.setText("");
                chkParagraphSecret.setSelected(false);

            } else if (e.getSource() == btnRemoveParagraph) {
                int selectedIndex = lstParagraphs.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(mainApp,
                        "Veuillez sélectionner un paragraphe à retirer.",
                        "Sélection requise",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                episodeParagraphs.remove(selectedIndex);
                paragraphModel.remove(selectedIndex);
            }
        }
    }

    private class EpisodeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedPersonnage == null) {
                JOptionPane.showMessageDialog(mainApp,
                    "Veuillez d'abord sélectionner un personnage.",
                    "Sélection requise",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedPersonnage.getMeneurDeJeu() == null) {
                JOptionPane.showMessageDialog(mainApp,
                    "Ce personnage n'a pas encore été validé par un MJ.",
                    "Validation requise",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }



            String episodeDate = txtEpisodeDate.getText().trim();
            if (episodeDate.isEmpty()) {
                episodeDate = "An 1"; 
            }

            try {
                if (e.getSource() == btnSaveEpisodeDraft) {
                    int result = JOptionPane.showConfirmDialog(mainApp,
                        "Êtes-vous sûr de vouloir sauvegarder cet épisode comme brouillon?\n\n" +
                        "Contenu: " + episodeParagraphs.size() + " paragraphes\n" +
                        "Date: " + episodeDate + "\n\n" +
                        "Le brouillon pourra être modifié avant validation.",
                        "Confirmation de sauvegarde du brouillon",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }

                    currentEpisode = episodeCtrl.creerEpisodeDraft(
                        selectedPersonnage.getNom(),
                        episodeDate);

                    for (ParagrapheSecret para : episodeParagraphs) {
                        episodeCtrl.ajouterParagrapheAEpisode(
                            selectedPersonnage.getNom(),
                            episodeDate,
                            para.getTexte(),
                            para.isSecret());
                    }

                    JOptionPane.showMessageDialog(mainApp,
                        "Brouillon d'épisode sauvegardé avec succès!\n" +
                        "Contient " + episodeParagraphs.size() + " paragraphes.",
                        "Sauvegarde réussie",
                        JOptionPane.INFORMATION_MESSAGE);

                    clearEpisodeBuilding();

                    mainApp.refreshAllViews();
                } else if (e.getSource() == btnConfirmDraft) {
                    int selectedIndex = lstDrafts.getSelectedIndex();
                    if (selectedIndex == -1) {
                        JOptionPane.showMessageDialog(mainApp,
                            "Veuillez sélectionner un brouillon à confirmer.",
                            "Sélection requise",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String selectedItem = draftModel.getElementAt(selectedIndex);
                    String dateRelative = selectedItem.split(" - ")[0];

                    boolean ownerIsMJ = selectedPersonnage.getJoueur().equals(selectedPersonnage.getMeneurDeJeu());
                    String validationMessage = ownerIsMJ ?
                        "Cette action validera complètement l'épisode." :
                        "Cette action valide l'épisode pour votre rôle (joueur ou MJ).";

                    int result = JOptionPane.showConfirmDialog(mainApp,
                        "Êtes-vous sûr de vouloir confirmer ce brouillon?\n\n" +
                        "Épisode: " + dateRelative + "\n\n" + validationMessage,
                        "Confirmation de validation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }

                    episodeCtrl.validerEpisode(
                        selectedPersonnage.getNom(),
                        dateRelative,
                        mainApp.getCurrentUser().getPseudo());

                    String successMessage = ownerIsMJ ?
                        "Brouillon confirmé avec succès!\nL'épisode est maintenant validé." :
                        "Brouillon confirmé avec succès!\nSi l'autre validation est complète, l'épisode sera validé.";

                    JOptionPane.showMessageDialog(mainApp,
                        successMessage,
                        "Confirmation réussie",
                        JOptionPane.INFORMATION_MESSAGE);

                    mainApp.refreshAllViews();
                    updateDraftList();
                }

                updateDisplay();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainApp,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createDraftConfirmationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Confirmation de Brouillons"));

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Brouillons en attente de validation"));

        draftModel = new DefaultListModel<>();
        lstDrafts = new JList<>(draftModel);
        JScrollPane listScroll = new JScrollPane(lstDrafts);
        listPanel.add(listScroll, BorderLayout.CENTER);

        panel.add(listPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnConfirmDraft = new JButton("Confirmer le Brouillon Sélectionné");
        btnConfirmDraft.addActionListener(new EpisodeActionListener());
        buttonPanel.add(btnConfirmDraft);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateDraftList() {
        draftModel.clear();
        if (selectedPersonnage == null) {
            return;
        }

        Utilisateur currentUser = mainApp.getCurrentUser();
        Joueur owner = selectedPersonnage.getJoueur();
        MeneurDeJeu mj = selectedPersonnage.getMeneurDeJeu();
        List<Episode> episodes = selectedPersonnage.getBiographie().getEpisodes();

        for (Episode episode : episodes) {
            if (episode.getStatut() == Episode.Status.DRAFT) {
                boolean ownerIsMJ = owner.equals(mj);
                boolean canValidate = false;
                String validationType = "";

                if (ownerIsMJ) {
                    if (currentUser.equals(owner) && (!episode.isValidatedByPlayer() || !episode.isValidatedByMJ())) {
                        canValidate = true;
                        validationType = "[Validation Requise]";
                    }
                } else {
                    if (currentUser.equals(owner) && !episode.isValidatedByPlayer()) {
                        canValidate = true;
                        validationType = "[Validation Joueur]";
                    } else if (currentUser.equals(mj) && !episode.isValidatedByMJ()) {
                        canValidate = true;
                        validationType = "[Validation MJ]";
                    }
                }

                if (canValidate) {
                    String displayText = episode.getDateRelative() + " - " + validationType +
                        " (" + episode.getParagraphesSecrets().size() + " paragraphes)";
                    draftModel.addElement(displayText);
                }
            }
        }
    }

    private void clearEpisodeBuilding() {
        episodeParagraphs.clear();
        paragraphModel.clear();
        txtEpisodeDate.setText("An 1");
    }
}
