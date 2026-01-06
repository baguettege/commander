# Commander

---

## Description

Commander provides a simple and modular way to handle commands in multiple environments through the CLI in java applications,
with strong type-safety.

---

## Features

- Type safe command execution and creation with generics
- Simple command registries
- Custom command contexts
- CLI argument parsing with support for quoted arguments
- Multiple command environment support

---

## Installation

Requires Java 16+

You can use Commander in one of two ways:
- **Include in the source code:** copy the `src` folder into your project
- **Include the JAR as an external library:** build Commander as a JAR and add it to your project

---

## Usage

1. Create a command context

    ```java
    import api.CommandContext;
    
    public final class ServerContext extends CommandContext<ServerContext> {
        public final Server server;
        
        public ServerContext(CommandRegistry<ServerContext> registry, String[] args, Server server) {
            super(registry, args);
            this.server = server;
        }
    }
    ```
   
2. Implement a command (must be stateless)

    ```java
    import api.Command;
    import exceptions.CommandArgumentException;
    import exceptions.CommandExecutionException;
    
    public class ServerCloseCommand implements Command<ServerContext> {
        public ServerCloseCommand() {}
    
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
   
3. Create a command registry and context factory

    ```java
    import api.CommandContextFactory;
    import api.CommandRegistry;
    
    CommandRegistry<ServerContext> serverRegistry = new CommandRegistry<>(
            ServerCloseCommand::new
    );
    
    CommandContextFactory<ServerContext> contextFactory = (registry, args) ->
            new ServerContext(registry, args, new Server(443));
    ```

4. Create a command reader

    ```java
    import api.CommandReader;
    
    CommandReader reader = new CommandReader(new Scanner(System.in));
    reader.registerEnvironment(
            "server1",
            serverRegistry,
            contextFactory
    );
    ```

5. Read and execute commands

    ```java
    import exceptions.CommandException;
    
    while (reader.hasNextCommand()) {
        try {
            reader.nextCommand();
        } catch(CommandException e) {
            System.out.println(e.getMessage());
        }
    }
    ```
   
6. Implementing multiple environments

    ```java
    CommandRegistry<ClientContext> clientRegistry = new CommandRegistry<>(
            ConnectCommand::new,
            DisconnectCommand::new
    );
    
    CommandContextFactory<ClientContext> contextFactory = (registry, args) ->
            new ServerContext(registry, args, new Client("localhost", 443));
    
    reader.registerEnvironment(
            "client",
            clientRegistry,
            contextFactory
    );
    ```

7. Removing environments

    ```java
    reader.unregisterEnvironment("client");
    ```
   
8. Inputting commands into the CLI
    
    With one registered environment: `command` `args` or `environment` `command` `args`

    With multiple environments: `environment` `command` `args`

---

## Exception Handling

`CommandReader`'s `.nextCommand()` may throw a `CommandException`, including:
- `UnknownCommandException` if the command given is unknown
- `CommandParseException` the command fails to parse
- `CommandArgumentException` if the args passed into the command are invalid
- `CommandExecutionException` if an error occurs during command execution
- `UnknownCommandEnvironmentException` if the given environment is unknown/unregistered with the `CommandReader`
- `NoCommandEnvironmentsException` if no environments are registered with the `CommandReader`

`CommandReader` may throw a `CommandRegisterException` if an environment is registered with the same name twice.

`CommandRegistry` may throw a `CommandRegisterException` if a command is registered with the same name twice.

---

## Notes
- Parsing and execution is handled by the framework, you just have to implement commands, contexts and registries
- Context creation is user-defined via a factory, for example `(registry, args) -> new ServerContext(registry, args, server)`
- Escape sequences using `\` is not supported
- `CommandReader`'s `.nextCommand()` is not thread-safe, and should only ever be called by a single thread
- `CommandReader`'s `.registerEnvironment()` and `.unregisterEnvironment()` is thread-safe, and can be called by multiple threads

---

## License

This project is licensed under the MIT License.