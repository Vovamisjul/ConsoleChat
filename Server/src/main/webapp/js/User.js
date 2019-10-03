class User {
    constructor() {
        this.registered = false;
    }
    register(id, type) {
        this.id = id;
        this.type = type;
        this.registered = true;
    }
    unregister() {
        this.registered = false;
    }
    isRegistered() {
        return this.registered;
    }
}

