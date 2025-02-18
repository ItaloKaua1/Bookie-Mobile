import android.content.Context
import java.util.Properties

object ConfigUtils {
    fun getGoogleBooksApiKey(context: Context): String {
        val properties = Properties()
        context.assets.open("local.properties").use { properties.load(it) }
        return properties.getProperty("GOOGLE_BOOKS_API_KEY", "")
    }
}
