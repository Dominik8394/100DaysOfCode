### #100DaysOfCode

# Objectives for the Challenge

- Get a better understanding of Spring Framework
- Learn how to persist data (effectively)
- Get a better understanding of Spring Data (JPA) and how to use it
- Learn how to use a NoSQL database such as MongoDB
- Learn a SQL-based Database
- Apply knowledge in some kind of project such as a small online-store backend to store user information, store items, user authentification etc.

# Data Persistance

## MongoDB
TODO
### What is MongoDB?
TODO
### Applications 
TODO
### Features

### Document Structure and working with Collections

Documents are basically JSON (JavaScript Object Notation) files which are structured as follows

        {
            field1: value1,
            field2: value2,
            array: [field1, field2],
            address: {
                street: value,
                city: value
            }
        }

JSON is a common file structure used in WWW to transmitt data between clients and server. 

If a document is generated & inserted into a collection with 

        db.customer.insert({first_name: "John", last_name: "Doe"});

you can retrieve the document by using

        db.customer.find();

this retruns a list of all objects in the collection as a JSON, e. g.

        { "_id" : ObjectId("5c222e425b987e0c6ac7485b"), "first_name" : "John", "last_name" : "Doe" }

The Id field was generated automatically by Mongo. In a Relation Database, this has to be done manually. Also you would have to take care of automatically incrementing the ID as well as setting it as the Primary Key in order to uniquely identify each object of your collection. 

Unlike RDBS, in a NoSQL database you don't have to specify the columns, e. g. if you create the following objects

        db.customer.insert([
            {first_name: "John", last_name: "Doe"},
            {first_name: "Steven", last_name: "Smith"},
            {first_name: "Steven", last_name: "Smith", gender: "male"},
            ]);

Notice that the last object holds an additional attribute the others don't. This would cause an error inside a RDBS because a gender columns hasn't been specified. But within Mongo this doesn't cause any harm and the objects are created with the last one holding an additional attribute specifying its gender.

Updating an object is almost the same as creating one. If you want to update an object in a collection you have to specify the following:

1. Some kind of predicate to identify the object you want to update
2. fields and values that you want to update

So for example if you want to update the customer "John Doe" you would use:

        db.customer.update({
            {first_name: "John"}, // identifier
            {
                first_name: "John", 
                last_name: "Doe",
                gender: "male"
            }
        }):

Note that the identifier uses the field "first_name" to look for the object. In a real-life application it is not recommended to use an attribute such as first_name since their could be objects that carry the same value. As you might have realized, in order to update an object you have to specify all fields of the object even if you only want to add an additional attribute or change one field value. This can become quite complex and time-consuming if the object has 10+ attributes. The following key-word can be used to lessen the pain of updating an object:

        db.customer.update({
            {first_name: "John"}, // identifier
            {
                $set: {gender: "male"}
            }
        }):

By using $set, the query is told to keep all previous information of the object and merely changing the attribute that is passed in after the set key-word. 


### MongoDB Syntax

        // create database
        // also switches directly into dbs
        use <name>

        // create a user that operates on db
        db.createUser({
            user: "dominik",
            pwd: "1234",
            roles: ["readWrite", "dbAdmin"]
        });

        
        /**
        * Create a collection:
        * Collections are similar * to tables in RDBS, they 
        * hold documents or records
        **/ 
        db.createCollection('customer');

        // insert object into collection
        db.customer.insert({first_name: "John", last_name: "Doe"});

        // insert multiple objects into collection
        db.customer.insert([
            {first_name: "John", last_name: "Doe"},
            {first_name: "Steven", last_name: "Smith"}
            ]);

        // update an object 
        db.customer.update({
            {first_name: "John"}, // used to identify the object that should be changed
            {first_name: "John", last_name: "Doe", gender: "male"}
        });

        // update an object with set
        db.customer.update({
            {first_name: "John"}, // used to identify the object that should be changed
            {$set:{ gender: "male"} // set saves all information previous the new attribute
        });

        // update an object with inc
        db.customer.update({
            {first_name: "John"}, 
            {$inc:{ age: "5"} // increments value by five
        });

        // remove a field from an object
        db.customer.update({
            {first_name: "Steven"},
            {$unset:{age: 1}}
        });

        // upsert if an object that should be updated is not existent
         db.customer.update({
            {first_name: "Maria"}, // used to identify the object that should be changed
            {first_name: "Maria", last_name: "Smith", gender: "female"},
            {upsert: true}
        });

        // remove an object
        db.customer.remove({
            {first_name: "Steven"}
        });

        // find an object with or
        db.customer.find({
            {$or:[{first_name: "Sharon"}, {first_name: "Troy"}]}
        });

        // find an object in assistance of lt (less than)function
        db.customer.find({
            age: {$lt: 40}
        });

        // find an object with value of a multi-attribute field
        db.customer.find({  
            {"address.city":"Boston"}
        });

        // Query for an array
        db.customer.find({memberships: "mem1
        });

        // sorting
        db.customer.find().sort({last_name: 1} // 1 sorts in ASC, whereas 2 sorts in DESC
        );

        // count documents
        db.customer.find({gender: "male"}).count();   

        or

        db.customer.find().count();

        or 

        db.customer.find().limit(4); // retrieves the first four

        // Iterating using for-each loop
        db.customer.find().forEach(function(doc) {print("Customer Name: " + doc.first_name)
        });
        