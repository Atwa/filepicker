import androidx.appcompat.app.AppCompatActivity
import com.atwa.filepicker.core.FileConverter
import com.atwa.filepicker.core.FilePicker
import okhttp3.RequestBody
import java.io.File

class PickerActivity() : AppCompatActivity() {
    fun convertFile() {
        val file: File = File("file")
        val requestBody: RequestBody = FileConverter.getInstance()
            .toRequestBody(FileConverter.getInstance().fileMediaType, file)
    }
}