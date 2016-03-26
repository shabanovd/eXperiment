package mneme.storage

import mneme.utils.toHexString
import mneme.utils.using
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.security.DigestOutputStream
import java.security.MessageDigest

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 */
class HashStorage(val dataFolder: Path, val tmpFolder: Path) {

    val SHA512 = "SHA-512"

    init {
        //make sure algorithm available
        MessageDigest.getInstance(SHA512)
    }

    fun put(input: InputStream): String {
        val processor = MessageDigest.getInstance(SHA512)

        val hashing = Files.createTempFile(tmpFolder, "hashing", "data");

        using {
            val fileStream = Files.newOutputStream(hashing).autoClose()

            val digestStream = DigestOutputStream(fileStream, processor).autoClose()

            input.copyTo(digestStream)
        }

        val hash = processor.digest().toHexString()

        //check hash storage
        val data = dataPath(dataFolder, hash);
        if (Files.notExists(data)) {
            Files.createDirectories(data.parent);
            Files.move(hashing, data);
        } else {
            Files.delete(hashing)
            //if fail it will be clean up next restart or it possible detect old files by last access time
        }

        return hash
    }

    fun get(hash: String): InputStream {
        return Files.newInputStream(dataPath(dataFolder, hash));
    }

    private fun dataPath(folder: Path, name: String): Path {
        return folder
                .resolve(name.substring(0, 4))
                .resolve(name.substring(4, 8))
                .resolve(name);
    }
}
