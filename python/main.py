import json
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
    import urllib.request
    response = urllib.request.urlopen(url)
    return response.read()


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
