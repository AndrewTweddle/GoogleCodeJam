use std::io;
use std::io::{BufRead, Write};

type Pressure = u64;
type CumStates = [CumState; 2];

#[derive(Clone, Copy)]
struct CumState {
    pressure: Pressure,
    cum_button_presses: Pressure,
}
type PressureRange = (Pressure, Pressure);

fn main() {
    let stdin = io::stdin();
    let stdin_lock = stdin.lock();

    let stdout = io::stdout();
    let mut stdout_lock = stdout.lock();

    let mut line_reader = stdin_lock.lines();
    let test_case_count = line_reader.next().unwrap().unwrap().parse::<u8>().unwrap();

    for t in 1..=test_case_count {
        let line = line_reader.next().unwrap().unwrap();
        let n_p_strs: Vec<&str> = line.split(' ').collect();
        let n = n_p_strs[0].parse::<usize>().unwrap();
        let p_ranges: Vec<PressureRange> = (0..n)
            .map(|_| {
                let xs: Vec<Pressure> = line_reader
                    .next()
                    .unwrap()
                    .unwrap()
                    .split_ascii_whitespace()
                    .map(|x_i_j_str| x_i_j_str.parse::<Pressure>().unwrap())
                    .collect();
                let min_x = xs.iter().min().unwrap();
                let max_x = xs.iter().max().unwrap();
                (*min_x, *max_x)
            })
            .collect();
        let initial_states = [CumState {
            cum_button_presses: 0,
            pressure: 0,
        }; 2];
        let final_states = p_ranges
            .iter()
            .fold(initial_states, |cum_states, &pressure_range| {
                fn get_cheapest_cum_presses(
                    next_pressure: Pressure,
                    final_pressure: Pressure,
                    prev_states: &CumStates,
                ) -> CumState {
                    let abs_pressure_diff = if final_pressure > next_pressure {
                        final_pressure - next_pressure
                    } else {
                        next_pressure - final_pressure
                    };
                    let min_cum_button_presseds = prev_states
                        .iter()
                        .map(|&state| {
                            state.cum_button_presses
                                + if next_pressure < state.pressure {
                                    state.pressure - next_pressure
                                } else {
                                    next_pressure - state.pressure
                                }
                                + abs_pressure_diff
                        })
                        .min()
                        .unwrap();
                    CumState {
                        cum_button_presses: min_cum_button_presseds,
                        pressure: final_pressure,
                    }
                }

                [
                    get_cheapest_cum_presses(pressure_range.1, pressure_range.0, &cum_states),
                    get_cheapest_cum_presses(pressure_range.0, pressure_range.1, &cum_states),
                ]
            });

        let answer = final_states[0]
            .cum_button_presses
            .min(final_states[1].cum_button_presses);
        writeln!(stdout_lock, "Case #{}: {}", t, answer).unwrap();
    }
}
