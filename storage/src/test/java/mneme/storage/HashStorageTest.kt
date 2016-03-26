package mneme.storage

import org.junit.Test
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 */
class HashStorageTest {

    @Test fun put_get() {
        var dataFolder = Paths.get("test/data")
        var tmpFolder = Paths.get("test/tmp")

        Files.createDirectories(dataFolder)
        Files.createDirectories(tmpFolder)

        val storage = HashStorage(dataFolder, tmpFolder)

        //1st
        val a1 = ByteArray(1000)
        Random().nextBytes(a1)

        val id1 = storage.put(ByteArrayInputStream(a1))

        assertEquals(id1.length, 128)

        val s1 = storage.get(id1)

        val b1 = ByteArray(1000)

        assertEquals(1000, s1.read(b1))
        assertTrue(Arrays.equals(a1, b1))

        //2nd
        val a2 = ByteArray(1000)
        Random().nextBytes(a1)

        val id2 = storage.put(ByteArrayInputStream(a2))

        assertEquals(id2.length, 128)

        assertNotEquals(id1, id2)

        val s2 = storage.get(id2)

        val b2 = ByteArray(1000)

        assertEquals(1000, s2.read(b2))
        assertTrue(Arrays.equals(a2, b2))
    }
}