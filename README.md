# Warbot-Online
Dépôt du projet Warbot Online

## Pré-requis
- Apache Maven

- TurtleKit doit être installé dans le dépôt maven local.

"""bash
mvn install:install-file -Dfile=turtlekit-3.0.0.4c.jar -DgroupId=edu -DartifactId=TurtleKit -Dversion=3.0.0.4c -Dpackaging=jar
"""

- Warbot doit être installé dans le dépôt maven local.

"""bash
git clone https://github.com/ProjectWarbot/Warbot.git
cd Warbot
mvn clean compile install
"""

### Comment lancer le site

Cloner le dépôt

"""bash
git clone https://github.com/ProjectWarbot/Warbot-Online.git
"""

Lancer le site

"""bash
mvn test tomcat7:run
"""
