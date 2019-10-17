# TCP
### TS_lab_project

#### Informacje organizacyjne:
<ol>
  <li>Kolejność #inclide jest istotna przy kompilacji MinGW. <strong>NIE</strong> zmieniać jej</li>
<li>Plik serwera serwer.exe jest uruchamialny bez argumentów</li>
<li>Plik clienta client.exe wymaga podania adresu IPv4 jako argumentu:
<ol><li>Należy go zatem uruchamiać przy użyciu konsoli np. <code>client.exe 192.168.0.15</code></li>
<li>W przypadku gdy client i serwer są uruchamiane na tej samej maszynie można użyć <code>client.exe localhost</code></li>
</ol></li>
<li>Kompilacja przy użyciu MinGW:</li>
  <ul>
  <li>Serwer: <code>g++ -g main.cpp -o serwer.exe -lws2_32</code></li>
  <li>Client: <code>g++ -g main.cpp -o client.exe -lws2_32</code></li>
  </ul>
</ol>

#### Polecenie:
<ul>
<strong>Wariant 8 – model komunikacji 1↔1</strong>
<li>Protokół warstwy transportowej: TCP.</li>
<li>Struktura nagłówka protokołu binarnego: pole operacji (4 bity), pole odpowiedzi (3 bity), pole
długości danych (64 bity), pole danych o zmiennym rozmiarze, dodatkowe pola zdefiniowane przez
programistę (następujące po polu danych).</li>
<li>Funkcje oprogramowania:
<ul>
<li>klienta:
<ul>
<li>nawiązanie połączenia z serwerem,</li>
<li>uzyskanie identyfikatora sesji,</li>
<li>wysłanie zaproszenia do drugiego klienta,</li>
<li>przyjęcie/odrzucenie zaproszenie,</li>
<li>przesłanie wiadomości tekstowej (binarna postać znaków ASCII),</li>
<li>zamknięcie sesji komunikacyjnej,</li>
<li>zakończenie połączenia;</li>
</ul></li>
<li>serwera:
<ul>
<li>wygenerowanie identyfikatora sesji,</li>
<li>poinformowanie klienta, czy inny klient jest dostępny w sieci:</li>
<ul>
<li>w przypadku braku osiągalności należy zwrócić błąd;</li>
</ul>
<li>pośredniczenie w transmisji.</li>
</ul></ul>
<li>Wymagania dodatkowe:</li>
<ul>
<li>identyfikator sesji powinien być przesyłany w każdym komunikacie.</li>
</ul></ul>
