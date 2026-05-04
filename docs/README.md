# MegaLinter Demo Project

This is a demonstration project showing various linting errors and how to fix them using MegaLinter. This project contains intentional errors in multiple languages including Java, Go, JavaScript, TypeScript, Python, YAML, JSON, and more.

The purpose of this repository is to help developers understand how MegaLinter works and how to configure it properly for their projects. By examining the differences between the main branch (with errors) and the fix/megalinter-issues branch (with fixes), you can learn best practices for code quality.

## Getting Started

To get started with this project, you'll need to have Node.js installed. Then run the following commands to set up MegaLinter:

```bash
npx mega-linter-runner --install
```

This will create the necessary configuration files for MegaLinter. After that, you can run the linter locally:

```bash
npx mega-linter-runner
```

## Project Structure

- `java/` - Java source files with intentional bugs (unclosed streams, null dereferences, thread safety issues)
- `go/` - Go source files with intentional bugs (unhandled errors, goroutine leaks, race conditions)
- `src/` - JavaScript and TypeScript files with formatting and type issues
- `python/` - Python files with PEP8 violations and other issues
- `config/` - Configuration files (YAML, JSON) with formatting issues
- `docs/` - Documentation with markdown lint issues
- `scripts/` - Shell scripts with formatting issues
- `.github/workflows/` - CI configuration with deprecated actions

## How to Use This Demo

1. Checkout the main branch to see all the intentional errors
2. Run MegaLinter to see the reported issues
3. Checkout the fix/megalinter-issues branch to see the corrected code
4. Compare the branches to understand what was fixed

## Using the MegaLinter Enforcer Skill

This project includes a project-local OpenCode skill called "megalinter-enforcer" that can help you diagnose and fix MegaLinter issues automatically. To use it, simply ask OpenCode something like "fix the megalinter errors" or "check code quality".

The skill will:
1. Run MegaLinter to identify issues
2. Parse the error output
3. Guide you through fixing each issue
4. Apply auto-fixes where possible
5. Verify the fixes by re-running MegaLinter

## Resources

- [MegaLinter Documentation](https://megalinter.io/latest)
- [OpenCode Skills Documentation](https://opencode.ai/docs/skills)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

## License

MIT
