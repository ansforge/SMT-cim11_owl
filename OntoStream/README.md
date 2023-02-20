# Tutoriel de prise en main de OntoStream

Dans ce tutoriel, nous verrons comment�:

* [Build de projet](#build)
* [Streaming CIM11 sous format JSONLD](#streamingcim)
* [Convertir CIM11-JSONLD en format CIM11-RDF/SKOS](#jsontordfskos)
* [Convertir CIM11 en format OWL](#toowl)

## Build projet<a id="build"></a>

* Ex�cuter la commande `mvn clean install` pour avoir le package `OntoStream-version-dist.zip`.

* En zipping ce package, il y a deux fichiers:

    * `OntoStream-version.jar` un ex�cutable java.
    * `configuration.properties` un fichier de configuration.


## Streaming CIM11 sous format JSONLD<a id="streamingcim"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre � jour les param�tres suivants : 

```java
# URL d'acc�s � la API CIM11 en ligne
entityURI = https://id.who.int/icd/entity
# racine et nom du fichier Ontology CIM11 sous format JSON en local
jsonFileName = C:\\Users\\agochath\\Documents\\cim11\\CIM11JSON-V0419.json
# Param�tres s�curit�s d'acces � la API CIM11
TOKEN_ENPOINT = https://icdaccessmanagement.who.int/connect/token
CLIENT_ID = 53f52cc7-32e7-41d7-a3ff-f1c0bcc2ad9c_3a1bab2f-57fb-4489-b322-87e59609db7c
CLIENT_SECRET = KsogkUqIYGfNNuV7WTtP8cH0lvx/DQv8tv0QQoUieI4=
SCOPE = icdapi_access
GRANT_TYPE = client_credentials
```

##### 2. Streaming de CIM11 sous format JSONLD

* Ex�cuter la commande `java -jar OntoStream-version.jar streamICD` pour streamer CIM11 dans le fichier `jsonFileName`.

##  Convertir CIM11-JSONLD en format CIM11-RDF/SKOS<a id="jsontordfskos"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre � jour les parame�tres suivants : 

```java
# racine et nom du fichier de label en Anglais.
labelFileName = C:\\Users\\agochath\\Documents\\cim11\\label-V0419.csv
# racine et nom du fichier Ontology CIM11 sous format SKOS
skosFileName = C:\\Users\\agochath\\Documents\\cim11\\cim11SKOS-V0618.xml
# racine et nom du fichier Ontology CIM11 sous format JSON en local
jsonFileName = C:\\Users\\agochath\\Documents\\cim11\\CIM11JSON-V0419.json
```

##### 2. Convertir CIM11-JSONLD en format CIM11-RDF/SKOS:

* Ex�cuter la commande `java -jar OntoStream-version.jar json2rdf`.
* Le programme prend en entr� le fichier `jsonFileName`. 
* En sortie, il y a deux fichiers:

    * `skosFileName` : CIM11 sous format RDF/SKOS.
    * `labelFileName` : tous les labels de CIM11 en anglais (� traduire en fran�ais).


## Convertir CIM11 en format OWL<a id="toowl"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre � jour les parame�tres suivants : 

```java
# racine et nom du fichier de mapping entre CIM11 et CIM10
mappingFileName = C:\\Users\\agochath\\Documents\\cim11\\11To10MapToOneCategory.xlsx
# racine et nom du fichier de label traduites en Français.
traductionFileName = C:\\Users\\agochath\\Documents\\cim11\\icd11-translations-01.xlsx
# racine et nom du fichier Ontology CIM11 sous format SKOS
skosFileName = C:\\Users\\agochath\\Documents\\cim11\\cim11SKOS-V0618.xml
# fichier Ontology CIM11 sous format OWL
owlFileName = C:\\Users\\agochath\\Documents\\cim11\\CIM11-V0618.owl
```

##### 2. Convertir CIM11 en format OWL:

* Ex�cuter la commande `java -jar OntoStream-version.jar skos2owl`.
* Le programme prend en entr� trois fichiers:  

    * `skosFileName` : CIM11 sous format RDF/SKOS.
    * `traductionFileName` : tous les labels de CIM11 traduit en fran�ais.
    * `mappingFileName` : fichier de mapping entre cim11 et cim10.

* En sortie, il y a un fichier:

    * `owlFileName` : CIM11 sous format OWL.
   


## Licence

Ce projet est mis � disposition sous licence [LOv2 ](https://github.com/etalab/licence-ouverte/blob/master/LO.md) par l'ASIP Sant�.
