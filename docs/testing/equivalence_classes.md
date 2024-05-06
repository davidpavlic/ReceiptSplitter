# Äquivalenzklassen

## Klasse ReceiptProcessor

Methode, die getestet wird: `calculateDebtByContact(contactItems: List<ContactItem>)`

#### Gültige Äquivalenzklassen
1. Die Kalkulation wird mit einer Liste von ContactItems, die mindestens ein Element beinhaltet, korrekt durchgeführt.

#### Ungültige Äquivalenzklassen
2. Die Kalkulation wird mit einer leeren Liste (null) von ContactItems durchgeführt.
3. Die Kalkulation wird mit einer Liste von invaliden ContactItems durchgeführt. (z.B. Betrag ist negativ)

| Äquivalenzklasse   | Methoden Parameter                                                       | Initialer Objekt Zustand                                                          | Erwartetes Ergebnis                                                                  |
|--------------------|--------------------------------------------------------------------------|-----------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| 1: Gültige Liste   | Liste mit gültigen ContactItems                                          | Es sind gültige Contacts vorhanden und der Receipt ist als Instanzvariabel gesetzt | Der Betrag wird korrekt berechnet und zurückgegeben                                  |
| 2: Leere Liste     | Leere Liste                                                              | Any                                                                               | Der Betrag wird nicht berechnet und es wird eine IllegalArgumentException aufgerufen |
| 3: Ungültige Liste | Liste mit ContactItems, die einen negativen Betrag haben                 | Es sind gültige Contacts vorhanden und der Receipt ist als Instanzvariabel gesetzt | Der Betrag wird nicht berechnet und es wird eine IllegalArgumentException aufgerufen |
| 3: Ungültige Liste | Liste mit ContactItems, die einen nicht existierenden Contact beinhalten | Es sind keine Contacts vorhanden und der Receipt ist als Instanzvariabel gesetzt  | Der Betrag wird nicht berechnet und es wird eine Custom Exception aufgerufen         |


---
#### Methode die getestet wird: `parseReceipt(ocrString: String)`
#### Gültige Äquivalenzklassen

1. Receipt Objekt wird mit einem gültigen OCR String erzeugt und ist korrekt formatiert.
#### Ungültige Äquivalenzklassen
2. Receipt Objekt wird mit einem String, welcher ungültige Informationen beinhaltet, erzeugt.
3. Receipt Objekt wird mit einem leeren String erzeugt.

| Äquivalenzklasse     | Methoden Parameter                                                       | Initialer Objekt Zustand | Erwartetes Ergebnis                              |
|----------------------|--------------------------------------------------------------------------|--------------------------|--------------------------------------------------|
| 1: Gültiger String   | Gültiger OCR String die informationen einer Quittung beinhalten          | Any                      | Ein gültiges Receipt Objekt wird erzeugt         |
| 2: Ungültiger String | String, welcher mit einem Random String Generator erzeugt wurde          | Any                      | Es wird eine InvalidArgumentException aufgerufen |
| 2: Ungültiger String | String, welcher ein nicht unterstütztes Format einer Quittung beinhaltet | Any                      | Es wird eine InvalidArgumentException aufgerufen |
| 3: Leerer String     | Leerer String                                                            | Any                      | Es wird eine InvalidArgumentException aufgerufen |

## Klasse Router

Methode, die getestet wird: `gotoScene(enum: Enum)`

#### Gültige Äquivalenzklassen

1. Die Methode wird mit einem gültigen Enum aufgerufen und die richtige Scene wird instanziiert.

#### Ungültige Äquivalenzklassen

2. Die Methode wird mit einem "null" Enum aufgerufen.

TODO: Äquivalenzklassen ändern mit Constructor Test Cases

| Äquivalenzklasse  | Methoden Parameter                                    | Initialer Objekt Zustand                           | Erwartetes Ergebnis                         |
|-------------------|-------------------------------------------------------|----------------------------------------------------|---------------------------------------------|
| 1: Gültiger Enum  | Enum, welcher mit einer Java FXML Datei verbunden ist | Stage und sceneMap mit gültigen Werten instanziert | Die richtige Scene wird instanziert         |
| 2: Null Enum      | Null                                                  | any                                                | Es wird eine NullPointerException geworfen  |

## Klasse ContactRepository
Methode, die getestet wird: `addContact(name: String, email: String)`

#### Gültige Äquivalenzklassen
1. Das Contact Objekt wird mit einem gültigen Namen und einer gültigen Email erzeugt.

#### Ungültige Äquivalenzklassen
2. Das Contact Objekt wird mit einer ungültigen Email erzeugt.
3. Das Contact Objekt wird mehrfach mit den gleichen Parametern erzeugt.

| Äquivalenzklasse             | Methoden Parameter                     | Initialer Objekt Zustand                                                | Erwartetes Ergebnis                      |
|------------------------------|----------------------------------------|-------------------------------------------------------------------------|------------------------------------------|
| 1: Gültige Email             | Gültiger Name und Email                | Contact mit dieser Email exisitert nicht                                | Contact Objekt wird erzeugt              |
| 1: Gültige Email             | Gültiger Name und Email                | Contact mit diesem Namen exisitert bereits aber mit einer anderen Email | Contact Objekt wird erzeugt              |
| 2: Leere Email               | Gültiger Name, Email mit leerem String | Contact mit dieser Email exisitert nicht                                | Es wird eine Custom Exception aufgerufen |
| 3: Contact exisitert bereits | Gültiger Name und Email                | Contact mit dieser Email exisitert bereits                              | Es wird eine Custom Exception aufgerufen |



--------------------------------------------
#### Methode, die getestet wird: `removeContact(email: String)`

#### Gültige Äquivalenzklassen
1. Das Contact Objekt wird mit einer gültigen Email gelöscht.

#### Ungültige Äquivalenzklassen
2. Das Contact Objekt wird mit einem leeren String gelöscht.
3. Das Contact Objekt wird mit einer Email gelöscht, die nicht existiert.

| Äquivalenzklasse         | Methoden Parameter         | Initialer Objekt Zustand                         | Erwartetes Ergebnis                              |
|--------------------------|----------------------------|--------------------------------------------------|--------------------------------------------------|
| 1: Gültige Email         | Gültige Email              | Contact existiert mit dieser Email               | Contact Objekt wird gelöscht                     |
| 1: Gültige Email         | Gültige Email in Uppercase | Contact mit dieser Email existiert  in Lowercase | Contact Objekt wird gelöscht                     |
| 2: Leere Email           | Leerer String              | Any                                              | Es wird eine IllegalArgumentException aufgerufen |
| 3: Email existiert nicht | Gültige Email              | Contact existiert mit dieser Email nicht         | Es wird eine InvalidArgumentException aufgerufen |


## Klasse ImageExtractor
Methode die getestet wird: `extractOCR(lfile: File)`

#### Gültige Äquivalenzklassen
1. Das OCR wird mit einer gültigen Bilddatei durchgeführt, die lesbaren Text enthält.
2. Das OCR wird mit einer gültigen Bilddatei durchgeführt, die jedoch keinen lesbaren Text enthält.

#### Ungültige Äquivalenzklassen
3. Die OCR wird mit einem ungültigen Dateityp durchgeführt.
4. Die OCR wird mit einer leeren Datei durchgeführt.
5. Die OCR wird mit einer Bilddatei grösser als 10 MB durchgeführt.


| Äquivalenzklasse       | Methoden Parameter                   | Initialer Objekt Zustand     | Erwartetes Ergebnis                              |
|------------------------|--------------------------------------|------------------------------|--------------------------------------------------|
| 1: Gültiges Bild       | Gültige Bilddatei mit lesbarem Text  | Tesseract Objekt instanziert | Extrahierter String wird zurückgegeben           |
| 2: Kein lesbarer Text  | Gültige Bilddatei ohne lesbaren Text | Tesseract Objekt instanziert | Leerer String wird zurückgegeben                 |
| 3: Ungültige Bilddatei | Falscher Dateityp                    | Any                          | Es wird eine IllegalArgumentException aufgerufen |
| 4: Leere Datei         | Leere Datei                          | Any                          | Es wird eine IllegalArgumentException aufgerufen |
| 3: Grosse Bilddatei    | Bilddatei > 10MB                     | Tesseract Objekt instanziert | Es wird eine Custom Exception aufgerufen         |





