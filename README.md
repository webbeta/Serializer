# webBeta Serializer

[![Build Status](https://travis-ci.org/webbeta/Serializer.svg?branch=master)](https://travis-ci.org/webbeta/Serializer)
[![Coverage Status](https://codecov.io/gh/webbeta/Serializer/branch/master/graph/badge.svg)](https://codecov.io/gh/webbeta/Serializer)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/cc4bdde6e4434963be65533dc32907ac)](https://app.codacy.com/app/mowcixo/Serializer?utm_source=github.com&utm_medium=referral&utm_content=webbeta/Serializer&utm_campaign=Badge_Grade_Settings)
[![Scrutinizer Code Quality](https://scrutinizer-ci.com/g/webbeta/Serializer/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/webbeta/Serializer/?branch=master)* 

ITERACION ACTUAL 
https://

[![GitHub license](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Maven](https://img.shields.io/maven-central/v/es.webbeta/serializer.svg)](http://mvnrepository.com/artifact/es.webbeta/serializer)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/webbeta/Serializer.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/webbeta/Serializer/alerts/)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/webbeta/Serializer.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/webbeta/Serializer/context:java)

* [What is webBeta Serializer?](#what-is-webbeta-serializer?)
* [Quick examples](#quick-examples)
* [License](#license)

## What is webBeta Serializer?

YML based serializer for Java. Based on JMS serializer https://jmsyst.com/libs/serializer/master/reference/yml_reference

To create a Serializer instance:

```java
Cache cache = new Cache() {
    @Override
    public String get(String key) { return null; }
    
    @Override
    public void set(String key, String content) {}
    
    @Override
    public void remove(String key) {}
};

ConfigurationProvider configurationProvider = new ConfigurationProvider() {
    @Override
    public boolean getBoolean(String key, boolean defaultValue) { return true; }
    
    @Override
    public String getString(String key, String defaultValue) {  return ""; }
};

Environment environment = () -> false;

Serializer serializer = SerializerBuilder.build()
        .withCache(cache)
        .withConfigurationProvider(configurationProvider)
        .withEnvironment(environment)
        .get();
```

And to add a logger instance:

```java
Logger logger = new Logger() {
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
