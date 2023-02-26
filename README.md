# CIM-11-MMS en OWL

Le répertoire contient les scripts (java) pour extraire et convertir la [CIM-11 MMS](https://icd.who.int/browse11/l-m/fr#/http%3a%2f%2fid.who.int%2ficd%2fentity%2f1136813326) à partir des [APIs de l'OMS](https://icd.who.int/icdapi) .

La création du fichier de la CIM-11-MMS passe par 4 étapes (qui va inclure le français et l'anglais) : 

1. Génrer le fichier en [OWL](https://www.w3.org/OWL/), qui est le format utilisé sur le [SMT](https://smt.esante.gouv.fr/).
2. Concaténer les deux versions en français en anglais pour au final avoir un seule fichier OWL comme celui publié sur le SMT.





### Génerer les deux fichier FR et EN en OWL ###

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
#### - racine et nom du fichier sous le format SKOS : #### 

````
skosFileName = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_skos.xml

````

#### - racine et nom du fichier temporaire sous le format SKOS : #### 

````
skosFileNameTmp = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_skos_tmp.xml

````

#### - racine et nom du fichier sous le format OWL : #### 

````
owlFileName = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl.owl

````

#### - racine et nom du fichier temporaire sous le format OWL : #### 

````
owlFileNameTmp = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl_tmp.owl

````

#### - l'ontologie finale CIM11 sous format OWL : (Optionel) #### 

````
owlModelingFileNameFR = dossier_qui_contient_le_fichier/CGTS_SEM_ICD11-MMS-R202301-FR-V2.owl

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

#### 2. Lancer le script de géneration du fichier :

````
`java -jar icd11-owl-version.jar owlCim11Mms -langue fr -output dossier_qui_contient_le_fichier/Cim11-mms-fr.owl `.

````

````
`java -jar icd11-owl-version.jar owlCim11Mms -langue en -output dossier_qui_contient_le_fichier/Cim11-mms-en.owl `.

````


### Concaténation des version en français et en anglais ###

#### 1. Mettre à jours le fichier de configurations:

Dans le fichier `configuration.properties`, mettre à  jour les paramètres suivants : 

#### - le fichier OWL en fr : (Optionel) #### 

````
owlModelingFileNameFR = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl_fr.owl

````

#### - le fichier OWL en en : (Optionel) #### 

````
owlModelingFileNameEN = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl_en.owl

````

#### - le fichier OWL temporaire en fr/en  #### 

````
owlModelingFileNameEN_FR = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl_fr_en_tmp.owl

````

#### - le fichier OWL  en fr/en (Optionel)  #### 

````
owlModelingFileNameEN_FR_2 = dossier_qui_contient_le_fichier/CIM11-MMS-au_format_owl_fr_en.owl

````


#### 2. Lancer le script de concaténation:

`java -jar icd11-owl-version.jar concatenateCim11`
