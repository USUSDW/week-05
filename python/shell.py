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