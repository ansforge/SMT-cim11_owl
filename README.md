# CIM-11-MMS en OWL

Le répertoire contient les scripts (java) pour extraire et convertir la [CIM-11 MMS](https://icd.who.int/browse11/l-m/fr#/http%3a%2f%2fid.who.int%2ficd%2fentity%2f1136813326) à partir des [APIs de l'OMS](https://icd.who.int/icdapi) .

La création du fichier de la CIM-11-MMS passe par 4 étapes (qui va inclure le français et l'anglais) : 


1. Extraire la CIM-11 au format [JSON-LD](https://json-ld.org/) depuis l'API OMS.
2. Convertir la CIM-11 du format JSON-LD vers le format [RDF/SKOS](https://www.w3.org/TR/skos-reference/).
3. Génrer le fichier en [OWL](https://www.w3.org/OWL/), qui est le format utilisé sur le [SMT](https://smt.esante.gouv.fr/).
4. Concaténer les deux versions en français en anglais pour au final avoir un seule fichier OWL comme celui publié sur le SMT.





### Extraire la CIM-11 sous format JSON-LD ###

#### 1. Mettre à jours le fichier de configurations:

Dans le fichier `configuration.properties`, mettre à  jour les paramètres suivants : 


#### - le paramètre pour la langue : #### 

````
cimLanguage = fr
#cimLanguage = en 
````

#### - le paramètre pour l'URI d'accès à l'API de l'OMS : #### 

````
entityURI = https://id.who.int/icd/release/11/2023-01/mms

````

#### - le nom de fichier du fichier généré au format JSON-LD : #### 

````
jsonFileName = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_jsonld.json

````

#### - les paramètres de connexion à l'API : ####

```java
Paramètres sécurités d'acces à  la API CIM11

TOKEN_ENPOINT = https://icdaccessmanagement.who.int/connect/token
CLIENT_ID = ***
CLIENT_SECRET = ***
SCOPE = icdapi_access
GRANT_TYPE = client_credentials
```

#### 2. Lancer le script d'extraxtion :

`java -jar icd11-owl-version.jar streamICD`.


### Convertir le fichier JSON-LD vers RDF/SKOS ###

#### 1. Mettre à jours le fichier de configurations:

Dans le fichier `configuration.properties`, mettre à  jour les paramètres suivants : 

#### - racine et nom du fichier sous le format SKOS : #### 

````
skosFileName = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_skos.xml

````


#### 2. Lancer le script de conversion :

`java -jar icd11-owl-version.jar json2rdf`.


### Convertir SKOS en OWL ###


#### 1. Mettre à jours le fichier de configurations:

Dans le fichier `configuration.properties`, mettre à  jour les paramètres suivants : 

#### - racine et nom du fichier sous le format OWL : #### 

````
owlFileName = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl.owl

````


#### 2. Lancer le script de conversion :

`java -jar icd11-owl-version.jar skos2owl`.




### Mettre en forme le fichier OWL par rapport au modèle ###

#### 1. Mettre à jours le fichier de configurations:

Dans le fichier `configuration.properties`, mettre à  jour les paramètres suivants : 

#### - l'ontologie finale CIM11 sous format OWL : #### 

````
owlModelingFileNameFR = dossier_qui_contient_le_fichier/CGTS_SEM_ICD11-MMS-R202301-FR-V2.owl

````


#### 2. Lancer le script de conversion :

`java -jar icd11-owl-version.jar modelingMmsCim11`


### Concaténation des version en français et en anglais ###

#### 1. Mettre à jours le fichier de configurations:

Dans le fichier `configuration.properties`, mettre à  jour les paramètres suivants : 

#### - le fichier OWL en fr : #### 

````
owlModelingFileNameFR = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl_fr.owl

````

#### - le fichier OWL en en : #### 

````
owlModelingFileNameFR = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl_en.owl

````


#### 2. Lancer le script de concaténation:

`java -jar icd11-owl-version.jar concatenateCim11`
