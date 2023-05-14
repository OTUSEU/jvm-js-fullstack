


import storage.MapStorage
import kotlin.test.BeforeTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StorageTest {

    lateinit var storage: MapStorage

    @BeforeTest
    fun before() {
        storage = MapStorage()
    }

    @Test
    fun testInsert() {
        val item = ShoppingListItem("test", 1)
        storage.insert(item)
        assertEquals("test", storage.get(item.id)?.desc)

    }
}