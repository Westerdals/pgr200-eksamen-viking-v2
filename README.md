[![Build Status](https://travis-ci.com/Westerdals/pgr200-eksamen-viking-v2.svg?token=hcaAw9PzjH9pgNPyimyp&branch=master)](https://travis-ci.com/Westerdals/pgr200-eksamen-viking-v2)

# Kjøring

1. Oppdater innlevering.properties med database settings i root (Når mvn package blir kjørt vil properties automatisk flytte seg). 
2. Når du står i root, naviger til `cd database-main/target/` 
3. Kjør `java -jar server.jar`
4. I nytt terminalvindu kjører du `java -jar client.jar` med ønsket [kommando](#kommandoer)

Eksempel:

```
> mvn clean package
> mvn test
> cd database-main/target/
> java -jar server.jar
> java -jar client.jar
```

## Kommandoer

| Kommando | Handling | Respons |
| -------- | -------- | ------- |
| reset | resetter databasen, ikke over http | Database has been reset |
| insert talk mytalk mydesc oldTopic | legger inn en talk i databasen, dersom talk ikke eksisterer legges dette også inn | Successfully inserted mytalk with topic: oldTopic into conference_talks |
| insert talk without topic | legger til talken "without" uten en topic | Successfully inserted without into conference_talk |
| list talks | lister alle talks | 1 mytalk mydesc oldTopic 2 without topic null | 
| list topics | lister alle topics | 1 Science 2 Programming 3 Hacking 4 oldTopic |
| insert topic newTopic | legger inn en  topic i database | Successfully inserted newtopic into topic |
| insert topic oldtopic | legger inn en topic som allerede eksisterer | topic oldTopic already exists |
| list talks with oldTopic | lister talks med en gitt topic | 1 mytalk mydesc oldTopic |
| retrieve talk 1 | henter ut talk med id 1 | 1 mytalk mydesc oldTopic | 
| retrieve topic 1 | henter ut topic med id 1 | Science | 
| delete talk 1 | sletter talk med id 1 | Successfully deleted conference talk mytalk |
| update talk 1 title value | oppdatere en talk med en id som ikke eksisterer | The talk you tried to update does not exist |
| update talk 2 title value | oppdatere talk 2 title med "value" |  Successfully updated conference talk 2 with value in title |


# Egenvurdering
Gruppemedlemmer:
1. Marcus Jøsendal (josmar17)
2. Markus Archer Dreyer (dremar17)

I denne filen vil vi gå punktvis over de forskjellige aspektene ved prosjekteksamen i PRO200.
Dette er da tredjeparts-software som Git, Travis CI og Maven, arbeidsinnstats, tester og
selve tekniske implementasjonen.

Link til parprogrammeringsvideo https://www.youtube.com/watch?v=o_rxW60PzoU


## Kode og tester
Som oppgaven tilsier skal vi sende data til en socket, sende det videre til en serversocket,
sende det til databasen og sende en response tilbake til klienten. Vi mener vi har fått 
implementert alle stegene i denne prosessen på en god måte (noen steder bedre enn andre naturligvis).

### Klienten og HTTP
Vi starter med å ta input fra brukeren og sende dette videre til en klasse som bygger ett 
HTTP-request og en uri basert på hva brukeren skriver inn. Requesten sendes til serveren,
får en Http-Response som klienten får tilbake (på en formatert måte selfølgelig). Vi følte
vi gjorde dette på en god og funksjonell måte, og fikk implementert funksjonalitet for å
hente ut en enkel Talk, liste alle talks, liste alle talks med ett gitt topic, oppdatere
en eksisterende Talk og slette en talk. Vi føler derfor vi har greid å implementere såppas
mye funksjonalitet som gir klienten mye frihet over dataen som ligger i databasen. 

### Databasen
I databasen parser vi requestet og setter parametere basert på dette. Med disse parameterene
kaller vi på den riktige DAO-metoden slik at ønskelig operasjon blir utført. Klassen som
kaller på DAO-metodene kunne vært bedre strukturert. Vi så litt for sent at vi burde
ha implementert noe form for pattern som får koden til å se litt mer strukturert ut og at det
senere blir lettere å utvide den. Ett pluss med denne ArgumenReader-klassen er at den gir klienten
veldig god informasjon om hva som som eventuelt gikk bra/dårlig med requesten. Selve DAO-klassene
våre fungere veldig bra. Her har vi fått til generic list, retrieve, insert (!), update og delete. 
Vi føler at denne generiske implementasjonen løfter løsningen betraktelig, fordi den håndterer alt 
som blir "kastet" inn. 

### Testing
Vi har skrevet tester som tester alle klassene våre og noen har jo naturligvis bedre testdekning enn andre.
Vi har passet på å skrive tester til nesten alle delene i programmet vårt; alt fra URIBuilderen til databasen, slik
at det blir lettere å se hvor det faktisk har gått galt hvis en test ikke kjører. Vi mener selv at kvaliteten
på testene er god og tester programmet vårt på en god måte. Det ble til slutt 30 tester som ga oss 
83% testdekning (i følge Intellij), noe vi er er utrolig fornøyde med. 

### Git og Travis CI
Vi har brukt Git til versjonskontroll i dette prosjetet. Begge kunne dette fra før, og nå som vi er ferdig
med dette prosjektet har vi fått mye bedre kontroll på hvordan Git fungerer og hvordan bruke det på en god
måte. Vi har også tatt bruk i Travis CI. Dette var jo noe helt nytt, men følte det bidro til å hele tiden
se at alt kjørte slik det skulle. Vi hadde mye problemer å få buildet prosjektet i Travis, selv om alle
testene kjørte grønt lokalt. Dette viste seg å være et problem med H2 databasen. Etter utallige timer med
debugging fikk vi endelig ordnet det (Dette er hvorfor vi har så stygg Travis historikk). 

### Maven


### Sammarbeid og arbeidsmengde 
Sammarbeidet innad gruppen har fungert utrolig bra. Kunnskapsnivået til gruppemedlemmene er såppas likt at
ingen har "dratt" den andre igjennom hele prosjekt og vi har begge fått muligheten til å lære utrolig mye.
Arbeidsfordelingen har vært relativt 50/50. Markus har for det meste holdt på med Serveren og ArgumentReaderen
og fått Maven oppe å gå slik vi ville ha det. Marcus har hatt fokus på databasen, Uri-Builderen og testing.
Når vi skulle implementere ny funskjonalitet benyttet vi oss av parprogrammering slik at alt stemte i alle
ledd av løsningen. Vi har jobbet jenvt med eksamensoppgaven fra vi fikk arbeidskrav 2. Arbeidsmengden har
økt betraktelig den siste uken før innleveringen, hvor vi har sittet i snitt 12 timer på skolen om dagen. 
Dette resulterte jo i en oppgave vi er utrolig fornøyde med, noe som gjør alt slitet verdt det. 


## Sammendrag og karakter
Vi har utført alle punktene som oppgaveteksten nevner. Data overføres over sockets, inn i databasen og tilbake
til klienten. Samtidig som alle punktene har blitt oppfylt har vi fått til å implementere Travis CI,
delt inn prosjektet vårt i to Maven moduler, logging-framework og 83% test coverage. For innlevering 2
fikk vi tilbakemelding som fortalte oss at dette var en sterk besvarelse som hadde holdt til en A hvis noen
problemer ble fikset. Disse problemene er nå fikset. Databasen er "satt sammen" med Http/Socket
funksjonaliteten og vi har fått gjort noen forbedringer her og der. Det er noen ting som burde ha vært 
refaktorert/fikset, som å refaktorere arugementreader og få til at klienten kan bruke mellomrom for å legge
inn talk/description/topic, men unntatt det mener jeg vi har fått til en veldig solid løsning med ekstra utover
det som er forventet. Vi innfører alle kravene som settes for en B karakter, i tillegg har vi generisk database, 
høy testdekning, en velfungerende HTTP server, god logging, builder i Travis og mye funskjonell programmering.
Vi mener derfor vi fortjener en A. 
