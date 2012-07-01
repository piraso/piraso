package ard.piraso.server.service;

/**
 * Singleton user registry.
 */
public enum UserRegistrySingleton {
    INSTANCE;

    protected UserRegistry registry;

    public void setRegistry(UserRegistry registry) {
        this.registry = registry;
    }

    public UserRegistry getRegistry() {
        return registry;
    }
}
