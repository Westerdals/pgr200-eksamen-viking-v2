# Kjøring
1. `mvn test`
2. `mvn clean package`
3. `cd database-main/target/`

Inni target mappen kan du kjøre `java -jar database-main-0.0.1-SNAPSHOT.jar`
med følgende kommandoer: 

## Kommandoer:
* `reset` - resetter databasen (flyway.clean og flyway.migrate)
* `list talks` - lister alle talks
* `list topics` - lister alle topics
* `retrieve topic 1` - lister topic med id 1
* `retrieve talk 1` - lister topic med id 1
* `insert talk “Helvetes Hacking” “Hvordan å hacke” Hacking` - Legger til en talk med tittel, beskrivelse og topic
* `insert talk Hacks hackerskills` - Legger til en talk med tittel og beskrivelse

Eksempel: `java -jar database-main-0.0.1-SNAPSHOT.jar list talks` (lister alle talks)

# Designretningslinjer**
##Migrations
Migration-scriptet er lagt opp slik at Id og Title er de første kolonnene, og foreignkey
 er alltid den siste. Create table kjøres også alltid med "IF NOT EXISTS" slik at man 
 ikke får feilmelding dersom den allerede eksisterer.
 
 For enkelthets skyld legges det til tre Topics i Topic tabellen slik at man har noen kolonner å teste med:
  * Science
  * Programming
  * Hacking
 
 