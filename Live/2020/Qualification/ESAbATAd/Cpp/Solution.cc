#include <iostream>
#include <bitset>
#include <sstream>

using std::bitset;
using std::cin;
using std::cout;
using std::cerr;
using std::string;

template<std::size_t B>
class Solver
{
  public:
    Solver(): mBitsPerHalf(B/2) {};
    
    bool TryToSolve()
    {
        while (mQueryCount < 150)
        {
            cerr << "DEBUG: Next loop starting. mQueryCount = " << mQueryCount << std::endl;
            
            // Solution found?
            if (mPairsOfBitsRead == mBitsPerHalf)
            {
                cerr << "DEBUG: mPairsOfBitsRead = " << mPairsOfBitsRead << std::endl;
                return TryToSendSolution();
            }
            
            // Database about to undergo a quantum fluctuation event?
            if (mQueryCount > 0 && mQueryCount % 10 == 0)
            {
                cerr << "DEBUG: Database about to experience quantum fluctuation" << std::endl;
                if (!TryToQueryForToggledBits())
                {
                    return false;
                }
                if (!TryToQueryForReversedOrderOfBits())
                {
                    return false;
                }
            }
            
            // Read a pair of bits
            if (!TryToQueryForNextPairOfCorrespondingBits())
            {
                return false;
            }
        }
        
        cerr << "ERROR: 150 queries completed with no solution" << std::endl;
        return false;
    }
    
  private:
    bool TryToSendSolution()
    {
        string Solution = GetSolution();
        cout << Solution << std::endl;
        cerr << "DEBUG: solution sent is: " << Solution << std::endl;
        
        char Response;
        cin >> Response;
        if (Response == 'Y')
        {
            return true;
        };
    
        return false;
    }
    
    string GetSolution()
    {
        std::ostringstream Oss;
    
        for (int i = 0; i < mBitsPerHalf; ++i)
        {
            if (i < mPairsOfBitsRead)
            {
                Oss << ((*mpForwardsHalf)[i] ? '1' : '0');
            }
            else
            {
                Oss << '?';
            }
        }
        for (int i = mBitsPerHalf-1; i >= 0; --i)
        {
            if (i < mPairsOfBitsRead)
            {
                Oss << ((*mpBackwardsHalf)[i] ? '1' : '0');
            }
            else
            {
                Oss << '?';
            }
        }
        
        return Oss.str();
    }
    
    bool TryToQueryForToggledBits()
    {
        cerr << "DEBUG: TryToQueryForToggledBits()" << std::endl;
        
        if (mIndexOfSymmetricPair == -1)
        {
            // All pairs are anti-symmetric, so toggling has no effect on our current state of knowledge.
            // Perform a dummy query, to always read in pairs.
            return PerformDummyQuery();
        }
        
        // If it has changed, flip all bits
        bool OldVal = (*mpForwardsHalf)[mIndexOfSymmetricPair];
        bool NewVal;
        if (!TryToQueryForBitInPosition(mIndexOfSymmetricPair, NewVal))
        {
            return false;
        }
        
        if (NewVal != OldVal)
        {
            ToggleBits();
        }
        
        return true;
    }
    
    void ToggleBits()
    {
        cerr << "DEBUG: ToggleBits()" << std::endl;
        mpForwardsHalf->flip();
        mpBackwardsHalf->flip();
        cerr << "DEBUG: solution so far is: " << GetSolution() << std::endl;
    }
    
    bool TryToQueryForReversedOrderOfBits()
    {
        cerr << "DEBUG: TryToQueryForReversedOrderOfBits()" << std::endl;
    
        if (mIndexOfAntiSymmetricPair == -1)
        {
            // Forwards and backwards halves have been identical so far, so reversal has no effect on our state.
            // Perform a dummy query, to always read in pairs.
            return PerformDummyQuery();
        }
        
        bool OldVal = (*mpForwardsHalf)[mIndexOfAntiSymmetricPair];
        bool NewVal;
        if (!TryToQueryForBitInPosition(mIndexOfAntiSymmetricPair, NewVal))
        {
            return false;
        };
    
        // If a value has changed, then the order was reversed, so reverse our bits to synchronize with the database
        if (NewVal != OldVal)
        {
            ReverseOrderOfBits();
        }
        
        return true;
    }
    
    void ReverseOrderOfBits()
    {
        cerr << "DEBUG: ReverseOrderOfBits()" << std::endl;
        mIsReversed = !mIsReversed;
        if (mIsReversed)
        {
            mpForwardsHalf = &mHalf2;
            mpBackwardsHalf = &mHalf1;
        }
        else
        {
            mpForwardsHalf = &mHalf1;
            mpBackwardsHalf = &mHalf2;
        }
        cerr << "DEBUG: solution so far is: " << GetSolution() << std::endl;
    }
    
    bool TryToQueryForNextPairOfCorrespondingBits()
    {
        cerr << "DEBUG: TryToQueryForNextPairOfCorrespondingBits()" << std::endl;
        
        bool NextBitInFirstHalf;
        bool NextBitInLastHalf;
    
        if (!TryToQueryForBitInPosition(mPairsOfBitsRead, NextBitInFirstHalf))
        {
            return false;
        }
        (*mpForwardsHalf)[mPairsOfBitsRead] = NextBitInFirstHalf;
    
        if (!TryToQueryForBitInBackwardsHalf(mPairsOfBitsRead, NextBitInLastHalf))
        {
            return false;
        }
        (*mpBackwardsHalf)[mPairsOfBitsRead] = NextBitInLastHalf;
    
        if (NextBitInFirstHalf == NextBitInLastHalf)
        {
            mIndexOfSymmetricPair = mPairsOfBitsRead;
            cerr << "DEBUG:     mIndexOfSymmetricPair = " << mIndexOfSymmetricPair << std::endl;
        }
        else
        {
            mIndexOfAntiSymmetricPair = mPairsOfBitsRead;
            cerr << "DEBUG:     mIndexOfAntiSymmetricPair = " << mIndexOfAntiSymmetricPair << std::endl;
        }
        ++mPairsOfBitsRead;
        cerr << "DEBUG:     mPairsOfBitsRead = " << mPairsOfBitsRead << std::endl;
        cerr << "DEBUG:     solution so far is: " << GetSolution() << std::endl;
        return true;
    }
    
    bool PerformDummyQuery()
    {
        cerr << "DEBUG: PerformDummyQuery()" << std::endl;
        bool DummyValue;
        return TryToQueryForBitInPosition(0, DummyValue);
    }
    
    bool TryToQueryForBitInPosition(size_t ZeroBasedIndex, bool &Value)
    {
        cout << (ZeroBasedIndex + 1) << std::endl;
        char Response;
        cin >> Response;
        ++mQueryCount;
        if (Response == '1')
        {
            Value = true;
            cerr << "DEBUG: query bit in position " << ZeroBasedIndex + 1 << " = " << Value << std::endl;
            return true;
        }
        else if (Response == '0')
        {
            Value = false;
            cerr << "DEBUG: query bit in position " << ZeroBasedIndex + 1 << " = " << Value << std::endl;
            return true;
        }
        cerr << "ERROR: Query produced unexpected response: " << Response << std::endl;
        return false;
    }
    
    bool TryToQueryForBitInBackwardsHalf(size_t ZeroBasedIndexFromTheEnd, bool &Value)
    {
        cerr << "DEBUG: TryToQueryForBitInBackwardsHalf().  ZeroBasedIndexFromTheEnd = "
            << ZeroBasedIndexFromTheEnd << std::endl;
        size_t ZeroBasedForwardsIndex = B - 1 - ZeroBasedIndexFromTheEnd;
        return TryToQueryForBitInPosition(ZeroBasedForwardsIndex, Value);
    }
    
    int mQueryCount = 0;
    int mPairsOfBitsRead = 0;
    int mIndexOfSymmetricPair = -1;
    int mIndexOfAntiSymmetricPair = -1;
    bool mIsReversed = false;
    size_t mBitsPerHalf;
    bitset<B> mHalf1;
    bitset<B> mHalf2;
    bitset<B> *mpForwardsHalf = &mHalf1;
    bitset<B> *mpBackwardsHalf = &mHalf2;
};

int main()
{
    size_t T, B;
    cin >> T >> B;
    for (size_t t = 0; t < T; ++t)
    {
        if (B == 10)
        {
            Solver<10> Solver;
            if (!Solver.TryToSolve())
            {
                cerr << "ERROR: Unable to solve case # " << t+1 << " with size " << B << std::endl;
                return 1;
            }
        }
        else if (B == 20)
        {
            Solver<20> Solver;
            if (!Solver.TryToSolve())
            {
                cerr << "ERROR: Unable to solve case # " << t+1 << " with size " << B << std::endl;
                return 1;
            }
        }
        else if (B == 100)
        {
            Solver<100> Solver;
            if (!Solver.TryToSolve())
            {
                cerr << "ERROR: Unable to solve case # " << t+1 << " with size " << B << std::endl;
                return 1;
            }
        }
        else
        {
            cerr << "ERROR: Unexpected value of B: " << B << std::endl;
            return 1;
        }
    }
    return 0;
}
