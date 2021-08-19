# Disclaimer
This program is free for learning purposes. 

# Chatterserver
A stand-alone instant messenger server with simple GUI that allows you to control the server without closing the application. The threads are managed properly by the executor service and the GUI runs on the Event Dispatching Thread for thread-safety.

# Object-Oriented approach
1. The OOP approach separates concerns and allows room for easy improvement so anyone can improve it to its own taste.
2. The interfaces are used to separate common behaviors and let any class implement them when needed.
3. Classes that have common purposes are grouped into the same package.
4. There's also room for inheritance if needed.
5. All classes are loosely coupled to promote indepedence and avoid unneccessary dependencies.

# Semantics
Everything in the program is named to reflect its purpose. The naming of the packages, classes, objects, functions, and variables all follow proper semantics which reflects what they do in the program and plays a big role in testing.

# Logging
The logging allows for additional understanding of the application for analytic purpose.
Two types of logging in the program, one is rough and the other is not. Rough logging is the print lines attached to most of the line of codes to see or track whether the code functions as intended and to track where any problem occurs. This rough logging was included when I was testing the code but left there just in case anybody is interested, but you can remove them carefully without mistakenly deleting any other thing unintentionally, and it will not affect the code. The other logging is a logger class from one of the utility tools in the program. The comments allow you to see clearly my intention when writing the code and what each line of code is aimed to achieve.

# How it works
1. One or more client(s) [GITHUB] (https://github.com/0lapro/chatterclient) can join the chat  by connecting to the server with a username.
2. Either the server or client can run independently on the same network and either one of them can start first without affecting each other.
3. Click the connect button and enter your username to start chatting
4. If you stop the server while clients are connected and chatting, no message will be sent or received while the server is stopped. But if ou start the server again, all connections will automatically become active again and chatting can resume.
5. If you close the server GUI, no message will be sent or received and if you start the server again unlike 4 above, user(s) must connected users must first reconnect before chatting can resume.
6. On the same network, you can run the chatterserver and chatterclient on either same or separate computers on Windows or Linux where Java and JDK is installed or you can add or tweak one or few lines of code to add one-to-one chatting capability and run it on different networks completely.


