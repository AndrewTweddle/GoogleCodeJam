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
        let s = line_reader.next().unwrap().unwrap();

        let mut ch_by_count: Vec<(char, usize)> = Vec::with_capacity(s.len());
        let mut prev = ' ';
        let mut ch_count = 0;
        for ch in s.chars() {
            if ch == prev {
                ch_count += 1;
            } else {
                if ch_count > 0 {
                    ch_by_count.push((prev, ch_count));
                }
                prev = ch;
                ch_count = 1;
            }
        }
        if ch_count > 0 {
            ch_by_count.push((prev, ch_count));
        }
        ch_by_count.push((' ', 0));  // To form a valid last pair

        let answer: String = ch_by_count.windows(2).map(|pair| {
            let curr_ch = pair[0].0;
            let next_ch = pair[1].0;
            let repetitions = pair[0].1 * if curr_ch < next_ch { 2 } else { 1 };
            (0..repetitions).map(|_| curr_ch).collect::<String>()
        }).collect();

        writeln!(stdout_lock, "Case #{}: {}", t, answer).unwrap();
    }
}
