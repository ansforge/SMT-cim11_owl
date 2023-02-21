# SMT-ICD11
This repository will contains all scripts used to convert and extract ICD-11 from OMS website to the SMT


## Streaming CIM11 sous format JSONLD<a id="streamingcim"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre à  jour les paramètres suivants : 

```java
# language selectionnée
cimLanguage = fr
# URL d'accès à  la API CIM11 en ligne
entityURI = https://id.who.int/icd/release/11/2023-01/mms
# racine et nom du fichier Ontology CIM11 sous format JSON en local
jsonFileName = D:\\cim11\\mms\\test\\CIM11_JSON_R202301_FR.json
# Paramètres sécurités d'acces à  la API CIM11
TOKEN_ENPOINT = https://icdaccessmanagement.who.int/connect/token
CLIENT_ID = 53f52cc7-32e7-41d7-a3ff-f1c0bcc2ad9c_3a1bab2f-57fb-4489-b322-87e59609db7c
CLIENT_SECRET = KsogkUqIYGfNNuV7WTtP8cH0lvx/DQv8tv0QQoUieI4=
SCOPE = icdapi_access
GRANT_TYPE = client_credentials
```

##### 2. Streaming de CIM11 sous format JSONLD

* Exécuter la commande `java -jar icd11-owl-version.jar streamICD` pour streamer CIM11 dans le fichier `jsonFileName`.

##  Convertir CIM11-JSONLD en format CIM11-RDF/SKOS<a id="jsontordfskos"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre à  jour les parameètres suivants : 

```java
# racine et nom du fichier Ontology CIM11 sous format SKOS
firstSkosFileName = D:\\cim11\\mms\\test\\CIM11-MMS-SKOS-R202301-FR-V1.xml
# racine et nom du fichier Ontology CIM11 sous format SKOS
skosFileName = D:\\cim11\\mms\\test\\CIM11-MMS-SKOS-R202301-FR.xml
# racine et nom du fichier Ontology CIM11 sous format JSON en local
jsonFileName = D:\\cim11\\mms\\test\\CIM11_JSON_R202301_FR.json
```

##### 2. Convertir CIM11-JSONLD en format CIM11-RDF/SKOS:

* Exécuter la commande `java -jar icd11-owl-version.jar json2rdf`.
* Le programme prend en entré le fichier `jsonFileName`. 
* En sortie, il y a deux fichiers:

    * `skosFileName` : CIM11 sous format RDF/SKOS.
    * `labelFileName` : CIM11 sous format RDF/SKOS.


## Convertir CIM11 en format OWL<a id="toowl"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre à  jour les parameètres suivants : 

```java
# racine et nom du fichier de mapping entre CIM11 et CIM10
mappingFileName = C:\\Users\\agochath\\Documents\\cim11\\11To10MapToOneCategory.xlsx
# racine et nom du fichier de label traduites en FranÃ§ais.
traductionFileName = C:\\Users\\agochath\\Documents\\cim11\\icd11-translations-01.xlsx
# racine et nom du fichier Ontology CIM11 sous format SKOS
skosFileName = C:\\Users\\agochath\\Documents\\cim11\\cim11SKOS-V0618.xml
# fichier Ontology CIM11 sous format OWL
owlFileName = C:\\Users\\agochath\\Documents\\cim11\\CIM11-V0618.owl
```

##### 2. Convertir CIM11 en format OWL:

* Exécuter la commande `java -jar OntoStream-version.jar skos2owl`.
* Le programme prend en entré trois fichiers:  

    * `skosFileName` : CIM11 sous format RDF/SKOS.
    * `traductionFileName` : tous les labels de CIM11 traduit en français.
    * `mappingFileName` : fichier de mapping entre cim11 et cim10.

* En sortie, il y a un fichier:

    * `owlFileName` : CIM11 sous format OWL.
  

