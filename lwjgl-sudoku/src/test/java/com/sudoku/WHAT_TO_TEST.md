# So what do we acutally need to test?
1. Test the behaviour
2. Test Edge Cases
3. Test Rules
4. Test Different Inputs

# How to make the actual test
##  Arrange
Calculator calc = new Calculator();

## Act
int result = calc.add(2, 3);

## Assert
assertEquals(5, result);