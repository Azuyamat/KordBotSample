import button.ButtonManager
import buttons.TicketButton
import command.CommandManager
import commands.Panel
import dev.kord.cache.map.MapLikeCollection
import dev.kord.cache.map.internal.MapEntryCache
import dev.kord.cache.map.lruLinkedHashMap
import dev.kord.core.Kord
import dev.kord.gateway.ALL
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.github.cdimascio.dotenv.Dotenv
import modal.ModalManager
import modals.TicketModal

private val dotEnv = Dotenv.load()
private val TOKEN = dotEnv["TOKEN"] ?: error("No token found in .env file")
val TICKETCATEGORY = dotEnv["TICKETCATEGORY"] ?: error("No ticket category found in .env file")

lateinit var kord : Kord

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    kord = Kord(TOKEN) {
        cache {
            users { cache, description ->
                MapEntryCache(cache, description, MapLikeCollection.concurrentHashMap())
            }

            messages { cache, description ->
                MapEntryCache(cache, description, MapLikeCollection.lruLinkedHashMap(maxSize = 100))
            }

            members { cache, description ->
                MapEntryCache(cache, description, MapLikeCollection.none())
            }
        }
    }

    val commandManager = CommandManager(kord).apply {
        registerInteractions("commands")
    }
    val buttonManager = ButtonManager(kord).apply {
        registerInteractions("buttons")
    }
    val modalManager = ModalManager(kord).apply {
        registerInteractions("modals")
    }


    kord.login {
        intents {
            +Intents.ALL
        }
        presence {
            playing("with a new bot")
        }
    }
}