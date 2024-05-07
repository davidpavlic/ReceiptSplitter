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
git clone https://github.zhaw.ch/PM2-IT23taZH-mach-muon-pasu/Team01-LightningMcKings-Projekt2-ReceiptSplitter.git
```
- Öffnen Sie das Projekt in ihrer IDE.
- Kopieren Sie die Datei `local.gradle.properties` ins Projektverzeichnis als `gradle.properties` und fügen Sie die Werte der Env vars zu. 
- Kopieren Sie die Datei `local.contacts.csv`ins Verzeichnis `app` und bennen Sie es als `contacts.csv`. 
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

# Known Issues
## Email Versand
Der Email Versand funktioniert nicht in jedem Netzwerk. Manchmal kann es dazu kommen, dass die Firewall des Gerätes oder des Netzwerks den Versand blockiert.
Wir haben ein Timeout von 20 Sekunden gesetzt, falls der Versand nicht funktioniert. In diesem Fall wird eine Fehlermeldung ausgegeben.
Im ZHAW Netzwerk funktioniert der Versand jedoch. Dies wurde bei allen Authoren dieses Projekts getestet.

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

#### Beispiele für PRs
1. Feat/26-List-Items-View-Implementation: https://github.zhaw.ch/PM2-IT23taZH-mach-muon-pasu/Team01-LightningMcKings-Projekt2-ReceiptSplitter/pull/61
2. Feat/38-Receipt-Processor-Creation: https://github.zhaw.ch/PM2-IT23taZH-mach-muon-pasu/Team01-LightningMcKings-Projekt2-ReceiptSplitter/pull/52

### Branch Naming
- Feature Branches: `feat/<issue-number>-<short-description>`
- Infrastructure Branches: `infra/<issue-number>-<short-description>`
- Bugfix Branches: `fix/<issue-number>-<short-description>`
- Hotfix Branches: `hotfix/<issue-number>-<short-description>`

## Überlegungen zu Struktur und Aufbau


### Grundsätze und Designentscheidungen
Durch die Unterteilung in Pakete ist auf einen Blick erkennbar, wo bestimmte Funktionalitäten implementiert sind.
Dies erleichtert nicht nur neue Entwickler im Projekt, sich zurechtzufinden, sondern vereinfacht auch die Wartung und das Testen des Codes.
Die modulare Natur der Struktur ermöglicht es, Teile der Anwendung unabhängig voneinander zu skalieren oder zu verbessern.
Beispielsweise können neue Services oder Controller hinzugefügt werden, ohne bestehende Funktionalitäten zu stören.
Die klare Trennung der Benutzeroberflächendefinitionen (.fxml-Dateien) von der Logik (.java) erleichtert die Arbeit von Entwicklern,
die parallel an diesen arbeiten können, ohne sich gegenseitig zu beeinflussen.

### Dependency Injection
Das Konzept der Dependency Injection (DI) wird verwendet, um die Abhängigkeiten zwischen den verschiedenen Komponenten Ihrer Anwendung zu verwalten und zu minimieren. 
Wir haben uns für DI und gegen Singletons oder Factory Pattern entschieden, da DI die Flexibilität und Testbarkeit des Codes erhöht.
In der start-Methode der Main-Klasse werden so die Repositories ContactRepository und ReceiptProcessor wie auch der Router initialisiert.

#### Instanziierung von Abhängigkeiten
ContactRepository wird mit dem Pfad zur CSV-Datei initialisiert. Dies entkoppelt die Datenquelle vom ContactRepository, 
da die Datei durch eine andere ersetzt werden kann, ohne das Repository direkt zu modifizieren.

ReceiptProcessor wird als neue Instanz erstellt, was zeigt, dass es eigenständig funktioniert, 
aber verwendet wird, um mit anderen Teilen der Anwendung zusammenzuarbeiten.

Der Router erhält seine Abhängigkeiten (stage, contactRepository, receiptProcessor) über seinen Konstruktor und kann diese so an die Controller weitergeben. 
Dies erleichtert das Testen, da Mock- oder Stub-Objekte während des Testens leicht eingesetzt werden können.


### Implementation des MVC-Patterns
Diese Trennung der Verantwortlichkeiten innerhalb des MVC-Designs fördert die Wiederverwendbarkeit und die Unabhängigkeit der Komponenten, 
erleichtert das Testen und die Wartung der Anwendung und ermöglicht eine klarere Modularisierung der Funktionalität. 
Die Kommunikation zwischen den Komponenten wird durch die Implementation des Observer Pattern sichergestellt.

#### Model
Die model-Komponente beinhaltet Klassen wie Contact, Receipt, und ReceiptItem. Diese Klassen repräsentieren die Datenstruktur und die Logik der Anwendung. 
Sie halten die Daten und definieren Methoden, die zur Manipulation dieser Daten verwendet werden. 
Beispielsweise implementiert Contact Methoden, um Kontaktinformationen zu speichern und abzurufen, 
während Receipt und ReceiptItem die Logik für die einzelnen Quittungselemente zur Verfügung stellen

#### View
Die resources-Komponente, insbesondere der Unterordner pages, enthält alle FXML-Dateien wie MainWindow.fxml, Login.fxml, und NewContact.fxml. 
Diese FXML-Dateien definieren die Benutzeroberfläche der Anwendung. Sie beschreiben, wie verschiedene GUI-Komponenten angeordnet sind und wie sie aussehen, 
was den Views in einem typischen MVC-Pattern entspricht.

#### Controller
Die controller-Komponente beinhaltet Klassen wie MainWindowController, LoginController, und NewContactController. 
Jeder Controller ist für die Verarbeitung der Benutzerinteraktionen, die Steuerung der Anwendungslogik und die Aktualisierung der View zuständig. 
Die Controller lauschen auf Aktionen des Benutzers (z.B. Button-Klicks), manipulieren Model-Daten entsprechend und aktualisieren die Views.


### Ordnerstruktur und Packages
Die Anwendung wurde in verschiedene Pakete unterteilt, um die Trennung von Anliegen zu gewährleisten. Unser System basiert auf der Idee, Klassen nach Verwendungszweck zu gruppieren. 
Dementsprechend findet sich bspw. das Observer Interface nicht im interfaces-package. Übersichtlichkeit und Wartbarkeit sind die Hauptziele dieser Struktur.

- **interfaces**: Enthält Interfaces, die bestimmte Verhaltensweisen definieren, die von anderen Klassen implementiert werden können. 
Dies fördert die lose Kopplung und macht den Code flexibler und einfacher zu warten.

- **controller**: Hier sind alle Controller-Klassen untergebracht, die für die Verarbeitung der Benutzerinteraktion und die 
Verbindung zwischen der Benutzeroberfläche und dem Modell verantwortlich sind.

- **model**: Dieses Verzeichnis enthält Klassen, die die Datenstrukturen  der Anwendung repräsentieren. 
Diese Trennung sorgt dafür, dass das Modell unabhängig von der Benutzeroberfläche entwickelt und verändert werden kann.

- **repository**: Hier befinden sich Klassen, die den Zugriff auf Datenquellen und die Datenverwaltung für die Anwendung bereitstellen.

- **service**: Die Service-Schicht implementiert Logik, die sich über mehrere Modelle oder Bereiche erstreckt 
und zentrale Funktionen für die Anwendung koordiniert.

- **util**: Enthält Hilfsklassen und -funktionen, die in verschiedenen Teilen der Anwendung verwendet werden können. Ein Beispiel hierfüre wäre ContactDropdown, welches in den Controllern mehrmals verwendet wird.
- 
-**enums**: Enums, die in der Anwendung verwendet werden, finden sich in diesem Package.

- **pages**: Enthält die FXML-Dateien und Controller-Klassen für die verschiedenen Seiten der JavaFX-Anwendung/ des GUI.

-**receipts**: Quittungen können hier abgelegt werden.




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

