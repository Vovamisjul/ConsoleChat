class User {
    #registered = false;
    register() {
        this.#registered = true;
    }
    unregister() {
        this.#registered = false;
    }
    isRegistered() {
        return this.#registered;
    }
}

