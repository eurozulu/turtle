# Turtle
### LOGO Interpreter code challange  

As an experiment in writing an interpreter I picked up a very old code challange from my works archives.  
Initially written with the ancient art of Java swing for the GUI, as per the original challange.  
Perhaps will convert to more modern framework. (Flutter?)  

### Usage
`java -jar turtle.jar`

#### GUI
The Main screen has three panels:    

- Draw Panel  
  Displays the graphic output of the commands.  
- Command Panel  
  The Edit panel to view and enter commands
- Console  
  Displays output and error information.  
  
  
#### Draw Panel
Draw Panel contains the Turtle, showing the current draw point.  
As commands are entered, the Turtle will react and leave a path of lines behind it, forming a graphic.  

#### Command Panel
The command panel is where you enter commands for the turtle.  
Type a command and hit enter and that command will be passed to the Turtle.
Hitting enter on any line will execute the last line above the cursor.  
To execute the entrie window of commands, use the `Run all` button.  
Right clicking on this window gives an option menu to clear the screen.  
The command panel may be saved or loaded from file using the file menu.


#### Console
The console panel is where the output of the commands and any errors are displayed.  
It is a mirror of the Standard OUT and Standard ERR streams.  
Right clicking on this window gives an option menu to clear the screen.

### Toobar
The Menu bar contains some simple navigation buttons to move the turtle and control the Pen.  
Clicking the 'Draw' button will enable the pen (pen down command).
The command will be entered into the command screen. 

---
## Logo

The Language consists of simple commands which use one or more parameters.  
e.g. `forward 10`  
This will move the turtle forward 10 'units'.  
  
Commands may be followed by additional command using a space to seperate them.  
e.g. `forward 10 right 90 forward 20`  
This produces three commands, Forward, right and forward.  Any number of commands may be strung together like this.  
  
Basic Turtle commands:  

| Command | sbbreviations | Argument(s) | Description | Example |
| --------|---------------|-------------|------------ | ------- |
| `forward` | `fd`  | Single number of units to move | Moves forward at the current angle of rotation | `fd 25` |
| `back`  | `bk` | Single number of units to move | Moves backwards at the current angle of rotation | `bk 25` |
| `left`  | `lt` | Single number of the angle to rotate | Rotates the turtle by the number of degrees | `lt 90` |
| `right`  | `rt` | Single number of the angle to rotate | Rotates the turtle by the number of degrees | `rt 90` |
  
The Turtle leaves a path trace to draw a line whenever the "pen" is on.  
To ensur ethe pen is on use the `pen down` commands.  The path can be cleared using the `clean` command.  

Additional control commands:  


| Command | sbbreviations | Argument(s) | Description | Example |
| --------|---------------|-------------|------------ | ------- |
| `pen` | `pu, pd, draw`  | Single argument of "yes/no" or up/down or true false | Controls the pen state.  When pen is down the path is drawn. | `pen down` |
| `hideturtle`  | `ht` | none | Hides the turtle, leaving just the path on screen | `hideturtle` |
| `showturtle`  | `st` | none | Shows the turtle, showing the path and turtle on screen | `showturtle` |
| `home`  | `hm` | none | Moves the turtle to the center of the screen without leaving a path.  Current path is unchanged. | `home` |
| `clean`  | `cn` | none | Clears the current path, leaving turtle in position | `clean` |
| `cleanscreen`  | `cs` | none | Clears the current path, returns turtle to home | `cleanscreen` |
  

### Repeating
Commands may be repeated any number of times using the `repeat` command.  
Repeat has a 'count' number to define the number of times to repeat, followed by a 'code block',  
A series of commands enclosed in square brackets.  
e.g. `repeat 4 [ fd 20 lt 90 ]`  
This will execute the commands `fd 20 lt 90` four times, generating a square.  
  

### Patterns  
Patterns are predefined commands grouped into a single name.  
They are similar to simple 'functions'.  Once defined, the pattern name can be used to execute all the grouped commands in the pattern.  
  
A pattern is defined using the `pattern` command, followed by the pattern name, followed by a 'code block',  
A series of commands enclosed inside square brackets.  
e.g. `pattern square [ fd 10 lt 90 fd 10 lt 90 fd 10 lt 90 fd 10 ]`  
Defining the pattern has no output, but creates the new `square` command.  
After defining the pattern it may be called by simply giving its name.  
e.g. `square`  
  
Both `repeat` and `pattern` may be nested inside each other.  
e.g. `pattern square [ repeat 4 [ fd 10 lt 90 ]]`  
This will create a new pattern which generates exxactly the same as the previous square example.  
  
#### Pattern Arguments
Patterns may define arguments, which must be provided when the pattern is called.  
To define an argument use the `{}` placeholder to mark where that value should appear within the pattern.  
e.g. `pattern square [ repeat 4 [  fd {} lt 90 ]]`  
The forward command will use the argument following `square` to define the forward distance.  
`square 40` or `square 3` draws squares of different sizes.  
Arguments are placed in each placehold in the order they appear.

----  

#### Files
The command window can be saved using the file menu or replaced with a file loaded via the open menu.
