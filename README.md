# TCP
### TS_lab_project

#### TODO:
 - [x] Konstrukcja nagłówka
 - [ ] Forma konstruowania komunikatu przez klientów
 - [ ] Wysyłanie i odbieranie komunikatu

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
<li>[ ] nawiązanie połączenia z serwerem,</li>
<li>[ ] uzyskanie identyfikatora sesji,</li>
<li>[ ] wysłanie zaproszenia do drugiego klienta,</li>
<li>[ ] przyjęcie/odrzucenie zaproszenie,</li>
<li>[ ] przesłanie wiadomości tekstowej (binarna postać znaków ASCII),</li>
<li>[ ] zamknięcie sesji komunikacyjnej,</li>
<li>[ ] zakończenie połączenia;</li>
</ul></li>
<li>serwera:
<ul>
<li>[ ] wygenerowanie identyfikatora sesji,</li>
<li>[ ] poinformowanie klienta, czy inny klient jest dostępny w sieci:</li>
<ul>
<li>[ ] w przypadku braku osiągalności należy zwrócić błąd;</li>
</ul>
<li>[ ] pośredniczenie w transmisji.</li>
</ul></ul>
<li>Wymagania dodatkowe:</li>
<ul>
<li>[ ] identyfikator sesji powinien być przesyłany w każdym komunikacie.</li>
</ul></ul>

##### Notes:
 - [ASCII/HEX/BIN](https://www.asciitohex.com)
 - [Remove spaces](https://www.browserling.com/tools/remove-all-whitespace)
 - [U2 Converter](https://www.exploringbinary.com/twos-complement-converter/)
