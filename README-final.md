# Designretningslinjer
##Migrations
Migration-scriptet er lagt opp slik at Id og Title er de første kolonnene, og foreignkey
 er alltid den siste. Create table kjøres også alltid med "IF NOT EXISTS" slik at man 
 ikke får feilmelding dersom den allerede eksisterer.
 