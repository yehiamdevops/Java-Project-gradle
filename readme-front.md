# workflow:
- change stages add properties to the object user this object will be sent as a json to the back
# javafx:
- the one class who extends application is the opening screen the others getting called 
# signup:
- for the whole process of the sign up im going to fill an object and eventully it will get created in db but only after the process is done until then the object will be passed along the stages.
# UserDetails:
- username
- pictures(up to 6)


# PrefrencesWindow:
- birth date
- gender 
- the gender you would like to date with
- bio

# whats next:
- ## token on java:
  - use the prefrences class for easier token manage


# main page:
- get a token on login 
- save the token in some token manger class
- /myProfile route(token require):
  - new stage on java fx
  - get request of the profile based on token on load
  - put request + button event for changing details 
- /like and /dislike routes means like and dislike logic
- like collection on mongodb:
 - model:
  - likesender
  - likereciever
- match collection
- match logic:
  - token for username findone the sender
  - List<User> for username findone the reciver
  - on any findone the object of the reciver must contain the sender and object of the sender must contain the reciever.
- if token stop working clean it from token manager
- cancel match logic:
 - cancel button event 
 - /cancelmatch route
 - deleteone on mongo db and delete one from the canceled client deleteone for the match collection 
- drop token on logout -- tokenmanager.cleantoken
- add pictue modulo logic
- add object<user> logic
- empty list is a null list
# queue:
- peek(): Retrieves the head of the queue without removing it. If the queue is empty, it returns null.
- poll(): Retrieves and removes the head of the queue. If the queue is empty, it also returns null.





# get request:
- only in get requests the type will be list of users -- List<User>


# login window:
- add usr or padd invalid in javafx

# on logout action:
- clear token
- switch stage to login
# object mapper:
- om.convertValue - can cast Map<String, Object > to class of your needs.
the args it needs is a map and class you created that its properties matches the type value of the map keys. 


# dislike/like javafx logic:
  - clean the user in gui
  - move to another object in queue
  - if the queue is null use pagination
  - if after pagination its null 
  - show user i cannot display users for him.
  - if user liking another user 
  - send the user he liked under his name in a likes collection
  - send the liked user a notifaction someone liked them
  - do the above actions
  - if they both liked each other comments contain the object of one another in there comment section
  they both removed from the likes collection and moving into matched collection


# remove match:
  - it deletes the match at both ends
  - gui remove the match at 