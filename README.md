# Commander

A type-safe, immutable command-line framework for Java with environment-based command isolation.

Commander intentionally avoids reflection, annotations, and runtime mutability in favor of
explicit, compile-time safe APIs.

---

## Features

- **Type-safe** - Generic context ensures compile-time type safety
- **Immutable architecture** - All objects immutable after construction with defensive copying
- **Environment isolation** - Commands organized into isolated namespaces with shared type converters
- **Flexible arguments** - Support for positional arguments, key-value options, and boolean flags
- **Modular converters** - Extensible type conversion system with 8 standard converters included
- **Builder pattern API** - Easy construction of all framework objects
- **Comprehensive documentation** - Full javadoc across all classes

---

## Why Commander?

Commander is designed for applications that need:
- Strong compile-time safety
- Multiple isolated environments/namespaces
- Zero runtime mutability
- No reflection or annotations

---

## Installation

- **Include in the source code:** copy the `src` folder into your project
- **Include the JAR as an external library:** download a release JAR and include in your project

---

## Usage

### 1. Define a Context

```java
public final class ServerContext extends Context<ServerContext> {
    private final Server server;

    public ServerContext(
            Arguments args,
            CommandGroup group,
            Server server
    ) {
        super(args, group);
        this.server = server;
    }
    
    public Server server() {
        return server;
    }
}
```

### 2. Create Command Definitions

```java
// Define arguments
ArgumentDefinition<String> nameArg = ArgumentDefinition.<String>builder()
        .name("name")
        .type(String.class)
        .description("The server name")
        .build();

// Define options
OptionDefinition<Integer> portOption = OptionDefinition.<Integer>builder()
        .key("port")
        .type(Integer.class)
        .defaultValue(443)
        .description("The port to bind the server to")
        .build();

// Define flags
FlagDefinition whitelistFlag = FlagDefinition.builder()
        .name("whitelist")
        .description("Enable whitelist for client connections")
        .build();

// Define command
CommandDefinition<ServerContext> startCommand = CommandDefinition.<ServerContext>builder()
        .name("start")
        .handler(context -> {
            String name = context.args().arg("name", String.class);
            Integer port = context.args().option("port", Integer.class).get(); // will not be empty as default value was assigned
            boolean whitelist = context.args().flag("whitelist");
            
            context.server().start(name, port, whitelist);
            System.out.println("Started server on port " + port);
        })
        .arg(nameArg)
        .option(portOption)
        .flag(whitelistFlag)
        .build();
```

### 3. Build Environment and Framework

```java
// Create command group
CommandGroup<ServerContext> group = CommandGroup.builder()
        .command(startCommand)
        .command(stopCommand)
        .command(kickCommand)
        .build();

// Create environment with converters
Environment<ServerContext> serverEnv = Environment.<ServerContext>builder()
        .name("server")
        .contextFactory(
                (args, group) -> new ServerContext(args, group, server)
        )
        .group(group)
        .converter(new StringConverter(), String.class)
        .converter(new IntegerConverter(), Integer.class)
        .converter(new BooleanConverter(), Boolean.class)
        .build();

// Build framework
CommandFramework framework = CommandFramework.builder()
        .environment(serverEnv)
        .environment(fileEnv)
        .build();
```

### 4. Execute Commands

```java
framework.execute("server start game-server --port=8080 --whitelist");
framework.execute("server kick alice --reason=Spamming --force");
framework.execute("server stop");
```

---

## Command syntax

Commands follow this format:
```
<environment> <command> [args] [--option=value] [--flag]
```
Options and flags can appear anywhere after the command name.

**Examples:**
- `server kick alice` - positional argument
- `server kick bob --reason=Idle` - with option
- `server stop --force` - with flag
- `server start 443 --max-clients=100 --verbose` - multiple parameters
- `server kick --reason=Spamming john` - options/flags can appear anywhere after command

---

## Architecture

### Execution Pipeline

1. **Tokenization** - `Tokenizer` splits input text into tokens (handling quoted tokens and escapes)
2. **Parsing** - `InvocationParser` extracts environment, command, args, options, and flags
3. **Resolution** - `ArgumentResolver` converts strings to typed objects using `Converter`s
4. **Execution** - `CommandHandler` executes with typed context

### Core Components

- **CommandFramework** - top-level entry point managing multiple environments and orchestration
- **Environment** - isolated namespace holding commands, converters and context type
- **CommandGroup** - container for command definitions, preserving insertion order
- **CommandDefinition** - complete command specification with handler and parameters
- **Context** - Base class providing access to arguments and command introspection
- **Arguments** - Resolved, typed arguments passed to command handlers
- **Converter** - Modular type conversion interface

---

## Standard Converters

Commander includes several built-in `Converter` implementations:

- `StringConverter` - converter for `String`
- `IntegerConverter` - parses strings into `Integer`
- `LongConverter` - parses strings into `Long`
- `ShortConverter` - parses strings into `Short`
- `FloatConverter` - parses strings into `Float`
- `DoubleConverter` - parses strings into `Double`
- `BooleanConverter` - parses strings into `Boolean` (`true` or `false`)
- `PathConverter` - parses strings into `java.nio.file.Path`

### Custom Converters

Create custom converters by implementing the `Converter` interface:

```java
public class EmailConverter implements Converter {
    @Override
    public Email convert(String text) throws ConversionFailedException {
        if (!text.contains("@")) {
            throw new ConversionFailedException("Invalid email format", text);
        }
        return new Email(text);
    }
}
```

---

## Exception Handling

Commander provides a comprehensive exception hierarchy:

```
CommanderException
├── ArgumentException
│   ├── ArgumentNotFoundException
│   ├── InvalidArgumentCountException
│   ├── ArgumentAlreadyExistsException
│   ├── OptionAlreadyExistsException
│   └── FlagAlreadyExistsException
├── CommandException
│   ├── CommandNotFoundException
│   ├── CommandAlreadyExistsException
│   └── CommandExecutionException
├── ConverterException
│   ├── ConversionFailedException
│   ├── ConverterNotFoundException
│   └── ConverterAlreadyExistsException
├── EnvironmentException
│   ├── EnvironmentNotFoundException
│   └── EnvironmentAlreadyExistsException
└── ParseException
    ├── TokenizationException
    └── InvocationFormatException
```

All exceptions extend `CommanderException` (a `RuntimeException`), allowing applications to catch all
Commander-related exceptions with a single catch block if desired.

---

## Advanced features

### Tokenization with quotes and escapes

Handles quoted strings with spaces
- `server message "Hello, World!"` tokenizes to `["server", "message", "Hello, World!"]`

Supports escape sequences: \n, \\, \"
- `server message "Line 1\nLine 2"` tokenizes to `["server", "message", "Line 1\nLine 2"]`

---

### Help Commands via Context

Access all commands at runtime through the context:

```java
CommandHandler<ServerContext> helpHandler = context -> {
    List<CommandDefinition<ServerContext>> commands = context.group().getAll();
    
    System.out.println("Available commands:");
    for (CommandDefinition<ServerContext> cmd : commands) {
        System.out.println("  " + cmd.name());
    }
};
```

---

### Multiple Environments

```java
CommandFramework framework = CommandFramework.builder()
    .environment(serverEnv)
    .environment(databaseEnv)
    .environment(fileEnv)
    .build();

framework.execute("server start game-server --port=443");
framework.execute("database migrate");
framework.execute("file backup logs/");
```

---

## Design Principles

- **Immutability** - all definitions, environments and frameworks immutable after construction (with defensive copying)
- **Type safety** - generics ensure compile-time type safety
- **Isolation** - environments completely separated from each other
- **Consistency** - builder pattern used for all complex objects
- **Clarity** - comprehensive javadoc and clear exception messages

---

## Requirements

- Java 11+
- No external dependencies

---

## License

- This project is licensed under the MIT License.