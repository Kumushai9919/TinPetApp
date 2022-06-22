 <!-- PROJECT LOGO page -->
 <h3 align="center"> TinPetApp - Android Application using MongoDB Realm <br> <br> 틴펫</h3> <br>
 <p align="center">
  <img width="270" height="430" alt="Screen Shot 2022-06-03 at 5 53 59 PM" src="https://user-images.githubusercontent.com/83897840/171822416-7dcdb593-627a-44c0-b081-ec14b8efc585.png">
  
   
<!-- Table of Contents -->
 <details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
       <li><a href="#preview">Preview</a></li>
        <li><a href="#system-design">System Design</a></li> 
       <li><a href="#tools-used">Tools used</a></li>
        <li><a href="#roadmap">Roadmap</a></li>
      </ul>
    </li>
     <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#mongoDB-Realm-Database">MongoDB Realm Database</a></li>
      </ul>
    </li>
    <li><a href="#features">Features</a></li>
    <li><a href="#contributing">Contributing</a></li>
<!--    If you would like to contribute in this project, you are always welcome.  -->
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li> 
  </ol>
</details>
 
 
 
 
 <!-- ABOUT THE PROJECT -->
## About The Project
 There are currently thousands of dating apps on the internet like Tinder where people can connect with each other and find love and friendship. However, there are not many such interesting applications for pets. Our little friends need attention and communication just like humans, so I decided to develop an app that provides  the ability to find friends around by registering pets.<br>
 
 
### Preview
 This application is a project where you can create a user account and enter pet information such as the pet's name, location, photo, and hobbies to find nearby friends through the user's location. If you like a friend, you can click the heart button below the main page picture to save  and view detailed information about that user and view them in the saved section.
 
 <p align="center">
  <img width="790" alt="Screen Shot 2022-06-03 at 5 53 18 PM" src="https://user-images.githubusercontent.com/83897840/171822313-f048039c-0189-4c23-88c3-980e5f920818.png">

  
### System Design
 MongoDB Realm is a serverless platform and mobile database. MongoDB Realm syncs data in realm time and executes it immediately when data is changed in the user or Atlas database. Both iOS and Android mobile apps include the Realm mobile database. Your app reads and writes data to and from its Realm database, whether the device is connected to a network or not.
Whenever a device is within network range, Realm synchronizes data with other devices via the Realm backend service.
MongoDB Realm is also responsible for data synchronization with MongoDB Atlas Cloud.
  <p align="center"> 
 <img width="398" alt="image" src="https://user-images.githubusercontent.com/83897840/174522445-223dc2f9-a0bb-4464-b966-c0dd8525a448.png">

### Tools used
  <b> Tools that I used to implement the project </b>
* Design Template: <a href="https://www.figma.com/file/YwiNTaDrtweS1S8wJyaxki/TinPet?node-id=0%3A1"> Tinpet Figma</a>
* Mobile Appication: <a href="https://www.java.com/ru/">Java</a>, <a href="https://developer.android.com/studio">Android Studio</a>
* DataBase: <a href="https://www.mongodb.com/realm" >MongoDB Realm Database</a>
* Map: <a href="https://www.google.com/maps/" >Google Map</a>
  
### RoadMap
   <p align="center"> 
    <img width="871" alt="Screen Shot 2022-06-22 at 2 06 35 PM" src="https://user-images.githubusercontent.com/83897840/174947882-bf9c02c7-9472-4841-b758-856daf43e618.png">
 
    
### Prerequisites  
  MongoDB Realm implementation
  -  Create new cluster <br> 
  -  Implement a database and collection
  -  Add connection string into your application code
    
### MongoDB Realm Database
    
  - Configured petdatas-database in MongoDB Atlas and built pets and users Collection to store data of users and pets. 
    <br>
    <img width="333" alt="image" src="https://user-images.githubusercontent.com/83897840/174953185-f06c1c0e-93de-4136-9b6b-ea705e5c9b01.png">
 
  - In the App Services created Application-0 in MongoDB Realm, and in the Users section, it shows the current registered users of this app. Each user is      signed up by email and has its own ID.
    
   <img width="104" alt="image" src="https://user-images.githubusercontent.com/83897840/174958356-e2a54ed0-bcd4-42ca-bbf2-48e587756576.png"> <img width="232" alt="image" src="https://user-images.githubusercontent.com/83897840/174958377-db31440b-9da3-4931-a79a-051329fa4eb5.png">



    
## Features
    
## Contributing

## License
Distributed under the MIT License. See LICENSE for more information.

## Contact
You can contact me here: 
    email: kumushnaz@gmail.com
    instagram: [kumushn_](https://www.instagram.com/kumushn_/)
