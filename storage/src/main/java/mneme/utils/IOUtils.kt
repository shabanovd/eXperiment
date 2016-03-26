package mneme.utils

import java.util.*

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 */
internal class ResourceHolder : AutoCloseable {
    val resources = ArrayList<AutoCloseable>()

    fun <T : AutoCloseable> T.autoClose(): T {
        resources.add(this)
        return this
    }

    override fun close() {
        resources.reverse()
        resources.forEach {
            try {
                it.close()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}

inline internal fun <R> using(block: ResourceHolder.() -> R): R {
    val holder = ResourceHolder()
    try {
        return holder.block()
    } finally {
        holder.close()
    }
}