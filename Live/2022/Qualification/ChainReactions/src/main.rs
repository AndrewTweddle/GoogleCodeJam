use std::io;
use std::io::{BufRead, Write};

struct Module {
    fun_factor: u32,     // This module's own fun factor (excluding its inputs)
    output_index: usize, // The inverted, shifted index of the module pointed to
    min_input_branch_fun: u64,  // The minimum input fun of any main input branch thus far
    // When min_input_fun is zero, then no inputs have been propagated to this module yet
    total_other_input_fun: u64, // The total fun of all input branches that terminated (not main)
}

impl Module {
    fn new(fun_factor: u32, output_index: usize) -> Self {
        Module {
            fun_factor,
            output_index,
            min_input_branch_fun: 0,
            total_other_input_fun: 0,
        }
    }

    fn receive_input(&mut self, main_branch_fun: u64, other_branches_fun: u64) {
        if self.min_input_branch_fun == 0 {
            self.min_input_branch_fun = main_branch_fun;
            self.total_other_input_fun += other_branches_fun;
        } else if main_branch_fun >= self.min_input_branch_fun {
            self.total_other_input_fun += main_branch_fun + other_branches_fun;
        } else {
            // terminate the current min input branch, and move into total other branch total
            self.total_other_input_fun += self.min_input_branch_fun + other_branches_fun;
            self.min_input_branch_fun = main_branch_fun;
        }
    }

    fn calculate_main_branch_and_other_branches_fun(&self) -> (u64, u64) {
        if self.min_input_branch_fun == 0 {
            // This is an initiator
            (self.fun_factor as u64, 0)
        } else {
            let main_branch_fun = self.min_input_branch_fun.max(self.fun_factor as u64);
            (main_branch_fun, self.total_other_input_fun)
        }
    }

    fn calculate_terminal_fun(&self) -> u64 {
        // calculate the total fun at the abyss (i.e. the terminal module)
        let (main_fun, other_fun) = self.calculate_main_branch_and_other_branches_fun();
        main_fun + other_fun
    }
}

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
        let fun_factors: Vec<u32> = line_reader
            .next()
            .unwrap()
            .unwrap()
            .split_whitespace()
            .map(|ffstr| ffstr.parse::<u32>().unwrap())
            .collect();
        let pointers: Vec<usize> = line_reader
            .next()
            .unwrap()
            .unwrap()
            .split_whitespace()
            .map(|pointer_str| pointer_str.parse::<usize>().unwrap())
            .collect();

        // Build up all modules, but invert the indices, so that index n is now the abyss
        let mut modules: Vec<Module> = Vec::with_capacity(n + 1);
        for i in 0..n {
            let inv_index = n - i - 1;
            modules.push(Module::new(fun_factors[inv_index], n - pointers[inv_index]));
        }
        // Add the abyss at the end
        modules.push(Module::new(0, n));

        for i in 0..n {
            let src = &mut modules[i];
            let target_index = src.output_index;
            let (main_branch_fun, other_branches_fun)
                = src.calculate_main_branch_and_other_branches_fun();
            let target_module = &mut modules[target_index];
            target_module.receive_input(main_branch_fun, other_branches_fun);
        }

        let max_total_fun = modules[n].calculate_terminal_fun();
        writeln!(stdout_lock, "Case #{}: {}", t, max_total_fun).unwrap();
    }
}
