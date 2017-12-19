Dylan Morgen
dmorgen2
33

For my 2048 project I have several classes. 
First is the main class (Project1_2048.java)
This class starts and creates my jframe, and locks it to a size. 
The Tile class holds the blueprint for the tile object. The tile object holds the color, value, origin on screen, position in the board array, and the method for choosing the color of the tile. 
The Canvas class (Canvas.java) holds the bulk of the code for the game. It creates a 2D array of Tiles, and has a method to paint all of those tiles, as well as the score and various messages. The class holds a keyboard listener for certain inputs, which run respective methods. To move up down left or right according to 2048 rules, I first rotate the array so the direction I'm moving is left to right. I then run a 'move' method on it, which keeps track of all the intricacies of 2048. The move method calls on the nexttile method for each tile, which either finds a tile to swap with, or outputs a 'stay put' output. I have a few other methods (like those for displaying a message, game over, centering text, and reset), which are used for various purposes. All of my code is commented, in case the reviewer has any issues understanding it. 
One key difference with the directions is that I chose to make the game like the original game re the score. Rather than keep track of the number of moves, the score is comprised of the sum of every combination. 
I put a lot of effort into making it work and look like the original game, and hope this is reflected in the final product. 
For convenience, I also am including a runnable jar file. (if you are on a mac, you have to right click on it in the finder and press open to be able to run it). 
Thank you

 