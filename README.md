# TCP
### TS_lab_project

#### TODO:
 - [x] Konstrukcja nagłówka
 - [x] Forma konstruowania komunikatu przez klientów
 - [x] Wysyłanie i odbieranie komunikatu
 - [x] Wysyłanie przez serwer komunikatu do **wskazanego** klienta (Unicast)
 - [x] Wysyłanie przez serwer komuniaktu do obu klientów (Multicast/Broadcast)
 - [x] Obsługa komunikatów
 - [ ] Obsługa błędów

#### Polecenie:
<ul>
<strong>Wariant 8 – model komunikacji 2↔1</strong>
<li>Protokół warstwy transportowej: TCP.</li>
<li>Struktura nagłówka protokołu binarnego: pole operacji (4 bity), pole odpowiedzi (3 bity), pole
długości danych (64 bity), pole danych o zmiennym rozmiarze, dodatkowe pola zdefiniowane przez
programistę (następujące po polu danych).</li>
<li>Funkcje oprogramowania:
<ul>
<li>klienta:
<ul>
<li>[x] nawiązanie połączenia z serwerem,</li>
<li>[x] uzyskanie identyfikatora sesji,</li>
<li>[x] wysłanie zaproszenia do drugiego klienta,</li>
<li>[x] przyjęcie/odrzucenie zaproszenie,</li>
<li>[x] przesłanie wiadomości tekstowej (binarna postać znaków ASCII),</li>
<li>[x] zamknięcie sesji komunikacyjnej,</li>
<li>[x] zakończenie połączenia;</li>
</ul></li>
<li>serwera:
<ul>
<li>[x] wygenerowanie identyfikatora sesji,</li>
<li>[x] poinformowanie klienta, czy inny klient jest dostępny w sieci:</li>
<ul>
<li>[x] w przypadku braku osiągalności należy zwrócić błąd;</li>
</ul>
<li>[x] pośredniczenie w transmisji.</li>
</ul></ul>
<li>Wymagania dodatkowe:</li>
<ul>
<li>[x] identyfikator sesji powinien być przesyłany w każdym komunikacie.</li>
</ul></ul>

##### Notes:
 - [ASCII/HEX/BIN](https://www.asciitohex.com)
 - [Remove spaces](https://www.browserling.com/tools/remove-all-whitespace)
 - [U2 Converter](https://www.exploringbinary.com/twos-complement-converter/)
 - [Core code - "Java Socket Progarmming Examples"](https://cs.lmu.edu/~ray/notes/javanetexamples/)
