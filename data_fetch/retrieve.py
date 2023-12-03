from aocd.models import Puzzle
from aocd.get import current_day, most_recent_year

if __name__ == "__main__":
    day = current_day()
    #day = 1
    year = most_recent_year()

    puzzle = Puzzle(year, day)
    input_data = puzzle.input_data
    with open("../src/Day%02d.txt" % puzzle.day, "w") as text_file:
        text_file.write(input_data)

    if len(puzzle.examples) != 1:
        print(f"Warning, there are {len(puzzle.examples)} examples instead of 1!")
    #print(dir(puzzle.examples[0]))
    example_data = puzzle.examples[0].input_data
    with open("../src/Day%02d_test.txt" % puzzle.day, "w") as text_file:
        text_file.write(example_data)
        
    print(f'Data for day {day} successfully written')
