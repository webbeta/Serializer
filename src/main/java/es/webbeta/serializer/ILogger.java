package es.webbeta.serializer;

public interface ILogger {

    enum Level {
        ERROR
    }

    Level getLevel();
    void setLevel(Level level);

    void error(String message);

}
