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
 
 