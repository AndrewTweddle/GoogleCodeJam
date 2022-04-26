use std::collections::VecDeque;
use std::io;
use std::io::{BufRead, Write};

fn main() {
    let stdin = io::stdin();
    let stdin_lock = stdin.lock();

    let stdout = io::stdout();
    let mut stdout_lock = stdout.lock();

    let mut line_reader = stdin_lock.lines();
    let test_case_count = line_reader.next().unwrap().unwrap().parse::<u8>().unwrap();

    for t in 1..=test_case_count {
        let _n = line_reader
            .next()
            .unwrap()
            .unwrap()
            .parse::<usize>()
            .unwrap();
        let mut d: VecDeque<u32> = line_reader
            .next()
            .unwrap()
            .unwrap()
            .split_ascii_whitespace()
            .map(|d_str| d_str.parse::<u32>().unwrap())
            .collect();

        let mut answer: usize = 0;
        let mut biggest_val: u32 = 0;
        while !d.is_empty() {
            let next_val = (if d.front() < d.back() {
                d.pop_front()
            } else {
                d.pop_back()
            })
            .unwrap();
            if next_val >= biggest_val {
                biggest_val = next_val;
                answer += 1;
            }
        }

        writeln!(stdout_lock, "Case #{}: {}", t, answer).unwrap();
    }
}
