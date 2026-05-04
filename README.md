# MegaLinter Demo Project

A demonstration repository showing intentional linting errors across multiple languages and how to fix them using [MegaLinter](https://megalinter.io/latest).

## 🎯 Purpose

This project serves as a hands-on tutorial for:
- Understanding MegaLinter and its capabilities
- Learning how to fix common linting errors in Java, Go, JavaScript, TypeScript, Python, and more
- Using the included `megalinter-enforcer` OpenCode skill to automate fixes
- Comparing buggy vs. fixed code side-by-side

## 📁 Project Structure

```
linter/
├── java/                    # Java files (PMD, Checkstyle, SpotBugs errors)
├── go/                      # Go files (go vet, golint, gofmt errors)
├── src/                     # JS/TS files (ESLint, Prettier errors)
├── python/                  # Python files (Pylint, Flake8, Black errors)
├── config/                  # YAML/JSON config files (yamllint, jsonlint errors)
├── docs/                    # Markdown files (markdownlint errors)
├── scripts/                 # Shell scripts (shfmt errors)
├── .github/workflows/       # GitHub Actions (actionlint errors)
├── .agents/skills/          # Project-local OpenCode skill
│   └── megalinter-enforcer/
│       └── SKILL.md         # The enforcer skill definition
├── .mega-linter.yml         # MegaLinter configuration
└── Dockerfile               # Dockerfile (hadolint errors)
```

## 🌿 Git Branches

| Branch | Description |
|--------|-------------|
| `main` | Contains **intentional lint errors** to demonstrate MegaLinter detection |
| `fix/megalinter-issues` | Contains **fixed versions** of all files, passing all linters |

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd linter
```

### 2. Install MegaLinter Runner

```bash
npm install -g mega-linter-runner
```

### 3. Run MegaLinter (on main branch)

```bash
git checkout main
npx mega-linter-runner
```

You'll see output showing all the linting errors across different files.

### 4. Use the OpenCode Skill

If using OpenCode, the project-local skill will be automatically discovered:

```
opencode> load skill megalinter-enforcer
opencode> fix all the megalinter errors
```

The skill will:
1. Run MegaLinter to identify issues
2. Categorize errors (auto-fixable vs. manual)
3. Apply auto-fixes where possible
4. Guide you through manual fixes with examples
5. Verify all fixes pass

### 5. Compare with Fixed Branch

```bash
git diff main fix/megalinter-issues
```

This shows exactly what was changed to fix each lint error.

## 🐛 Types of Errors Demonstrated

### Java (`java/Main.java`)
- ❌ Unclosed `FileInputStream` (resource leak)
- ❌ Null dereference risk (`processData(null)`)
- ❌ Thread-unsafe shared counter (race condition)
- ❌ Generic `catch (Exception)` (bad practice)
- ❌ Static collection memory leak
- ❌ Unused imports

### Go (`go/main.go`)
- ❌ Unhandled errors (`http.Get` returns error, not just response)
- ❌ Goroutine leak (no `sync.WaitGroup`)
- ❌ Race condition (unprotected `sharedCounter`)
- ❌ Nil pointer dereference risk
- ❌ `defer` in loop (defers until function exit)
- ❌ Unused imports and variables

### JavaScript (`src/index.js`)
- ❌ Missing semicolons
- ❌ Unused variables
- ❌ Bad formatting (missing spaces)
- ❌ Inconsistent brace style

### TypeScript (`src/utils.ts`)
- ❌ Excessive `any` type usage
- ❌ Missing return types
- ❌ Untyped arrays

### Python (`python/main.py`)
- ❌ PEP8 violations (spacing around operators)
- ❌ Missing whitespace after commas
- ❌ Unused imports
- ❌ Non-standard function naming

### Config Files
- ❌ **YAML**: Bad indentation, unquoted special characters
- ❌ **JSON**: Trailing commas, bad formatting
- ❌ **Markdown**: Lines >120 chars, missing alt text
- ❌ **Shell**: No shebang, unused variables
- ❌ **Dockerfile**: `latest` tag, uppercase commands
- ❌ **GitHub Actions**: Deprecated action versions

## 🛠️ Using the MegaLinter Enforcer Skill

The project includes a custom OpenCode skill at `.agents/skills/megalinter-enforcer/SKILL.md`.

**To use it:**
1. Open OpenCode in this project directory
2. Ask: "Can you help me fix the MegaLinter errors?"
3. The skill will automatically load and guide you through fixes

**Skill capabilities:**
- Runs MegaLinter and parses output
- Explains each error type with examples
- Applies auto-fixes (formatting, etc.)
- Guides manual fixes for complex issues
- Verifies fixes by re-running linter

## 📊 MegaLinter Configuration

The `.mega-linter.yml` file enables these linters:

| Language | Linters |
|----------|---------|
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

## 🔗 Resources

- [MegaLinter Documentation](https://megalinter.io/latest)
- [OpenCode Skills Guide](https://opencode.ai/docs/skills)
- [PMD Java Rules](https://pmd.github.io/pmd/pmd_rules_java.html)
- [Go Vet Documentation](https://pkg.go.dev/cmd/vet)
- [ESLint Rules](https://eslint.org/docs/latest/rules/)
- [Pylint Messages](https://pylint.pycqa.org/en/latest/messages/messages_list.html)

## 📝 License

MIT

---

**Note**: This project is intentionally broken to serve as a learning tool. The `fix/megalinter-issues` branch contains the corrected versions of all files.
