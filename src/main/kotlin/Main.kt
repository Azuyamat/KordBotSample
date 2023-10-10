import dev.kord.cache.map.MapLikeCollection
import dev.kord.cache.map.internal.MapEntryCache
import dev.kord.cache.map.lruLinkedHashMap
import dev.kord.core.Kord
import dev.kord.gateway.ALL
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.github.cdimascio.dotenv.Dotenv

private val dotEnv = Dotenv.load()
private val token = dotEnv["TOKEN"] ?: error("No token found in .env file")
lateinit var kord : Kord

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    kord = Kord(token) {
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
    kord.login {
        intents {
            +Intents.ALL
        }
        presence {
            playing("with a new bot")
        }
    }
}