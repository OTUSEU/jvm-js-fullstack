package storage

/**
 *  вторая часть Урока 4 :
 *  Создаем серсвис
 *  Заменяем его в нашем сервере: src/jvmMain/kotlin/Server.kt с 18-24
 */
import ShoppingListItem

class MapStorage {
    // выступает в качестве таблицы хранения
    // ShoppingList выдает объект ShoppingListItem.kt который передается с фронта
    private val table = HashMap<Int, ShoppingListItem>()
    // несколько методов, чтобы мы могли добавлять, удалять итд
    fun get(id: Int): ShoppingListItem? {
        return table[id]
    }

    fun insert(item: ShoppingListItem) {
        table[item.id] = item
    }

    fun delete(id: Int) {
        table.remove(id)
    }
    // возвращаем из таблицы все значения
    fun getAll(): MutableCollection<ShoppingListItem> {
        return table.values
    }
}