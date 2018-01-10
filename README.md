# geo-user-project #

System for user geolocation consisting of a REST server and Android app for users.


## PREREQUISITES

- Database MySQL 5.7
- Oracle JDK 8


## ACCESS THE DATABASE

In the same directory as the server executable jar, create "**settingsdb.json**" file by entering the following text:
```
{userDB:'**enter the username of the database user**',passwordDB:'**enter the password of the database user**'}
```
###### default values: {userDB:'root',passwordDB:''}



## BEFORE RUNNING THE SERVER

In the same directory as the server executable jar, create "**settings.json**" file by entering the following text:
```
{port:**enter number of server port**,web_base_dir:'**enter name of the static content web directory**'}
```
###### default values: {port:8182,web_base_dir:web}
