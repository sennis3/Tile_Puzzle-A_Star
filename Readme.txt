Sean Ennis
sennis3, 653900061
CS 411
Assignment 6 - A* Search
March 7, 2020

Navigate to the bin directory in sennis3_astar and run it with "java AStarSearch"

The program will prompt for the initial configuration state of the tiles a list of 16 numbers (0-15) with a space between each number. The 0 represents the empty space on the board.

The goal state would be represented as: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0

The program will automatically run the search twice with two different heuristics: misplaced tiles and Manhattan distance. The results for the misplaced tile heuristic is displayed first with the Manhattan distance results after that.

The program will only run test case at a time, so if you want to try a another initial state, the program needs to be run again.

I implemented my search tree that would expand child nodes in the order up, down, left, and right.

