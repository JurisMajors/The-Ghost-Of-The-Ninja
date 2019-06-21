![Game logo][gamelogo]

[gamelogo]: src/group4/res/textures/start-bg.png "Game logo"


Watch the trailer here: https://youtu.be/y6DaoWH9sGQ

======

In the provided zip you will find two jar files, one for windows and one for linux, and a src directory. The src directory contains all our resources.
To run the game type `java -jar platformer_linux.jar` (if you are running linux).

## Game controls

You can move the the player left and right with the *a* and the *d* key respectively.
Press *space* to jump, and press *enter* to attack in the direction you are currently facing.
Holding *shift* while walking causes you to move slightly.

When walking over a totem you will see a popup with the controls available to spawn the ghosts.
The popup looks as follows

[ghostcontrols]: src/group4/res/textures/ghost_controls.jpeg
[ghostcontrols]

In short the controls are:
+ *c* - challenge the ghost, you have to race the ghost to the next totem with the same colour as the totem you are currently standing on. If you beat him you win 500 points.
+ *g* - the ghost will lead the way through the level, all you need to do is follow him. This action costs 100 score.
+ *h* - the ghost takes you on its back and moves you through the level to the next totem of the same colour as the current one. this action costs 500 score.

*C* for challanging, costs no score.
*H* for regular help. He simply shows where to go (100 score)
*G* for carry help. He carries you with him (500 score)

For testing purposes, we also added `-free` flag so that these helpers cost no score.

## Starting level

When you spawn in the game you start in a beginner level which has an exit to all the levels in the game. Walk into one of those levels to start playing.

## AI Training

We have added a live training feature to our program. You can see live training of an AI with or without GUI.
To enable this feature, you must use the `-train` flag.
If you choose the `-nogui` flag, no rendering will happend and it will apply multi-threading.

If you use the `-trainhelp` flag, then you can see all possible parameter changes that you can make for training (mutation, population etc.)

## Other

For seeing other options (not including AI params) that you have, use the `-help` flag.

Enjoy !!!!
