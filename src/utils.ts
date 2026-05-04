export function greet(name: any): any {
  return "Hello " + name;
}

export const config: any = {
  timeout: 5000,
  retries: 3,
};

export function processData(data: any) {
  return data;
}

export class UserManager {
  private users: any[] = [];

  addUser(user: any): void {
    this.users.push(user);
  }

  getUsers(): any[] {
    return this.users;
  }
}

export function calculate(a: any, b: any) {
  return a + b;
}
