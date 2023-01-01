<meta name="google-site-verification" content="INBb9H70zb0cDkqgBLAYtuwQSwrtc07yNG8tFwr8fNQ" />

# `<Battleship in Java>`

## Description

A digital reproduction of the board game Battleship.

This project has been developed as a school project for learning Sockets in Java.

## Technologies used

**Java 19**: programming languange used for the code

**TCP Sockets**: Transmission Control Protocol has been used for the Client-Server communication, as it was required by the exercise delivery.

**Java Swing**: for the graphics, in order to develop all the users' screens.

## How run the program

For testing the project, just import battleship folder in VS Code:

- Run **Server.java** for running the server, where users will connect to.
- Run **Main.java** for playing the game.

## How play the game

### Connection

After starting up the server (1 - for connecting, 2 - for showing server ip)

![server](img/server.png)

Users can connect using server ip. (After signing up, users are saved from the Server into res/txt/users.txt as username;password)

![menu](img/menu.png)

![login](img/login.png)

### Starting a new match

After login, each user can send a request for playing to another user for starting a new match.

(Select the user's nickname you want to play with and click on play. If the other user accepts your request, the match starts)

![users online](img/users.png)

### Managing ships position

Now, each user can manage his ships position:

- **left mouse click** for selecting the ship to move
- **left click again** to confirm the new position
- **right click** for turning the ship

![dashboard](img/dashboard.png)

After each user has clicked on START, game starts and every user can send attacks to the opponent, by clicking on the opponent grid.

![match](img/match.png)

Game ends when the ships of one of the two are sunk all.

Furthermore, users can communicate using the chat, where events are also shown.

![chat](img/chat.png)

### Additional features

- Before starting the match, player can use RANDOM POSITIONING button for setting up the ships randomly.
- When one of the user disconnects from the Server, the other player is advised with an alert and match ends.
- All clients' operations (connect, disconnect, game events, messages) are stored in res/txt/logs.txt file and the server can view them using PRINT ALL USERS OPERATIONS (4) command.

## Credits

### Icons

[Battleship icon created by iconixar](https://www.flaticon.com/free-icon/ship_3939752?term=battleship&page=1&position=9&origin=tag&related_id=3939752) 

[User icon created by Freepik](https://www.flaticon.com/free-icon/user_456212?term=user&page=1&position=2&origin=search&related_id=456212)

[Info icon created by Freepik](https://www.flaticon.com/free-icon/info_471662?term=info&page=1&position=4&origin=search&related_id=471662)

[Battleship icon created by smashingstocks](https://www.flaticon.com/free-icon/battleship_6175141?term=battleship&page=1&position=2&origin=search&related_id=6175141)

[Error icon created by Ilham Fitrotul Hayat](https://www.flaticon.com/free-icon/cross_2569174?term=error&page=1&position=3&origin=search&related_id=2569174)

[Send icon created by Freepik](https://www.flaticon.com/free-icon/send_786407?term=send&page=1&position=21&origin=search&related_id=786407)

## License

[GNU General Public License v3.0](LICENSE)

