# File Storage Service - Spring Boot Application

This Spring Boot application is designed to handle file uploads, compression, and storage in a MySQL database. The uploaded files, including images, are compressed and stored in the database. Additionally, the application provides an endpoint to retrieve and decompress files based on their filename.

## Feature

- Upload files (images, etc.) and compress them for storage in the database.
- Retrieve and decompress files based on their filename.
- Maximum file size for uploads: 5MB.

## Technologies Used

- Spring Boot
- Spring Web
- Spring Data JPA
- Lombok
- MySQL

## Controller Class

The StorageController class is the main controller for the File Storage Service Spring Boot application. It is responsible for handling HTTP requests related to file storage and retrieval.

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
---
Feel free to explore & study the code, and use them as a reference for your own learning.

Happy coding! 😄
