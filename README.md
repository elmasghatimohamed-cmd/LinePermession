# LinePermission

LinePermission est une application Java en ligne de commande pour gérer des fichiers protégés avec des droits d'accès. Elle permet à des utilisateurs de créer un compte, se connecter, manipuler des fichiers et consulter des statistiques sur les activités enregistrées.

## Fonctionnalités

- Création de compte et connexion utilisateur
- Déconnexion et gestion de session
- Création, liste, lecture, écriture et suppression de fichiers
- Contrôle d'accès par propriétaire et autres utilisateurs
- Gestion des droits avec permissions de lecture, écriture et suppression
- Journal d'activités et statistiques d'accès

## Structure du projet

- `src/main/java/ma/youcode/lineperm` : code source principal
- `src/main/java/.../model` : modèles utilisateur et fichiers
- `src/main/java/.../service` : logique de fichiers et utilisateurs
- `src/main/java/.../access` : contrôle d'accès
- `src/main/java/.../log` : gestion des logs et statistiques
- `src/main/java/.../ui` : interface console
- `resources/` : fichiers de données persistants

## Prérequis

- Java JDK 8 ou plus récent
- Un terminal (PowerShell, CMD ou Bash)

## Exécution

Depuis la racine du projet, compilez puis lancez l'application :

### Windows PowerShell

```powershell
javac -d target/classes (Get-ChildItem -Path src/main/java -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
java -cp target/classes ma.youcode.lineperm.Main
```

## Commandes disponibles

Une fois lancé, l'application propose ces commandes :

- `signup` : créer un compte
- `login` : se connecter
- `logout` : se déconnecter
- `ls` : lister les fichiers
- `touch <nom>` : créer un fichier
- `cat <nom>` : afficher le contenu d'un fichier
- `nano <nom>` : modifier le contenu d'un fichier
- `chmod <droits> <nom>` : modifier les droits d'un fichier
- `rm <nom>` : supprimer un fichier
- `stats` : afficher les statistiques des logs
- `exit` : quitter l'application

## Exemple de droits

Les fichiers sont protégés par des droits pour le propriétaire et les autres utilisateurs.

- `r` : lecture
- `w` : écriture
- `d` : suppression

Exemple :

```text
chmod +r fichier.txt
chmod -w fichier.txt
```

## Notes

Les données sont enregistrées dans les fichiers du dossier `resources/`, ce qui permet de conserver l'état des utilisateurs et des fichiers entre plusieurs exécutions.

## Auteur

Projet Java réalisé dans le cadre de l'apprentissage de la gestion des droits d'accès et de la manipulation de fichiers en console.
