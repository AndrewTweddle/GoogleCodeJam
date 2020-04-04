import sys

test_case_count = int(sys.stdin.readline())

for t in range(1, test_case_count+1):
    digits_string = sys.stdin.readline().strip()
    digits = [int(ch) for ch in digits_string]
    # insert an initial dummy zero to seed the first comparison
    digits.insert(0, 0)
    output = "Case #{0}: ".format(t)
    num_digits = len(digits)
    for i in range(num_digits):
        # Compare each digit to the preceding digit
        prev_digit = digits[i]

        if i == num_digits - 1:
            # Treat like an imaginary zero at the end
            curr_digit = 0
            # But don't output it
            digit_str = ''
        else:
            curr_digit = digits[i + 1]
            digit_str = digits_string[i]

        if curr_digit == prev_digit:
            output += digit_str
        else:
            if curr_digit > prev_digit:
                parenthesis_count = curr_digit - prev_digit
                parenthesis = '('
            else:
                parenthesis_count = prev_digit - curr_digit
                parenthesis = ')'
            parentheses = parenthesis_count * parenthesis
            output += parentheses + digit_str
    output += "\n"
    sys.stdout.write(output)
