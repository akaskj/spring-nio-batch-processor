Spring nio and batch processor

In this project Java nio is used to look for changes in a particular directory(user-feed)
and when a new file is created a Spring batch job is run on the file and the data is populated
to the database(user database and user_data table).


To run the project/verify:

1. clone project
2. run sql script which is in resources/project/user.sql in you local mysql setup
   (this will setup all tables and databases required)
3. create directory "user-feed" in yur home directory and "processed" inside user-feed
4. run command "mvn clean install"
5. run mvn spring-boot:run
6. copy sample.csv into user-feed directory

   Magic Happens!! (documentation coming soon)

7. verify user_data table and data should be populated