---
name: megalinter-enforcer
description: Enforce MegaLinter rules and fix lint errors for Java, Go, JS, TS, Python, YAML, JSON, Markdown, Shell, Docker, and GitHub Actions. Use when user mentions "megalinter", "lint failures", "fix linting", "code quality checks", or wants to ensure code passes all linters.
---

# MegaLinter Enforcer

A project-local skill for diagnosing and fixing MegaLinter violations in this demo repository. This skill helps developers understand and resolve linting errors across multiple languages and file formats.

## When to Use This Skill

Use this skill when:
- User mentions "megalinter", "lint", "code quality", "fix linting errors"
- Files are failing MegaLinter checks in CI
- Need to understand why a linter is complaining
- Want to apply auto-fixes for formatting issues
- Setting up MegaLinter for the first time

## Supported Languages & Tools

| Language/Tool | Linters Used |
|---------------|--------------|
| Java | PMD, Checkstyle, SpotBugs |
| Go | go vet, golint, gofmt, gocyclo |
| JavaScript | ESLint, Prettier |
| TypeScript | ESLint, Prettier |
| Python | Pylint, Flake8, Black |
| YAML | yamllint |
| JSON | jsonlint |
| Markdown | markdownlint |
| Shell | shfmt |
| Docker | hadolint |
| GitHub Actions | actionlint |

## Step-by-Step Workflow

### Step 1: Check MegaLinter Setup

Verify that MegaLinter is properly configured:

```bash
# Check if .mega-linter.yml exists
ls -la .mega-linter.yml

# If missing, run installation
npx mega-linter-runner --install
```

The installation wizard will:
1. Ask about your project type (select "documentation" for multi-language projects)
2. Generate `.mega-linter.yml` configuration file
3. Optionally create GitHub Actions workflow

### Step 2: Run MegaLinter

Execute MegaLinter to identify all issues:

```bash
npx mega-linter-runner
```

For specific files or folders:
```bash
npx mega-linter-runner -p java/ --filesonly java/Main.java
```

**Expected output**: MegaLinter will scan all files and report errors by linter name.

### Step 3: Parse and Categorize Errors

MegaLinter outputs errors in this format:
```
[MAIN] Scanning all files...
[JAVA_PMD] Found 5 issues in java/Main.java
  - Line 8: Avoid unused imports
  - Line 15: Close resource in try-with-resources
  ...
```

**Categorize by:**
1. **Auto-fixable** - Formatting issues (prettier, black, gofmt, shfmt)
2. **Manual fixes needed** - Logic issues, bad practices (PMD, SpotBugs, pylint)

### Step 4: Fix Auto-Fixable Issues

For linters that support `--fix` or auto-formatting:

```bash
# Apply all available fixes
npx mega-linter-runner --fix

# Or fix specific languages
npx mega-linter-runner --fix -e "ENABLE=JAVASCRIPT,TYPESCRIPT,PYTHON"
```

**Linters with auto-fix:**
- JavaScript/TypeScript: ESLint (`--fix`), Prettier
- Python: Black, autopep8
- Go: gofmt, goimports
- Shell: shfmt
- JSON: fixjson (manual formatting)

### Step 5: Guide Manual Fixes

For issues that require manual intervention, provide specific guidance:

#### Java Common Issues

**PMD - Unused import:**
```java
// BAD:
import java.net.URL;  // Unused

// FIX: Remove unused import
```

**PMD - Close resources:**
```java
// BAD:
FileInputStream fis = new FileInputStream("file.txt");
// ... use fis
// Never closed!

// FIX: Use try-with-resources
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // ... use fis
} // Automatically closed
```

**SpotBugs - Null dereference:**
```java
// BAD:
processData(null);

private static void processData(String input) {
    if (input.equals("test")) {  // NullPointerException if null
        // ...
    }
}

// FIX: Add null check
private static void processData(String input) {
    if (input != null && input.equals("test")) {
        // ...
    }
}
```

**SpotBugs - Thread safety:**
```java
// BAD:
private static int counter = 0;
// Used from multiple threads without synchronization

// FIX: Use AtomicInteger
import java.util.concurrent.atomic.AtomicInteger;
private static AtomicInteger counter = new AtomicInteger(0);
// Increment: counter.incrementAndGet();
```

#### Go Common Issues

**go vet - Unhandled error:**
```go
// BAD:
resp, err := http.Get("https://example.com")
resp.Body.Close()  // If err != nil, resp is nil!

// FIX:
resp, err := http.Get("https://example.com")
if err != nil {
    log.Fatal(err)
}
defer resp.Body.Close()
```

**go vet - Unused variable:**
```go
// BAD:
var unusedVar string
_ = unusedVar  // Still flagged by some linters

// FIX: Remove unused variable entirely
```

**Race condition - Unprotected shared variable:**
```go
// BAD:
var sharedCounter int
go func() { sharedCounter++ }()  // Race condition!

// FIX: Use sync/atomic or mutex
import "sync/atomic"
var sharedCounter int64
go func() { atomic.AddInt64(&sharedCounter, 1) }()
```

**defer in loop:**
```go
// BAD:
for i := 0; i < 5; i++ {
    defer fmt.Println("deferred:", i)  // Deferred until function exit!
}

// FIX: Wrap in function or move outside loop
for i := 0; i < 5; i++ {
    func(idx int) {
        defer fmt.Println("deferred:", idx)
        // ...
    }(i)
}
```

#### JavaScript/TypeScript Common Issues

**ESLint - Missing semicolons:**
```javascript
// BAD:
const x = 10
console.log(x)

// FIX:
const x = 10;
console.log(x);
```

**ESLint - Unused variables:**
```javascript
// BAD:
const unusedVar = 42;
console.log("Hello");

// FIX:
// Remove unusedVar or use it
```

**TypeScript - any type:**
```typescript
// BAD:
function greet(name: any): any {
    return "Hello " + name;
}

// FIX:
function greet(name: string): string {
    return "Hello " + name;
}
```

#### Python Common Issues

**PEP8 - Whitespace:**
```python
# BAD:
def process_data( data ):
    result=[]
    for item in data:
        if item>10:
            result.append( item*2 )

# FIX:
def process_data(data):
    result = []
    for item in data:
        if item > 10:
            result.append(item * 2)
```

**Import issues:**
```python
# BAD:
import os
import sys
# ... don't use os or sys

# FIX: Remove unused imports
```

#### YAML Common Issues

**yamllint - Indentation:**
```yaml
# BAD:
production:
  host: 0.0.0.0
    port: 8080  # Wrong indentation!

# FIX:
production:
  host: 0.0.0.0
  port: 8080
```

**yamllint - Unquoted special chars:**
```yaml
# BAD:
api_key: abc123!@#

# FIX:
api_key: "abc123!@#"
```

#### JSON Common Issues

**jsonlint - Trailing commas:**
```json
// BAD:
{
  "name": "test",
  "version": "1.0.0",  // Trailing comma!
}

// FIX:
{
  "name": "test",
  "version": "1.0.0"
}
```

#### Markdown Common Issues

**markdownlint - Line length:**
```markdown
# BAD:
This is a very long line that exceeds the recommended 120 character limit for markdown files and will trigger a linting error from markdownlint which checks for line length issues.

# FIX:
This is a very long line that exceeds the recommended 120 character
limit for markdown files and will trigger a linting error from
markdownlint which checks for line length issues.
```

**markdownlint - Missing alt text:**
```markdown
# BAD:
![Image](image.png)

# FIX:
![Description of image](image.png)
```

#### Shell Script Issues

**shfmt - Missing shebang:**
```bash
# BAD:
ENV="production"
echo "Deploying"

# FIX:
#!/bin/bash
ENV="production"
echo "Deploying"
```

#### Dockerfile Issues

**hadolint - pin versions:**
```dockerfile
# BAD:
FROM node:latest

# FIX:
FROM node:18-alpine
```

**hadolint - COPY vs COPY (case):**
```dockerfile
# BAD:
COPY package.json .  # Should be consistent

# FIX:
COPY package.json .
```

#### GitHub Actions Issues

**actionlint - Deprecated actions:**
```yaml
# BAD:
- uses: actions/checkout@v1

# FIX:
- uses: actions/checkout@v4
```

### Step 6: Verify Fixes

After applying fixes, re-run MegaLinter:

```bash
npx mega-linter-runner
```

If all issues are resolved, you'll see:
```
[MAIN] All files passed linting!
```

### Step 7: Commit Fixes

Once all linter errors are resolved:

```bash
git add .
git commit -m "fix: resolve all megalinter lint errors"
```

## Advanced: Customizing MegaLinter

### Disable Specific Linters

Edit `.mega-linter.yml`:
```yaml
DISABLE:
  - JAVA_PMD      # Disable PMD for Java
  - PYTHON_PYLINT  # Disable Pylint for Python
  - COPYPASTE      # Disable copy-paste detection
  - SPELL          # Disable spell check
```

### Enable Only Specific Linters

```yaml
ENABLE:
  - JAVA_PMD
  - JAVASCRIPT_ES
  - PYTHON_PYLINT
```

### Apply Fixes Automatically

Add to `.mega-linter.yml`:
```yaml
APPLY_FIXES: all  # Or list specific linters: [JAVASCRIPT_ES, PYTHON_BLACK]
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| MegaLinter not found | Run `npm install mega-linter-runner -g` |
| Docker permission denied | Add user to docker group: `sudo usermod -aG docker $USER` |
| Too many errors to fix | Use `APPLY_FIXES: all` for auto-fixable issues first |
| Java linters failing | Ensure Java is installed: `java -version` |
| Go linters failing | Ensure Go is installed: `go version` |
| False positives | Disable specific linters in `.mega-linter.yml` |

## Project-Specific Notes

This demo project has two branches:
- `main` - Contains intentional lint errors
- `fix/megalinter-issues` - Contains fixed versions

To compare fixes:
```bash
git diff main fix/megalinter-issues -- java/Main.java
```

## Resources

- [MegaLinter Documentation](https://megalinter.io/latest)
- [PMD Rules](https://pmd.github.io/pmd/pmd_rules_java.html)
- [Go Vet Documentation](https://pkg.go.dev/cmd/vet)
- [ESLint Rules](https://eslint.org/docs/latest/rules/)
- [Pylint Messages](https://pylint.pycqa.org/en/latest/messages/messages_list.html)
- [yamllint Documentation](https://yamllint.readthedocs.io/)
