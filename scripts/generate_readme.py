import argparse
import html
import os
import re
import subprocess
import sys
from pathlib import Path


START_MARKER = "<!-- leetcode-ai-readme:start -->"
END_MARKER = "<!-- leetcode-ai-readme:end -->"
DEFAULT_MODEL = "gpt-5.4-mini"
FALLBACK_MODELS = ("gpt-4o-mini",)


def run_git(args):
    return subprocess.run(
        ["git", *args],
        check=False,
        capture_output=True,
        text=True,
    )


def is_zero_sha(value):
    return bool(value) and set(value) == {"0"}


def changed_java_files():
    before = os.getenv("BEFORE_SHA", "").strip()
    after = os.getenv("AFTER_SHA", "").strip() or "HEAD"

    commands = []
    if before and not is_zero_sha(before):
        commands.append(["diff", "--name-only", before, after, "--", "*.java"])
    commands.append(["diff", "--name-only", "HEAD~1", "HEAD", "--", "*.java"])

    for command in commands:
        result = run_git(command)
        if result.returncode == 0:
            files = [
                Path(line.strip())
                for line in result.stdout.splitlines()
                if line.strip().endswith(".java")
            ]
            existing = [path for path in files if path.exists()]
            if existing:
                return sorted(set(existing))

    return []


def target_java_files(target):
    target_path = Path(target)
    if target_path.is_dir():
        return sorted(target_path.rglob("*.java"))
    if target_path.is_file() and target_path.suffix == ".java":
        return [target_path]
    raise SystemExit(f"Target path is not a Java file or folder: {target}")


def all_java_files():
    return sorted(path for path in Path(".").rglob("*.java") if ".git" not in path.parts)


def strip_tags(value):
    return re.sub(r"<[^>]+>", "", value)


def title_from_folder(folder):
    slug = re.sub(r"^\d+-", "", folder.name)
    words = slug.replace("-", " ").split()
    return " ".join(word.capitalize() for word in words) if words else folder.name


def problem_metadata(readme_text, folder):
    title = title_from_folder(folder)
    link = f"https://leetcode.com/problems/{re.sub(r'^\\d+-', '', folder.name)}/"
    difficulty = "Unknown"

    h2_match = re.search(r'<h2><a href="([^"]+)">(.+?)</a></h2>', readme_text, re.DOTALL)
    if h2_match:
        link = html.unescape(h2_match.group(1)).strip()
        title_text = strip_tags(h2_match.group(2))
        title = html.unescape(title_text).strip()

    h3_match = re.search(r"<h3>(Easy|Medium|Hard)</h3>", readme_text)
    if h3_match:
        difficulty = h3_match.group(1)

    return title, link, difficulty


def remove_existing_generated_section(readme_text):
    pattern = re.compile(
        rf"\n*{re.escape(START_MARKER)}.*?{re.escape(END_MARKER)}\n*",
        re.DOTALL,
    )
    return pattern.sub("\n", readme_text).rstrip()


def managed_section(body):
    body = body.strip()
    body = body.replace(START_MARKER, "").replace(END_MARKER, "").strip()
    if not body.startswith("## "):
        body = f"## Approach & Solution\n\n{body}"
    return f"{START_MARKER}\n{body}\n{END_MARKER}\n"


def candidate_models(preferred_model):
    models = [preferred_model, DEFAULT_MODEL, *FALLBACK_MODELS]
    unique = []
    for model in models:
        model = (model or "").strip()
        if model and model not in unique:
            unique.append(model)
    return unique


def safe_error_message(error):
    message = str(error)
    api_key = os.getenv("OPENAI_API_KEY", "")
    if api_key:
        message = message.replace(api_key, "[redacted]")
    message = " ".join(message.split())
    return message[:1000]


def generate_ai_section(client, models, java_file, readme_text, solution):
    title, link, difficulty = problem_metadata(readme_text, java_file.parent)
    prompt = f"""
Create a concise Markdown explanation for a LeetCode Java solution.

Return only this section:

## Approach & Solution

### Approach
Explain the core idea in clear interview-style language.

### Step-by-step
Use short bullets.

### Complexity
- Time: ...
- Space: ...

### Solution Notes
Mention any important Java implementation details.

Do not include the full problem statement. Do not include the full Java code.

Problem title: {title}
Problem link: {link}
Difficulty: {difficulty}
Folder: {java_file.parent.as_posix()}

Existing README content:
{readme_text[:5000]}

Java solution:
```java
{solution[:9000]}
```
""".strip()

    last_error = None
    for model in models:
        try:
            print(f"Generating README section for {java_file} with {model}")
            response = client.responses.create(
                model=model,
                input=prompt,
            )
            output_text = getattr(response, "output_text", "").strip()
            if output_text:
                return output_text
            last_error = RuntimeError("OpenAI returned an empty response.")
            print(f"OpenAI returned an empty response with {model}.", file=sys.stderr)
        except Exception as error:
            last_error = error
            print(
                f"OpenAI generation failed with {model}: "
                f"{error.__class__.__name__}: {safe_error_message(error)}",
                file=sys.stderr,
            )

    raise RuntimeError(
        "OpenAI README generation failed for all configured models. "
        "Check OPENAI_API_KEY, billing/credits, and OPENAI_MODEL."
    ) from last_error


def update_readme_for_java(java_file, client, models):
    readme_file = java_file.parent / "README.md"
    readme_text = readme_file.read_text(encoding="utf-8", errors="ignore") if readme_file.exists() else ""
    solution = java_file.read_text(encoding="utf-8", errors="ignore")

    body = generate_ai_section(client, models, java_file, readme_text, solution)
    updated = remove_existing_generated_section(readme_text)
    if updated:
        updated = f"{updated}\n\n{managed_section(body)}"
    else:
        title, link, difficulty = problem_metadata(readme_text, java_file.parent)
        updated = f"# {title}\n\n- Link: {link}\n- Difficulty: {difficulty}\n\n{managed_section(body)}"

    readme_file.write_text(updated.rstrip() + "\n", encoding="utf-8")
    print(f"Updated {readme_file}")


def parse_args():
    parser = argparse.ArgumentParser(description="Generate README explanations for changed LeetCode Java files.")
    parser.add_argument("--changed-only", action="store_true", help="Only process Java files changed in the latest push.")
    parser.add_argument("--all", action="store_true", help="Process every Java file in the repository.")
    return parser.parse_args()


def main():
    args = parse_args()
    target = os.getenv("TARGET_PATH", "").strip()

    if target:
        java_files = target_java_files(target)
    elif args.all:
        java_files = all_java_files()
    elif args.changed_only:
        java_files = changed_java_files()
    else:
        java_files = changed_java_files()

    if not java_files:
        print("No Java files selected for README generation.")
        return 0

    api_key = os.getenv("OPENAI_API_KEY", "").strip()
    if not api_key:
        print("OPENAI_API_KEY is not set; skipping AI README generation.")
        print("Add OPENAI_API_KEY in repository Actions secrets to enable this workflow.")
        return 0

    try:
        from openai import OpenAI
    except ImportError:
        print("The openai package is not installed.", file=sys.stderr)
        return 1

    client = OpenAI(api_key=api_key)
    preferred_model = os.getenv("OPENAI_MODEL", DEFAULT_MODEL).strip() or DEFAULT_MODEL
    models = candidate_models(preferred_model)
    print(f"Model order: {', '.join(models)}")

    for java_file in java_files:
        update_readme_for_java(java_file, client, models)

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
