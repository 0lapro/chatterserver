### Disclaimer
This program is free for learning and educational purposes. 

# Chatterserver
A stand-alone instant messenger server with simple GUI that allows you to control the server without closing the application.

## Semantics
Everything in the program is named to reflect its purpose. This makes writing test cases unambiguous and testing more meaningful.

## Object-Oriented approach
1. The OOP approach separates concerns and allows room for easy improvement so anyone can improve this server to its own taste.
2. The interfaces are used to separate common behaviors and let any class implement them when needed.
3. Classes that have common purposes are grouped into the same package.
4. There's also room for inheritance if needed.
5. All classes are loosely coupled to promote indepedence and avoid unneccessary dependencies.

## Multithreading
The threads are managed properly by the executor service and the GUI runs on the Event Dispatching Thread for thread-safety.

## Networking
ServerSocket is the server socket that waits for client requests to come in over the network.
Socket (called just socket) is the server endpoint used for communicating with the client.

## Serialization
This is added so that the server can restore its last working state or any chosen normal state in case of any unexpected incident. If anything happens to the server and it shuts down unexpectedly (which is not supposed to happen), all you need to do is restart the server and it will restore to it last working state.

## Logging
The logging allows additional understanding of the application for analytic purpose.
The comments allow you to see clearly my intention when writing the code and what each line of code is aimed to achieve.

## How it works
1. One or more client(s) [GITHUB] (https://github.com/0lapro/chatterclient) can join the chat  by connecting to the server with a username.
2. Either the server or client can run independently on the same network and either one of them can start first without affecting each other.
3. Click the start server button to start the server.
4. If you stop the server while clients are connected and chatting, no message will be sent or received while the server is stopped. But if you start the server again, all connections will automatically become active again and chatting can resume.
5. If you close the server GUI, no message will be sent or received and if you start the server again unlike 4 above, connected users must reconnect before chatting can resume.
6. On the same network, you can run the chatterserver and chatterclient on either same or separate computers on Windows or Linux where Java and JDK is installed or you can add or tweak one or few lines of code to add a one-to-one chatting capability and run it on different networks completely.


