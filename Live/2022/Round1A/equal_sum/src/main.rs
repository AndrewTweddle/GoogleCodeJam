use std::io;
use std::io::{BufRead, Write};
use std::cmp::Reverse;
use std::collections::BTreeSet;

fn main() {
    let stdin = io::stdin();
    let stdin_lock = stdin.lock();

    let stdout = io::stdout();
    let mut stdout_lock = stdout.lock();

    let mut line_reader = stdin_lock.lines();
    let test_case_count = line_reader.next().unwrap().unwrap().parse::<u8>().unwrap();

    for _t in 1..=test_case_count {
        let n = line_reader.next().unwrap().unwrap().parse::<u64>().unwrap();

        let pows_of_2_count = n.min(29);  // powers of 2 up to 2^29 < 10^9
        let mut all_nums_set = BTreeSet::<u64>::new();

        // Write out powers of 2
        let mut next_number = 1;
        for _ in 1..=pows_of_2_count {
            all_nums_set.insert(next_number);
            write!(stdout_lock, "{} ", next_number).unwrap();
            next_number <<= 1;
        }

        // Add sequential small numbers to make up the extra numbers
        next_number = 3;
        while all_nums_set.len() < n as usize - 1 {
            if !all_nums_set.contains(&next_number) {
                write!(stdout_lock, "{} ", next_number).unwrap();
                all_nums_set.insert(next_number);
            }
            next_number += 1
        }

        let last_num: u64 = all_nums_set.iter().sum();
        all_nums_set.insert(last_num);
        writeln!(stdout_lock, "{}", last_num).unwrap();

        let given_nums_str = line_reader.next().unwrap().unwrap();
        given_nums_str
            .trim()
            .split_whitespace()
            .for_each(|num_str| {
                let given_num = num_str.parse::<u64>().unwrap();
                all_nums_set.insert(given_num);
            });

        // Sort the given numbers in reverse order...
        let mut all_nums: Vec<u64> = all_nums_set.iter().map(|&i| i).collect();
        all_nums.sort_by_key(|&g| Reverse(g));
        let total_sum: u64 = all_nums.iter().sum();
        let target = total_sum / 2;

        let mut nums_1: Vec<u64> = Vec::with_capacity(2 * n as usize - 1);
        let mut nums_2: Vec<u64> = Vec::with_capacity(2 * n as usize - 1);
        let mut sum_1: u64 = 0;

        // Try to get set 1 as close to the target as possible (without going over):
        for &g in &all_nums {
            if sum_1 + g <= target {
                nums_1.push(g);
                sum_1 += g;
            } else {
                nums_2.push(g);
            }
        }

        let last_num = nums_1.pop().unwrap();

        for &num in nums_1.iter() {
            write!(stdout_lock, "{} ", num).unwrap();
        }
        writeln!(stdout_lock, "{} ", last_num).unwrap();
    }
}
