# Executor

A lightweight, type-safe command execution framework for Java that provides a 
flexible and extensible way to build command-line interfaces and interactive shells.

---

## Features

- **Type-safe command arguments** - Define typed arguments and options with automatic conversion and validation
- **Hierarchical commands** - Support for subcommands (e.g., `git commit`, `docker run`)
- **Multiple execution modes** - Synchronous, Asynchronous, and interactive shell execution
- **Extensible type system** - Built-in type converters for common types with support for custom converters
- **Environment based organization** - Group related commands into isolated environments
- **Validation support** - Built-in argument and option validation with custom validators
- **Exception handling** - Comprehensive exception hierarchy for error handling
- **Consistent builder pattern** - All classes use the builder pattern for consistency

---

## Why Executor?

Executor is designed for applications that need:
- Strong compile-time safety
- Zero runtime mutability
- Multiple isolated environments
- No reflection or annotations

---

## Installation

Download the latest release from the [releases page](https://github.com/baguettege/executor/releases)
and add it to your project's classpath.

Alternatively, you can include the `src` directory directly in your project.

---

## Quick Start

### 1. Define a `Context`

```java
public class ServerContext extends Context<ServerContext> {
    private final Server server;
    
    public ServerContext(
            ContextParameters<ServerContext> params,
            Server server
    ) {
        super(params);
        this.server = server;
    }
    
    public Server server() {
        return server;
    }
}
```

### 2. Create a `ContextFactory`

```java
Server server;
ContextFactory<ServerContext> contextFactory =
        (params -> new ServerContext(params, server));
```

### 3. Register `Converter`s

```java
ConverterRegistry converterRegistry = ConverterRegistry.builder()
        .register(new StringConverter(), String.class)
        .register(new IntegerConverter(), Integer.class)
        .register(new BooleanConverter(), Boolean.class)
        .build();
```

### 4. Define `Command`s

```java
CommandSpec<ServerContext> startCommand = CommandSpec.<ServerContext>builder()
        .name("start")
        .description("Starts the server on a given port")
        .arg(ArgSpec.<Integer>builder()
                .name("port")
                .description("The port to bind the server to")
                .type(Integer.class)
                .validator(port -> port >= 0 && port <= 0xFFFF) // optional, can be null
                .build())
        .option(OptionSpec.<String>builder()
                .name("welcome-msg")
                .description("The welcome message for the server")
                .type(String.class)
                .defaultValue("A new server") // optional, can be null
                .build())
        .action(context -> {
            Integer port = context.getArg("port", Integer.class);
            String welcomeMsg = context.getOption("welcome-msg", String.class);
            
            context.server().start(port, welcomeMsg);
        }) // optional, can be null
        .build();
```

### 5. Build a `CommandRegistry`

```java
CommandRegistry<ServerContext> commandRegistry = CommandRegistry.builder()
        .register(startCommand)
        .build();
```

### 6. Create an `Environment`

```java
Environment<ServerContext> serverEnv = Environment.<ServerContext>builder()
        .name("server")
        .commands(commandRegistry)
        .converters(converterRegistry)
        .contextFactory(contextFactory)
        .build();
```

### 7. Create and use a `CommandExecutor`

```java
// single environment executor
CommandExecutor executor = MonoCommandEngine.<ServerContext>builder()
        .environment(serverEnv)
        .build();

// execute commands
executor.execute("start 443");
executor.execute("close --reason=Shutdown")
```

---

## Advanced Usage

### Hierarchical Commands

Create commands with subcommands:

```java
CommandSpec<GitContext> commitCommand = CommandSpec.<GitContext>builder()
        .name("commit")
        .description("Commit changes")
        .action(context -> System.out.println("Committing..."))
        .build();

CommandSpec<GitContext> gitCommand = CommandSpec.<GitContext>builder()
        .name("git")
        .description("Git version control")
        .subRegistry(CommandRegistry.<GitContext>builder()
                .register(commitCommand)
                .build())
        .build();

// usage: git commit
```

### Multiple Environments

```java
CompositeCommandEngine engine = CompositeCommandEngine.builder()
        .environment(serverEnv)
        .environment(databaseEnv)
        .environment(clientEnv)
        .build();

// execution: <environment> <command...> <args...>
engine.execute("server start 443");
engine.execute("client connect localhost 443");
```

### Async Execution

```java
AsyncCommandExecutor asyncExecutor = AsyncCommandExecutor.builder()
        .executor(engine)
        .nThreads(4)
        .exceptionHandler(e -> e.printStackTrace())
        .build();

asyncExecutor.execute("server start 443");
asyncExecutor.close(); // shutdown when done, or use try-with-resources
```

### Interactive Shell

```java
CommandShell shell = CommandShell.builder()
        .scanner(new Scanner(System.in))
        .executor(executor)
        .exceptionHandler(e -> System.err.println(e.getMessage()))
        .build();

shell.start(); // runs on daemon thread
// logic...
shell.close(); // shutdown when done, or use try-with-resources
```

### Custom Validators

```java
ArgSpec<Integer> ageArg = ArgSpec.<Integer>builder()
        .name("age")
        .description("User age")
        .type(Integer.class)
        .validator(age -> age >= 0 && age <= 150)
        .build();
```

### Custom Converters

```java
public class EmailConverter implements Converter<Email> {
    @Override
    public Email convert(String string) throws ConversionException {
        if (!string.contains("@")) {
            throw new ConversionException("Invalid email: " + string);
        }
        return new Email(string);
    }
}

// register it
ConverterRegistry converterRegistry = ConverterRegistry.builder()
        .register(new EmailConverter(), Email.class)
        .build();
```

---

## Built-in Converters

- `StringConverter` - Pass-through for `String`s
- `IntegerConverter` - Converts to `Integer`
- `LongConverter` - Converts to `Long`
- `ShortConverter` - Converts to `Short`
- `FloatConverter` - Converts to `Float`
- `DoubleConverter` - Converts to `Double`
- `BooleanConverter` - Converts to `Boolean` ("true" / "false")

---

## Exception Hierarchy

```
ExecutorException
├── ArgException
│   ├── ArgCountException
│   ├── ArgNotFoundException
│   └── ArgValidationException
├── CommandException
│   ├── CommandNotFoundException
│   └── CommandExecutionException
├── ConverterException
│   ├── ConversionException
│   └── ConverterNotFoundException
└── ParseException
    └── TokenizationException
```

---

## Input Syntax

### Positional Arguments

```
command arg1 arg2 arg3
```

### Named Options

```
command --option=value --flag=true
```

### Quoted Strings

```
command "argument with spaces" --text="quoted value"
```

### Environment

```
environment command arg1 arg2 arg3
```

### Hierarchical Commands

```
command1 command2 arg1
```

### Escape Sequences

- `\\` - Backslash
- `\"` - Double quote
- `\n` - Newline
- `\r` - Carriage return
- `\t` - Tab
- `\b` - Backspace

---

## Architecture

The framework follows a layered architecture:

1. **Input layer** - Tokenization of command strings
2. **Parsing layer** - Separation of arguments and options
3. **Binding layer** - Type conversion and validation
4. **Execution layer** - Command execution with typed context
5. **Output layer** - Exception handling and result processing

---

## Design Principles

- **Immutability** - All classes are immutable after construction
- **Type safety** - Generics ensure compile-time type safety
- **Consistency** - Builder pattern used for all objects

---

## Thread Safety

- All classes are immutable and thread-safe
- `AsyncCommandExecutor` handles concurrency internally with a thread pool
- `CommandShell` uses a single internal daemon thread for handling the interactive shell

---

## Notes

- This project was renamed from `Commander` to `Executor` to avoid confusion with `JCommander`

---

## Requirements

- Built for Java 17+, but may support lower levels. Check release information for specifics
- No external dependencies

---

## License

- This project is licensed under the MIT License.