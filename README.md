# FilePicker

Android library to facilitate files and image picking up process whether from storage or camera and ability to convert them to request
bodies without permission.

**The goal of this library is to get rid of the hassle of file picking up handling and reduce effort
made in uploading them to server without the need to ask use for a single permission.**

## Features:

- Launch different file types picking requests (Image ,Video ,Pdf and File for general). 
- Get a callback with related meta according to the request type.
- Meta can be (fileName,size in Kb, bitmap and file object).
- Convert picked up files to request bodies to use them with api requests to upload them to server.
- Not a single permission is required.

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
	    implementation 'com.github.atwa:filepicker:2.0.0'
	}
  ```

## Usage :
- Initialize instance in activity/fragment as member variable.
  ```
  private val filePicker = FilePicker.getInstance(this)
  ```

- Image picking
  ```
  fun pickImage() {
        filePicker.pickImage() { meta ->
            val name : String? = meta?.name
            val sizeKb : Int? = meta?.sizeKb
            val file : File? = meta?.file
            val bitmap : Bitmap? = meta?.bitmap
        }
    }
  ```
- Image multi picking
  ```
  fun pickMultiImage() {
        filePicker.pickMultiImage { list ->
              list.forEach { meta ->
                  val name : String? = meta?.name
                  val sizeKb : Int? = meta?.sizeKb
                  val file : File? = meta?.file
                  val bitmap : Bitmap? = meta?.bitmap
              }
          }
    }
  ```
- Image capture
  ```
  fun captureImage() {
        filePicker.captureCameraImage() { meta ->
            val name : String? = meta?.name
            val sizeKb : Int? = meta?.sizeKb
            val file : File? = meta?.file
            val bitmap : Bitmap? = meta?.bitmap
        }
    }
  ```  
- Video capture
  ```
  fun captureVideo() {
        filePicker.captureCameraVideo() { meta ->
            val name: String? = meta?.name
            val sizeKb: Int? = meta?.sizeKb
            val file: File? = meta?.file
            val thumbnail: Bitmap? = meta?.thumbnail
        }
    }
  ```  
- Pdf picking
  ```
  fun pickPdf() {
        filePicker.pickPdf() { meta ->
            val name: String? = meta?.name
            val sizeKb: Int? = meta?.sizeKb
            val file: File? = meta?.file
        }
    }
  ```
- Video picking
  ```
  fun pickVideo() {
        filePicker.pickVideo() { meta ->
            val name: String? = meta?.name
            val sizeKb: Int? = meta?.sizeKb
            val file: File? = meta?.file
            val thumbnail: Bitmap? = meta?.thumbnail
        }
    }
  ```  
- File picking
  ```
  fun pickFile() {
        filePicker.pickFile() { meta ->
            val name: String? = meta?.name
            val sizeKb: Int? = meta?.sizeKb
            val file: File? = meta?.file
        }
    }
  ```
- File picking filtered with MIME type
  ```
  fun pickMimeFile() {
        filePicker.pickMimeFile(mimeType = "application/pdf") { meta ->
            val name: String? = meta?.name
            val sizeKb: Int? = meta?.sizeKb
            val file: File? = meta?.file
        }
    }
  ```
- File picking from Initial directory
  ```
  fun pickPathFile() {
        filePicker.pickPathFile(initialDirectoryPath = "") { meta ->
            val name: String? = meta?.name
            val sizeKb: Int? = meta?.sizeKb
            val file: File? = meta?.file
        }
    }
  ```
- File picking from Initial directory with MIME type
  ```
  fun pickFile() {
        filePicker.pickFile(initialDirectoryPath = "",mimeType = "application/pdf") { meta ->
            val name: String? = meta?.name
            val sizeKb: Int? = meta?.sizeKb
            val file: File? = meta?.file
        }
    }
  ```
- File multi picking 
  ```
  fun pickMultiFiles() {
        filePicker.pickMultiFiles { list ->
              list.forEach { meta ->
                  val name: String? = meta?.name
                  val sizeKb: Int? = meta?.sizeKb
                  val file: File? = meta?.file
              }
          }
    }
  ```
- File multi picking filtered with MIME type
  ```
  fun pickMultiFiles() {
        filePicker.pickMultiMimeFiles(mimeType = "application/pdf") { list ->
              list.forEach { meta ->
                  val name: String? = meta?.name
                  val sizeKb: Int? = meta?.sizeKb
                  val file: File? = meta?.file
              }
          }
    }
  ```
- File multi picking with initial directory path
  ```
  fun pickMultiFiles() {
        filePicker.pickMultiPathFiles(initialDirectoryPath = "") { list ->
              list.forEach { meta ->
                  val name: String? = meta?.name
                  val sizeKb: Int? = meta?.sizeKb
                  val file: File? = meta?.file
              }
          }
    }
  ```
- File picking from Initial directory with MIME type
  ```
  fun pickMultiFiles() {
        filePicker.pickMultiFiles(initialDirectoryPath = "",mimeType = "application/pdf") { list ->
              list.forEach { meta ->
                  val name: String? = meta?.name
                  val sizeKb: Int? = meta?.sizeKb
                  val file: File? = meta?.file
              }
          }
    }
  ```
- File converting to request body
  ```
  fun convertFile() {
        val file: File = File("file")
        val requestBody: RequestBody = FileConverter.getInstance()
            .toRequestBody(file, FileConverter.getInstance().fileMediaType)
    }
  ```
  
### Version history
| Version | Release note                                                                                                                                                                                                                                                |
|--------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.0.0  | Initial release.                                                                                                                                                                                                                                            |
| 1.0.1  | Increase buffer size (8GB) to be larger for better performance.                                                                                                                                                                                             |
| 1.0.3  | Fix bug : LifecycleOwner Activity is attempting to register while current state is RESUMED.                                                                                                                                                                 |
| 1.0.4  | Enhancement : Avoid possible activity reference leaking.  <br /> Feature : Implement camera image picker.                                                                                                                                                   |
| 1.0.5  | Feature : Support fragments.  <br /> Feature : Implement file picking from initial directory. <br /> fix : obsolete app icon unintended overriding.                                                                                                         |
| 1.0.6  | Enhancement : Add file extension to video files and images picked from camera.                                                                                                                                                                              |
| 1.0.7  | Fix bug : Captured image file from camera is corrupted.                                                                                                                                                                                                     |
| 1.0.8  | Enhancement : High quality camera image.                                                                                                                                                                                                                    |
| 1.0.9  | Refactor : Replaced coroutines by Executor & Handler.     <br /> Update : Updated compileSdk to 34.                                                                                                                                                         |
| 1.0.10 | Fix bug : capture image issue for the second time.     <br /> Fix bug : installing multiple apps using the library on the same device.         <br /> Close issue : https://github.com/Atwa/filepicker/issues/25                                            |  
| 2.0.0  | Feature : Filter files with MIME type. closes issue https://github.com/Atwa/filepicker/issues/14  <br /> Feature : Support multipicker. closes issue https://github.com/Atwa/filepicker/issues/19 <br /> Feature : Capture video from camera. closes issue https://github.com/Atwa/filepicker/issues/23 |  


### Contribution

Please feel free to make a pull request or fork.

### Rate

If you find this repository useful please give it a star .
