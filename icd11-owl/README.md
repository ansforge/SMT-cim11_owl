# SMT-ICD11
This repository will contains all scripts used to convert and extract ICD-11 from OMS website to the SMT

##### Rq: Refaire tous les étapes avec modifications dans le fichier 
`configuration.properties` :
 ##### cimLanguage = fr 
  puis
 ##### cimLanguage = en


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
# racine et nom du fichier Ontology CIM11 sous format SKOS
skosFileName = D:\\cim11\\mms\\test\\CIM11-MMS-SKOS-R202301-FR.xml
# fichier Ontology CIM11 sous format OWL
owlFileName = D:\\cim11\\mms\\test\\CGTS_SEM_ICD11-MM-R202301-FR-V1.owl
# fichier Ontology CIM11 sous format OWL
owlFileName1 = D:\\cim11\\mms\\test\\CGTS_SEM_ICD11-MMS-R202301-FR-V0.owl
```

##### 2. Convertir CIM11 en format OWL:

* Exécuter la commande `java -jar icd11-owl-version.jar skos2owl`.
* Le programme prend en entré le fichier:  

    * `skosFileName` : CIM11 sous format RDF/SKOS.
    

* En sortie, il y a un fichier:

    * `owlFileName` : CIM11 sous format OWL.
    * `owlFileName1` : CIM11 sous format OWL.


## Modeling Ontology CIM11 <a id="toowl"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre à  jour les parameètres suivants : 

```java
# fichier Ontology CIM11 sous format OWL
owlFileName = D:\\cim11\\mms\\test\\CGTS_SEM_ICD11-MM-R202301-FR-V1.owl
# fichier Ontology CIM11 sous format OWL
owlModelingFileNameFR = D:\\cim11\\mms\\test\\CGTS_SEM_ICD11-MMS-R202301-FR-V2.owl
```

##### 2. Convertir CIM11 en format OWL:

* Exécuter la commande `java -jar icd11-owl-version.jar modelingMmsCim11`.
* Le programme prend en entré le fichier:  

    * `owlFileName` : CIM11 sous format OWL.
    

* En sortie, il y a un fichier:

    * `owlModelingFileNameFR` : CIM11 sous format OWL.


## Concatenation Ontology CIM11 FR et EN <a id="toowl"></a>

##### 1. Configurations:

Dans le fichier `configuration.properties`, mettre à  jour les parameètres suivants : 

```java
# fichier Ontology CIM11 en Francais sous format OWL générer à l'étape précedante 
owlModelingFileNameFR = D:\\cim11\\mms\\test\\CGTS_SEM_ICD11-MMS-R202301-FR-V2.owl
# fichier Ontology CIM11 en anglais sous format OWL générer à l'étape précedante
owlModelingFileNameEN = D:\\cim11\\mms\\test\\CGTS_SEM_ICD11-MMS-R202301-EN-V2.owl
# fichier Ontology CIM11 FR\EN
owlModelingFileNameEN_FR = D:\\cim11\\mms\\test\\CGTS_SEM_ICD11-MMS-R202202-EN-FR-V2.owl
```

##### 2. Convertir CIM11 en format OWL:

* Exécuter la commande `java -jar icd11-owl-version.jar concatenateCim11`.
  ##### Rq: modifier  IRI de owlModelingFileNameFR ou  owlModelingFileNameEN avant de lancer le script

* Le programme prend en entré le deux  fichier:  

    * `owlModelingFileNameFR` : CIM11 sous format OWL.
    * `owlModelingFileNameEN` : CIM11 sous format OWL.
    

* En sortie, il y a un fichier:

    * `owlModelingFileNameEN_FR` : CIM11 sous format OWL.
   


## Licence

Ce projet est mis à  disposition sous licence [LOv2 ](https://github.com/etalab/licence-ouverte/blob/master/LO.md) par l'ASIP Santé.
