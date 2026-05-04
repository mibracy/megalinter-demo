# MegaLinter Issues on Main Branch

This document summarizes all the linting issues found by MegaLinter on the `main` branch. Each issue is categorized by language/tool with specific examples and fix guidance.

## Summary Table

| Descriptor | Linter | Errors | Key Issues |
|------------|--------|--------|-----------|
| **JAVA** | checkstyle | 23 | Unused imports, trailing spaces, missing Javadoc, magic numbers |
| **JAVA** | pmd | 8 | Unused imports, short class name, non-final params |
| **GO** | golangci-lint | 1 | Typechecking error (no module) |
| **GO** | revive | 1 | Missing package comment |
| **JAVASCRIPT** | standard | 17 | Unused vars, no semicolons, wrong quotes, bad indent |
| **TYPESCRIPT** | ts-standard | 1 | Missing tsconfig.json |
| **PYTHON** | ruff | 5 | Unused imports (os, sys, json, List, Dict) |
| **PYTHON** | bandit | 1 | B310: urllib.urlopen security risk |
| **PYTHON** | flake8 | 33 | PEP8 violations |
| **JSON** | jsonlint | 1 | Trailing comma in package.json:9 |
| **YAML** | yamllint | 14 | Trailing spaces, bad indentation, missing `---` |
| **YAML** | v8r | 1 | Invalid `.mega-linter.yml` (wrong property names) |
| **MARKDOWN** | markdownlint | 19 | Missing code block languages, table style issues |
| **DOCKERFILE** | hadolint | 1 | Uppercase `COPY`, `latest` tag |
| **REPOSITORY** | kics | 4 | Docker: no USER, no HEALTHCHECK, `latest` tag, unpinned action |
| **REPOSITORY** | checkov | 3 | Docker `latest` tag, no USER, GitHub unpinned action |
| **REPOSITORY** | trivy | 1 | Dockerfile: 3 misconfigurations |
| **BASH** | shellcheck | 1 | Scripts missing shebang |
| **BASH** | shfmt | 1 | Formatting issues |

---

## Java Issues (`java/Main.java`)

### checkstyle (23 errors)

| Line | Error | Description |
|------|-------|-------------|
| 2:8 | UnusedImports | Unused import `java.io.FileNotFoundException` |
| 10:1 | HideUtilityClassConstructor | Utility classes should not have a public or default constructor |
| 11:5 | MissingJavadocVariable | Missing a Javadoc comment for `cache` |
| 12:5 | MissingJavadocVariable | Missing a Javadoc comment for `counter` |
| 14:5 | MissingJavadocMethod | Missing a Javadoc comment for `main` |
| 14:29 | FinalParameters | Parameter `args` should be final |
| 21:13 | WhitespaceAfter | 'while' is not followed by whitespace |
| 21:30 | WhitespaceAround | '{' is not preceded with whitespace |
| 35:65 | MagicNumber | '10' is a magic number |
| 36:29 | MagicNumber | '100' is a magic number |
| 59:37 | FinalParameters | Parameter `url` should be final |

### pmd (8 errors)

| Line | Rule | Description |
|------|------|-------------|
| 2 | UnnecessaryImport | Unused import 'java.io.FileNotFoundException' |
| 10 | NoPackage | All classes must belong to a named package |
| 10 | UseUtilityClass | All methods are static. Consider adding a private no-args constructor |
| 10 | ShortClassName | Avoid short class names like Main |
| 35 | LocalVariableCouldBeFinal | Local variable 'executor' could be declared final |
| 44 | LocalVariableCouldBeFinal | Local variable 'url' could be declared final |
| 49 | MethodArgumentCouldBeFinal | Parameter 'input' is not assigned and could be declared final |

### Intentionally Bugs in Java Code

#### 1. Resource Leak (Unclosed FileInputStream)
```java
// BUG: FileInputStream not closed
FileInputStream fis = null;
try {
    fis = new FileInputStream("/tmp/test.txt");
    // ... use fis
} catch (IOException e) {
    e.printStackTrace();
}
// FIX: Use try-with-resources
try (FileInputStream fis = new FileInputStream("/tmp/test.txt")) {
    // ... use fis
} // Automatically closed
```

#### 2. Null Dereference Risk
```java
// BUG: Passing null, then calling .equals() on it
processData(null);

private static void processData(String input) {
    if (input.equals("test")) {  // NullPointerException!
        System.out.println("Matched!");
    }
}
// FIX: Add null check
private static void processData(String input) {
    if (input != null && input.equals("test")) {
        System.out.println("Matched!");
    }
}
```

#### 3. Thread-Unsafe Shared Counter
```java
// BUG: Race condition with 100 goroutines
private static int counter = 0;
// ...
executor.submit(() -> {
    counter++;  // Race condition!
    cache.add("item-" + counter);
});
// FIX: Use AtomicInteger
private static final AtomicInteger counter = new AtomicInteger(0);
// Increment: counter.incrementAndGet();
```

#### 4. Generic Exception Catch (Bad Practice)
```java
// BUG: Catching generic Exception
} catch (Exception e) {
    e.printStackTrace();
}
// FIX: Catch specific exceptions
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.err.println("Sleep interrupted: " + e.getMessage());
}
```

---

## Go Issues (`go/main.go`)

### golangci-lint (1 error)
- Typechecking error: directory prefix `.` does not contain main module or its selected dependencies

### revive (1 error)
- Line 1:1: should have a package comment

### Intentionally Bugs in Go Code

#### 1. Unhandled Errors
```go
// BUG: Ignoring error from http.Get
resp, err := http.Get("https://api.example.com/users")
if err != nil {
    fmt.Println("Error:", err)  // Just printing, not handling
}
defer resp.Body.Close()  // If err != nil, resp is nil -> panic!
// FIX: Proper error handling
resp, err := http.Get("https://api.example.com/users")
if err != nil {
    log.Printf("Error fetching users: %v", err)
    return
}
defer resp.Body.Close()
```

#### 2. Race Condition (Unprotected Shared Variable)
```go
// BUG: Race condition on sharedCounter
var sharedCounter int
go func() {
    sharedCounter++  // Multiple goroutines, no synchronization!
}()
// FIX: Use mutex or atomic
var sharedCounter int64
var mu sync.Mutex
go func() {
    mu.Lock()
    sharedCounter++
    mu.Unlock()
}()
// Or use atomic: atomic.AddInt64(&sharedCounter, 1)
```

#### 3. Defer in Loop
```go
// BUG: defer in loop - defers until function exit!
for i := 0; i < 5; i++ {
    defer fmt.Println("deferred:", i)  // All run at function exit
}
// FIX: Wrap in function or move outside loop
for i := 0; i < 5; i++ {
    func(idx int) {
        defer fmt.Println("deferred:", idx)
        // ...
    }(i)
}
```

#### 4. Nil Pointer Dereference Risk
```go
// BUG: Passing nil to function that dereferences
processUser(nil)

func processUser(u *User) {
    fmt.Println(u.Name)  // Panic if u is nil!
}
// FIX: Add nil check
func processUser(u *User) {
    if u != nil {
        fmt.Println(u.Name)
    }
}
```

#### 5. Unused Imports/Variables
```go
// BUG: Unused imports
import "io/ioutil"  // Deprecated and unused
import "time"        // Only used once, but still imported
// FIX: Remove unused imports
```

---

## JavaScript Issues (`src/index.js`)

### standard (17 errors)

| Line | Rule | Description |
|------|------|-------------|
| 1:7 | no-unused-vars | 'unusedVar' is assigned a value but never used |
| 2:5 | prefer-const | 'x' is never reassigned. Use 'const' instead |
| 3:1 | no-var | Unexpected var, use let or const instead |
| 5:13 | space-before-function-paren | Missing space before function parentheses |
| 11:13 | quotes | Strings must use singlequote |
| 15:10 | no-unused-vars | 'multiply' is defined but never used |
| 15:18 | space-before-function-paren | Missing space before function parentheses |
| 15:20 | comma-spacing | A space is required after ',' |
| 20:22 | quotes | Strings must use singlequote |
| 22:1 | keyword-spacing | Expected space(s) after "if" |
| 23:1 | indent | Expected indentation of 2 spaces but found 0 |

### Fix Examples

```javascript
// BUG: No semicolons, var usage, bad formatting
const unusedVar = 42
var y = 20
function multiply(a,b){
const res = a * b
return res
}

// FIX:
"use strict";

function multiply(a, b) {
    const res = a * b;
    return res;
}

const data = { name: "test", value: 100 };

if (data.value > 50) {
    console.log("Value is greater than 50");
}
```

---

## TypeScript Issues (`src/utils.ts`)

### ts-standard (1 error)
- Unable to locate the project file. A project file (tsconfig.json or tsconfig.eslint.json) is required

### Intentionally Bad TypeScript

```typescript
// BUG: Excessive any type usage
export function greet(name: any): any {
    return "Hello " + name;
}

export const config: any = {
    timeout: 5000,
    retries: 3,
};

export class UserManager {
    private users: any[] = [];  // Untyped array
    addUser(user: any): void {  // any parameter
        this.users.push(user);
    }
}

// FIX:
export function greet(name: string): string {
    return "Hello " + name;
}

export const config: { timeout: number; retries: number } = {
    timeout: 5000,
    retries: 3,
};

export class UserManager {
    private users: Array<Record<string, unknown>> = [];
    addUser(user: Record<string, unknown>): void {
        this.users.push(user);
    }
}
```

---

## Python Issues (`python/main.py`)

### ruff (5 errors)

| Line | Rule | Description |
|------|------|-------------|
| 1:8 | F401 | `os` imported but unused |
| 2:8 | F401 | `sys` imported but unused |
| 3:8 | F401 | `json` imported but unused |
| 5:20 | F401 | `typing.List` imported but unused |
| 5:25 | F401 | `typing.Dict` imported but unused |

### bandit (1 error)

| Line | Rule | Description |
|------|------|-------------|
| 16:15 | B310 | Audit url open for permitted schemes (urllib.urlopen) |

### flake8 (33 errors)
- E301: expected 1 blank line
- E302: expected 2 blank lines
- E231: missing whitespace after ','
- E225: missing whitespace around operator
- E251: unexpected spaces around keyword / parameter equals

### Fix Examples

```python
# BUG: Bad formatting, unused imports, security issue
import os
import sys
import json
from typing import List,Dict

def process_data( data ):
    """Process the input data"""
    result = []
    for item in data:
        if item>10:
            result.append( item*2 )
    return result

def fetch_url( url ):
    response = urllib.request.urlopen( url )  # bandit B310
    return response.read()

# FIX:
import urllib.request
from typing import List, Dict

def process_data(data: List[int]) -> List[int]:
    """Process the input data."""
    result = []
    for item in data:
        if item > 10:
            result.append(item * 2)
    return result

def fetch_url(url: str) -> bytes:
    """Fetch data from URL."""
    response = urllib.request.urlopen(url)
    return response.read()
```

---

## JSON Issues (`config/package.json`)

### jsonlint (1 error)

| File | Line | Error |
|------|------|-------|
| config/package.json | 9:3 | Trailing comma in object |

```json
// BUG: Trailing comma
{
  "scripts": {
    "start": "node src/index.js",
    "test": "echo \"Error: no test specified\" && exit 1",  // <-- trailing comma
  },
  dependencies: {  // <-- unquoted key
    "express": "^4.18.0",
  },  // <-- trailing comma
}

// FIX:
{
  "scripts": {
    "start": "node src/index.js",
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "dependencies": {
    "express": "^4.18.0"
  }
}
```

---

## YAML Issues (`config/settings.yml`, `.github/workflows/ci.yml`, `.mega-linter.yml`)

### yamllint (14 errors)

| File | Line | Error |
|------|------|-------|
| .github/workflows/ci.yml | 1:1 | missing document start "---" |
| .github/workflows/ci.yml | 3:1 | truthy value should be one of [false, true] |
| .github/workflows/ci.yml | 5:16 | too many spaces inside brackets |
| .github/workflows/ci.yml | 12:1 | trailing spaces |
| .mega-linter.yml | 1:1 | missing document start "---" |
| config/settings.yml | 1:1 | missing document start "---" |

### v8r (1 error on `.mega-linter.yml`)

| Error |
|-------|
| `# must NOT have additional properties, found additional property 'MAIN'` |
| `# must NOT have additional properties, found additional property 'JAVA'` |
| (All uppercase property names are invalid - should be lowercase) |

### Fix Examples

```yaml
# BUG: Missing ---, truthy values, bad indentation
production:
  host: 0.0.0.0
  port: 8080
  debug: false

# FIX:
---
production:
  host: "0.0.0.0"
  port: 8080
  debug: false
```

```yaml
# BUG: Uppercase property names (invalid)
MAIN:
  PLUGINS: []
  ENFORCE_JAVA: true

# FIX: Lowercase property names
main:
  plugins: []
  enforce_java: true
```

---

## Markdown Issues (`README.md`, `docs/README.md`, `.agents/skills/megalinter-enforcer/SKILL.md`)

### markdownlint (19 errors)

| File | Line | Rule | Description |
|------|------|------|-------------|
| README.md | 15 | MD040 | Fenced code blocks should have a language specified |
| README.md | 36:85 | MD060 | Table pipe does not align with header |
| SKILL.md | 22:17 | MD060 | Table column style issue |
| SKILL.md | 72 | MD040 | Fenced code blocks should have a language specified |

### Fix Examples

```markdown
<!-- BUG: No language specified in code block -->
```
npx megalinter-runner
```

<!-- FIX: Add language -->
```bash
npx megalinter-runner
```

```markdown
<!-- BUG: Table formatting -->
| Branch | Description |
|--------|-------------|
| `main` | Contains errors |

<!-- FIX: Proper spacing -->
| Branch | Description |
|--------|-------------|
| `main` | Contains errors |
```

---

## Dockerfile Issues (`Dockerfile`)

### hadolint (1 error)

| Line | Rule | Description |
|------|------|-------------|
| 3 | DL3020 | Use `COPY` (not `COPY` - case sensitive in some linters) |

### kics (4 errors)

| Severity | Issue |
|----------|-------|
| HGH | Missing USER Instruction |
| MEDIUM | Image Version Using 'latest' |
| LOW | Unpinned Actions Full Length Commit SHA |
| LOW | Add HEALTHCHECK instruction |

### checkov (2 errors)

| Rule | Description |
|------|-------------|
| CKV_DOCKER_7 | Ensure the base image uses a non latest version tag |
| CKV_DOCKER_3 | Ensure that a user for the container has been created |

### Fix Examples

```dockerfile
# BUG: latest tag, no USER, no HEALTHCHECK, uppercase COPY
FROM node:latest

WORKDIR /app

COPY package.json .
RUN npm install

COPY . .

EXPOSE 3000

CMD ["npm", "start"]

# FIX:
FROM node:18-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY package.json .
RUN npm install

COPY . .

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:3000/ || exit 1

USER appuser

EXPOSE 3000

CMD ["npm", "start"]
```

---

## GitHub Actions Issues (`.github/workflows/ci.yml`)

### actionlint (2 errors)

| Line | Error |
|------|-------|
| 5:16 | too many spaces inside brackets |
| 7:16 | too many spaces inside brackets |

### kics (1 error)

| Severity | Issue |
|----------|-------|
| LOW | Unpinned Actions Full Length Commit SHA |

### checkov (1 error)

| Rule | Description |
|------|-------------|
| CKV2_GHA_1 | Ensure top-level permissions are not set to write-all |

### Fix Examples

```yaml
# BUG: Deprecated action versions, bad formatting
name: CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v1
    
    - name: Setup Node.js
      uses: actions/setup-node@v1
      with:
        node-version: '18'

# FIX:
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

permissions:
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
        
      - name: Setup Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '18'
```

---

## Shell Script Issues (`scripts/deploy.sh`)

### shellcheck (1 error)

| Line | Error |
|------|-------|
| 1 | Missing shebang (`#!/bin/bash`) |

### shfmt (1 error)

| Error |
|-------|
| Formatting issues (missing indentation, no shebang) |

### Fix Examples

```bash
# BUG: No shebang, unused variables
ENV="production"
PORT=8080

echo "Deploying to $ENV environment"

function cleanup() {
    echo "Cleaning up..."
}

# FIX:
#!/bin/bash

ENV="production"
PORT=8080

echo "Deploying to $ENV environment"
echo "Server will run on $HOST:$PORT"

npm install
npm run build
npm start
```

---

## Repository (CI/CD) Issues

### dustilock (1 error)
- Invalid character '}' looking for beginning of object key string (in `config/package.json`)

### trivy (1 error)
- Dockerfile: 3 misconfigurations (no USER, `latest` tag, no HEALTHCHECK)

---

## How to Fix All Issues

### Step 1: Checkout the Fix Branch
```bash
git checkout fix/megalinter-issues
```

### Step 2: Compare Changes
```bash
git diff main fix/megalinter-issues
```

### Step 3: Run MegaLinter with Auto-Fix
```bash
npx megalinter-runner --fix
```

### Step 4: Apply Manual Fixes
Refer to the fix examples above for each language/tool.

### Step 5: Verify Fixes
```bash
npx megalinter-runner
# Should show all checks passing
```

---

## Using the MegaLinter Enforcer Skill

This project includes a project-local OpenCode skill at `.agents/skills/megalinter-enforcer/SKILL.md`.

To use it:
1. Open OpenCode in this project directory
2. Ask: "Can you help me fix the MegaLinter errors?"
3. The skill will automatically load and guide you through fixes

The skill provides:
- Auto-detection of MegaLinter setup
- Running `npx megalinter-runner`
- Parsing errors by linter/file
- Explaining complex Java/Go bugs with fix examples
- Applying auto-fixes with `--fix`
- Guiding manual fixes for logic issues
- Verifying fixes by re-running linter

---

## Resources

- [MegaLinter Documentation](https://megalinter.io/latest)
- [PMD Java Rules](https://pmd.github.io/pmd/pmd_rules_java.html)
- [Go Vet Documentation](https://pkg.go.dev/cmd/vet)
- [ESLint Rules](https://eslint.org/docs/latest/rules/)
- [Pylint Messages](https://pylint.pycqa.org/en/latest/messages/messages_list.html)
- [yamllint Documentation](https://yamllint.readthedocs.io/)
- [hadolint Rules](https://github.com/hadolint/hadolint/wiki/)
- [actionlint Documentation](https://github.com/rhysd/actionlint)
