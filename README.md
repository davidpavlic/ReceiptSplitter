# Team01-LightningMcKings-Projekt2-ReceiptSplitter
## Beschreibung
Dieses Java Konsolen Programm startet bei Ausführung eine JavaFX Applikation um eine Quittung aufzuteilen.
Dies ist ein Software Projekt im Rahmen des Moduls Software-Projekt-2.

### Abhängigkeiten
Um das Programm auszuführen, benötigt es eine Java Umgebung.
Wir verwenden zudem untenstehende Version von Gradle als Build Tool und Dependency Management Tool.

* OpenJDK version 21 -> https://openjdk.org/projects/jdk/21/
* Gradle version 8.6 -> https://gradle.org/install/

## Anwendung starten
- Öffnen Sie ihre bevorzugte IDE (VSCode, IntelliJ, Eclipse etc.).
- Clonen Sie das Repository an einen Ort ihrer Wahl und öffnen Sie es in der IDE.
```
git clone https://github.zhaw.ch/PM2-IT23taZH-mach-muon-pasu/team01-lightningmckings-projekt1-racetrack.git
```
- Öffnen Sie das Projekt in ihrer IDE.
- Führen sie folgende Befehle aus um mit dem Gradle Wrapper die Anwendung zu "builden" und zu starten.
### Linux/MacOS
```
./gradlew build
./gradlew run
```
### Windows
```
.\gradlew build
.\gradlew run
```


## Unit Test ausführen
### Linux/MacOS
```
./gradlew test
```
### Windows
```
.\gradlew test
```

## Hilfestellung

- [VsCode für java Umgebung einrichten](https://code.visualstudio.com/docs/java/java-tutorial)
- [IntelliJ für java Umgebung einrichten](https://www.jetbrains.com/help/idea/run-java-applications.html#run_application)
- [Eclipse für java Umgebung einrichten](https://www.golinuxcloud.com/set-up-java-with-eclipse-ide/#Write_and_compile_your_first_Java_program_using_Eclipse_IDE)

# Branching Modell
Wir verwenden den "Git Develop Branch Workflow", der eine Erweiterung des Feature-Branching-Workflows ist,
bei dem ein zweiter, langfristig angelegter Develop-Branch neben dem Master-Branch existiert.
Dies ermöglicht es, die Entwicklung von Funktionen durchzuführen und nur stabile Versionen in den Master-Branch zu überführen.
Es ist ein Mittelweg zwischen dem einfachen Feature-Branch und dem Hauptbranch "Master".

### Pull Requests
Für die Bearbeitung von Pull Requests (PRs) im Rahmen unseres Git-Develop-Workflows gilt:
- Jeder PR muss mindestens ein Approval von einem anderen Teammitglied erhalten.
- Teammitglieder sind angehalten, PRs gründlich zu prüfen und konstruktive Kommentare zu hinterlassen, falls Verbesserungspotenzial besteht.
- Ein Approval darf nur erteilt werden, wenn der Code sorgfältig geprüft wurde und als qualitativ hochwertig befunden wurde.
- Branches und PRs werden stets auf dem Develop-Branch erstellt (Ausnahme: hotfix branches).

### Branch Naming
- Feature Branches: `feat/<issue-number>-<short-description>`
- Bugfix Branches: `fix/<issue-number>-<short-description>`
- Hotfix Branches: `hotfix/<issue-number>-<short-description>`

## Überlegungen zu Struktur und Aufbau
TODO


## Autoren
Teamname: LightningMcKings

Namen der Mitwirkenden:
- David Pavlic
- Kaspar Streiff
- Ryan Simmonds
- Suhejl Asani

## Anhang
- [Klassendiagramm](docs/classdiagram/classdiagramm.png)
- [Äquivalenzklassen](docs/testing/equivalence_classes.md)

