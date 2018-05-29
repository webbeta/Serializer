# webBeta Serializer

[![Build Status](https://travis-ci.org/webbeta/Serializer.svg?branch=master)](https://travis-ci.org/webbeta/Serializer)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE)

* [What is webBeta Serializer?](#what-is-webbeta-serializer?)
* [Quick examples](#quick-examples)
* [License](#license)

## What is webBeta Serializer?

YML based serializer for Java. Based on JMS serializer https://jmsyst.com/libs/serializer/master/reference/yml_reference

To create a Serializer instance:

```java
ICache cache = new ICache() {
    @Override
    public String get(String key) { return null; }
    
    @Override
    public void set(String key, String content) {}
    
    @Override
    public void remove(String key) {}
};

IConfigurationProvider configurationProvider = new IConfigurationProvider() {
    @Override
    public boolean getBoolean(String key, boolean defaultValue) { return true; }
    
    @Override
    public String getString(String key, String defaultValue) {  return ""; }
};

IEnvironment environment = () -> false;

Serializer serializer = SerializerBuilder.build()
        .withCache(cache)
        .withConfigurationProvider(configurationProvider)
        .withEnvironment(environment)
        .get();
```

And to add a logger instance:

```java
ILogger logger = new ILogger() {
    @Override
    public Level getLevel() { return Level.ERROR; }

    @Override
    public void setLevel(Level level) { }

    @Override
    public void error(String message) { }
};

serializer.setLogger(logger);
```
## Quick examples

Class Foo:

```java
package foo.bar;

class Foo {
    
    private String bar = "foobar";
    
    public String getBar() {
        return bar;
    }
    
    public Long getCalc() {
        return 2 + 2;
    }
    
}

```

Yml file (named ```foo.bar.Foo.yml```):

```yaml
foo.bar.Foo:
  virtual_properties:
    getCalc:
      serialized_name: calc
      groups: [barGroup]
  properties:
    bar:
      groups: [fooGroup]

```

Usage:

```java
serializer.serialize(fooInstance, "fooGroup", "barGroup");

// it will return
// {"bar": "foobar", "calc": 4}
```

## License

[MIT](LICENSE)
