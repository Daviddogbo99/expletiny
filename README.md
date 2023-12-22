TinyPet - 2023
============================

## Description
TinyPet est un projet effectué dans le cadre du cours de Données massives et cloud. Cette application web nous permet de créer, signer des pétitions en étant identifier avec son compte Google. L'application affiche la liste des pétitions signés, et le top 100 des pétitions.

## Auteurs : 
ARGA Théo
BROSSARD Victor
FISHER Daniel

## Exécution de l'application
### Setup

    gcloud init
    gcloud auth application-default login
### Maven
#### Running locally

    mvn appengine:run
#### Deploying

    mvn clean package appengine:deploy
# Rendu :

Url de la page web [`https://tinypet2023-fba.ey.r.appspot.com/`](https://tinypet2023-fba.ey.r.appspot.com/)

## Kinds :
### Pétition :
![Kind pétition](https://gitlab.univ-nantes.fr/E203173Q/projet-tinypet-2023/-/blob/main/img/firefox_G7fvHLujeR.png)
Les pétitions ont un nom, un corps, une date, un créateur, une liste de tag et un nombre de signatures.

### Signataires :
![Kind Signataires](https://gitlab.univ-nantes.fr/E203173Q/projet-tinypet-2023/-/blob/main/img/firefox_G7fvHLujeR.png)
Les signataires ont 10 listes de signataires, permettant un grand nombre de signatures.
Elles sont liées aux pétitions par lien de parentée.

## Fonctionnement du projet : 

L'api du backend est fonctionelle, et permet de :
- Creer une pétition (avec ou sans login) en fournissant le nom et le corps
- Obtenir une pétition par nom
- Signer une pétition (avec ou sans login), avec vérification, pas de double signature, et transaction pour assurer ne pas pouvoir signer plus q'une fois en signant tres vite.
- Obtenir une liste des 100 pétitions les plus signées
- Obtenir une liste des personnes ayant signer une Petition
- Obtenir toutes les pétitions signées par un User.

Il est possible d'intéragir avec l'API [ici](https://apis-explorer.appspot.com/apis-explorer/?base=https://tinypet2023-fba.appspot.com/_ah/api&root=https://tinypet2023-fba.appspot.com/_ah/api#p/).  

Le frontend n'est pas complet.



