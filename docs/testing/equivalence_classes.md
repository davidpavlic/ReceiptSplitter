# Äquivalenzklassen

## Klasse ReceiptProcessor

Methode die getestet wird: `calculateDebtByContact(contactItems: List<ContactItem>)`

#### Gültige Äquivalenzklassen
1. Die Kalkulation wird mit einer Liste von ContactItems die mindestens ein Element beinhaltet, korrekt durchgeführt.

#### Ungültige Äquivalenzklassen
2. Die Kalkulation wird mit einer leeren Liste (null) von ContactItems durchgeführt.
3. Die Kalkulation wird mit einer Liste von invaliden ContactItems durchgeführt. (z.B. Betrag ist negativ)

| Äquivalenzklasse   | Methoden Parameter                                                          | Initialer Objekt Zustand                                                            | Erwartetes Ergebnis                                                                  |
|--------------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| 1: Gültige Liste   | Liste mit gültigen ContactItems                                             | Es sind gültige Contacts vorhanden und der Receipt ist als Instanz variabel gesetzt | Der Betrag wird korrekt berechnet und zurückgegeben                                  |
| 2: Leere Liste     | Leere List                                                                  | Any                                                                                 | Der Betrag wird nicht berechnet und es wird eine IllegalArgumentException aufgerufen |
| 3: Ungültige Liste | Liste mit ContactItems die einen negativen Betrag haben                     | Es sind gültige Contacts vorhanden und der Receipt ist als Instanz variabel gesetzt | Der Betrag wird nicht berechnet und es wird eine IllegalArgumentException aufgerufen |
| 3: Ungültige Liste | Liste mit ContactItems welches einen nicht existierenden Contact beinhalten | Es sind keine Contacts vorhanden und der Receipt ist als Instanz variabel gesetzt   | Der Betrag wirt nicht berechnet und es wird eine Custom Exception aufgerufen         |


---
#### Methode die getestet wird: `parseReceipt(ocrString: String)`
#### Gültige Äquivalenzklassen

1. Receipt Objet wird mit einem gültigen OCR String erzeugt und ist korrekt formatiert.
#### Ungültige Äquivalenzklassen
2. Receipt Objet wird mit einem String welches ungültigen Informationen beinhaltet, erzeugt.
3. Receipt Objet wird mit einem leeren String erzeugt.

| Äquivalenzklasse   | Methoden Parameter                                                      | Initialer Objekt Zustand | Erwartetes Ergebnis                             |
|--------------------|-------------------------------------------------------------------------|--------------------------|-------------------------------------------------|
| 1: Gültiger String | Gültiger OCR String die informationen einer Quittung beinhalten         | Any                      | Ein gültiges Receipt Objekt wird erzeugt        |
| 2: Invalid String  | String welches mit einem Random String generator erzeugt wurde          | Any                      | Es wird ein InvalidArgumentException aufgerufen |
| 2: Invalid String  | String welches ein nicht unterstütztes Format einer Quittung beinhaltet | Any                      | Es wird ein InvalidArgumentException aufgerufen |
| 3: Leerer String   | Leerer String                                                           | Any                      | Es wird ein InvalidArgumentException aufgerufen |

## Klasse Router

Methode die getestet wird: `gotoScene(enum: Enum)`

#### Gültige Äquivalenzklassen

1. Die Methode wird mit einem gültigen Enum aufgerufen und die richtige Scene wird instanziert.

#### Ungültige Äquivalenzklassen

2. Die Methode wird mit einem ungültigen Enum aufgerufen (Enum welches kein File verbunden hat).
3. Die Methode wird mit einem null Enum aufgerufen.

| Äquivalenzklasse   | Methoden Parameter                              | Initialer Objekt Zustand                           | Erwartetes Ergebnis                              |
|--------------------|-------------------------------------------------|----------------------------------------------------|--------------------------------------------------|
| 1: Gültiger Enum   | Enum welches eine Java FXML Datei verbunden hat | Stage und sceneMap mit gültigen Werten instanziert | Die richtige Scene wird instanziert              |
| 2: Ungültiger Enum | Enum welches kein File verbunden hat            | Stage und sceneMap mit gültigen werten instanziert | Es wird eine InvalidArgumentException aufgerufen |
| 2: Ungültiger Enum | Enum welches kein File verbunden hat            | SceneMap ist leer                                  | Es wird eine InvalidArgumentException aufgerufen |
| 3: Null Enum       | Null                                            | any                                                | Es wird eine IllegalArgumentException aufgerufen |

## Klasse ContactReposittory
Methode die getestet wird: `addContact(name: String, email: String)`

#### Gültige Äquivalenzklassen
1. Der Contact Objekt wird mit einem gültigen Namen und einer gültigen Email erzeugt.

#### Ungültige Äquivalenzklassen
2. Der Contact Objekt wird mit einem ungültigen Email erzeugt.
3. Der Contact Objekt wird mehrfach mit den gleichen Parametern erzeugt.

| Äquivalenzklasse             | Methoden Parameter                     | Initialer Objekt Zustand                                               | Erwartetes Ergebnis                      |
|------------------------------|----------------------------------------|------------------------------------------------------------------------|------------------------------------------|
| 1: Gültige Email             | Gültiger Name und Email                | Contact mit dieser Email exisitert nicht                               | Contact Objekt wird erzeugt              |
| 1: Gültige Email             | Gültiger Name und Email                | Contact mit diesem Name exisitert bereits aber mit einer anderen Email | Contact Objekt wird erzeugt              |
| 2: Leere Email               | Gültiger Name, Email mit leeren String | Contact mit dieser Email exisitert nicht                               | Es wird eine Custom Exception aufgerufen |
| 3: Contact Exisitert bereits | Gültiger Name und Email                | Contact mit dieser Email exisitert bereits                             | Es wird eine Custom Exception aufgerufen |



--------------------------------------------
#### Methode die getestet wird: `removeContact(email: String)`

#### Gültige Äquivalenzklassen
1. Der Contact Objekt wird mit einer gültigen Email gelöscht.

#### Ungültige Äquivalenzklassen
2. Der Contact Objekt wird mit einem leeren String gelöscht.
3. Der Contact Objekt wird mit einer Email gelöscht, die nicht existiert.

| Äquivalenzklasse         | Methoden Parameter | Initialer Objekt Zustand                 | Erwartetes Ergebnis                             |
|--------------------------|--------------------|------------------------------------------|-------------------------------------------------|
| 1: Gültige Email         | Gültige Email      | Contact exisitert mit dieser Email       | Contact wird gelöscht                           |
| 2: Leere Email           | Leerer String      | Any                                      | Es wird ein IllegalArgumentException aufgerufen |
| 3: Email exisitert nicht | Gültige Email      | Contact exisitert mit dieser Email nicht | Es wird ein InvalidArgumentException aufgerufen |


## Klasse ImageExtractor
Methode die getestet wird: `extractOCR(lfile: File)`

#### Gültige Äquivalenzklassen
1. Das OCR wird mit einer gültigen Bilddatei durchgeführt.

#### Ungültige Äquivalenzklassen
2. Das OCR wird mit einem ungültigen Dateityp durchgeführt.
3. Das OCR wird mit einer leeren Datei durchgeführt.

| Äquivalenzklasse       | Methoden Parameter | Initialer Objekt Zustand     | Erwartetes Ergebnis                             |
|------------------------|--------------------|------------------------------|-------------------------------------------------|
| 1: Gültiges Bild       | Gültige Bilddatei  | Tesseract Objekt instanziert | Extrahierter String wird zurückgegeben          |
| 2: Ungültige Bilddatei | Falscher Dateityp  | Any                          | Es wird ein IllegalArgumentException aufgerufen |
| 3: Leere Datei         | Leere Datei        | Any                          | Es wird ein IllegalArgumentException aufgerufen |





