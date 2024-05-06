# Testkonzept

## Einleitung
Das Testkonzept basiert auf der Nutzung von Äquivalenzklassentests, um sicherzustellen, dass die Methoden in verschiedenen Szenarien wie erwartet funktionieren.
Durch das Testen repräsentativer Instanzen aus jeder Klasse kann die Funktionsweise der Applikation effektiv überprüft werden, ohne jeden möglichen Eingabewert einzeln zu testen.

Die ReceiptProcessor-Klasse wurde ausgewählt, weil ihre calculateDebtByContact-Methode kritisch für die Ablauflogik ist, indem sie berechnet, wie viel jeder Kontakt basierend auf den geteilten Belegen schuldet.
Diese Methode muss mit gültigen und ungültigen Daten gründlich getestet werden, um ihre Robustheit zu gewährleisten. Eine Äquivalenzklasse, welche wir anfangs definiert hatten, fiel hier weg: Ungültige Daten.
Dieses Szenario kann nicht getestet werden, da die Erstellung von Items, die bereits negative Beträge enthalten, aufgrund von Validierungsregeln im System nicht möglich ist. 
Das System verhindert die Erstellung solcher Items, wodurch dieser Testfall nicht ausführbar ist. Stattdessen haben wir Tests für das ReceiptItem sowie ContactReceiptItem hinzugefügt.

Die Router-Klasse regelt die Navigation innerhalb der Applikation.
Das Testen der gotoScene-Methode ist wesentlich, um sicherzustellen, dass die korrekten Ansichten geladen werden und keine unerwarteten Fehler auftreten, wenn ungültige oder null Enums übergeben werden.

Die ContactRepository-Klasse ist zentral für das Kontaktmanagement.
Es ist notwendig, das Hinzufügen und Entfernen von Kontakten mit verschiedenen E-Mail-Validierungsregeln zu testen, um die Integrität der Kontaktinformationen zu gewährleisten.

Schliesslich ist die ImageExtractor-Klasse verantwortlich für die Extraktion von Text aus Bildern. Da wir dafür eine externe Library verwenden, haben wir auf das Testing dieser Klasse verzichtet.

Insgesamt helfen die Äquivalenzklassentests, sicherzustellen, dass die Methoden in erwarteter Weise reagieren und dass Ausnahmen korrekt gehandhabt werden..

# Testkonzept

## Äquivalenzklassen

### Klasse ReceiptItem

**Methoden:** `Konstruktor, ungültige Parameter durch setPrice, setName, setAmount`

### Gültige Äquivalenzklassen
1. Kalkulation mit gültigen Attributen (Preis, Name, Anzahl).

#### Ungültige Äquivalenzklassen
2.  Aufruf mit negativem Preis.
3. Aufruf mit ungültigem Namen.
4. Aufruf mit ungültiger Anzahl.


#### Testtabellen
| Äquivalenzklasse               | Name Testmethode    | Methoden Parameter | Zustand vor Ausführung     | Erwartetes Ergebnis |
|--------------------------------|-----|--------------|----------------------------|---------------------|
| 1: Gültige Attribute           |   constructor_ValidAttributes_ItemCreated()   | Positiver Preis, gültiger Name, positive Anzahl | Keine Voraussetzung | Objekt wird korrekt erstellt |
| 2: Negativer Preis             |  setPrice_NegativePrice_ThrowsException| Negativer Preis | Gültige Anfangswerte       | Wirft Exception     |
| 3: Ungültiger Name |       setName_InvalidName_ThrowsException()        | Ungültiger Name | Gültige Anfangswerte      |    Wirft Exception                     |
| 4: Ungültige Anzahl            |                              setAmount_InvalidAmount_ThrowsException                      |     Ungültige Anzahl            |             Gültige Anfangswerte              |          Wirft Exception                                    |


### Klasse ContactReceiptItem

**Methoden:** `Konstruktor, ungültige Parameter durch setPrice, setName, setAmount`

### Gültige Äquivalenzklassen
1. Kalkulation mit gültigen Attributen (Preis, Name, Anzahl).

#### Ungültige Äquivalenzklassen
2.  Aufruf mit negativem Preis.
3. Aufruf mit ungültigem Namen.
4. Aufruf mit ungültigem Kontakt.


#### Testtabellen
| Äquivalenzklasse      | Name Testmethode    | Methoden Parameter                               | Zustand vor Ausführung     | Erwartetes Ergebnis |
|-----------------------|-----|--------------------------------------------------|----------------------------|---------------------|
| 1: Gültige Attribute  |   constructor_ValidAttributes_ItemCreated()   | Positiver Preis, gültiger Name, gültiger Kontakt | Keine Voraussetzung | Objekt wird korrekt erstellt |
| 2: Negativer Preis    |  setPrice_NegativePrice_ThrowsException| Negativer Preis                                  | Gültige Anfangswerte       | Wirft Exception     |
| 3: Ungültiger Name    |       setName_InvalidName_ThrowsException()        | Ungültiger Name                                  | Gültige Anfangswerte      |    Wirft Exception                     |
| 4: Ungültiger Kontakt |                            setContact_InvalidContact_ThrowsException                  | Kontakt ist 'null'                                |             Gültige Anfangswerte              |          Wirft Exception                                    |


### Klasse ReceiptProcessor

**Methode:** `calculateDebtByContact(contactItems: List<ContactItem>)`

#### Gültige Äquivalenzklassen
1. Kalkulation mit mindestens einem gültigen `ContactItem`.

#### Ungültige Äquivalenzklassen
2. Kalkulation mit einer leeren Liste.


#### Testtabellen
| Äquivalenzklasse               | Name Testmethode    | Methoden Parameter   | Zustand vor Ausführung     | Erwartetes Ergebnis |
|--------------------------------|-----|----------------------|----------------------------|---------------------|
| 1: Gültige Liste               |   calculateDebtByPerson_ValidInput_CalculationValid()   | Gültige ContactItems | Gültige Contacts vorhanden | Korrekte Berechnung |
| 2: Leere Liste                 |  calculateDebtByPerson_EmptyList_ThrowException()   | Leere Liste          | Keine Voraussetzung        | Wirft Exception     |
| 3: Nicht-existierender Kontakt |       calculateDebtByPerson_NonExistingContact_ThrowException()        | Gültige ContactItems | Ungültiger Contact         |    Wirft Exception                     |


### Klasse Router

**Methode:** `gotoScene(enum: Enum)`

#### Gültige Äquivalenzklassen
1. Aufruf mit gültigem Enum.

#### Ungültige Äquivalenzklassen
2. Aufruf mit ungültigem Enum.
3. Aufruf mit `null` Enum.

#### Testtabellen
| Äquivalenzklasse | Name Testmethode                                   | Methoden Parameter | Zustand vor Ausführung | Erwartetes Ergebnis |
|------------------|----------------------------------------------------|--------------------|------------------------|---------------------|
| 1: Gültiger Enum | gotoScene_ValidPage_SceneSwitched(Pages validPage) | Gültiges Enum      | Initialisierte sceneMap | Szene wird geladen  |
| 2: Ungültiger Enum | gotoScene_PageWithoutLastPage_ThrowsException()    | Ungültiges Enum  | Initialisierte sceneMap | Wirft Exception     |
| 3: Null Enum     | gotoScene_NullPage_ThrowsException()               | Null              | Beliebiger Zustand      | Wirft Exception     |


### Klasse ContactRepository

**Methode:** `addContact(name: String, email: String)`

#### Gültige Äquivalenzklassen
1. Hinzufügen mit gültigen Daten.

#### Ungültige Äquivalenzklassen
2. Hinzufügen mit ungültiger Email.
3. Mehrfacheintrag gleicher Daten.

#### Testtabellen
| Äquivalenzklasse | Name Testmethode                            | Methoden Parameter | Zustand vor Ausführung | Erwartetes Ergebnis |
|------------------|---------------------------------------------|--------------------|------------------------|---------------------|
| 1: Gültige Daten | addContact_ValidAttributes_ContactCreated() | Gültiger Name und Email | Kein vorheriger Kontakt | Kontakt wird erstellt |
| 2: Ungültige Email | ===                                         | Gültiger Name, ungültige Email | Kein vorheriger Kontakt | Fehlerauslösung |
| 3: Duplikat | ====                                        | Gleicher Name und Email bereits vorhanden | Kontakt existiert | Fehlerauslösung |



