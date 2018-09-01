# MongoDB GridFS
In MongoDB, use GridFS for storing files larger than 16 MB.

GridFS is a specification for storing and retrieving files that exceed the BSON-document size limit of 16 MB.

Instead of storing a file in a single document, GridFS divides the file into parts, or chunks [1], and stores each chunk as a separate document. By default, GridFS uses a default chunk size of 255 kB; that is, GridFS divides a file into chunks of 255 kB with the exception of the last chunk. The last chunk is only as large as necessary. Similarly, files that are no larger than the chunk size only have a final chunk, using only as much space as needed plus some additional metadata.

![alt text](https://res.cloudinary.com/haritkumar/image/upload/v1535802554/github/gridfs1.png)

## Setup instructions
- Import as maven project
- Create a mongo database named **mongodb** or edit **MongoConfig.java**
- Run on server
- Access http://localhost:8080/mongodb-gridfs/

![alt text](https://res.cloudinary.com/haritkumar/image/upload/v1535804733/github/1g.png)
![alt text](https://res.cloudinary.com/haritkumar/image/upload/v1535804733/github/2g.png)
![alt text](https://res.cloudinary.com/haritkumar/image/upload/v1535804733/github/3g.png)
![alt text](https://res.cloudinary.com/haritkumar/image/upload/v1535804733/github/4g.png)


## MongoDB setup
Connect to DB

```sh
mongod --dbpath /data/db/
```

Open another terminal and connect to DB

```sh
mongo
```
Create the user

```sh
use gridfs_db
db.createUser(
  {
    user: "root",
    pwd: "root",
    roles: [ { role: "readWrite", db: "gridfs_db" } ]
  }
)
```

Stop the MongoDB instance and start it again with access control

```sh
mongod --auth --dbpath /data/db
```

