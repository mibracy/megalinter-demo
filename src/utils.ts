export function greet(name: string): string {
    return "Hello " + name;
}

export const config: { timeout: number; retries: number } = {
    timeout: 5000,
    retries: 3,
};

export function processData(data: Record<string, unknown>): Record<string, unknown> {
    return data;
}

export class UserManager {
    private users: Array<Record<string, unknown>> = [];

    addUser(user: Record<string, unknown>): void {
        this.users.push(user);
    }

    getUsers(): Array<Record<string, unknown>> {
        return this.users;
    }
}

export function calculate(a: number, b: number): number {
    return a + b;
}
