# Äquivalenzklassen

## Klasse Car

### Methode accelerate
Für die Klasse Car haben wir die Äquivalenzklassen der Methode `accelerate` folgendermassen definiert:

#### Gültige Äquivalenzklassen
- Korrekte Beschleunigung (1-8): Beschleunigung, die die Geschwindigkeit in einer oder mehreren Dimensionen erhöht/verringert.
- Nullvektor: Keine Beschleunigung, die Geschwindigkeit bleibt unverändert.  


#### Ungültige Äquivalenzklassen
- Null-Eingabe: Der Paramater ist null.
- Nicht-Vektor-Eingaben: Eingabe von Datentypen, die keine Vektorwerte sind. Dies ist ein anderer, ungültiger Datentyp (nicht null).

| Äquivalenzklasse          | Repräsentativer Wert                                  | Erwartetes Ergebnis                                  | Bemerkungen                         |
|---------------------------|-------------------------------------------------------|------------------------------------------------------|-------------------------------------|
| Korrekte Beschleunigung 1 | DOWN_LEFT (-1, 1)                                     | Aktualisiere die Geschwindigkeit nach unten links    | Testet normale Beschleunigung       |
| Korrekte Beschleunigung 2 | DOWN (0, 1)                                           | Aktualisiere die Geschwindigkeit nach unten          | Testet normale Beschleunigung       |
| Korrekte Beschleunigung 3 | DOWN_RIGHT (1, 1)                                     | Aktualisiere die Geschwindigkeit nach unten rechts   | Testet normale Beschleunigung       |
| Korrekte Beschleunigung 4 | LEFT (-1, 0)                                          | Aktualisiere die Geschwindigkeit nach links          | Testet normale Beschleunigung       |
| Korrekte Beschleunigung 6 | RIGHT (1, 0)                                          | Aktualisiere die Geschwindigkeit nach rechts         | Testet normale Beschleunigung       |
| Korrekte Beschleunigung 7 | UP_LEFT (-1, -1)                                      | Aktualisiere die Geschwindigkeit nach oben links     | Testet normale Beschleunigung       |
| Korrekte Beschleunigung 8 | UP (0, -1)                                            | Aktualisiere die Geschwindigkeit nach oben           | Testet normale Beschleunigung       |
| Korrekte Beschleunigung 9 | UP_RIGHT (1, -1)                                      | Aktualisiere die Geschwindigkeit nach oben rechts    | Testet normale Beschleunigung       |
| Nullvektor                | NONE (0, 0)                                           | Keine Änderung der aktuellen Geschwindigkeit         | Testet den Fall ohne Beschleunigung |                          |                                               |                                                           |                                   |
| Ungültige Richtung        | null                                                  | Fehlerbehandlung oder keine Änderung                 | Testet den Fall mit null Eingabe    |
| Null Objekttypen          | Objekt, welches vom Typ "Direction" ist aber null ist | Fehlerbehandlung oder keine Änderung                 | Testet den Fall mit leerem Datentyp |

### Methode move
Für die Klasse Car haben wir die Äquivalenzklassen der Methode `move` folgendermassen definiert:

#### Gültige Äquivalenzklassen
- Gütige Bewegung: Eine Bewegung, die die Position des Autos in einer oder mehreren Dimensionen verändert. Entweder keine, positive, negative oder eine gemischte Bewegung.

#### Ungültige Äquivalenzklassen
- Overflow "positiv": Geschwindigkeitswerte, die extrem hoch sind, könnten zu unerwartetem Verhalten führen, wenn sie nicht korrekt gehandhabt werden.
- Overflow "negativ" : Geschwindigkeitswerte, die extrem niedrig sind, könnten zu unerwartetem Verhalten führen, wenn sie nicht korrekt gehandhabt werden.

| Äquivalenzklasse    | Aktuelle Position | Aktuelle Geschwindigkeit | Erwartetes Ergebnis nach move()      | Bemerkungen                                     |
|---------------------|-------------------|--------------------------|--------------------------------------|-------------------------------------------------|
| Gütige Bewegung     | (10, 5)           | (0, 0)                   | (10, 5)                              | Keine Bewegung; Position unverändert            |
| Gütige Bewegung     | (10, 5)           | (5, 5)                   | (15, 10)                             | Position erhöht sich in beiden Achsen           |
| Gütige Bewegung     | (10, 5)           | (-5, -5)                 | (5, 0)                               | Position verringert sich in beiden Achsen       |
| Gütige Bewegung     | (10, 5)           | (5, -5)                  | (15, 0)                              | Position erhöht sich in X, verringert sich in Y |
| Overflow "positiv"  | MAX_VALUE         | +1                       | Unerwartetes/ undefiniertes Ergebnis | Liegt ausserhalb der Grenzen                    |
| Overflow "negativ"  | MIN_VALUE         | -1                       | Unerwartetes/ undefiniertes Ergebnis | Liegt ausserhalb der Grenzen                    |

## Klasse Game

### Methode doCarTurn
Für die Klasse Game haben wir die Äquivalenzklassen der Methode `doCarTurn` folgendermassen definiert:

#### Gültige Äquivalenzklassen
- Normaler Pfad (TRACK): Das Auto beschleunigt und bewegt sich auf normalem Streckenabschnitt, ohne zu kollidieren oder die Ziellinie zu überqueren.
- Kollision mit einem anderen Auto (TRACK/CAR): Das Auto kollidiert auf der Strecke mit einem anderen Auto und geht in den Zustand "gecrasht" über.
- Kollision mit einer Wand (WALL): Das Auto fährt in eine Wand und wird als gecrasht markiert.
- Korrekte Überquerung der Ziellinie : Das Auto überquert die Ziellinie in der richtigen Richtung und wird als Gewinner markiert.
- Falsche Richtung an der Ziellinie: Das Auto erreicht die Ziellinie, aber in der falschen Richtung, und gewinnt daher nicht.

#### Ungültige Äquivalenzklassen
- Ungültige Beschleunigung: Die Beschleunigung liegt ausserhalb der erlaubten Zahlenwerte (null).
- Auto bereits gecrasht: Das Auto befindet sich bereits im Zustand "gecrasht", bevor der Zug beginnt.

| Äquivalenzklasse                                                                              | Beschleunigung         | Erwartetes Ergebnis                                       | Bemerkungen                                                 |
|-----------------------------------------------------------------------------------------------|------------------------|-----------------------------------------------------------|-------------------------------------------------------------|
| Normaler Pfad (TRACK)                                                                         | Gültige Richtung       | Auto bewegt sich zum Endpunkt ohne Zwischenfälle          | Standard-Szenario ohne besondere Ereignisse                 |
| Kollision mit einem anderen Auto (TRACK)                                                      | Gültige Richtung       | Auto wird als gecrasht markiert                           | Auto trifft auf ein anderes Auto und crasht                 |
| Kollision mit einer Wand (WALL)                                                               | Gültige Richtung       | Auto wird als gecrasht markiert                           | Auto fährt gegen eine Wand und crasht                       |
| Korrekte Überquerung der Ziellinie, "roundsToComplete" ändert den Wert von 1 auf 0 (FINISH_*) | Gültige Richtung       | Auto wird als Gewinner markiert                           | Auto überquert die Ziellinie in der richtigen Richtung      |
| Falsche Richtung an der Ziellinie (FINISH_*)                                                  | Gültige Richtung       | Auto gewinnt nicht, "roundsToComplete" wird auf 2 gesetzt | Auto erreicht die Ziellinie, aber in der falschen Richtung  |
| Ungültige Beschleunigung                                                                      | Ungültiger Wert (null) | Fehlermeldung                                             | Beschleunigungswerte sind ausserhalb des erlaubten Bereichs |
| Auto bereits gecrasht                                                                         | Gültige Richtung       | Auto bewegt sich nicht                                    | Auto ist bereits als gecrasht markiert                      |

