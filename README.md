# Running Instructions
This App is created using spring boot (backend) and Angular (frontend). The app is designed based on the sample_data.xlsx file data only.

## Frontend
    npx ng serve
## Backend
    ./mvnw spring-boot:run (Linux & MacOS)
    ./mvnw.cmd spring-boot:run (Windows)
## Importent Points
- The default port of Angular app is 4200. Hence 'localhost:4200' has been allowed in the backend configuration for CORS to work. If the origin is changed then that needs to be added in the application.properties file of the backend. To allow multiple origins, append then with comma.
- The above commands are for running the app in development mode.
- To test the backend without frontend, import the Postman collection file - postman.json - into postman.
- For uploading file, only use XLSX file format. Other formats are not acceptable by the server.
