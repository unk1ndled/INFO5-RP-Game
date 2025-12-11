# Application de Gestion de Rôles (Jeu de Rôle)

## Description du Projet

Ce projet est une application Java pour gérer les personnages et aventures dans un jeu de rôle. L'application permet aux joueurs de créer des personnages, aux maîtres de jeu (MJ) de valider les créations, et de gérer les biographies avec du contenu public et secret.

## Fonctionnalités Principales

### Pour les Joueurs
- Créer de nouveaux personnages avec nom, profession et date de naissance
- Écrire des épisodes dans la biographie de leurs personnages
- Valider leurs propres épisodes

### Pour les Maîtres de Jeu (MJ)
- Valider ou refuser les personnages créés par les joueurs
- Valider les épisodes écrits par les joueurs
- Accéder à tout le contenu (public et secret) des biographies

### Pour les Visiteurs
- Consulter les biographies publiques des personnages
- Accès limité : les contenus secrets ne sont pas visibles

### Gestion des Permissions
- **Contenu Public** : Visible par tout le monde
- **Contenu Secret** : Visible seulement par le propriétaire du personnage et le MJ
- **Validation Double** : Les épisodes nécessitent la validation du joueur ET du MJ

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

### 1. Sélection d'Utilisateur
- Commencez par sélectionner un utilisateur dans l'onglet "Sélection Utilisateur"
- **Visiteur** : Accès en lecture seule
- **Alice** : Joueuse (peut créer des personnages)
- **Abdel Raouf** : Joueur (peut créer des personnages)
- **Bob** : Maître de Jeu (peut valider les personnages)

### 2. Création de Personnages
- Allez dans l'onglet "Création Personnage"
- Remplissez le formulaire (nom, profession, date de naissance)
- Cliquez sur "Proposer Personnage"
- Le MJ devra ensuite valider le personnage

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
- **Affichage chronologique** : Les épisodes sont affichés triés par date
- **Permissions** : Le contenu secret n'est visible que par le propriétaire et le MJ

### 4. Consultation des Biographies
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
  - **Action irréversible** avec confirmation requise

## Architecture MVC

L'application suit le pattern **Modèle-Vue-Contrôleur** :

- **Modèle** : Classes métier (Utilisateur, Personnage, Episode, etc.)
- **Vue** : Interface Swing (panneaux et composants graphiques)
- **Contrôleur** : Logique métier (PersonnageController, EpisodeController)

## Tests

L'application inclut des tests unitaires pour vérifier :
- La logique de validation double des épisodes
- Les permissions d'accès aux biographies
- L'immutabilité des épisodes validés



## Notes Techniques

- Stockage en mémoire : Pas de base de données, tout est gardé en RAM
- Interface française : Tous les labels sont en français
- Validation stricte : Les règles métier sont appliquées de manière rigoureuse
- Tests TDD : Développement piloté par les tests pour assurer la qualité

---

*Ce projet démontre les concepts de base du développement Java : MVC, tests unitaires, interface graphique, et gestion des permissions utilisateur.*
