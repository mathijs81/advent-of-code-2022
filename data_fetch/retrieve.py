from aocd.models import Puzzle
from aocd.get import current_day, most_recent_year

if __name__ == "__main__":
    puzzle = Puzzle(most_recent_year(), current_day())
    example_data = puzzle.example_data
    input_data = puzzle.input_data

    with open("../src/Day%02d.txt" % puzzle.day, "w") as text_file:
        text_file.write(input_data)
    with open("../src/Day%02d_test.txt" % puzzle.day, "w") as text_file:
        text_file.write(example_data)
