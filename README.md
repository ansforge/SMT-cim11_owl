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
icd_language = fr 
````

#### - le paramètre pour l'URI d'accès à l'API de l'OMS : #### 

````
entityURI = https://id.who.int/icd/release/11/2023-01/mms

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
`java -jar icd11-owl.jar owlCim11Mms -l fr -o CIM11_FR.owl `.

````

````
`java -jar icd11-owl.jar owlCim11Mms -l en -o CIM11_EN.owl `.

````


### Concaténation des version en français et en anglais ###


#### 1. Lancer le script de concaténation:

#### Rq: modifier l'URI de l'ontology FR avant de lancer le script

`java -jar icd11-owl.jar concatenateCim11 -owl_fr CIM11_FR.owl -owl_en  CIM11_EN.owl -owl_complet CIM11_COMPLETE_FR_EN.owl  `
