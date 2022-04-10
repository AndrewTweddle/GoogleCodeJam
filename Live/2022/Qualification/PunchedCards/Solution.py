t = int(input())
for i in range(1, t + 1):
    r, c = [int(s) for s in input().split(" ")]
    print("Case #{}:".format(i))
    repetitions = c
    for row in range(2 * r + 1):
        if row % 2 == 0:
            prefix = "+-+"
            cell = "-+"
        else:
            prefix = "|.|"
            cell = ".|"

        # Override prefix for top left empty block
        if row <= 1:
            prefix = ".." + prefix[-1]

        print(prefix + (c-1) * cell)
