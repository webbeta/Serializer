package es.webbeta.serializer.base;

public interface ILogger {

    enum Level {
        ERROR
    }

    Level getLevel();
    void setLevel(Level level);

    void error(String message);

}
