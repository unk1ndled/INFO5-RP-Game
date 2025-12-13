# Application de Gestion de Rôles (Jeu de Rôle)

## Description du Projet

Ce projet est une application Java complète pour gérer les personnages, aventures et parties dans un univers de jeu de rôle. L'application permet aux joueurs de créer des personnages liés à des univers spécifiques, aux maîtres de jeu (MJ) de valider les créations et gérer les aventures, et de gérer des biographies complexes avec du contenu public et secret. Le système inclut la gestion des parties, des transferts de personnages, et des liaisons entre épisodes et aventures.

## Fonctionnalités Principales

### Gestion des Univers
- Création d'univers (ex: Pirates, Fantasy, Star Wars)
- Chaque personnage et aventure appartient à un univers spécifique

### Pour les Joueurs
- Créer de nouveaux personnages avec nom, profession, date de naissance et univers
- Proposer des personnages aux MJ pour validation
- Écrire des épisodes dans la biographie de leurs personnages (publics ou secrets)
- Valider leurs propres épisodes
- Transférer leurs personnages à d'autres joueurs
- Participer à des aventures organisées par les MJ

### Pour les Maîtres de Jeu (MJ)
- Valider ou refuser les personnages proposés par les joueurs
- Accéder aux biographies privées avant validation
- Valider les épisodes écrits par les joueurs
- Organiser des parties/aventure dans un univers
- Ajouter/retirer des personnages participants
- Finaliser les aventures avec un résumé
- Demander ou accepter des transferts de MJ pour des personnages
- Accéder à tout le contenu (public et secret) des biographies

### Pour les Visiteurs
- Consulter les biographies publiques des personnages
- Accès limité : les contenus secrets ne sont pas visibles
- Voir les propositions de parties

### Gestion des Permissions
- **Contenu Public** : Visible par tout le monde
- **Contenu Secret** : Visible seulement par le propriétaire du personnage et le MJ
- **Validation Double** : Les épisodes nécessitent la validation du joueur ET du MJ
- **Immutabilité** : Épisodes validés et aventures terminées ne peuvent plus être modifiés
- **Transferts Bloqués** : Impossible de transférer un personnage participant à une partie non terminée

### Gestion des Parties et Aventures
- Proposition de parties par n'importe quel utilisateur (devient MJ)
- Ajout de personnages validés du même univers
- Finalisation des parties pour créer des aventures
- Liaison manuelle d'épisodes aux aventures

## Technologies Utilisées

- **Java 17** : Langage de programmation
- **Maven** : Gestionnaire de dépendances et build
- **JUnit 5** : Tests unitaires
- **Swing** : Interface graphique utilisateur

## Structure du Projet

```
src/
├── main/java/com/polytech/
│   ├── model/           # Classes métier (Personnage, Episode, etc.)
│   ├── controller/      # Logique de contrôle
│   ├── repository/      # Gestion des données en mémoire
│   └── view/            # Interface graphique Swing
└── test/java/com/polytech/
    └── *Test.java       # Tests unitaires
```

## Comment Lancer l'Application

### Prérequis
- Java 17 ou supérieur installé
- Maven installé

### Lancement de l'Interface Graphique
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.polytech.view.ApplicationGUI"
```

### Lancement de la Version Console
```bash
mvn exec:java -Dexec.mainClass="com.polytech.App"
```

### Exécution des Tests
```bash
mvn test
```

## Utilisation de l'Application

### Version Console (App.java)
La version console démontre un scénario basique :
- Création d'univers et utilisateurs
- Création et validation de personnages
- Création et validation d'épisodes

### Version Graphique (ApplicationGUI)
**Note** : L'interface graphique peut ne pas refléter toutes les nouvelles fonctionnalités avancées (parties, transferts). Utilisez la version console pour tester complètement.

### 1. Sélection d'Utilisateur
- Commencez par sélectionner un utilisateur dans l'onglet "Sélection Utilisateur"
- **Visiteur** : Accès en lecture seule aux biographies publiques
- **Joueurs** : Peuvent créer des personnages et gérer leurs épisodes
- **MJ** : Peuvent valider personnages et épisodes, organiser parties

### 2. Création de Personnages
- Allez dans l'onglet "Création Personnage"
- Remplissez le formulaire (nom, profession, date de naissance, univers)
- Cliquez sur "Proposer Personnage"
- Le personnage est créé avec statut PROPOSE
- Le MJ devra ensuite valider le personnage (peut lire la bio privée)

### 3. Gestion des Épisodes
- Dans l'onglet "Gestion Épisodes"
- Sélectionnez un personnage validé
- **Construction d'épisode multi-paragraphes** :
  - Saisissez la date de l'épisode (ex: "An 2", "Mois 5")
  - Écrivez le contenu d'un paragraphe dans la zone de texte
  - **Pour marquer un paragraphe SECRET** : Cochez "Marquer comme SECRET"
  - Cliquez "Ajouter ce Paragraphe" pour l'ajouter à l'épisode
  - Répétez pour ajouter plusieurs paragraphes
  - Utilisez "Retirer le Paragraphe Sélectionné" pour supprimer des paragraphes
- **Sauvegarde** : Cliquez "Sauvegarder Épisode comme Brouillon" pour enregistrer
- **Validation** : Cliquez "Valider l'Épisode" (nécessite validation joueur + MJ)
- **Liaison à aventure** : Après validation, peut lier à une aventure terminée
- **Affichage chronologique** : Les épisodes sont affichés triés par date
- **Permissions** : Le contenu secret n'est visible que par le propriétaire et le MJ

### 4. Gestion des Parties (Console uniquement)
- Utilisez PartieController pour :
  - Proposer une partie dans un univers
  - Ajouter des personnages validés du même univers
  - Finaliser la partie pour créer une aventure
  - Les aventures terminées peuvent être liées aux épisodes

### 5. Transferts (Console uniquement)
- Transfert de joueur : Changement de propriétaire du personnage
- Transfert de MJ : Demande d'un nouveau MJ avec acceptation requise
- Bloqué si le personnage participe à une partie non terminée

### 6. Consultation des Biographies
- Onglet "Biographie"
- Sélectionnez un personnage
- **Affichage chronologique des épisodes** : Les épisodes validés sont affichés triés par date
- **Permissions d'accès** :
  - **Visiteur** : Secrets complètement cachés
  - **Propriétaire/MJ** : Secrets visibles (marqués en bleu avec [SECRET])
  - **Contenu public** : Toujours visible en noir
- **Révélation de secrets** (panneau de droite) :
  - Fonction réservée au propriétaire du personnage
  - Sélectionnez un secret et cliquez "Révéler le Secret Sélectionné"
  - **Action irréversible** (confirmation simulée en console)

## Architecture MVC

L'application suit le pattern **Modèle-Vue-Contrôleur** :

- **Modèle** : Classes métier (Utilisateur, Personnage, Episode, etc.)
- **Vue** : Interface Swing (panneaux et composants graphiques)
- **Contrôleur** : Logique métier (PersonnageController, EpisodeController)

## Tests

L'application inclut des tests unitaires complets pour vérifier :
- La logique de validation double des épisodes
- Les permissions d'accès aux biographies (public/privé)
- L'immutabilité des épisodes validés et aventures terminées
- La gestion des personnages (PROPOSE/ACCEPTE)
- La gestion des parties et participants
- Les transferts de personnages et MJ
- Les liaisons épisode-aventure
- L'irréversibilité de la révélation des secrets



## Notes Techniques

- Stockage en mémoire : Pas de base de données, tout est gardé en RAM
- Interface française : Tous les labels sont en français
- Validation stricte : Les règles métier sont appliquées de manière rigoureuse
- Tests TDD : Développement piloté par les tests pour assurer la qualité

---

*Ce projet démontre les concepts de base du développement Java : MVC, tests unitaires, interface graphique, et gestion des permissions utilisateur.*
