To run the application, you need to connect locally to the database or connect to the H2 database. The settings for these databases are located in the properties files. 
Then, execute the command java  ```-jar NewsParser-0.0.1-SNAPSHOT.jar ```
in the directory where this file is located. When the JavaFX window opens with a locally connected database, the news will immediately start displaying—all the news that was parsed.
However, if connected to the H2 database, you will first need to select a time range, and only then will the news start displaying.
