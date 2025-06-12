# LilChunks

Un pregeneratore di chunk moderno, efficiente e sicuro per server Minecraft.

## Caratteristiche

- **Generazione Efficiente**: Generazione di chunk ottimizzata con modalità sincrona e asincrona
- **Compatibilità Versioni**: Supporta le versioni di Minecraft dalla 1.16.5 alla 1.21.5
- **Configurabile**: Ampia gamma di opzioni di configurazione per le impostazioni di generazione
- **Monitoraggio Progressi**: Aggiornamenti in tempo reale e logging dettagliato
- **Sicurezza**: Misure di sicurezza integrate e gestione degli errori
- **Prestazioni**: Ottimizzato per un impatto minimo sul server

## Installazione

1. Scarica l'ultima versione dalla pagina delle release
2. Posiziona il file jar nella cartella `plugins` del tuo server
3. Riavvia il server o usa un gestore di plugin per caricare il plugin
4. Configura il plugin in `plugins/LilChunks/config.yml`

## Comandi

- `/lilchunks start <raggio>` - Avvia la generazione dei chunk dalla tua posizione attuale
- `/lilchunks stop` - Ferma la generazione dei chunk nel mondo corrente
- `/lilchunks status` - Controlla lo stato della generazione dei chunk
- `/lilchunks reload` - Ricarica la configurazione del plugin
- `/lilchunks help` - Mostra le informazioni di aiuto

## Permessi

- `lilchunks.use` - Permette l'uso dei comandi base
- `lilchunks.admin` - Permette l'uso dei comandi amministrativi (start, stop, reload)

## Configurazione

Il plugin può essere configurato in `plugins/LilChunks/config.yml`:

```yaml
# Impostazioni Generazione Chunk
chunk-generation:
  chunks-per-tick: 4
  use-async: true
  generate-structures: true
  generate-caves: true
  generate-features: true

# Impostazioni Logging
logging:
  debug: false
  detailed: false
  progress-interval: 5
```

## Compilazione dal Sorgente

1. Clona il repository
2. Installa Maven
3. Esegui `mvn package`
4. Trova il file jar compilato nella directory `target`

## Requisiti

- Java 17 o superiore
- Server Minecraft versione 1.16.5 a 1.21.5
- Server Bukkit/Spigot/Paper

## Supporto

Per supporto, per favore:
1. Controlla la [documentazione](https://github.com/hydr4/LilChunks/wiki)
2. Apri un issue su GitHub
3. Unisciti al nostro server Discord (in arrivo)

## Licenza

Questo progetto è rilasciato sotto la Licenza MIT - vedi il file [LICENSE](LICENSE) per i dettagli.

## Crediti

- Creato da Hydr4
- Ringraziamenti speciali a tutti i contributori 