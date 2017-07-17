The program is the server of the system, it need a MySQL databased to support operation. The adnimistrator account in the MySQl is 
username = "root"
password = "930213"

The databased is names "clients" and the table names  "users"
The attributes and type of attributes is as follow:

Field    | Type
username | varchar(20)
password | varchar(50)
id       | varchar(150)
mobile   | varchar(20)
email    | varchar(100)
mobileid | varchar(40)
state    | tinyint(1)        if state = 0 the account has been locked
kskey    | varchar(100)      to save the symmetric key

How to run the program:
1. make sure the database set correct
2. import the project into IDE (IntelliJ is best)
3. run the project