# FilePicker

Android library to facilitate files picking up process and ability to convert them to request
bodies.

**The goal of this app is to get rid of the hassle of file picking up handling and reduce effort
made in uploading them to server without the need to ask use for a single permission.**

## Features:

-Launch different file types picking intents/requests (Image, Pdf and File for general). -Get a
callback with bitmap in case of image alongside with file object. -Get a callback with a fileName in
case of Pdf & File alongside with file object. -Convert picked up files to request bodies to use
them with api requests to upload them to server, -Not a single permission is required.

## Dependency :

  ```
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```

  ```
  dependencies {
	    implementation 'com.github.Atwa:Android-FilePicker:1.0.4'
	}
  ```

## Usage :
- Initialize instance in activity whether as member variable or in onCreate()
  ```
  private val filePicker = FilePicker.getInstance(this)
  ```

- Image picking
  ```
  fun pickImage() {
        filePicker.pickImage() { result ->
            val bitmap : Bitmap? = result?.first
            val file : File? = result?.second
        }
    }
  ```
- Pdf picking
  ```
  fun pickPdf() {
        filePicker.pickPdf() { result ->
            val name: String? = result?.first
            val file: File? = result?.second
        }
    }
  ```
- File picking
  ```
  fun pickFile() {
        filePicker.pickFile() { result ->
            val name : String? = result?.first
            val file : File? = result?.second
        }
    }
  ```
- Image capture  
  ```
  fun captureImage() {
        filePicker.captureCameraImage() { result ->
            val name : String? = result?.first
            val file : File? = result?.second
        }
    }
  ```
- File converting to request body
  ```
  fun convertFile() {
        val file: File = File("file")
        val requestBody: RequestBody = FileConverter.getInstance()
            .toRequestBody(FileConverter.getInstance().fileMediaType, file)
    }
  ```
  
### Version history
| Version | Release note |
| ------  | ------------- |
| 1.0.0   | Initial release  |
| 1.0.1   | Increase buffer size to handle larger files (8GB Max)  |
| 1.0.3   | Fix bug : LifecycleOwner Activity is attempting to register while current state is RESUMED.  |
| 1.0.4   | Enhancement : Avoid possible activity reference leaking  <br /> Feature : Implement camera image picker.  |
  


### Contribution

Please feel free to make a pull request or fork.

### Rate

If you find this repository useful please give it a star .
