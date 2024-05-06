# Testkonzept

## Einleitung
Das Testkonzept basiert auf der Nutzung von Äquivalenzklassentests, um sicherzustellen, dass die Methoden in verschiedenen Szenarien wie erwartet funktionieren.
Durch das Testen repräsentativer Instanzen aus jeder Klasse kann die Funktionsweise der Applikation effektiv überprüft werden, ohne jeden möglichen Eingabewert einzeln zu testen.

Die ReceiptProcessor-Klasse wurde ausgewählt, weil ihre calculateDebtByContact-Methode kritisch für die Ablauflogik ist, indem sie berechnet, wie viel jeder Kontakt basierend auf den geteilten Belegen schuldet.
Diese Methode muss mit gültigen und ungültigen Daten gründlich getestet werden, um ihre Robustheit zu gewährleisten.

Die Router-Klasse regelt die Navigation innerhalb der Applikation.
Das Testen der gotoScene-Methode ist wesentlich, um sicherzustellen, dass die korrekten Ansichten geladen werden und keine unerwarteten Fehler auftreten, wenn ungültige oder null Enums übergeben werden.

Die ContactRepository-Klasse ist zentral für das Kontaktmanagement.
Es ist notwendig, das Hinzufügen und Entfernen von Kontakten mit verschiedenen E-Mail-Validierungsregeln zu testen, um die Integrität der Kontaktinformationen zu gewährleisten.

Schliesslich ist die ImageExtractor-Klasse verantwortlich für die Extraktion von Text aus Bildern, ein Schlüsselelement der Anwendung, das präzise funktionieren muss.
Verschiedene Bildszenarien müssen getestet werden, um die Zuverlässigkeit des OCR (Optical Character Recognition)-Prozesses zu garantieren.

Insgesamt helfen die Äquivalenzklassentests, sicherzustellen, dass die Methoden in erwarteter Weise reagieren und dass Ausnahmen korrekt gehandhabt werden..

# Testkonzept

## Äquivalenzklassen

### Klasse ReceiptProcessor

**Methode:** `calculateDebtByContact(contactItems: List<ContactItem>)`

#### Gültige Äquivalenzklassen
1. Kalkulation mit mindestens einem gültigen `ContactItem`.

#### Ungültige Äquivalenzklassen
2. Kalkulation mit einer leeren Liste.
3. Kalkulation mit invaliden `ContactItems` (negativer Betrag).
4. Kalkulation mit nicht existierenden `Contact` Referenzen.

#### Testtabellen
| Äquivalenzklasse | Methoden Parameter | Zustand vor Ausführung | Erwartetes Ergebnis |
|------------------|--------------------|------------------------|---------------------|
| 1: Gültige Liste | Gültige ContactItems | Gültige Contacts vorhanden | Korrekte Berechnung |
| 2: Leere Liste   | Leere Liste        | Keine Voraussetzung    | Fehlerauslösung     |
| 3: Ungültige Daten | Items mit negativem Betrag | Gültige Contacts vorhanden | Fehlerauslösung |
| 4: Nicht existierende Contacts | Items mit nicht existierenden Contacts | Keine Contacts vorhanden | Fehlerauslösung |

### Klasse Router

**Methode:** `gotoScene(enum: Enum)`

#### Gültige Äquivalenzklassen
1. Aufruf mit gültigem Enum.

#### Ungültige Äquivalenzklassen
2. Aufruf mit ungültigem Enum.
3. Aufruf mit `null` Enum.

#### Testtabellen
| Äquivalenzklasse | Methoden Parameter | Zustand vor Ausführung | Erwartetes Ergebnis |
|------------------|--------------------|------------------------|---------------------|
| 1: Gültiger Enum | Gültiges Enum      | Initialisierte sceneMap | Szene wird geladen  |
| 2: Ungültiger Enum | Ungültiges Enum  | Initialisierte sceneMap | Fehlerauslösung     |
| 3: Null Enum     | Null              | Beliebiger Zustand      | Fehlerauslösung     |

### Klasse ContactRepository

**Methode:** `addContact(name: String, email: String)`

#### Gültige Äquivalenzklassen
1. Hinzufügen mit gültigen Daten.

#### Ungültige Äquivalenzklassen
2. Hinzufügen mit ungültiger Email.
3. Mehrfacheintrag gleicher Daten.

#### Testtabellen
| Äquivalenzklasse | Methoden Parameter | Zustand vor Ausführung | Erwartetes Ergebnis |
|------------------|--------------------|------------------------|---------------------|
| 1: Gültige Daten | Gültiger Name und Email | Kein vorheriger Kontakt | Kontakt wird erstellt |
| 2: Ungültige Email | Gültiger Name, ungültige Email | Kein vorheriger Kontakt | Fehlerauslösung |
| 3: Duplikat | Gleicher Name und Email bereits vorhanden | Kontakt existiert | Fehlerauslösung |

### Klasse ImageExtractor

**Methode:** `extractOCR(lfile: File)`

#### Gültige Äquivalenzklassen
1. OCR mit lesbarem Text.
2. OCR ohne lesbaren Text.

#### Ungültige Äquivalenzklassen
3. OCR mit ungültigem Dateityp.
4. OCR mit leerer Datei.
5. OCR mit Datei über Größenlimit.

#### Testtabellen
| Äquivalenzklasse | Methoden Parameter | Zustand vor Ausführung | Erwartetes Ergebnis |
|------------------|--------------------|------------------------|---------------------|
| 1: Lesbarer Text | Gültige Bilddatei mit Text | Tesseract initialisiert | Text wird extrahiert |
| 2: Kein Text     | Gültige Bilddatei ohne Text | Tesseract initialisiert | Leerer String zurückgegeben |
| 3: Ungültiger Typ | Falscher Dateityp | Beliebiger Zustand | Fehlerauslösung |
| 4: Leere Datei   | Leere Datei        | Beliebiger Zustand | Fehlerauslösung |
| 5: Große Datei   | Datei > 10MB       | Tesseract initialisiert | Fehlerauslösung |
