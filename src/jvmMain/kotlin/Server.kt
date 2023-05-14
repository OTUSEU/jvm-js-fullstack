import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.*
import com.mongodb.ConnectionString
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import org.litote.kmongo.reactivestreams.KMongo
import storage.MapStorage

/**
 * lesson4 немножко подменили сервер
 * при запуске у меня
 * Не удается открыть эту страницу
 * Похоже, веб-страница по адресу http://0.0.0.0:9090/ содержит ошибки
 * или окончательно перемещена на новый веб-адрес.
 */
//val connectionString: ConnectionString? = System.getenv("MONGODB_URI")?.let {
//    ConnectionString("$it?retryWrites=false")
//}
//
//val client = if (connectionString != null) KMongo.createClient(connectionString).coroutine else KMongo.createClient().coroutine
//val database = client.getDatabase(connectionString?.database ?: "test")
//val collection = database.getCollection<ShoppingListItem>()
val collection = MapStorage()

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route(ShoppingListItem.path) {
                get {
                   //call.respond(collection.find().toList())
                    call.respond(collection.getAll())
                }
                post {
                    //collection.insertOne(call.receive<ShoppingListItem>())
                    collection.insert(call.receive<ShoppingListItem>())
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    //collection.deleteOne(ShoppingListItem::id eq id)
                    collection.delete(id)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}
