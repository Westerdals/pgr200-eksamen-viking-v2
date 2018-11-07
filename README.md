[![Build Status](https://travis-ci.com/Westerdals/pgr200-eksamen-viking-v2.svg?token=hcaAw9PzjH9pgNPyimyp&branch=master)](https://travis-ci.com/Westerdals/pgr200-eksamen-viking-v2)
# Generell Informasjon
* Marcus Jøsendal (josmar17) og Markus Dreyer (dremar17)
* Vi har jobbet godt sammen som gruppe og kommer til å jobbe videre sammen til den endelig innleveringen.
* Youtube-link til parprogrammeringsvideoen: https://www.youtube.com/watch?v=o_rxW60PzoU
* Vi har bare to tabeller, så vi følte ikke det var nødvendig å oprette et databasediagram i denne innleveringen. 
# Kjøring
## Maven
1. `mvn test`
2. `mvn clean package`

For å kjøre main-metoden, naviger til `cd database-main/target/`. Når du står i target mappen kan du kjøre `java -jar database-main-0.0.1-SNAPSHOT.jar`
med følgende kommandoer: 

Eksempel: `java -jar database-main-0.0.1-SNAPSHOT.jar list talks` (lister alle talks)


## Kommandoer:
* `reset` - resetter databasen (flyway.clean og flyway.migrate)
* `insert talk Hacks hackerskills` - Legger til en talk med tittel og beskrivelse
* `insert talk “Helvetes Hacking” “Hvordan å hacke” Hacking` - Legger til en talk med tittel, beskrivelse og topic 
(Dersom topic ikke eksisterer i Topic-tabellen, vil den også bli insertet der før Talken insertes)
* `list talk with topic Hacking` - Lister alle talks med Hacking som topic
* `list talks` - lister alle talks
* `list topics` - lister alle topics
* `retrieve topic 1` - lister topic med id 1
* `retrieve talk 1` - lister topic med id 1


# Designretningslinjer
##Migrations
Migration-scriptet er lagt opp slik at Id og Title er de første kolonnene, og foreignkey
 er alltid den siste. Create table kjøres også alltid med "IF NOT EXISTS" slik at man 
 ikke får feilmelding dersom den allerede eksisterer.
 
 For enkelthets skyld legges det til tre Topics i Topic tabellen slik at man har noen kolonner å teste med:
  * Science
  * Programming
  * Hacking
  
#Tilbakemeldinger
##Til annen gruppe
* Gruppen vi ga tilbakemelding til bestå av Solveig og Mats.
* Veldig god måte å kjøre programmet på. Oversiktlig for brukeren. Printer til brukeren på en fin måte.  
* Mangler at man kan opprettet en talk med et Topic.
* Bra at dere har mulighet til å ha flere Topics på en Talk og at man kan fjerne Topics.
* Bra at programmet kjører helt til brukeren manuelt avslutter det. 
* Bra at man kan slette ett object, men denne burde kanskje returnere en Boolean. Det samme gjelder Insert metoden. 

##Tilbakemelding på vår løsning
* Gi tilbakemelding til bruker når man resetter databasen.
* Fin tabell for å skrive ut av resultater av de forskjellige kommandoene.
* Bra generiske metoder.
* Kanskje implemetere for å fjerne Topic, legge til Topic til en eksisterende talk og kasnkje bytte Topic på en eksisterende talk. 
* Ryddig kode som har med mye som ikke er obligatorisk.
* Kanskje finne en måte å handle User input på en annen måte, ble kanskje litt mye kode. 
* Burde ha to separate Migrations filer for de forskjellige tabellene. 