package ard.piraso.server;

/**
 * Defines an interface that represents the entry point of context logging.
 */
public interface PirasoEntryPoint {
    String getPath();

    String getRemoteAddr();
}
