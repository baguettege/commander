# Commander

---

## Description
Commander provides a simple and modular way to handle commands through the CLI in java applications.

This framework provides simple abstraction for defining commands with strong type-safety.

---

## Features
- Type safe command execution and creation with generics
- Simple command registries
- Custom command contexts
- CLI argument parsing with support for quoted arguments and escape sequences (`\n`, `\\`, `\"`)

---

## Installation

Requires Java 15+

You can use Commander in one of two ways:
- **Include in the source code:** copy the `src` folder into your project
- **Include the JAR as an external library:** build Commander as a JAR and add it to your project

---

## Basic Usage

1. Create a Command Context

    ```java
    public class ServerContext extends CommandContext {
        public final Server server;
    
        public ServerContext(String[] args, Server server) {
            super(args);
            this.server = server;
        }
    }
    ```

2. Implement a command (must be stateless)

    ```java
    public class ServerCloseCommand implements Command<ServerContext> {
        @Override
        public void execute(ServerContext context) throws CommandArgumentException, CommandExecutionException {
            if (context.args().length != 1)
                throw new CommandArgumentException("Expected 1 arg as server port");
    
            int port;
            try {
                port = Integer.parseInt(context.args()[0]);
            } catch (NumberFormatException e) {
                throw new CommandArgumentException("Port was NaN: " + e.getMessage());
            }
    
            if (context.server.isClosed())
                throw new CommandExecutionException("Server is already closed");
            else
                context.server.close(port);
        }
        
        @Override
        public String getName() {
            return "close";
        }
    }
    ```

3. Create a Command Registry (all commands must be stateless)

    ```java
    CommandRegistry<ServerContext> registry =
            new CommandRegistry<>(
                    ServerCloseCommand::new
            );
    ```

4. Create a Command Reader

    ```java
    CommandReader<ServerContext> reader = new CommandReader<ServerContext>(
            new Scanner(System.in),
            registry,
            args -> new ServerContext(args, server)
    );
    ```

5. Read and execute commands

    ```java
    while (reader.hasNextCommand()) {
        reader.nextCommand();
    }
    ```
   
---

## Example Usage

```java
public class ServerMain {
    public static void main(String[] args) {
        CommandRegistry<ServerContext> registry =
                new CommandRegistry<>(
                        ServerCloseCommand::new
                );

        CommandReader<ServerContext> reader = new CommandReader<ServerContext>(
                new Scanner(System.in),
                registry,
                args -> new ServerContext(args, server)
        );

        while (reader.hasNextCommand()) {
            try {
                reader.nextCommand();
            } catch (CommandException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
```

---

## Exception Handling

`CommandReader`'s `.nextCommand()` may throw a `CommandException`, including:
- `UnknownCommandException`
- `CommandParseException`
- `CommandArgumentException`
- `CommandExecutionException`

`CommandRegistry` may throw a `CommandRegisterException` if a command is registered with the same name twice.

---

## Notes
- Parsing and execution is handled by the framework, you just have to implement commands, contexts and registries
- Context creation is user-defined via a factory, for example `args -> new ServerContext(args, server)`

---

## License

This project is licensed under the MIT License.