# Week 05: Functional Programming

Functional Programming is a paradigm that's made it into many different programming languages. At 
first glance, it may seem like it's just "using functions to solve problems," and to an extent, it is.
However, it's a little more nuanced than that. The big idea of functional programming is that you can
store, pass, and manipulate functions.

Functional Programming comes from mathematics, and borrows a few ideas from there. One of the ideas 
that gets upheld by the paradigm is immutability. Functions should not change their data as a side
effect, but should instead return copies or new data based on the existing data.

## About Functions and Methods, and References

Functions are one of the first things that we learn. They're essential for breaking code up into 
smaller and more understandable pieces. One of the lesser known things is that in Python, we can store
functions in variables in two different ways: lambdas and function references. 

Lambdas are meant to be short implementations of functions; simple bodies that do small pieces of 
work. In Python, the look like this:

```python
addTwo = lambda x: x + 2
print(addTwo(5)) # Prints 7
```

In Java, it's a little more complex, but they look like this:

```java
public class Main {
    interface ArithmeticOperation {
        int apply(int value);
    }

    public static void main(String[] args) {
        ArithmeticOperation addTwo = (value) -> value + 2;
        System.out.println(addTwo.apply(5)); // Prints 7
    }
}
```

The other option we have is function references. This is where we can write full function bodies, and 
then send them into other functions as parameters. Python makes this simple: you just pass the name as
a parameter.

```python
def printWithFive(fn):
    print(fn(5))

def addTwo(value):
    return value + 2

printWithFive(addTwo) # Notice how we don't add the parenthesis to call the function here.
```

In Java, we do much the same, but have to account for types, and use the double-colon operator:

```java
public class Main {
    interface ArithmeticOperation {
        int apply(int value);
    }

    public static void main(String[] args) {
        printWithFive(Main::addTwo); // Again, not calling the function, but that's a little clearer here.
    }
    
    public static void printWithFive(ArithmeticOperation operation) {
        System.out.println(operation(5));
    }

    public static void addTwo(int value) {
        return value + 2;
    }
}
```

## The Next Step

Let me show you one of the ways I like to take this to the next level. The main way I use these are in
higher-order functions. We'll talk about those next week. This week, let's look at another thing we
can do something with these functions.

A shell contains a variety of tools. I've created a simple one here that has a few built in commands
and nothing else. Right now it's built like a mess, not even split up into functions. Here's the python
version:

```python
from os import path
import os
import sys


def main():
    """
    Main function. Runs a mini shell.
    """
    while True:
        raw_input = input("?> ")
        command = ""
        args = []

        try:
            command, args = parse_input(raw_input)
        except:
            continue

        if command.lower() == "echo":
            print(" ".join(args))
        elif command.lower() == "fi":
            combined_args = " ".join(args)
            if os.access(combined_args, os.F_OK):
                filepath = path.abspath(combined_args)
                print(f"File {combined_args}")
                print(f"   Path: {filepath}")
                print(f"   Read: {'Yes' if os.access(combined_args, os.R_OK) else 'No'}")
                print(f"  Write: {'Yes' if os.access(combined_args, os.W_OK) else 'No'}")
                print(f"Execute: {'Yes' if os.access(combined_args, os.X_OK) else 'No'}")
            else:
                print(f"File {combined_args} does not exist.")
        elif command.lower() == "exit":
            sys.exit()


def parse_input(text):
    """
    Parses input into a command and parts, throwing an exception if no command is found.
    """
    parts = text.split(" ")
    if len(parts) < 1:
        raise Exception("Invalid command.")
    return parts[0], parts[1:]


if __name__ == "__main__":
    main()
```

I think we can clean this up some. First, I want to break out each of the tools into their own 
functions that return the input back out of them:

```python
from os import path
import os
import sys


def main():
    """
    Main function. Runs a mini shell.
    """
    while True:
        raw_input = input("?> ")
        command = ""
        args = []

        try:
            command, args = parse_input(raw_input)
        except:
            continue

        if command.lower() == "echo":
            print(echo(args))
        elif command.lower() == "fi":
            print(fileInfo(args))
        elif command.lower() == "exit":
            print(exit(args))


def echo(args):
    """
    Echos the text contained in the arguments.
    """
    return " ".join(args)


def fileInfo(args):
    """
    Shows the file info for the file indicated by the given arguments.
    """
    combined_args = " ".join(args)
    result = ""
    if os.access(combined_args, os.F_OK):
        filepath = path.abspath(combined_args)
        result += f"File {combined_args}\n"
        result += f"   Path: {filepath}\n"
        result += f"   Read: {'Yes' if os.access(combined_args, os.R_OK) else 'No'}\n"
        result += f"  Write: {'Yes' if os.access(combined_args, os.W_OK) else 'No'}\n"
        result += f"Execute: {'Yes' if os.access(combined_args, os.X_OK) else 'No'}"
    else:
        result += f"File {combined_args} does not exist."
    return result
    

def exit(args):
    """
    Quits the shell.
    """
    sys.exit(0)


def parse_input(text):
    """
    Parses input into a command and parts, throwing an exception if no command is found.
    """
    parts = text.split(" ")
    if len(parts) < 1:
        raise Exception("Invalid command.")
    return parts[0], parts[1:]


if __name__ == "__main__":
    main()
```

This is already a lot cleaner, but this isn't yet using functional programming. Next, I want to leave
these functions as they are, but also put them together into a dictionary by name:

```python
commands = {'echo': echo, 'fi': fileInfo, 'exit': exit}
```

This is keying their function names to the command names I want them to be called with. Now let's call
them!

```python
from os import path
import os
import sys

def echo(args):
    """
    Echos the text contained in the arguments.
    """
    return " ".join(args)


def fileInfo(args):
    """
    Shows the file info for the file indicated by the given arguments.
    """
    combined_args = " ".join(args)
    result = ""
    if os.access(combined_args, os.F_OK):
        filepath = path.abspath(combined_args)
        result += f"File {combined_args}\n"
        result += f"   Path: {filepath}\n"
        result += f"   Read: {'Yes' if os.access(combined_args, os.R_OK) else 'No'}\n"
        result += f"  Write: {'Yes' if os.access(combined_args, os.W_OK) else 'No'}\n"
        result += f"Execute: {'Yes' if os.access(combined_args, os.X_OK) else 'No'}"
    else:
        result += f"File {combined_args} does not exist."
    return result


def exit(args):
    """
    Quits the shell.
    """
    sys.exit(0)


commands = {'echo': echo, 'fi': fileInfo, 'exit': exit}


def main():
    """
    Main function. Runs a mini shell.
    """
    while True:
        raw_input = input("?> ")
        command = ""
        args = []

        try:
            command, args = parse_input(raw_input)
        except:
            continue

        if command in commands:
            print(commands[command](args))

def parse_input(text):
    """
    Parses input into a command and parts, throwing an exception if no command is found.
    """
    parts = text.split(" ")
    if len(parts) < 1:
        raise Exception("Invalid command.")
    return parts[0], parts[1:]


if __name__ == "__main__":
    main()
```

I like this a lot better. Now when I want to add another tool to my shell, I can just add another
function, and add it to the dictionary. No need to check if statements. No need to adjust the main
method. I've reduced the number of moving parts in here, and made it much easier to understand as 
well.

We can do the same in our Java shell. We have the Command interface, which sets up and checks the same
thing that we have above: Take in a list of arguments, spit out a single string. We can either set up
functions to do these things, or use SAM (single abstract method, aka lambdas) to add other 
functionality:

```java
public class Main {
    private static HashMap<String, Command> commands = new HashMap<>();

    public static void main(String[] args) {
        commands.put("echo", (args) -> String.join(" ", args));
        commands.put("exit", (args) -> System.exit(0));
        commands.put("sm", SpecialMessage::specialMessage);
        commands.put("fi", Main::fileInfo);
        Scanner scanner = new Scanner(System.in);
        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.print("?> ");
            String input = scanner.nextLine();
            List<String> parsedInput = parseInput(input);

            // Check for when there's not enough input.
            if (parsedInput.size() < 1) {
                System.out.println("Invalid command.");
                continue;
            }

            // Pull command off the front.
            String command = parsedInput.get(0);
            List<String> parsedArgs = parsedInput.subList(1, parsedInput.size());
            
            if (commands.containsKey(command)) {
                commands.get(command).run(parsedArgs);
            }
            else {
                System.out.println("Command not found.");
            }
        }
    }

    private String fileInfo(List<String> args) {
        String filename = String.join(" ", args);
        File file = new File(filename);
        if (file.exists()) {
            System.out.println("File " + file.getName());
            System.out.println("      Path: " + file.getAbsolutePath());
            System.out.println("  Readable: " + (file.canRead() ? "Yes" : "No"));
            System.out.println(" Writeable: " + (file.canWrite() ? "Yes" : "No"));
            System.out.println("Executable: " + (file.canExecute() ? "Yes" : "No"));
        }
        else {
            System.out.println("File does not exist.");
        }
    }
}
```

That's all for this week. Being able to read and use functions like this is the first step on our way 
to use functional programming, and that's what we'll talk about next week: Higher Order Functions.