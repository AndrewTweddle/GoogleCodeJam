#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int gcd(int a, int b)
{
    if (a == 0)
    {
        return b;
    }
    if (a > b)
    {
        return gcd(b, a);
    }
    return gcd(b % a, a);
}

const size_t TARGET_SAMPLE_SIZE = 50;
const size_t OUTLIER_SAMPLES_TO_OMIT = 20;

// To under-estimate the passage count, but reduce the impact of outliers, one can walk to an adjacent room
// and take the smaller of the two passage counts, by setting the following flag to true...
const bool USE_MIN_OF_PAIR = false;
const bool USE_MEDIAN_ESTIMATE = true;

class Solver
{
  public:
    Solver(int n, int k, int initial_r, int initial_p): n_(n), k_(k), initial_r_(initial_r), initial_p_(initial_p) {};
    
    void Solve()
    {
        // Sample rooms at an evenly spaced interval.
        // But look for an interval relatively prime to n, so that we don't visit the same room twice
        int interval;
        for (interval = n_ / k_; interval < n_; interval++)
        {
            if (gcd(interval, n_) == 1)
            {
                break;
            }
        }
        
        size_t data_set_count = (k_ + TARGET_SAMPLE_SIZE - 1) / TARGET_SAMPLE_SIZE;
        long long sample_size = k_ / data_set_count;
        if (USE_MIN_OF_PAIR)
        {
            data_set_count /= 2;
        }
        vector<vector<long long>> data_sets;
        
        for (int d = 0; d < data_set_count; d++)
        {
            data_sets.push_back(vector<long long>());
            data_sets.back().reserve(sample_size);
        }
        
        vector<long long> passage_sums(data_set_count, 0);
        
        // Now build up multiple data sets, taking one sample at a time
        auto r = initial_r_;
        
        for (int s = 0; s < sample_size; s++)
        {
            for (size_t d = 0; d < data_set_count; d++)
            {
                r += interval;
                if (r > n_)
                {
                    r = (r - 1) % n_ + 1;
                }
                cout << "T " << r << endl;
                int p;
                cin >> r >> p;
                if (USE_MIN_OF_PAIR)
                {
                    cout << "W" << endl;
                    int r2;
                    int p2;
                    cin >> r2 >> p2;
                    if (p2 < p)
                    {
                        p = p2;
                    }
                }
                data_sets[d].push_back(p);
            }
        }
    
        for (int d = 0; d < data_set_count; d++)
        {
            vector<long long> &data_set = data_sets[d];
            sort(data_set.begin(), data_set.end());
            long long sum_of_data = 0;
            for (int s = OUTLIER_SAMPLES_TO_OMIT; s < sample_size - OUTLIER_SAMPLES_TO_OMIT; s++)
            {
                sum_of_data += data_set[s];
            }
            passage_sums[d] = sum_of_data;
        }
        
        // Include the initial room only if we have little data, otherwise we'll ignore it, in case it skews the data
        if (data_set_count == 1)
        {
            sample_size += 1;
            passage_sums[0] += initial_p_;
        }
        else
        {
            // Rooms that are hubs (connecting to many other rooms) can be outliers in the datasets
            // which can skew the estimates significantly. So take the median estimate out of all the data sets.
            sort(passage_sums.begin(), passage_sums.end());
        }
    
        long long median_passage_sum = USE_MEDIAN_ESTIMATE ? passage_sums[data_set_count / 2] : passage_sums[0];
    
        if (USE_MEDIAN_ESTIMATE && data_set_count % 2 == 0)
        {
            median_passage_sum += passage_sums[data_set_count / 2 + 1];
            median_passage_sum /= 2;
        }
        // Every passage joins 2 rooms, so divide the total estimated passage count by 2
        long long denominator = sample_size - 2 * OUTLIER_SAMPLES_TO_OMIT;
        long long estimated_passages = n_ * median_passage_sum / denominator / 2;
        cout << "E " << estimated_passages << endl;
    }
    
  private:
    long long n_;
    int k_;
    int initial_r_;
    int initial_p_;
};

int main()
{
    int test_case_count;
    cin >> test_case_count;
    for (int t = 0; t < test_case_count; t++)
    {
        int n, k;
        cin >> n >> k;
        int r, p;  // room number (1-based), passages from room
        cin >> r >> p;
        if (k >= n)
        {
            // enumerate all rooms
            long long total_passage_count = p;
            for (int i = 0; i < n - 1; i++)
            {
                r++;
                if (r > n)
                {
                    r = 1;
                }
                cout << "T " << r << endl;
                cin >> r >> p;
                total_passage_count += p;
            }
            // Every passage joins 2 rooms, so divide the total passage count by 2
            cout << "E " << (total_passage_count / 2) << endl;
        }
        else
        {
            // We will estimate the passage count statistically
            Solver solver(n, k, r, p);
            solver.Solve();
        }
    }
    return 0;
}
