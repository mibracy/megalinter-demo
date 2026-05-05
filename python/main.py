"""Main module for demonstrating MegaLinter fixes."""

from typing import List


def process_data(data: List[int]) -> List[int]:
    """Process the input data."""
    result = []
    for item in data:
        if item > 10:
            result.append(item * 2)
    return result


def fetch_url(url: str) -> bytes:
    """Fetch data from URL safely."""
    import urllib.error
    import urllib.request

    if not url.startswith(("http://", "https://")):
        raise ValueError(f"Invalid URL scheme: {url}")

    try:
        response = urllib.request.urlopen(url, timeout=10)  # nosec B310
        return response.read()
    except urllib.error.URLError as e:
        raise ConnectionError(f"Failed to fetch {url}: {e}")


def main() -> None:
    """Run main function."""
    numbers = [1, 5, 15, 20, 25]
    processed = process_data(numbers)
    print(processed)
    x = 10
    y = 20
    z = x + y
    print("Sum: " + str(z))


if __name__ == "__main__":
    main()
