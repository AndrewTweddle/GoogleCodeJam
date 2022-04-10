use std::io;
use std::io::{BufRead, Write};

const MAX_DICE_SIZE: usize = 1_000_000;

fn main() {
    let stdin = io::stdin();
    let stdin_lock = stdin.lock();

    let stdout = io::stdout();
    let mut stdout_lock = stdout.lock();

    let mut line_reader = stdin_lock.lines();
    let test_case_count = line_reader.next().unwrap().unwrap().parse::<u8>().unwrap();

    for t in 1..=test_case_count {
        let n: usize = line_reader
            .next()
            .unwrap()
            .unwrap()
            .parse::<usize>()
            .unwrap();

        let mut size_counts: Vec<usize> = vec![0_usize; MAX_DICE_SIZE + 1];
        line_reader
            .next()
            .unwrap()
            .unwrap()
            .split_whitespace()
            .map(|size| size.parse::<usize>().unwrap())
            .for_each(|size| size_counts[size] += 1);

        let mut seq_len: usize = 0; // Sequences start at 1, so this is also the last dice value
        let mut dice_considered: usize = 0;  // So we can short-circuit the calculations

        for (size, &size_count) in size_counts.iter().enumerate().filter(|(_, &sc)| sc > 0) {
            seq_len += if seq_len + size_count > size {
                size - seq_len
            } else {
                size_count
            };
            dice_considered += size_count;
            if dice_considered == n {
                break;
            }
        }
        writeln!(stdout_lock, "Case #{}: {}", t, seq_len).unwrap();
    }
}
