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
	    implementation 'com.github.Atwa:FilePicker:1.0.0'
	}
  ```

## Usage :

- Image picking
  ```
  fun pickImage() {
        FilePicker.getInstance().pickImage(this) { result ->
            val bitmap : Bitmap? = result?.first
            val file : File? = result?.second
        }
    }
  ```
- Pdf picking
  ```
  fun pickPdf() {
        FilePicker.getInstance().pickPdf(this) { result ->
            val name: String? = result?.first
            val file: File? = result?.second
        }
    }
  ```
- File picking
  ```
  fun pickFile() {
        FilePicker.getInstance().pickFile(this) { result ->
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

### Contribution

Please feel free to make a pull request or fork.

### Rate

If you find this repository useful please give it a star .