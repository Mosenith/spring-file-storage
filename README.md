# File Storage Service - Spring Boot Application

This Spring Boot application is designed to handle file uploads, compression, and storage in a MySQL database. The uploaded files, including images, are compressed and stored in the database. Additionally, the application provides an endpoint to retrieve and decompress files based on their filename.

## Feature

- Upload files (images, etc.) + compress them and save in the Database or Folder.
- Retrieve and decompress files based on their filename.
- Maximum file size for uploads: 5MB.

## Technologies Used

- Spring Boot
- Spring Web
- Spring Data JPA
- Lombok
- MySQL

## Controller Class

### From Database
The StorageController class is the main controller for the File Storage Service Spring Boot application. It is responsible for handling HTTP requests related to file storage and retrieval from database.

```java
@RestController
@RequestMapping("/image")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        byte[] imageData = storageService.downloadImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_GIF)
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = storageService.uploadImage(file);

        return new ResponseEntity<>(uploadImage, HttpStatus.OK);
    }
}
```
### From File System

This is the file path which was set in Service Class. 
```java
private final String FOLDER_PATH = "/Users/mosenithheang/Downloads/FileStorage/";
public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        FileData fileData = fileDataRepository.save(FileData.builder()
        .name(file.getOriginalFilename())
        .type(file.getContentType())
        .filePath(filePath).build());

        // upload image to file system
        file.transferTo(new File(filePath));

        if(fileData != null) {
        return "File Successfully Uploaded : " + filePath;
        }

        return null;
        }

public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
        }
```
When calling @PostMapping, the provided file will be saved to the above folder.

```java
@GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData = storageService.downloadImageFromFileSystem(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_GIF)
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    // File System Storage
    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = storageService.uploadImageToFileSystem(file);

        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
```

#### Endpoints
* Endpoint: GET /image/{fileName} or POST /image
* Description: Allows the retrieval/upload of an image by its filename from the storage.
* HTTP Method: GET, POST
* Request Parameters:
- {fileName} => The filename of the image to retrieve.
- image => The image file to be uploaded.
* Response: HTTP Status Code: 200 OK
* Content Type: Image (GIF, JPEG, or PNG)
* Body: The image data in bytes or The response message indicating the successful upload.

### Utility for Image Compression and Decompression
The ImageUtils class in the com.filestorage.util package is a utility component responsible for image compression and decompression. It utilizes the Deflate algorithm for compression and the Inflate algorithm for decompression.

##### compressFile
Compresses the input byte array using the Deflate algorithm.

```java
public static byte[] compressFile(byte[] data)
```

##### decompressFile
Decompresses the input byte array using the Inflate algorithm.

```java
public static byte[] decompressFile(byte[] data)
```

## Database VS File System
Storing or retrieving files from a database prioritizes security over speed, even though it may take more time compared to using a file system. 
Hence, if frequent retrieval of files/images is necessary, employing a file system approach proves to be more convenient.

---
Feel free to explore & study the code, and use them as a reference for your own learning.

Happy coding! 😄
